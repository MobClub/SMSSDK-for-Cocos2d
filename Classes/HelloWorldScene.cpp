#include "HelloWorldScene.h"
#include "SMSSDK.h"
#include "SimpleAudioEngine.h"

USING_NS_CC;

using namespace std;
using namespace smssdk;
using namespace CocosDenshion;

Scene* HelloWorld::createScene()
{
    auto scene = Scene::create();
    auto layer = HelloWorld::create();
    
    scene->addChild(layer);
    return scene;
}

bool HelloWorld::init()
{
    if ( !Layer::init() )
    {
        return false;
    }
    
    Size winSize = Director::getInstance()->getWinSize();
    Vec2 origin = Director::getInstance()->getVisibleOrigin();
    
    //获取文本验证码
    MenuItemLabel *getCodeItem = MenuItemLabel::create(LabelTTF::create("Get Text Code", "Arial", 10),
                                                    CC_CALLBACK_1(HelloWorld::getTextCodeHandler, this));
    getCodeItem->setPosition(winSize.width/2 , 275);
    auto getCodeMenu = Menu::create(getCodeItem,NULL);
    getCodeMenu->setPosition(Vec2::ZERO);
    this->addChild(getCodeMenu);
    
    //获取语音验证码
    MenuItemLabel *getVoiceCode = MenuItemLabel::create(LabelTTF::create("Get Voice Code", "Arial", 10),
                                                       CC_CALLBACK_1(HelloWorld::getVoiceCodeHandler, this));
    getVoiceCode->setPosition(winSize.width/2 , 250);
    auto getVoiceCodeMenu = Menu::create(getVoiceCode,NULL);
    getVoiceCodeMenu->setPosition(Vec2::ZERO);
    this->addChild(getVoiceCodeMenu);
    
//    //提交验证码
//    MenuItemLabel *commitCodeItem = MenuItemLabel::create(LabelTTF::create("Commit Code", "Arial", 10),
//                                                          CC_CALLBACK_1(HelloWorld::commitCodeHandler, this));
//    commitCodeItem->setPosition(winSize.width/2 , 300);
//    auto commitCodeMenu = Menu::create(commitCodeItem,NULL);
//    commitCodeMenu->setPosition(Vec2::ZERO);
//    this->addChild(commitCodeMenu);
    
    
    //提交验证码，输入完毕后敲回车键
    CCSize size = CCDirector::sharedDirector()->getWinSize();
    Scale9Sprite* sacel9SprY = Scale9Sprite::create("Icon@2x.png");
    EditBox* box = EditBox::create(CCSizeMake(winSize.width/2, 25), sacel9SprY);
    //设置位置
    box->setPosition(ccp(winSize.width/2 + 75 , 225));
    box->setDelegate(this);
    //设置文本的颜色
    box->setFontColor(ccc3(255, 0, 0));
    
    //当编辑框中没有任何字符的提示
    box->setPlaceHolder("请在此处输入验证码");
    box->setPlaceholderFont("Arial", 10);
    //最大输入文本长度
    box->setMaxLength(4);
    this->addChild(box);
    
    
    //获取国家列表
    MenuItemLabel *getCoutriesItem = MenuItemLabel::create(LabelTTF::create("GetCountryCodes", "Arial", 10),
                                                                 CC_CALLBACK_1(HelloWorld::getCountriesHandler, this));
    getCoutriesItem->setPosition(winSize.width/2 , 200);
    auto getCoutriesMenu = Menu::create(getCoutriesItem,NULL);
    getCoutriesMenu->setPosition(Vec2::ZERO);
    this->addChild(getCoutriesMenu);
    
    
    //获取通讯录好友信息
    MenuItemLabel *getFriendsItem = MenuItemLabel::create(LabelTTF::create("Get Friends", "Arial", 10),
                                                           CC_CALLBACK_1(HelloWorld::getFriendsHandler, this));
    getFriendsItem->setPosition(winSize.width/2 , 175);
    auto getFriendsMenu = Menu::create(getFriendsItem,NULL);
    getFriendsMenu->setPosition(Vec2::ZERO);
    this->addChild(getFriendsMenu);
    
    //提交用户资料
    MenuItemLabel *submitUserinfoItem = MenuItemLabel::create(LabelTTF::create("Submit Userinfo", "Arial", 10),
                                                           CC_CALLBACK_1(HelloWorld::submitUserInfoHandler, this));
    submitUserinfoItem->setPosition(winSize.width/2 , 150);
    auto submitUserinfoMenu = Menu::create(submitUserinfoItem,NULL);
    submitUserinfoMenu->setPosition(Vec2::ZERO);
    this->addChild(submitUserinfoMenu);
    
    //获取版本号
    auto getVersionItem = MenuItemLabel::create(LabelTTF::create("Get Version", "Arial", 10),
                                           CC_CALLBACK_1(HelloWorld::getVersionHandler, this));
    getVersionItem->setPosition(winSize.width/2 , 125);
    
    auto getVersionMenu = Menu::create(getVersionItem,NULL);
    getVersionMenu->setPosition(Vec2::ZERO);
    this->addChild(getVersionMenu);
    
    //设置访问通讯录权限
    MenuItemLabel *enableWarnItem = MenuItemLabel::create(LabelTTF::create("EnableWarn", "Arial", 10),
                                                         CC_CALLBACK_1(HelloWorld::enableWarnHandler, this));
    enableWarnItem->setPosition(Director::getInstance()->getWinSize().width/2 , 100);
    auto enableWarn = Menu::create(enableWarnItem,NULL);
    enableWarn->setPosition(Vec2::ZERO);
    this->addChild(enableWarn);
    
    //展示注册界面
    MenuItemLabel *showRegPageItem = MenuItemLabel::create(LabelTTF::create("ShowRegPage", "Arial", 10),
                                                         CC_CALLBACK_1(HelloWorld::showRegPageHandler, this));
    showRegPageItem->setPosition(winSize.width/2 , 75);
    auto showRegPageItemMenu = Menu::create(showRegPageItem,NULL);
    showRegPageItemMenu->setPosition(Vec2::ZERO);
    this->addChild(showRegPageItemMenu);
    
    //展示通讯录界面
    MenuItemLabel *showContactsPage = MenuItemLabel::create(LabelTTF::create("showContactsPage", "Arial", 10),
                                                         CC_CALLBACK_1(HelloWorld::showContactsPageHandler, this));
    showContactsPage->setPosition(winSize.width/2 , 50);
    auto showContactsPageMenu = Menu::create(showContactsPage,NULL);
    showContactsPageMenu->setPosition(Vec2::ZERO);
    this->addChild(showContactsPageMenu);

    label = LabelTTF::create("resultMsg:", "Arial", 8);
    label->setPosition(Point(winSize.width/2 , 25));
    label->setHorizontalAlignment(TextHAlignment::CENTER);
    this->addChild(label);
    
    SMSSDK::setHandler(this);
    return true;
}

void HelloWorld::editBoxEditingDidBegin(cocos2d::extension::EditBox *editBox)
{
    
}

void HelloWorld::editBoxEditingDidEnd(cocos2d::extension::EditBox *editBox)
{
   
}

void HelloWorld::editBoxReturn(cocos2d::extension::EditBox *editBox)
{
    CCLOG("editboxreturn");
    string phone("186******");
    string zone("86");
    SMSSDK::commitCode(phone,zone,editBox->getText());
}

void HelloWorld::editBoxTextChanged(cocos2d::extension::EditBox *editBox, const std::string &text)
{
}


void HelloWorld::getTextCodeHandler(cocos2d::Ref* pSender)
{    
    string phone("18021058213");
    string zone("86");
    SMSSDK::getCode(TextCode,phone,zone);
}

//
void HelloWorld::getVoiceCodeHandler(cocos2d::Ref* pSender)
{
    string phone("18021058213");
    string zone("86");
    SMSSDK::getCode(VoiceCode,phone,zone);
}

void HelloWorld::commitCodeHandler(cocos2d::Ref *pSender)
{
    string phone("18021058213");
    string zone("86");
    SMSSDK::commitCode(phone,zone,"6358");
}

void HelloWorld::getFriendsHandler(cocos2d::Ref *pSender)
{
    SMSSDK::getFriends();
}

void HelloWorld::getCountriesHandler(cocos2d::Ref *pSender)
{
    SMSSDK::getSupportedCountries();
}

void HelloWorld::submitUserInfoHandler(cocos2d::Ref *pSender)
{
    UserInfo userinfo;
    userinfo.uid = "1111";
    userinfo.nickname = "test";
    userinfo.avatar = "https://qq.com/";
    userinfo.zone = "86";
    userinfo.phone = "18666668888";

    SMSSDK::submitUserInfo(userinfo);
}

void HelloWorld::getVersionHandler(cocos2d::Ref *pSender)
{    
    //获取版本号
	string version = SMSSDK::getVersion();
    log("version:%s",version.c_str());
    label->setString(version);
}

void HelloWorld::enableWarnHandler(cocos2d::Ref *pSender)
{    
    //设置访问通讯录权限
    SMSSDK::enableWarn(false);
}


void HelloWorld::showRegPageHandler(cocos2d::Ref *pSender)
{    
    SMSSDK::showRegisterPage(SMSSDKCodeType(1));
}

void HelloWorld::showContactsPageHandler(cocos2d::Ref *pSender)
{    
    SMSSDK::showContactsPage();
}

void HelloWorld::onComplete(SMSSDKActionType action,string result)
{
    log("action:%d|result:%s",(int)action,result.c_str());
    label->setString(result);
}

void HelloWorld::onError(SMSSDKActionType action,string result)
{
    log("action:%d|result:%s",(int)action,result.c_str());
    label->setString(result);
}
