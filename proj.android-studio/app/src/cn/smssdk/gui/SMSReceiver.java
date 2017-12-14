//#if def{lang} == cn
/*
 * 官网地站:http://www.mob.com
 * 技术支持QQ: 4006852216
 * 官方微信:ShareSDK   （如果发布新版本的话，我们将会第一时间通过微信将版本更新内容推送给您。如果使用过程中有任何问题，
 * 也可以通过微信与我们取得联系，我们将会在24小时内给予回复）
 * 
 * Copyright (c) 2014年 mob.com. All rights reserved.
 */
//#elif def{lang} == en
/*
 * Offical Website:http://www.mob.com
 * Support QQ: 4006852216
 * Offical Wechat Account:ShareSDK   (We will inform you our updated news at the first time by Wechat, if we release a new version.
 * If you get any problem, you can also contact us with Wechat, we will reply you within 24 hours.)
 * 
 * Copyright (c) 2013 mob.com. All rights reserved.
 */
//#endif
package cn.smssdk.gui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;
//#if def{lang} == cn
/** 短信监听接收器，用于自动获取短信验证码，然后自动填写到验证码区域*/
//#elif def{lang} == en
/** SMSReceiver is used to Intercept the sms and analysis the content, then input the verification-code automatically*/
//#endif
public class SMSReceiver extends BroadcastReceiver {
	private static final String ACTION_SMS_RECEIVER = "android.provider.Telephony.SMS_RECEIVED";
	private SMSSDK.VerifyCodeReadListener listener;
	
	public SMSReceiver(SMSSDK.VerifyCodeReadListener verifyCodeReadListener) {
		this.listener = verifyCodeReadListener;
	}


	//#if def{lang} == cn
	/**
	 * 不要使用AndroidManifest.xml配置的方式注册Receiver, 
	 * 请使用Context.registerReceiver注册监听器, 因为初始化的时候要传入监听器 
	 */
	//#elif def{lang} == en
	/** 
	 * do not use AndroidManifest.xml configuration register SMSReceiver, 
	 * please dynamically register an instance of this class with Context.registerReceiver(),
	 * and unregister it in {@link android.app.Activity#onDestroy() Activity.onDestroy()}.
	 */
	//#endif
	public SMSReceiver() {
		String msg = "Please dynamically register an instance of this class with Context.registerReceiver."
				+ "\r\nIf not, the SMSSDK.VerifyCodeReadListener will be null!";
		SMSLog.getInstance().i("SMSReceiver:" + msg);
	}
	
	public void onReceive(Context context, Intent intent) {
		if(ACTION_SMS_RECEIVER.equals(intent.getAction())) {
			Bundle bundle = intent.getExtras();
			if(bundle != null) {
				Object[] pdus = (Object[]) bundle.get("pdus");
				SmsMessage[] smsArr = new SmsMessage[pdus.length];
				for (int i = 0; i < pdus.length; i++) {
					smsArr[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
				}
			
				for (SmsMessage sms: smsArr) {
					if(sms != null) {
						SMSSDK.readVerificationCode(sms, listener);
					}
				}
			}// END if(bundle != null)
		}
	}
}
