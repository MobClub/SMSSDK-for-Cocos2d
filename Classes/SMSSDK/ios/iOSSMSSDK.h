//
//  iOSSMSSDK.h
//  HelloWorldDemo
//
//  Copyright © 2015年 mob.com. All rights reserved.
//

#ifndef _HelloWorldDemo_iOSSMSSDK_H_
#define _HelloWorldDemo_iOSSMSSDK_H_

#include <stdio.h>
#include "cocos2d.h"
#include "SMSSDKType.h"

namespace smssdk
{
        /**
         *	@brief	SMSSDK (iOS)
         */
        class iOSSMSSDK
        {
        
        public:
            /// <summsary>
            /// initialize SMSSDK
            /// </summary>
            static bool init(string appKey, string appSecret, bool isWarn);
            
            /// <summsary>
            /// Get vertificationCode
            /// </summary>
            static bool getCode(SMSSDKCodeType type, string phoneNumber, string zone, string tempCode);
            
            
            /// <summary>
            /// Commits the verification code.
            /// </summary>
            /// <param name="phoneNumber">Phone number.</param>
            /// <param name="zone">Zone.</param>
            /// <param name="verificationCode">Verification code.</param>
            static bool commitCode(string phoneNumber, string zone, string code);
            
            /// <summary>
            /// Gets the country code.
            /// </summary>
            /// <param name="zone">Zone.</param>
            static bool getSupportedCountries();
            
            /// <summary>
            /// Gets all contract friends.
            /// </summary>
            static bool getFriends();
            
            
            /// <summary>
            /// Submits the user info.
            /// </summary>
            /// <param name="userInfo">User info.</param>
            static bool submitUserInfo(UserInfo &userInfo);
            
            
            ///
            /// Smssdks the version.
            /// </summary>
            /// <returns>The version.</returns>
            /// <param name="sdkVersion">Sdk version.</param>
            static string getVersion();
            
            
            /// <summary>
            /// Enables Warn page when the sdk is reading contact friends.
            /// </summary>
            /// <param name="isWarn">If set to <c>true</c> state.</param>
            static bool enableWarn(bool isWarn);
            
            /// <summary>
            /// show Register Page with GUI Library
            /// </summary>
            static bool showRegisterPage(SMSSDKCodeType type,string tempCode);
            
            /// <summary>
            /// show Contacts Page with GUI Library
            /// </summary>
            static bool showContactsPage();
            
            static void setHandler(SMSSDKHandler* handler);
        };
}

#endif


