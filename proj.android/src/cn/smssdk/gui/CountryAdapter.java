/*
 * 官网地站:http://www.mob.com
 * 技术支持QQ: 4006852216
 * 官方微信:ShareSDK   （如果发布新版本的话，我们将会第一时间通过微信将版本更新内容推送给您。如果使用过程中有任何问题，
 * 也可以通过微信与我们取得联系，我们将会在24小时内给予回复）
 *
 * Copyright (c) 2014年 mob.com. All rights reserved.
 */
package cn.smssdk.gui;

import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mob.tools.utils.ResHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import cn.smssdk.SMSSDK;
import cn.smssdk.gui.GroupListView.GroupAdapter;
import cn.smssdk.gui.layout.SizeHelper;
import cn.smssdk.utils.SMSLog;


/** 自定义的国家列表，适配器，用于填充国家listview*/
public class CountryAdapter extends GroupAdapter {
	private HashMap<Character, ArrayList<String[]>> rawData;
	private ArrayList<String> titles;
	private ArrayList<ArrayList<String[]>> countries;
	private SearchEngine sEngine;

	public CountryAdapter(GroupListView view) {
		super(view);
		rawData = SMSSDK.getGroupedCountryList();
		initSearchEngine();
		search(null);
	}

	private void initSearchEngine() {
		sEngine = new SearchEngine();
		ArrayList<String> countries = new ArrayList<String>();
		for (Entry<Character, ArrayList<String[]>> ent : rawData.entrySet()) {
			ArrayList<String[]> cl = ent.getValue();
			for (String[] paire : cl) {
				countries.add(paire[0]);
			}
		}
		sEngine.setIndex(countries);
	}

	/**
	 * 搜索
	 * @param token
	 */
	public void search(String token) {
		ArrayList<String> res = sEngine.match(token);
		boolean isEmptyToken = false;
		if (res == null || res.size() <= 0) {
			res = new ArrayList<String>();
			isEmptyToken = true;
		}

		HashMap<String, String> resMap = new HashMap<String, String>();
		for (String r : res) {
			resMap.put(r, r);
		}

		titles = new ArrayList<String>();
		countries = new ArrayList<ArrayList<String[]>>();
		for (Entry<Character, ArrayList<String[]>> ent : rawData.entrySet()) {
			ArrayList<String[]> cl = ent.getValue();
			ArrayList<String[]> list = new ArrayList<String[]>();
			for (String[] paire : cl) {
				if (isEmptyToken || resMap.containsKey(paire[0])) {
					list.add(paire);
				}
			}
			if (list.size() > 0) {
				titles.add(String.valueOf(ent.getKey()));
				countries.add(list);
			}
		}

//		boolean isEmptyToken = TextUtils.isEmpty(token);
//
//		titles = new ArrayList<String>();
//		countries = new ArrayList<ArrayList<String[]>>();
//		for (Entry<Character, ArrayList<String[]>> ent : rawData.entrySet()) {
//			ArrayList<String[]> cl = ent.getValue();
//			ArrayList<String[]> list = new ArrayList<String[]>();
//			for (String[] paire : cl) {
//				if (isEmptyToken || paire[0].contains(token)) {
//					list.add(paire);
//				}
//			}
//			if (list.size() > 0) {
//				titles.add(String.valueOf(ent.getKey()));
//				countries.add(list);
//			}
//		}
	}

	public int getGroupCount() {
		return titles == null ? 0 : titles.size();
	}

	public int getCount(int group) {
		if (countries == null) {
			return 0;
		}

		ArrayList<String[]> list = countries.get(group);
		if (list == null) {
			return 0;
		}

		return list.size();
	}

	public String getGroupTitle(int group) {
		if(titles.size() != 0){
			return titles.get(group).toString();
		} else {
			return null;
		}
	}

	public String[] getItem(int group, int position) {
		String[] countriesArray = null;
		if(countries.size() != 0){
			try {
				countriesArray = countries.get(group).get(position);
			} catch (Throwable e) {
				SMSLog.getInstance().w(e);
			}
			return countriesArray;
		} else {
			return null;
		}
	}

	/** 获取组标题的view,如 组 A*/
	public View getTitleView(int group, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LinearLayout ll = new LinearLayout(parent.getContext());
			ll.setOrientation(LinearLayout.VERTICAL);
			ll.setBackgroundResource(ResHelper.getColorRes(parent.getContext(), "smssdk_bg_gray"));
			ll.setPadding(ResHelper.dipToPx(parent.getContext(), 10), 0, 0, 0);
			convertView = ll;

			SizeHelper.prepare(parent.getContext());

			TextView tv = new TextView(parent.getContext());
			tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, SizeHelper.fromPxWidth(16));
			int resId = ResHelper.getColorRes(parent.getContext(), "smssdk_lv_title_color");
			if (resId > 0) {
				tv.setTextColor(parent.getContext().getResources().getColor(resId));
			}
			int dp6 = SizeHelper.fromPxWidth(14);
			tv.setPadding(0, dp6, 0, dp6);
			tv.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			ll.addView(tv);
		}

		String title = getGroupTitle(group);
		TextView tv = (TextView) ((LinearLayout) convertView).getChildAt(0);
		tv.setText(title);
		return convertView;
	}

	/** listview 滑动时，改变组的标题 */
	public void onGroupChange(View titleView, String title) {
		TextView tv = (TextView) ((LinearLayout) titleView).getChildAt(0);
		tv.setText(title);
	}

	/** 设置国家列表listview组中的item项 */
	public View getView(int group, int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LinearLayout ll = new LinearLayout(parent.getContext());
			ll.setBackgroundColor(0xffffffff);
			ll.setPadding(ResHelper.dipToPx(parent.getContext(), 10), 0, ResHelper.dipToPx(parent.getContext(), 40), 0);
			convertView = ll;

			SizeHelper.prepare(parent.getContext());

			// 国家列表item的国家名，如“中国”
			TextView tv = new TextView(parent.getContext());
			int resId = ResHelper.getColorRes(parent.getContext(), "smssdk_lv_tv_color");
			if (resId > 0) {
				tv.setTextColor(parent.getContext().getResources().getColor(resId));
			}
			tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, SizeHelper.fromPxWidth(24));
			int dp16 = SizeHelper.fromPxWidth(30);
			tv.setPadding(0, dp16, 0, dp16);
			LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
			tvParams.weight = 1;
			ll.addView(tv, tvParams);

			// 国家列表item右侧的国家代码，如“+86”
			TextView tvCode = new TextView(parent.getContext());
			resId = ResHelper.getColorRes(parent.getContext(), "smssdk_tv_light_gray");
			if (resId > 0) {
				tvCode.setTextColor(parent.getContext().getResources().getColor(resId));
			}
			tvCode.setTextSize(TypedValue.COMPLEX_UNIT_PX, SizeHelper.fromPxWidth(24));
			tvCode.setPadding(0, dp16, 0, dp16);
			ll.addView(tvCode, new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		}

		String[] data = getItem(group, position);
		if(data != null){
			// 设置国家名
			TextView tv = (TextView) ((LinearLayout) convertView).getChildAt(0);
			tv.setText(data[0]);
			// 设置国家代码
			TextView tvCode = (TextView) ((LinearLayout) convertView).getChildAt(1);
			tvCode.setText("+" + data[1]);
		}
		return convertView;
	}

}
