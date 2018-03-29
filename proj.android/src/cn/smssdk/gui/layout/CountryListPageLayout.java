/*
 * 官网地站:http://www.mob.com
 * 技术支持QQ: 4006852216
 * 官方微信:ShareSDK   （如果发布新版本的话，我们将会第一时间通过微信将版本更新内容推送给您。如果使用过程中有任何问题，
 * 也可以通过微信与我们取得联系，我们将会在24小时内给予回复）
 *
 * Copyright (c) 2014年 mob.com. All rights reserved.
 */
package cn.smssdk.gui.layout;

import android.content.Context;
import android.widget.LinearLayout;

import com.mob.tools.utils.ResHelper;

import cn.smssdk.gui.CountryListView;
import cn.smssdk.gui.SearchView;

/**国家列表页面布局*/
public class CountryListPageLayout extends BasePageLayout {

	public CountryListPageLayout(Context c) {
		super(c, null);
	}

	protected void onCreateContent(LinearLayout parent) {
		// 搜索框
		SearchView searchView = new SearchView(context, true);
		parent.addView(searchView);

		// 国家列表rootView
		CountryListView countryList = new CountryListView(context);
		countryList.setId(ResHelper.getIdRes(context, "clCountry"));
		LinearLayout.LayoutParams listParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT, 1);
		countryList.setLayoutParams(listParams);
		countryList.setBackgroundResource(ResHelper.getColorRes(context, "smssdk_bg_gray"));

		parent.addView(countryList);
	}

}
