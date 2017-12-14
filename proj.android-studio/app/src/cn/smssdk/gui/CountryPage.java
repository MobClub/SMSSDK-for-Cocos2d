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
package cn.smssdk.gui;

import android.app.Dialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mob.tools.FakeActivity;
import com.mob.tools.utils.ResHelper;

import java.util.ArrayList;
import java.util.HashMap;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.GroupListView.OnItemClickListener;
import cn.smssdk.gui.layout.CountryListPageLayout;
import cn.smssdk.utils.SMSLog;


//#if def{lang} == cn
/** 国家列表界面*/
//#elif def{lang} == en
/** The page of countries*/
//#endif
public class CountryPage extends FakeActivity implements OnClickListener, TextWatcher, OnItemClickListener {
	private String id;
	//#if def{lang} == cn
	// 国家号码规则
	//#elif def{lang} == en
	// the rule of country
	//#endif
	private HashMap<String, String> countryRules;
	private EventHandler handler;
	private CountryListView listView;
	private EditText etSearch;
	private Dialog pd;
	
	public void setCountryId(String id) {
		this.id = id;
	}
	
	public void setCountryRuls(HashMap<String, String> countryRules) {
		this.countryRules = countryRules;
	}
	
	public void onCreate() {
		if (pd != null && pd.isShowing()) {
			pd.dismiss();
		}
		pd = CommonDialog.ProgressDialog(activity);
		if (pd != null) {
			pd.show();
		}
		//#if def{lang} == cn
		// 初始化搜索引擎
		//#elif def{lang} == en
		// initialize search engine
		//#endif
		SearchEngine.prepare(activity, new Runnable() {
			public void run() {
				afterPrepare();
			}
		});
	}
	
	private void afterPrepare() {
		runOnUIThread(new Runnable() {
			public void run() {
				CountryListPageLayout page = new CountryListPageLayout(activity);
				LinearLayout layout = page.getLayout();
				
				if (layout != null) {
					activity.setContentView(layout);
				}
				
				if (countryRules == null || countryRules.size() <= 0) {
					handler = new EventHandler() {
						@SuppressWarnings("unchecked")
						public void afterEvent(int event, final int result, final Object data) {
							if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
								runOnUIThread(new Runnable() {
									public void run() {
										if (pd != null && pd.isShowing()) {
											pd.dismiss();
										}
										
										if (result == SMSSDK.RESULT_COMPLETE) {
											onCountryListGot((ArrayList<HashMap<String,Object>>) data);
										} else {
											((Throwable) data).printStackTrace();											
											int resId = ResHelper.getStringRes(activity, "smssdk_network_error");
											if (resId > 0) {
												Toast.makeText(activity, resId, Toast.LENGTH_SHORT).show();
												
											}
											finish();
										}
									}
								});
							}
						}
					};
					//#if def{lang} == cn
					// 注册回调接口
					//#elif def{lang} == en
					// register the event listener
					//#endif
					SMSSDK.registerEventHandler(handler);
					//#if def{lang} == cn
					// 获取国家列表
					//#elif def{lang} == en
					// Gettng a list of countries which can send verification code
					//#endif
					SMSSDK.getSupportedCountries();
				} else {
					if (pd != null && pd.isShowing()) {
						pd.dismiss();
					}
					initPage();
				}
			}
		});
	}

	@Override
	public void onResume(){
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}
	
	private void initPage() {
		activity.findViewById(ResHelper.getIdRes(activity, "ll_back")).setOnClickListener(this);
		activity.findViewById(ResHelper.getIdRes(activity, "ivSearch")).setOnClickListener(this);
		activity.findViewById(ResHelper.getIdRes(activity, "iv_clear")).setOnClickListener(this);
		
		int resId = ResHelper.getIdRes(activity, "clCountry");
		listView = (CountryListView) activity.findViewById(resId);
		listView.setOnItemClickListener(this);
		
		resId = ResHelper.getIdRes(activity, "et_put_identify");
		etSearch = (EditText) activity.findViewById(resId);
		etSearch.addTextChangedListener(this);
	}
	
	private void onCountryListGot(ArrayList<HashMap<String, Object>> countries) {
		//#if def{lang} == cn
		// 解析国家列表
		//#elif def{lang} == en
		// analysis the countries' data
		//#endif
		for (HashMap<String, Object> country : countries) {
			String code = (String) country.get("zone");
			String rule = (String) country.get("rule");
			if (TextUtils.isEmpty(code) || TextUtils.isEmpty(rule)) {
				continue;
			}
			
			if (countryRules == null) {
				countryRules = new HashMap<String, String>();
			}
			countryRules.put(code, rule);
		}
		//#if def{lang} == cn
		// 回归页面初始化操作
		//#elif def{lang} == en
		// come back to initialize
		//#endif
		initPage();
	}
	
	public void onItemClick(GroupListView parent, View view, int group, int position) {
		if(position >= 0){
			String[] country = listView.getCountry(group, position);
			if (countryRules != null && countryRules.containsKey(country[1])) {
				id = country[2];
				finish();
			} else {
				int resId = ResHelper.getStringRes(activity, "smssdk_country_not_support_currently");
				if (resId > 0) {
					Toast.makeText(activity, resId, Toast.LENGTH_SHORT).show();
				}
			}
		}	
	}
	
	public void onClick(View v) {
		int id = v.getId();
		int idLlBack = ResHelper.getIdRes(activity, "ll_back");
		int idIvSearch = ResHelper.getIdRes(activity, "ivSearch");
		int idIvClear = ResHelper.getIdRes(activity, "iv_clear");
		if (id == idLlBack) {
			finish();
		} else if (id == idIvSearch) {
			//#if def{lang} == cn
			// 搜索
			//#elif def{lang} == en
			// init the search view
			//#endif
			int idLlTitle = ResHelper.getIdRes(activity, "llTitle");
			activity.findViewById(idLlTitle).setVisibility(View.GONE);
			int idLlSearch = ResHelper.getIdRes(activity, "llSearch");
			activity.findViewById(idLlSearch).setVisibility(View.VISIBLE);
			etSearch.getText().clear();
			etSearch.requestFocus();
		} else if (id == idIvClear) {
			etSearch.getText().clear();
		}
	}
	
	public boolean onKeyEvent(int keyCode, KeyEvent event) {
		try {
			int resId = ResHelper.getIdRes(activity, "llSearch");
			if (keyCode == KeyEvent.KEYCODE_BACK
					&& event.getAction() == KeyEvent.ACTION_DOWN
					&& activity.findViewById(resId).getVisibility() == View.VISIBLE) {
				activity.findViewById(resId).setVisibility(View.GONE);
				resId = ResHelper.getIdRes(activity, "llTitle");
				activity.findViewById(resId).setVisibility(View.VISIBLE);
				etSearch.setText("");
				return true;
			}
		} catch (Throwable e) {
			SMSLog.getInstance().w(e);
		}
		return super.onKeyEvent(keyCode, event);
	}
	
	public boolean onFinish() {
		//#if def{lang} == cn
		// 销毁监听接口
		//#elif def{lang} == en
		// unregister the event listener
		//#endif
		SMSSDK.unregisterEventHandler(handler);
		//start activity for result
		HashMap<String, Object> res = new HashMap<String, Object>();
		res.put("id", id);
		//res.put("rules", countryRules);
		res.put("page", 1);
		setResult(res);
		return super.onFinish();
	}
	
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		
	}
	
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		listView.onSearch(s.toString().toLowerCase());
	}
	
	public void afterTextChanged(Editable s) {
		
	}

}
