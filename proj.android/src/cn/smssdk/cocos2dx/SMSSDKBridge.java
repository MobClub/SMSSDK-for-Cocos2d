package cn.smssdk.cocos2dx;

import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;

import org.cocos2dx.lib.Cocos2dxActivity;
import org.cocos2dx.plugin.PluginWrapper;

import com.mob.MobSDK;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.ContactsPage;
import cn.smssdk.gui.RegisterPage;
import cn.smssdk.utils.SPHelper;

public class SMSSDKBridge {
    private static boolean DEBUG = true;

    private static Context context;

    public static void init(String appKey, String appSecret, boolean isWarn) {
        if (TextUtils.isEmpty(appKey) || TextUtils.isEmpty(appSecret))
            return;
        if (context == null) {
            context = Cocos2dxActivity.getContext().getApplicationContext();
        }
		if(Looper.myLooper() == null)
        	Looper.prepare();
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
    }

    public static void getTextCode(String zone, String phone) {
        SMSSDK.getVerificationCode(zone,phone);
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

    public static void showRegisterPage() {
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

        registerPage.setRegisterCallback(handler);
        registerPage.show(context);
    }

    public static void showContactsPage() {
        ContactsPage contactsPage = new ContactsPage();
        contactsPage.show(context);
    }

    private native static void onJavaCallback(String resp);
}
