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
    

    CCSize size = CCDirector::sharedDirector()->getWinSize();
    
    Scale9Sprite *box9SprY = Scale9Sprite::create("Icon@2x.png");
    EditBox *box = EditBox::create(CCSizeMake(winSize.width/2, 25), box9SprY);
    box->setPosition(ccp(winSize.width/2 + 80 , 300));
    box->setDelegate(this);
    box->setFontColor(ccc3(0, 0, 9));
    box->setPlaceHolder("请输入手机号");
    box->setPlaceholderFont("Arial", 8);
    box->setFont("Arial", 8);
    box->setMaxLength(20);
    this->addChild(box);
    phoneBox = box;
    
    Scale9Sprite *zone9SprY = Scale9Sprite::create("Icon@2x.png");
    EditBox *zBox = EditBox::create(CCSizeMake(winSize.width/2, 25), zone9SprY);
    zBox->setPosition(ccp(winSize.width/2 + 80 , 280));
    zBox->setDelegate(this);
    zBox->setFontColor(ccc3(0, 0, 9));
    zBox->setPlaceHolder("请输入区号");
    zBox->setPlaceholderFont("Arial", 8);
    zBox->setFont("Arial", 8);
    zBox->setMaxLength(5);
    this->addChild(zBox);
    zoneBox = zBox;
    
    Scale9Sprite *code9SprY = Scale9Sprite::create("Icon@2x.png");
    EditBox *vBox = EditBox::create(CCSizeMake(winSize.width/2, 25), code9SprY);
    vBox->setPosition(ccp(winSize.width/2 + 80 , 260));
    vBox->setDelegate(this);
    vBox->setFontColor(ccc3(0, 0, 9));
    vBox->setPlaceHolder("请输入验证码");
    vBox->setPlaceholderFont("Arial", 8);
    vBox->setMaxLength(6);
    vBox->setFont("Arial", 8);
    this->addChild(vBox);
    codeBox = vBox;
    
    //获取文本验证码
    MenuItemLabel *getCodeItem = MenuItemLabel::create(LabelTTF::create("Get Text Code", "Arial", 10),
                                                    CC_CALLBACK_1(HelloWorld::getTextCodeHandler, this));
    getCodeItem->setPosition(winSize.width/2 , 240);
    getCodeItem->setColor(ccc3(8, 0, 190));
    auto getCodeMenu = Menu::create(getCodeItem,NULL);
    getCodeMenu->setPosition(Vec2::ZERO);
    this->addChild(getCodeMenu);
    
    //获取语音验证码
    MenuItemLabel *getVoiceCode = MenuItemLabel::create(LabelTTF::create("Get Voice Code", "Arial", 10),
                                                       CC_CALLBACK_1(HelloWorld::getVoiceCodeHandler, this));
    getVoiceCode->setPosition(winSize.width/2 , 220);
    getVoiceCode->setColor(ccc3(8, 0, 190));
    auto getVoiceCodeMenu = Menu::create(getVoiceCode,NULL);
    getVoiceCodeMenu->setPosition(Vec2::ZERO);
    this->addChild(getVoiceCodeMenu);
    
    //提交验证码
    MenuItemLabel *commitCodeItem = MenuItemLabel::create(LabelTTF::create("Commit Code", "Arial", 10),
                                                          CC_CALLBACK_1(HelloWorld::commitCodeHandler, this));
    commitCodeItem->setPosition(winSize.width/2 , 200);
    commitCodeItem->setColor(ccc3(8, 0, 190));
    auto commitCodeMenu = Menu::create(commitCodeItem,NULL);
    commitCodeMenu->setPosition(Vec2::ZERO);
    this->addChild(commitCodeMenu);
    
    //获取国家列表
    MenuItemLabel *getCoutriesItem = MenuItemLabel::create(LabelTTF::create("GetCountryCodes", "Arial", 10),
                                                                 CC_CALLBACK_1(HelloWorld::getCountriesHandler, this));
    getCoutriesItem->setPosition(winSize.width/2 , 180);
    getCoutriesItem->setColor(ccc3(8, 0, 190));
    auto getCoutriesMenu = Menu::create(getCoutriesItem,NULL);
    getCoutriesMenu->setPosition(Vec2::ZERO);
    this->addChild(getCoutriesMenu);
    
    
    //获取通讯录好友信息
    MenuItemLabel *getFriendsItem = MenuItemLabel::create(LabelTTF::create("Get Friends", "Arial", 10),
                                                           CC_CALLBACK_1(HelloWorld::getFriendsHandler, this));
    getFriendsItem->setPosition(winSize.width/2 , 160);
    getFriendsItem->setColor(ccc3(8, 0, 190));
    auto getFriendsMenu = Menu::create(getFriendsItem,NULL);
    getFriendsMenu->setPosition(Vec2::ZERO);
    this->addChild(getFriendsMenu);
    
    //提交用户资料
    MenuItemLabel *submitUserinfoItem = MenuItemLabel::create(LabelTTF::create("Submit Userinfo", "Arial", 10),
                                                           CC_CALLBACK_1(HelloWorld::submitUserInfoHandler, this));
    submitUserinfoItem->setPosition(winSize.width/2 , 140);
    submitUserinfoItem->setColor(ccc3(8, 0, 190));
    auto submitUserinfoMenu = Menu::create(submitUserinfoItem,NULL);
    submitUserinfoMenu->setPosition(Vec2::ZERO);
    this->addChild(submitUserinfoMenu);
    
    //获取版本号
    auto getVersionItem = MenuItemLabel::create(LabelTTF::create("Get Version", "Arial", 10),
                                           CC_CALLBACK_1(HelloWorld::getVersionHandler, this));
    getVersionItem->setPosition(winSize.width/2 , 120);
    getVersionItem->setColor(ccc3(8, 0, 190));
    auto getVersionMenu = Menu::create(getVersionItem,NULL);
    getVersionMenu->setPosition(Vec2::ZERO);
    this->addChild(getVersionMenu);
    
    //设置访问通讯录权限
    MenuItemLabel *enableWarnItem = MenuItemLabel::create(LabelTTF::create("EnableWarn", "Arial", 10),
                                                         CC_CALLBACK_1(HelloWorld::enableWarnHandler, this));
    enableWarnItem->setPosition(Director::getInstance()->getWinSize().width/2 , 100);
    enableWarnItem->setColor(ccc3(8, 0, 190));
    auto enableWarn = Menu::create(enableWarnItem,NULL);
    enableWarn->setPosition(Vec2::ZERO);
    this->addChild(enableWarn);
    
    //展示注册界面
    MenuItemLabel *showRegPageItem = MenuItemLabel::create(LabelTTF::create("ShowRegPage", "Arial", 10),
                                                         CC_CALLBACK_1(HelloWorld::showRegPageHandler, this));
    showRegPageItem->setPosition(winSize.width/2 , 80);
    showRegPageItem->setColor(ccc3(8, 0, 190));
    auto showRegPageItemMenu = Menu::create(showRegPageItem,NULL);
    showRegPageItemMenu->setPosition(Vec2::ZERO);
    this->addChild(showRegPageItemMenu);
    
    //展示通讯录界面
    MenuItemLabel *showContactsPage = MenuItemLabel::create(LabelTTF::create("showContactsPage", "Arial", 10),
                                                         CC_CALLBACK_1(HelloWorld::showContactsPageHandler, this));
    showContactsPage->setPosition(winSize.width/2 , 60);
    showContactsPage->setColor(ccc3(8, 0, 190));
    auto showContactsPageMenu = Menu::create(showContactsPage,NULL);
    showContactsPageMenu->setPosition(Vec2::ZERO);
    this->addChild(showContactsPageMenu);

    label = LabelTTF::create("resultMsg:", "Arial", 8);
    label->setPosition(Point(winSize.width/2 , 40));
    label->setHorizontalAlignment(TextHAlignment::CENTER);
    label->setColor(ccc3(255, 0, 0));
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

}

void HelloWorld::editBoxTextChanged(cocos2d::extension::EditBox *editBox, const std::string &text)
{
    string str(editBox->getText());
    
    if (editBox == phoneBox)
    {
        phone = text;
    }
    
    if (editBox == zoneBox)
    {
        zone = text;
    }
    
    if (editBox == codeBox)
    {
        code = text;
    }
}

void HelloWorld::getTextCodeHandler(cocos2d::Ref* pSender)
{
    SMSSDK::getCode(TextCode,phone,zone);
}

//
void HelloWorld::getVoiceCodeHandler(cocos2d::Ref* pSender)
{
    SMSSDK::getCode(VoiceCode,phone,zone);
}

void HelloWorld::commitCodeHandler(cocos2d::Ref *pSender)
{
    SMSSDK::commitCode(phone,zone,code);
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
    userinfo.zone = zone;
    userinfo.phone = phone;

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
    string logText = StringUtils::format("action:%d|result:%s",(int)action,result.c_str());
    
    log("%s",logText.c_str());
    
    label->setString(logText);
}

void HelloWorld::onError(SMSSDKActionType action,string result)
{
    string logText = StringUtils::format("action:%d|result:%s",(int)action,result.c_str());
    
    log("%s",logText.c_str());
    
    label->setString(logText);
}
