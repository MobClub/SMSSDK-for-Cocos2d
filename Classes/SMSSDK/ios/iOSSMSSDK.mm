//
//  iOSSMSSDK.cpp
//  HelloWorldDemo
//
//  Copyright © 2015年 mob.com. All rights reserved.
//

#include "iOSSMSSDK.h"
#import <SMS_SDK/SMSSDK.h>
#import <MOBFoundation/MOBFRegex.h>
#import <SMS_SDK/Extend/SMSSDK+AddressBookMethods.h>
#import <SMS_SDK/Extend/SMSSDK+ExtexdMethods.h>
#import <MOBFoundation/MOBFoundation.h>
#import "SMSSDKUI.h"

using namespace smssdk;
using namespace std;


static SMSSDKHandler* _handler = nullptr;

#pragma mark - SMSSDK 接口
#pragma mark 初始化

bool iOSSMSSDK::init(string appKey, string appSecret, bool isWarn)
{
    NSString * appKeyStr = nil;
    NSString * appSecretStr =nil;
    
    if (appKey.length() != 0 && appSecret.length() != 0)
    {
        appKeyStr = [NSString stringWithCString:appKey.c_str() encoding:NSUTF8StringEncoding];
        appSecretStr= [NSString stringWithCString:appSecret.c_str() encoding:NSUTF8StringEncoding];
    }
    [SMSSDK registerApp:appKeyStr withSecret:appSecretStr];
    [SMSSDK enableAppContactFriends:isWarn];
    return true;
}

bool iOSSMSSDK::getCode(SMSSDKCodeType codeType, string phoneNumber, string zone)
{
    NSString  *phoneNumberStr = [NSString stringWithCString:phoneNumber.c_str() encoding:NSUTF8StringEncoding];
    NSString *zoneStr = [NSString stringWithCString:zone.c_str() encoding:NSUTF8StringEncoding];
    SMSGetCodeMethod smsGetCodeMethod = (SMSGetCodeMethod)codeType;
    
    if (phoneNumber.length() != 0 && zone.length() != 0){
        
        [SMSSDK getVerificationCodeByMethod:smsGetCodeMethod phoneNumber:phoneNumberStr zone:zoneStr customIdentifier:nil result:^(NSError *error) {
            
            //            int action = 1;
            if (!error )
            {
                string res("");
                //转化回到JSONString的状
                if (smsGetCodeMethod == SMSGetCodeMethodSMS)
                {
                    NSLog(@"获取文本验证码成功后返回数据_%s",res.c_str());
                    NSString *resString = @"getTextCodeSuccess";
                    res = [resString cStringUsingEncoding:NSUTF8StringEncoding];
                    
                }
                else
                {
                    NSLog(@"获取语音验证码成功后返回数据_%s",res.c_str());
                    NSString *resString = @"getVoiceCodeSuccess";
                    res = [resString cStringUsingEncoding:NSUTF8StringEncoding];
                }
                
                if (_handler != nullptr) {
                    _handler->onComplete(Action_GetCode, res);
                }
                
            }
            else
            {
                NSMutableDictionary * resultErrorMsg =[NSMutableDictionary dictionaryWithObjectsAndKeys:@(error.code),@"code" ,error.domain,@"domain",error.userInfo,@"userInfo",  nil];
                //转化回到JSONString的状态码
                NSString *resultMsg= [MOBFJson jsonStringFromObject:resultErrorMsg];
                
                string res([resultMsg UTF8String]);
                
                if (_handler != nullptr) {
                    _handler->onError(Action_GetCode, res);
                }
            }
            
        }];
    }
    return true;
}

bool iOSSMSSDK::commitCode (string phoneNumber, string zone, string verificationCode)
{
    NSString *phoneNumberStr = nil;
    NSString *zoneStr = nil;
    NSString *verificationCodeStr = nil;
    
    if (phoneNumber.length() != 0 && zone.length() != 0 && verificationCode.length() != 0 && verificationCode.length() != 0)
    {
        phoneNumberStr = [NSString stringWithCString:phoneNumber.c_str() encoding:NSUTF8StringEncoding];
        zoneStr = [NSString stringWithCString:zone.c_str() encoding:NSUTF8StringEncoding];
        verificationCodeStr = [NSString stringWithCString:verificationCode.c_str() encoding:NSUTF8StringEncoding];
        //        observerStr = [NSString stringWithCString:observer encoding:NSUTF8StringEncoding];
        
        [SMSSDK commitVerificationCode:verificationCodeStr phoneNumber:phoneNumberStr zone:zoneStr result:^(NSError *error) {
            
            NSMutableDictionary *resultDic = [NSMutableDictionary dictionaryWithCapacity:0];
            [resultDic setObject:[NSNumber numberWithInt:2] forKey:@"action"];
            
            if (!error)
            {
                string res("commitVerificationCode");
                if (_handler != nullptr) {
                    _handler->onComplete(Action_CommitCode, res);
                }
            }
            else
            {
                NSMutableDictionary * resultErrorMsg =[NSMutableDictionary dictionaryWithObjectsAndKeys:@(error.code),@"code" ,error.domain,@"domain",error.userInfo,@"userInfo",  nil];
                //转化回到JSONString的状态码
                NSString *resultMsg= [MOBFJson jsonStringFromObject:resultErrorMsg];
                
                string res([resultMsg UTF8String]);
                
                if (_handler != nullptr) {
                    _handler->onError(Action_CommitCode, res);
                }
                
            }
        }];
    }
    return true;
}

bool iOSSMSSDK::getSupportedCountries()
{
    
    [SMSSDK getCountryZone:^(NSError *error, NSArray *zonesArray) {
        
        if (!error)
        {
            string res("getCountryZoneSuccess");
            if (_handler != nullptr) {
                _handler->onComplete(Action_GetSupportedCountries, res);
            }
        }
        else
        {
            NSMutableDictionary * resultErrorMsg =[NSMutableDictionary dictionaryWithObjectsAndKeys:@(error.code),@"code" ,error.domain,@"domain",error.userInfo,@"userInfo",  nil];
            //转化回到JSONString的状态码
            NSString *resultMsg= [MOBFJson jsonStringFromObject:resultErrorMsg];
            
            string res([resultMsg UTF8String]);
            
            if (_handler != nullptr) {
                _handler->onError(Action_GetSupportedCountries, res);
            }
            
        }
    }];
    return true;
}

bool iOSSMSSDK::getFriends()
{
    
    [SMSSDK getAllContactFriends:^(NSError *error, NSArray *friendsArray) {
        
        NSMutableDictionary *resultDic = [NSMutableDictionary dictionaryWithCapacity:0];
        [resultDic setObject:[NSNumber numberWithInt:5] forKey:@"action"];
        
        if (!error)
        {
            string res("getFriendsSuccess");
            if (_handler != nullptr) {
                _handler->onComplete(Action_GetFriends, res);
            }
        }
        else
        {
            NSMutableDictionary * resultErrorMsg =[NSMutableDictionary dictionaryWithObjectsAndKeys:@(error.code),@"code" ,error.domain,@"domain",error.userInfo,@"userInfo",  nil];
            //转化回到JSONString的状态码
            NSString *resultMsg= [MOBFJson jsonStringFromObject:resultErrorMsg];
            
            string res([resultMsg UTF8String]);
            
            if (_handler != nullptr) {
                _handler->onError(Action_GetFriends, res);
            }
        }
    }];
    return true;
}

bool iOSSMSSDK::submitUserInfo (UserInfo &userInfo)
{
    SMSSDKUserInfo *smsUserInfo = [[SMSSDKUserInfo alloc] init];
    smsUserInfo.phone = [NSString stringWithCString:userInfo.phone.c_str() encoding:NSUTF8StringEncoding];
    smsUserInfo.nickname = [NSString stringWithCString:userInfo.nickname.c_str() encoding:NSUTF8StringEncoding];
    smsUserInfo.avatar = [NSString stringWithCString:userInfo.avatar.c_str() encoding:NSUTF8StringEncoding];
    smsUserInfo.uid = [NSString stringWithCString:userInfo.uid.c_str() encoding:NSUTF8StringEncoding];
    
    [SMSSDK submitUserInfoHandler:smsUserInfo result:^(NSError *error)
     {
         NSMutableDictionary *resultDic = [NSMutableDictionary dictionaryWithCapacity:0];
         [resultDic setObject:[NSNumber numberWithInt:4] forKey:@"action"];
         if (!error)
         {
             string res("submitUserInfoSuccess");
             if (_handler != nullptr) {
                 _handler->onComplete(Action_SubmitUserInfo, res);
             };
             
         }
         else
         {
             NSMutableDictionary * resultErrorMsg =[NSMutableDictionary dictionaryWithObjectsAndKeys:@(error.code),@"code" ,error.domain,@"domain",error.userInfo,@"userInfo",  nil];
             //转化回到JSONString的状态码
             NSString *resultMsg= [MOBFJson jsonStringFromObject:resultErrorMsg];
             
             string res([resultMsg UTF8String]);
             
             if (_handler != nullptr) {
                 _handler->onError(Action_SubmitUserInfo, res);
             }
         }
         
     }];
    return true;
}

string iOSSMSSDK::getVersion ()
{
    NSString *versionString = [SMSSDK SMSSDKVersion];
    string resultString = [versionString cStringUsingEncoding: NSUTF8StringEncoding];
    string res (resultString);
    if (_handler != nullptr) {
        _handler->onComplete(Action_GetVersion, res);
    }
    return res;
    
}

bool iOSSMSSDK::enableWarn (bool isWarn)
{
    NSLog(@"__iosEnableAppContractFriends__**state_%d",isWarn);
    [SMSSDK enableAppContactFriends:isWarn];
    return true;
}


//SMSSDK_Demo UI
bool iOSSMSSDK::showRegisterPage(SMSSDKCodeType type)
{
    [SMSSDKUI showVerificationCodeViewWithMetohd:SMSGetCodeMethodSMS result:^(enum SMSUIResponseState state, NSString *phoneNumber, NSString *zone, NSError *error) {
        
        if (!error)
        {
            string res ("");
            
            if (state == SMSUIResponseStateSuccess)
            {
                NSString *resString = @"commitCodeSuccess";
                res = [resString cStringUsingEncoding:NSUTF8StringEncoding];
            }
            else if (state == SMSUIResponseStateCancel)
            {
                NSString *resString = @"showRegisterViewSuccess";
                res = [resString cStringUsingEncoding:NSUTF8StringEncoding];
            }
            else if (state == SMSUIResponseStateFail)
            {
                NSString *resString = @"showRegisterViewFailer";
                res = [resString cStringUsingEncoding:NSUTF8StringEncoding];
            }
            
            if (_handler != nullptr) {
                _handler->onComplete(Action_GetSupportedCountries, res);
            }
        }
        else
        {
            
            NSMutableDictionary * resultErrorMsg =[NSMutableDictionary dictionaryWithObjectsAndKeys:@(error.code),@"code" ,error.domain,@"domain",error.userInfo,@"userInfo",  nil];
            //转化回到JSONString的状态码
            NSString *resultMsg= [MOBFJson jsonStringFromObject:resultErrorMsg];
            
            string res([resultMsg UTF8String]);
            
            if (_handler != nullptr) {
                _handler->onError(Action_GetSupportedCountries, res);
            }
            
        }
        
    }];
    
    return true;
    
}

bool iOSSMSSDK::showContactsPage()
{
    
    [SMSSDKUI showGetContactsFriendsViewWithNewFriends:[NSMutableArray array] newFriendClock:^(enum SMSResponseState state, int latelyFriendsCount) {
        
    } result:^{
        
        NSString *resString = @"showContractFriendsViewSuccess";
        
        string resultString = [resString cStringUsingEncoding: NSUTF8StringEncoding];
        string res(resultString);
        if (_handler != nullptr) {
            _handler->onComplete(Action_GetFriends, res);
        }
        
        if (_handler != nullptr) {
            _handler->onError(Action_GetFriends, res);
        }
        
    }];
    return true;
}

void iOSSMSSDK::setHandler(SMSSDKHandler *handler)
{
    _handler = handler;
}


