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
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.tools.FakeActivity;
import com.mob.tools.gui.AsyncImageView;
import com.mob.tools.utils.ResHelper;

import org.json.JSONObject;

import java.util.HashMap;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.entity.Profile;
import cn.smssdk.gui.util.GUISPDB;
import cn.smssdk.utils.SMSLog;


/**
 * 头像界面
 */
public class AvatarPage extends FakeActivity implements OnClickListener {

	public static final String EXTRA_PROFILE = "extra_profile";
	private Dialog pd;

	private AsyncImageView ivAvatar;
	private Button btSubmit;
	private Profile profile;

	private String avatarUrl;

	private EventHandler handler;

	@Override
	public void onCreate() {
		super.onCreate();
		activity.setContentView(ResHelper.getLayoutRes(getContext(), "smssdk_avatar_page"));
		initView();
		SMSSDK.registerEventHandler(handler = new EventHandler() {
			@Override
			public void afterEvent(int event, int result, Object data) {
				super.afterEvent(event, result, data);

				if (pd != null && pd.isShowing()) {
					pd.dismiss();
				}

				if (result == SMSSDK.RESULT_COMPLETE) {
					if (event == SMSSDK.EVENT_SUBMIT_USER_INFO) {
						// 缓存用户资料
						GUISPDB.setProfile(profile);
						// 将结果通知给前一个页面
						HashMap<String, Object> resMap = new HashMap<String, Object>();
						resMap.put("res", true);
						setResult(resMap);
						finish();
					}
				} else {
					if (event == SMSSDK.EVENT_SUBMIT_USER_INFO) {
						int status;
						try {
							((Throwable) data).printStackTrace();
							Throwable throwable = (Throwable) data;

							JSONObject object = new JSONObject(
									throwable.getMessage());
							String des = object.optString("detail");
							status = object.optInt("status");
							if (!TextUtils.isEmpty(des)) {
								Toast.makeText(activity, des, Toast.LENGTH_SHORT).show();
								return;
							}
						} catch (Exception e) {
							SMSLog.getInstance().w(e);
						}
					}
				}
			}
		});
	}

	public void show(Context context) {
		show(context, null);
	}

	private void initView(){
		TextView tv = findViewById(ResHelper.getIdRes(getContext(), "tv_left"));
		tv.setText("");
		tv.setOnClickListener(this);

		tv = findViewById(ResHelper.getIdRes(getContext(), "tv_title"));
		tv.setText("");

		tv = findViewById(ResHelper.getIdRes(getContext(), "tv_right"));
		tv.setVisibility(View.INVISIBLE);

		ivAvatar = findViewById(ResHelper.getIdRes(getContext(), "iv_avatar"));
		ivAvatar.setRound(ResHelper.dipToPx(getContext(), 120 / 2));
		ivAvatar.setOnClickListener(this);
		btSubmit = findViewById(ResHelper.getIdRes(getContext(), "bt_submit_profile"));
		btSubmit.setOnClickListener(this);

		Intent i = activity.getIntent();
		if (i != null) {
			profile = (Profile)i.getSerializableExtra(EXTRA_PROFILE);
		}
		if (profile != null) {
			refreshAvator(profile.getAvatar(), profile.getNickName());
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == ResHelper.getIdRes(getContext(), "tv_left")) {
			finish();
		} else if (id == ResHelper.getIdRes(getContext(), "iv_avatar")) {
			AvatarPickerPage page = new AvatarPickerPage();
			page.showForResult(activity, null, this);
		} else if (id == ResHelper.getIdRes(getContext(), "bt_submit_profile")) {
			// 提交用户资料
			submitProfile();
		}
	}

	private void submitProfile() {
		if (profile == null) {
			Toast.makeText(getContext(), ResHelper.getStringRes(getContext(), "smssdk_msg_profile_empty"), Toast.LENGTH_SHORT).show();
			return;
		}
		profile.setAvatar(avatarUrl);
		String text = ((TextView)findViewById(ResHelper.getIdRes(getContext(), "et_nickname"))).getText().toString();
		profile.setNickName(text);

		if (pd != null && pd.isShowing()) {
			pd.dismiss();
		}
		pd = CommonDialog.ProgressDialog(activity);
		if (pd != null) {
			pd.show();
		}
		SMSSDK.submitUserInfo(profile.getUid(), profile.getNickName(), profile.getAvatar(), profile.getCountry(), profile.getPhoneNum());
	}

	@Override
	public void onResult(HashMap<String, Object> data) {
		super.onResult(data);
		String url = null != data ? String.valueOf(data.get(AvatarPickerPage.INTENT_PICK_URL)) : null;
		refreshAvator(url, null);
	}

	private void refreshAvator(String url, String nick) {
		avatarUrl = url;
		if (!TextUtils.isEmpty(url)) {
			final Context context = activity;
			ivAvatar.execute(avatarUrl, ResHelper.getBitmapRes(context, "smssdk_cp_default_avatar"));
			TextView tv = findViewById(ResHelper.getIdRes(getContext(), "tv_avatar"));
			tv.setVisibility(View.INVISIBLE);
		}

		if (!TextUtils.isEmpty(nick)) {
			TextView tv = findViewById(ResHelper.getIdRes(getContext(), "et_nickname"));
			tv.setText(nick);
		}
	}

	@Override
	public void onDestroy() {
		SMSSDK.unregisterEventHandler(handler);
	}
}
