//
//  SMSSDKType.h
//
//  Copyright © 2015年 mob.com. All rights reserved.
//

#ifndef _SMSSDKTYPE_H_ 
#define _SMSSDKTYPE_H_

#include "cocos2d.h"
#include <stdint.h>
#include <string>

USING_NS_CC;
using namespace std;

#if COCOS2D_VERSION < 0x00030000

#define C2DXDictionary CCDictionary
#define C2DXArray CCArray
#define C2DXString CCString
#define C2DXPoint CCPoint
#define C2DXDouble CCDouble
#define C2DXInteger CCInteger
#define C2DXObject CCObject
#define C2DXPointMake(x,y) CCPointMake(x, y)
#define C2DXObjectAtIndex(i) objectAtIndex(i)

#else

#define C2DXDictionary __Dictionary
#define C2DXArray __Array
#define C2DXString __String
#define C2DXPoint cocos2d::Point
#define C2DXDouble __Double
#define C2DXInteger __Integer
#define C2DXObject Ref
#define C2DXPointMake(x,y) cocos2d::Point{static_cast<float>(x), static_cast<float>(y)}
#define C2DXObjectAtIndex(i) getObjectAtIndex(i)

#endif

namespace smssdk
{
        /**
         *	@brief	验证码类型
         */
        enum SMSSDKCodeType
        {
            TextCode = 0, /**< 文本验证码*/
            VoiceCode = 1 /**< 语音验证码*/
        };

        /**
         *	@brief	 类型
         */
        enum SMSSDKActionType
        {
            Action_GetCode = 1,
            Action_CommitCode = 2,
            Action_GetSupportedCountries = 3,
            Action_SubmitUserInfo = 4,
            Action_GetFriends = 5,
            Action_GetVersion = 6
        };
        
        /**
         *	@brief	返回状态
         */
        enum SMSSDKStatus
        {
            StatusSuccess = 1, /**< 成功 */
            StatusFail = 2, /**< 失败 */
        };

        typedef struct _UserInfo {
            string uid;
            string nickname;
            string avatar;
            string zone;
            string phone;
        } UserInfo;

        class SMSSDKHandler
        {
        public:
            virtual void onComplete(SMSSDKActionType action,string result) = 0;
            virtual void onError(SMSSDKActionType action,string result) = 0;
        };
}

#endif
