#ifndef __HELLOWORLD_SCENE_H__
#define __HELLOWORLD_SCENE_H__

#include "SMSSDK/SMSSDKType.h"
#include "cocos2d.h"
#include "cocos-ext.h"
using namespace cocos2d;
using namespace extension;
using  namespace smssdk;

class HelloWorld : public cocos2d::Layer,public SMSSDKHandler,public EditBoxDelegate
{
public:
    static cocos2d::Scene* createScene();
    virtual bool init();
    
    void menuCloseCallback(CCObject* pSender);
    virtual void editBoxEditingDidBegin(cocos2d::extension::EditBox *editBox);
    virtual void editBoxEditingDidEnd(EditBox *editBox);
    virtual void editBoxTextChanged(EditBox *editBox,const std::string &text);
    virtual void editBoxReturn(EditBox *editBox);
    
    //获取文本验证码
    void getTextCodeHandler(cocos2d::Ref* pSender);
    
    //获取语音验证码
    void getVoiceCodeHandler(cocos2d::Ref* pSender);
    
    //提交验证码
    void commitCodeHandler(cocos2d::Ref* pSender);
    
    //获取好友
    void getFriendsHandler(cocos2d::Ref* pSender);
    
    //获取国家列表
    void getCountriesHandler(cocos2d::Ref* pSender);
    
    //提交Userinfo
    void submitUserInfoHandler(cocos2d::Ref* pSender);
    
    //获取版本号
    void getVersionHandler(cocos2d::Ref* pSender);

    //打开警告
    void enableWarnHandler(cocos2d::Ref* pSender);
    
    //GUI 打开注册页面
    void showRegPageHandler(cocos2d::Ref* pSender);
    
    //GUI 打开好友列表
    void showContactsPageHandler(cocos2d::Ref* pSender);

    void onComplete(SMSSDKActionType action,string result) override ;
    void onError(SMSSDKActionType action,string result) override ;

    CREATE_FUNC(HelloWorld);
private:
    LabelTTF* label;
};

#endif 
