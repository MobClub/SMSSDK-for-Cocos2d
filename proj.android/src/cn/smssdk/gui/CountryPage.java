/*
 * 官网地站:http://www.mob.com
 * 技术支持QQ: 4006852216
 * 官方微信:ShareSDK   （如果发布新版本的话，我们将会第一时间通过微信将版本更新内容推送给您。如果使用过程中有任何问题，
 * 也可以通过微信与我们取得联系，我们将会在24小时内给予回复）
 *
 * Copyright (c) 2014年 mob.com. All rights reserved.
 */
package cn.smssdk.gui;

import android.app.Dialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
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


/** 国家列表界面*/
public class CountryPage extends FakeActivity implements OnClickListener, TextWatcher, OnItemClickListener {
	private String id;
	// 国家号码规则
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
		// 初始化搜索引擎
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
					// 注册回调接口
					SMSSDK.registerEventHandler(handler);
					// 获取国家列表
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
		// 设置页面布局不随软键盘的弹出而变化，防止右侧字母ScrollBar随软键盘上下移动
		activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

		activity.findViewById(ResHelper.getIdRes(activity, "ll_back")).setOnClickListener(this);

		TextView tv = (TextView) activity.findViewById(ResHelper.getIdRes(activity, "tv_title"));
		int resId = ResHelper.getStringRes(activity, "smssdk_choose_country");
		if (resId > 0) {
			tv.setText(resId);
		}

		resId = ResHelper.getIdRes(activity, "clCountry");
		listView = (CountryListView) activity.findViewById(resId);
		listView.setOnItemClickListener(this);

		resId = ResHelper.getIdRes(activity, "et_put_identify");
		etSearch = (EditText) activity.findViewById(resId);
		etSearch.addTextChangedListener(this);
		activity.findViewById(ResHelper.getIdRes(activity, "iv_clear")).setOnClickListener(this);
	}

	private void onCountryListGot(ArrayList<HashMap<String, Object>> countries) {
		// 解析国家列表
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
		// 回归页面初始化操作
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
		int idIvClear = ResHelper.getIdRes(activity, "iv_clear");
		if (id == idLlBack) {
			finish();
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
		// 销毁监听接口
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
