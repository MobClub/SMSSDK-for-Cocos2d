#include "SMSSDK_android.h"

#if COCOS2D_DEBUG > 0
#define  LOG_TAG    "SMSSDKBridge"
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)
#else
#define  LOGD(...) 
#endif

#ifdef __cplusplus
extern "C" {
#endif

using namespace smssdk;
using namespace std;

static SMSSDKHandler* _handler = NULL;

JNIEXPORT void JNICALL Java_cn_smssdk_cocos2dx_SMSSDKBridge_onJavaCallback
  (JNIEnv * env, jclass thiz, jstring resp) {
  	if (resp == NULL || _handler == NULL)
  	{
  		return;
  	}

	CCJSONConverter* json = CCJSONConverter::sharedConverter();
	const char* ccResp = env->GetStringUTFChars(resp, JNI_FALSE);
	//CCLog("ccResp = %s", ccResp);
	C2DXDictionary* dic = json->dictionaryFrom(ccResp);
	env->ReleaseStringUTFChars(resp, ccResp);
	CCNumber* status = (CCNumber*) dic->objectForKey("status"); // Success = 1, Fail = 2
	CCNumber* action = (CCNumber*) dic->objectForKey("action"); //
	C2DXString* res = (C2DXString*) dic->objectForKey("res");
	if (res == nullptr) {
		res = new C2DXString("NULL");
	}
	log("status:%d",status->getIntValue());
	log("action:%d",action->getIntValue());
	log("res:%s",res->_string.c_str());
	// TODO add codes here
	if(1 == status->getIntValue()){
		_handler->onComplete(static_cast<SMSSDKActionType>(action->getIntValue()), res->_string);
	}else if(2 == status->getIntValue()){
		_handler->onError(static_cast<SMSSDKActionType>(action->getIntValue()), res->_string);
	}

	dic->release();
}

bool SMSSDK_android::getMethod(JniMethodInfo &mi, const char *methodName, const char *paramCode) {
	return JniHelper::getStaticMethodInfo(mi, "cn/smssdk/cocos2dx/SMSSDKBridge", methodName, paramCode);
}

void SMSSDK_android::releaseMethod(JniMethodInfo &mi) {
	if(mi.classID != NULL)
		mi.env->DeleteLocalRef(mi.classID);
}

bool SMSSDK_android::initSDKJNI(string appKey, string appSecret, bool isWarn) {
	JniMethodInfo mi;
	bool isHave = getMethod(mi, "init", "(Ljava/lang/String;Ljava/lang/String;Z)V");
	if (!isHave) {
		return false;
	}
	
	jstring jAppKey = mi.env->NewStringUTF(appKey.c_str());
	jstring jAppSecret = mi.env->NewStringUTF(appSecret.c_str());
	jboolean jIsWarn = isWarn?JNI_TRUE:JNI_FALSE;

	mi.env->CallStaticVoidMethod(mi.classID, mi.methodID, jAppKey, jAppSecret,jIsWarn);
	releaseMethod(mi);
	return true;
}

bool SMSSDK_android::getCodeJNI(SMSSDKCodeType type, string zone, string phone, string tempCode) {
	JniMethodInfo mi;
	bool isHave;
	if(type == TextCode) {
		isHave = getMethod(mi, "getTextCode", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V");
	} else {
		isHave = getMethod(mi, "getVoiceCode", "(Ljava/lang/String;Ljava/lang/String;)V");
	}

	if (!isHave) {
		return false;
	}

	jstring jZone = mi.env->NewStringUTF(zone.c_str());
	jstring jPhone = mi.env->NewStringUTF(phone.c_str());
	jstring jTempCode = mi.env->NewStringUTF(tempCode.c_str());

	mi.env->CallStaticVoidMethod(mi.classID, mi.methodID, jZone, jPhone, jTempCode);
	releaseMethod(mi);
	return true;
}

bool SMSSDK_android::commitCodeJNI(string zone, string phone, string code) {
	JniMethodInfo mi;
	bool isHave = getMethod(mi, "commitCode", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V");
	if (!isHave) {
		return false;
	}

	jstring jZone = mi.env->NewStringUTF(zone.c_str());
	jstring jPhone =  mi.env->NewStringUTF(phone.c_str());
	jstring jCode =  mi.env->NewStringUTF(code.c_str());

	mi.env->CallStaticVoidMethod(mi.classID, mi.methodID, jZone, jPhone, jCode);
	releaseMethod(mi);
	return true;
}

bool SMSSDK_android::getSupportedCountriesJNI() {
	JniMethodInfo mi;
	bool isHave = getMethod(mi, "getSupportedCountries", "()V");
	if (!isHave) {
		return false;
	}

	mi.env->CallStaticVoidMethod(mi.classID, mi.methodID);
	releaseMethod(mi);
	return true;
}

bool SMSSDK_android::getFriendsJNI() {
	JniMethodInfo mi;
	bool isHave = getMethod(mi, "getFriendsInApp", "()V");
	if (!isHave) {
		return false;
	}

	mi.env->CallStaticVoidMethod(mi.classID, mi.methodID);
	releaseMethod(mi);
	return true;
}

bool SMSSDK_android::submitUserInfoJNI(UserInfo &userinfo){
	JniMethodInfo mi;
	bool isHave = getMethod(mi, "submitUserInfo", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V");
	if (!isHave) {
		return false;
	}
	jstring jUid = mi.env->NewStringUTF(userinfo.uid.c_str());
	jstring jUsername = mi.env->NewStringUTF(userinfo.nickname.c_str());
	jstring jAvatar = mi.env->NewStringUTF(userinfo.avatar.c_str());
	jstring jCountry = mi.env->NewStringUTF(userinfo.zone.c_str());
	jstring jPhone = mi.env->NewStringUTF(userinfo.phone.c_str());

	mi.env->CallStaticVoidMethod(mi.classID, mi.methodID, jUid, jUsername, jAvatar, jCountry, jPhone);
	releaseMethod(mi);
	return true;
}

string SMSSDK_android::getVersionJNI(){
	JniMethodInfo mi;
	bool isHave = getMethod(mi, "getVersion", "()Ljava/lang/String;");
	if(!isHave){
		return NULL;
	}
	jstring version = (jstring)mi.env->CallStaticObjectMethod(mi.classID, mi.methodID);
	releaseMethod(mi);

	return JniHelper::jstring2string(version);
}


bool SMSSDK_android::enableWarnJNI(bool isWarn){
	JniMethodInfo mi;
	bool isHave = getMethod(mi, "enableWarn", "(Z)V");
	if (!isHave) {
		return false;
	}
	jboolean jIsWarn = isWarn?JNI_TRUE:JNI_FALSE;
	mi.env->CallStaticVoidMethod(mi.classID, mi.methodID, jIsWarn);
	releaseMethod(mi);

	return true;
}

bool SMSSDK_android::showRegisterPageJNI(string tempCode)
{
	JniMethodInfo mi;
	bool isHave = getMethod(mi, "showRegisterPage", "(Ljava/lang/String;)V");
	if (!isHave) {
		return false;
	}
	jstring jTempCode = mi.env->NewStringUTF(tempCode.c_str());
	mi.env->CallStaticVoidMethod(mi.classID, mi.methodID, jTempCode);
	releaseMethod(mi);

	return true;
}

bool SMSSDK_android::showContactsPageJNI()
{
	JniMethodInfo mi;
	bool isHave = getMethod(mi, "showContactsPage", "()V");
	if (!isHave) {
		return false;
	}

	mi.env->CallStaticVoidMethod(mi.classID, mi.methodID);
	releaseMethod(mi);

	return true;
}

void SMSSDK_android::setHandlerAndroid(SMSSDKHandler* handler)
{
	_handler = handler;
}

#ifdef __cplusplus
}
#endif
