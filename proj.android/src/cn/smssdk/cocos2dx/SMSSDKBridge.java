package cn.smssdk.cocos2dx;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import org.cocos2dx.lib.Cocos2dxActivity;
import org.cocos2dx.plugin.PluginWrapper;

import com.mob.MobSDK;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.ContactsPage;
import cn.smssdk.gui.RegisterPage;
import cn.smssdk.utils.SMSLog;
import cn.smssdk.utils.SPHelper;

public class SMSSDKBridge {
	private static final String TAG = "SMSSDKBridge";
    private static boolean DEBUG = true;

    private static Context context;

    public static void init(final String appKey, final String appSecret, final boolean isWarn) {
        if (TextUtils.isEmpty(appKey) || TextUtils.isEmpty(appSecret))
            return;
        if (context == null) {
            context = Cocos2dxActivity.getContext().getApplicationContext();
        }
		if(Looper.myLooper() == null)
        	Looper.prepare();
        
        // SMSSDK init must be processed on UI thread, otherwise, GL thread & UI thread will wait for each other(only in Android7.0).
		Handler handler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				MobSDK.init(context, appKey, appSecret);
		        if (isWarn) {
		        	SMSSDK.setAskPermisionOnReadContact(isWarn);
		        }
		        EventHandler handler = new EventHandler(){
		            public void afterEvent(int event, int result, Object data) {
		                final String resp = JavaTools.javaActionResToCS(event, result, data);
		                PluginWrapper.runOnGLThread(new Runnable() {
		                    @Override
		                    public void run() {
		                        onJavaCallback(resp);
		                    }
		                });

		            }
		        };
		        SMSSDK.registerEventHandler(handler);
				return false;
			}
		});
		handler.sendEmptyMessage(0);
    }

    public static void getTextCode(String zone, String phone, String tempCode) {
    	SMSLog.getInstance().d("=========>getTextCode. tempCode= " + tempCode);
        SMSSDK.getVerificationCode(zone,phone,tempCode,null);
    }

    public static void getVoiceCode(String zone, String phone) {
        SMSSDK.getVoiceVerifyCode(zone,phone);
    }

    public static void commitCode(String zone, String phone, String code) {
        SMSSDK.submitVerificationCode(zone,phone,code);
    }

    public static void getSupportedCountries() {
        SMSSDK.getSupportedCountries();
    }

    public static void getFriendsInApp() {
        SMSSDK.getFriendsInApp();
    }

    public static void submitUserInfo(String uid, String nickname, String avatar,
                               String country, String phone) {
        SMSSDK.submitUserInfo(uid,nickname,avatar,country,phone);
    }

    public static String getVersion() {
        return SMSSDK.getVersion();
    }

    public static void enableWarn(boolean isWarn) {
        SPHelper.getInstance().setWarnWhenReadContact(isWarn);
    }

    public static void showRegisterPage(String tempCode) {
        RegisterPage registerPage = new RegisterPage();
        EventHandler handler = new EventHandler(){
            public void afterEvent(int event, int result, Object data) {
                final String resp = JavaTools.javaActionResToCS(event, result, data);
                PluginWrapper.runOnGLThread(new Runnable() {
                    @Override
                    public void run() {
                        onJavaCallback(resp);
                    }
                });
            }
        };
        SMSLog.getInstance().d("=========>Open ResgisterPage. tempCode= " + tempCode);
        registerPage.setTempCode(tempCode);
        registerPage.setRegisterCallback(handler);
        registerPage.show(context);
    }

    public static void showContactsPage() {
        ContactsPage contactsPage = new ContactsPage();
        contactsPage.show(context);
    }

    private native static void onJavaCallback(String resp);
}
