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
import android.widget.LinearLayout;

import com.mob.tools.utils.ResHelper;

import cn.smssdk.gui.ContactsListView;
import cn.smssdk.gui.PersonalInfoView;
import cn.smssdk.gui.SearchView;

//#if def{lang} == cn
/**联系人列表页面布局*/
//#elif def{lang} == en
/**contact list page layout*/
//#endif
public class ContactListPageLayout extends BasePageLayout {

	public ContactListPageLayout(Context c) {
		super(c, null);
	}

	protected void onCreateContent(LinearLayout parent) {
		// 搜索框
		SearchView searchView = new SearchView(context, true);
		parent.addView(searchView);

//		LayoutInflater inflater = LayoutInflater.from(context);
//		View personalInfoView = inflater.inflate(R.layout.smssdk_personal_info, null);
//		parent.addView(personalInfoView);

		// 我的资料
		parent.addView(PersonalInfoView.create(context));

		//通讯录列表（包括我的资料）rootView
		ContactsListView contactsList = new ContactsListView(context);
		contactsList.setId(ResHelper.getIdRes(context, "clContact"));
		LinearLayout.LayoutParams listParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT, 1);
		contactsList.setLayoutParams(listParams);
		
		parent.addView(contactsList);
	}

}
