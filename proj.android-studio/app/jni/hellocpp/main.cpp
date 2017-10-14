#include "AppDelegate.h"
#include "cocos2d.h"
#include "platform/android/jni/JniHelper.h"
#include <jni.h>
#include <android/log.h>

#define  LOG_TAG    "main"
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)

using namespace cocos2d;

// Cocos2dx sdk 3.7以下
// void cocos_android_app_init (JNIEnv* env, jobject thiz) {
//     LOGD("cocos_android_app_init");
//     AppDelegate *pAppDelegate = new AppDelegate();
// }

// Cocos2dx sdk 3.7及以上
void cocos_android_app_init (JNIEnv* env) {
    LOGD("cocos_android_app_init");
    AppDelegate *pAppDelegate = new AppDelegate();
}
