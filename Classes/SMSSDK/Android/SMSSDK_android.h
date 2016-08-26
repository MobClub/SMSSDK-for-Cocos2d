#include "AppDelegate.h"
#include "cocos2d.h"

#if COCOS2D_VERSION >= 0x00030000
#include "base/CCEventType.h"
#else
#include "CCEventType.h"
#endif

#include "platform/android/jni/JniHelper.h"
#include <jni.h>
#include <android/log.h>
#include "SMSSDKType.h"
#include "CCJSONConverter.h"

USING_NS_CC;
using namespace smssdk;

#ifndef _SMSSDKBRIDGE_H_
#define _SMSSDKBRIDGE_H_
#ifdef __cplusplus
extern "C" {
#endif

/*
 * Class:     cn.smssdk.cocos2d.SMSSDKBridge
 * Method:    onJavaCallback
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_cn_smssdk_cocos2dx_SMSSDKBridge_onJavaCallback
  (JNIEnv * env, jclass thiz, jstring resp);

bool getMethod(JniMethodInfo &mi, const char *methodName, const char *paramCode);

void releaseMethod(JniMethodInfo &mi);

//jni methods
bool initSDKJNI(string appKey, string appSecret, bool isWarn);

bool getCodeJNI(SMSSDKCodeType codetype,string zone,string phone);

bool commitCodeJNI(string zone, string phone, string code);

bool getSupportedCountriesJNI();

bool getFriendsJNI();

bool submitUserInfoJNI(UserInfo& userinfo);

string getVersionJNI();

bool enableWarnJNI(bool isWarn);

bool showRegisterPageJNI();

bool showContactsPageJNI();

void setHandlerAndroid(SMSSDKHandler* handler);

#ifdef __cplusplus
}
#endif
#endif
