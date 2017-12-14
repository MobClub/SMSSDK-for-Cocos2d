//
//  iOSSMSSDK.cpp
//  HelloWorldDemo
//
//  Copyright © 2015年 mob.com. All rights reserved.
//

#include "iOSSMSSDK.h"
#import <MOBFoundation/MOBFRegex.h>
#import <MOBFoundation/MOBFoundation.h>
#import <SMS_SDK/SMSSDK.h>
#import <SMS_SDK/SMSSDK+ContactFriends.h>

#import "SMSSDKUI.h"

using namespace smssdk;
using namespace std;


static SMSSDKHandler* _handler = nullptr;
static UIWindow *_window;

#pragma mark - SMSSDK 接口
#pragma mark 初始化

bool iOSSMSSDK::init(string appKey, string appSecret, bool isWarn)
{
    NSLog(@"3.0.0版本后appkey和appSecret自动配置到info.plist里,init方法不需要实现");
    return true;
}

bool iOSSMSSDK::getCode(SMSSDKCodeType codeType, string phoneNumber, string zone, string tempCode)
{
    NSString *phoneNumberStr = [NSString stringWithCString:phoneNumber.c_str() encoding:NSUTF8StringEncoding];
    NSString *zoneStr = [NSString stringWithCString:zone.c_str() encoding:NSUTF8StringEncoding];
    NSString *tempCodeStr = [NSString stringWithCString:tempCode.c_str() encoding:NSUTF8StringEncoding];
    SMSGetCodeMethod smsGetCodeMethod = (SMSGetCodeMethod)codeType;
    
    NSLog(@"Send:%@,tempCode:%@",phoneNumberStr,tempCodeStr);
    
    if (phoneNumber.length() != 0 && zone.length() != 0)
    {
        [SMSSDK getVerificationCodeByMethod:smsGetCodeMethod phoneNumber:phoneNumberStr zone:zoneStr template:tempCodeStr result:^(NSError *error) {
            
            NSLog(@"%@",error);
            
            if (!error )
            {
                string res("");
                //转化回到JSONString的状
                NSString *resString = nil;
                if (smsGetCodeMethod == SMSGetCodeMethodSMS)
                {
                    NSLog(@"获取文本验证码成功后返回数据_%s",res.c_str());
                    resString = @"getTextCodeSuccess";
                }
                else
                {
                    NSLog(@"获取语音验证码成功后返回数据_%s",res.c_str());
                    resString = @"getVoiceCodeSuccess";
                }
                
                res = [resString cStringUsingEncoding:NSUTF8StringEncoding];
                
                if (_handler != nullptr)
                {
                    _handler->onComplete(Action_GetCode, res);
                }
            }
            else
            {
                NSMutableDictionary * resultErrorMsg =[NSMutableDictionary dictionaryWithObjectsAndKeys:@(error.code),@"code" ,error.domain,@"domain",error.userInfo,@"userInfo",  nil];
                //转化回到JSONString的状态码
                NSString *resultMsg= [MOBFJson jsonStringFromObject:resultErrorMsg];
                
                string res([resultMsg UTF8String]);
                
                if (_handler != nullptr)
                {
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
        
        [SMSSDK commitVerificationCode:verificationCodeStr phoneNumber:phoneNumberStr zone:zoneStr result:^( NSError *error) {
            NSMutableDictionary *resultDic = [NSMutableDictionary dictionaryWithCapacity:0];
            [resultDic setObject:[NSNumber numberWithInt:2] forKey:@"action"];
            
            if (!error)
            {
                string res("commitCodeSucess");
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
                
                if (_handler != nullptr)
                {
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
            
            if (_handler != nullptr)
            {
                _handler->onComplete(Action_GetSupportedCountries, res);
            }
        }
        else
        {
            NSMutableDictionary * resultErrorMsg =[NSMutableDictionary dictionaryWithObjectsAndKeys:@(error.code),@"code" ,error.domain,@"domain",error.userInfo,@"userInfo",  nil];
            //转化回到JSONString的状态码
            NSString *resultMsg= [MOBFJson jsonStringFromObject:resultErrorMsg];
            
            string res([resultMsg UTF8String]);
            
            if (_handler != nullptr)
            {
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
            
            if (_handler != nullptr)
            {
                _handler->onComplete(Action_GetFriends, res);
            }
        }
        else
        {
            NSMutableDictionary * resultErrorMsg =[NSMutableDictionary dictionaryWithObjectsAndKeys:@(error.code),@"code" ,error.domain,@"domain",error.userInfo,@"userInfo",  nil];
            //转化回到JSONString的状态码
            NSString *resultMsg= [MOBFJson jsonStringFromObject:resultErrorMsg];
            
            string res([resultMsg UTF8String]);
            
            if (_handler != nullptr)
            {
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
    
    [SMSSDK submitUserInfo:smsUserInfo result:^(NSError *error)
     {
         NSMutableDictionary *resultDic = [NSMutableDictionary dictionaryWithCapacity:0];
         [resultDic setObject:[NSNumber numberWithInt:4] forKey:@"action"];
         if (!error)
         {
             string res("submitUserInfoSuccess");
             
             if (_handler != nullptr)
             {
                 _handler->onComplete(Action_SubmitUserInfo, res);
             };
             
         }
         else
         {
             NSMutableDictionary * resultErrorMsg =[NSMutableDictionary dictionaryWithObjectsAndKeys:@(error.code),@"code" ,error.domain,@"domain",error.userInfo,@"userInfo",  nil];
             //转化回到JSONString的状态码
             NSString *resultMsg= [MOBFJson jsonStringFromObject:resultErrorMsg];
             
             string res([resultMsg UTF8String]);
             
             if (_handler != nullptr)
             {
                 _handler->onError(Action_SubmitUserInfo, res);
             }
         }
         
     }];
    return true;
}


string iOSSMSSDK::getVersion ()
{
    NSString *versionString = [SMSSDK sdkVersion];
    string resultString = [versionString cStringUsingEncoding: NSUTF8StringEncoding];
    string res (resultString);
    
    if (_handler != nullptr)
    {
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
    SMSSDKUIGetCodeViewController *vc = [[SMSSDKUIGetCodeViewController alloc] initWithMethod:SMSGetCodeMethodSMS];
    
    UINavigationController *nav = [[UINavigationController alloc] initWithRootViewController:vc];
    
    UIViewController *rootVC = [[UIApplication sharedApplication].delegate viewController];
    
    [rootVC presentViewController:nav animated:YES completion:nil];

    return true;
}


bool iOSSMSSDK::showContactsPage()
{
    [SMSSDK getAllContactFriends:^(NSError *error, NSArray *friendsArray) {
        
        if (error)
        {
            NSLog(@"%s,%@",__func__,error);
        }
        else
        {
            NSLog(@"%@",friendsArray);
            
            SMSSDKUIContactFriendsViewController *vc = [[SMSSDKUIContactFriendsViewController alloc] initWithContactFriends:friendsArray];
            UINavigationController *nav = [[UINavigationController alloc] initWithRootViewController:vc];
            
            UIViewController *rootVC = [[UIApplication sharedApplication].delegate viewController];
            
            [rootVC presentViewController:nav animated:YES completion:nil];

        }
    }];

    return true;
}


void iOSSMSSDK::setHandler(SMSSDKHandler *handler)
{
    _handler = handler;
}


