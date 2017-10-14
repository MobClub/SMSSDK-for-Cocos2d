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
package cn.smssdk.gui.layout;

import android.content.Context;
import android.view.ViewGroup;

//#if def{lang} == cn
/**布局工厂*/
//#elif def{lang} == en
/**layout factory*/
//#endif
public class LayoutFactory
{
	
	static ViewGroup create(Context context,String name) {
		ViewGroup v = null;
		if(name.equals("smssdk_back_verify_dialog")) {
			v = BackVerifyDialogLayout.create(context);
		} else if(name.equals("smssdk_contact_detail_page")) {
			ContactDetailPageLayout page = new ContactDetailPageLayout(context);
			v = page.getLayout();
		} else if(name.equals("smssdk_contact_list_page")) {
			ContactListPageLayout page = new ContactListPageLayout(context);
			v = page.getLayout();
		} else if(name.equals("smssdk_contacts_listview_item")) {
			v = ContactsListviewItemLayout.create(context);
		} else if(name.equals("smssdk_country_list_page")) {
			CountryListPageLayout page = new CountryListPageLayout(context);
			v = page.getLayout();
		} else if(name.equals("smssdk_input_identify_num_page")) {
			IdentifyNumPageLayout page = new IdentifyNumPageLayout(context);
			v = page.getLayout();
		} else if(name.equals("smssdk_progress_dialog")) {
			v = ProgressDialogLayout.create(context);
		} else if(name.equals("smssdk_register_page")) {
			RegisterPageLayout layout = new RegisterPageLayout(context);
			v = layout.getLayout();
		} else if(name.equals("smssdk_send_msg_dialog")) {
			v = SendMsgDialogLayout.create(context);
		}
		
		return v;
	}
	
}
