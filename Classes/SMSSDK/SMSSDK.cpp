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
    return initSDKJNI(appKey, appSecret, isWarn);
    
#elif CC_TARGET_PLATFORM == CC_PLATFORM_IOS
    
    //iOS
    return iOSSMSSDK::init(appKey, appSecret, isWarn);
    
#endif
    return false;
}

bool SMSSDK::getCode(SMSSDKCodeType codeType, string phone, string zone)
{
#if CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID
    
    //Andorid
    return getCodeJNI(codeType, zone, phone);
    
#elif CC_TARGET_PLATFORM == CC_PLATFORM_IOS
    
    //iOS
    return iOSSMSSDK::getCode(codeType, phone,zone);
    
#endif
}

bool SMSSDK::commitCode(string phone, string zone, string code)
{
#if CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID
    
    //Andorid
    return commitCodeJNI(zone, phone, code);
    
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
    return getSupportedCountriesJNI();
    
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
    return getFriendsJNI();
    
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
    return submitUserInfoJNI(userinfo);
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
    return getVersionJNI();
    
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
    return enableWarnJNI(isWarn);
    
#elif CC_TARGET_PLATFORM == CC_PLATFORM_IOS
    
    //iOS
    return iOSSMSSDK::enableWarn(isWarn);
    
#endif
    return false;
}

bool SMSSDK::showRegisterPage(SMSSDKCodeType type)
{
#if CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID

    //Andorid
    return showRegisterPageJNI();

#elif CC_TARGET_PLATFORM == CC_PLATFORM_IOS

    //iOS
    return iOSSMSSDK::showRegisterPage(type);

#endif
    return false;
}

bool SMSSDK::showContactsPage()
{
#if CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID

    //Andorid
    return showContactsPageJNI();

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
    setHandlerAndroid(handler);

#elif CC_TARGET_PLATFORM == CC_PLATFORM_IOS

    //iOS
    iOSSMSSDK::setHandler(handler);
    
#endif
}
