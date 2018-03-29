//
//  SMSSDK.cpp
//  SMSSDKSample
//
//  Copyright © 2015年 mob.com. All rights reserved.
//

#include "SMSSDK.h"

#if CC_TARGET_PLATFORM == CC_PLATFORM_IOS

#include "iOSSMSSDK.h"

#elif CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID

#include "SMSSDK_android.h"

#endif

using namespace smssdk;

bool SMSSDK::init(string appKey, string appSecret, bool isWarn)
{
#if CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID
    
    //Andorid
    return SMSSDK_android::initSDKJNI(appKey, appSecret, isWarn);
    
#elif CC_TARGET_PLATFORM == CC_PLATFORM_IOS
    
    //iOS
    return iOSSMSSDK::init(appKey, appSecret, isWarn);
    
#endif
    return false;
}

bool SMSSDK::getCode(SMSSDKCodeType codeType, string phone, string zone, string tempCode)
{
#if CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID
    
    //Andorid
    return SMSSDK_android::getCodeJNI(codeType, zone, phone, tempCode);
    
#elif CC_TARGET_PLATFORM == CC_PLATFORM_IOS
    
    //iOS
    return iOSSMSSDK::getCode(codeType, phone,zone,tempCode);
    
#endif
}

bool SMSSDK::commitCode(string phone, string zone, string code)
{
#if CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID
    
    //Andorid
    return SMSSDK_android::commitCodeJNI(zone, phone, code);
    
#elif CC_TARGET_PLATFORM == CC_PLATFORM_IOS
    
    //iOS
    return iOSSMSSDK::commitCode(phone,zone, code);
    
#endif
    return false;
}

bool SMSSDK::getSupportedCountries()
{
#if CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID
    
    //Andorid
    return SMSSDK_android::getSupportedCountriesJNI();
    
#elif CC_TARGET_PLATFORM == CC_PLATFORM_IOS
    
    //iOS
    return iOSSMSSDK::getSupportedCountries();
    
#endif
    return false;
}

bool SMSSDK::getFriends()
{
#if CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID
    
    //Andorid
    return SMSSDK_android::getFriendsJNI();
    
#elif CC_TARGET_PLATFORM == CC_PLATFORM_IOS    
    //iOS
    return iOSSMSSDK::getFriends();
    
#endif
    
    return false;
}

bool SMSSDK::submitUserInfo(UserInfo &userinfo)
{
#if CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID
    
    //Andorid
    return SMSSDK_android::submitUserInfoJNI(userinfo);
#elif CC_TARGET_PLATFORM == CC_PLATFORM_IOS
    
    //iOS
    return iOSSMSSDK::submitUserInfo(userinfo);
    
#endif
	return false;
}

string SMSSDK::getVersion()
{
#if CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID

    //Andorid
    return SMSSDK_android::getVersionJNI();
    
#elif CC_TARGET_PLATFORM == CC_PLATFORM_IOS

    //iOS
    return iOSSMSSDK ::getVersion();

#endif
    return NULL;
}

bool SMSSDK::enableWarn(bool isWarn)
{
#if CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID
    
    //Andorid
    return SMSSDK_android::enableWarnJNI(isWarn);
    
#elif CC_TARGET_PLATFORM == CC_PLATFORM_IOS
    
    //iOS
    return iOSSMSSDK::enableWarn(isWarn);
    
#endif
    return false;
}

bool SMSSDK::showRegisterPage(SMSSDKCodeType type, string tempCode)
{
#if CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID

    //Andorid
    return SMSSDK_android::showRegisterPageJNI(tempCode);

#elif CC_TARGET_PLATFORM == CC_PLATFORM_IOS

    //iOS
    return iOSSMSSDK::showRegisterPage(type,tempCode);

#endif
    return false;
}

bool SMSSDK::showContactsPage()
{
#if CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID

    //Andorid
    return SMSSDK_android::showContactsPageJNI();

#elif CC_TARGET_PLATFORM == CC_PLATFORM_IOS

    //iOS
    return iOSSMSSDK::showContactsPage();

#endif
    return false;
}

void SMSSDK::setHandler(SMSSDKHandler* handler) 
{
#if CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID

    //Android
	SMSSDK_android::setHandlerAndroid(handler);

#elif CC_TARGET_PLATFORM == CC_PLATFORM_IOS

    //iOS
    iOSSMSSDK::setHandler(handler);
    
#endif
}
