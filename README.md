SMSSDK for Cocos2d-x
===
### This is a sample of SMSSDK for Cocos2d-x.
**supported original SMSSDK version:**

- Android - V3.1.0~
- iOS - V3.1.0~
- Cocos2d-x v3.1.0~

----------------------------------------------------

##The notes for fast integration of Cocos2D-x##

### *Integration of general part*

###### Step 1 : Download Cocos2D-x bridge of SMSSDK

Open Github and download [SMSSDK-for-Cocos2d](https://github.com/MobClub/SMSSDK-for-Cocos2d) section. Copy SMSSDK folder under Classes to your own Classes folder.
For Eclipse user, use proj.android.For Android Studio(Need 2.2 or up) user,use proj.android-studio.IOS user use proj.ios_mac. 

###### Step 2 : Use SDK

1.Please import Header first :

> * #include "SMSSDK.h"

> * using namespace smssdk;



2.Implements SMSSDKHandler and set it to SDK

add a class to implement SMSSDKHandler, then set it to SDK to handle callback

class Demo:SMSSDKHandler
....
SMSSDK::setHandler(demo);

3.now you can use SDK to do something

SMSSDK.getCode(SMSCodeType(0),"86","18688888888");
SMSSDK.getFriends();

#### About Callback data
Some APIs will send data to your SMSSDKHandler instance which you set.This callback data is a json string.You can use  any JSON library to handle it.The action is what API you call.

onComplete(SMSSDKActionType action, string resp)
onError(SMSSDKActionType action, string resp)

#### About GUI

This two APIs bellow is GUI APIs.

showRegisterPage(SMSCodeType getCodeMethodType)
showContactsPage()
## For Android setting
If you want to  use this GUI, you can add ShortMessageSDKGUI library in Eclipse. This ShortMessageSDKGUI library is open source at [SMSSDK for Android](https://github.com/MobClub/SMSSDK-for-Android),you can modify whatever you want to do.
For Android Studio, a SMSSDKGUI.aar is already put in.

## For iOS setting
If you don't want to  use this GUI,you can note the methods in SMSSDK class,or do nothing in the method'body in the bridge file of your xcode project.

Until now,the Cocos2d-x party is everything ok,the next you need to import the SMSSDK to the project.If your want to see the method of importting the SDK,please click here:[The Document of importting SMSSDK for iOS](https://github.com/MobClub/SMSSDK-for-iOS)

**Finally, if you have any other questions, please do not be hesitate to contact us:**

> * Customer Service QQ : 4006852216

> * or Skype:amber

More information About SMSSDK, please visit our website [Mob.com](http://www.mob.com)
