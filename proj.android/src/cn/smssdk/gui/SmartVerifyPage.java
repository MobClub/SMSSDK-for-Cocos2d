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
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mob.tools.FakeActivity;
import com.mob.tools.utils.ResHelper;

import java.util.HashMap;

import cn.smssdk.gui.layout.BackVerifyDialogLayout;
import cn.smssdk.gui.layout.IdentifyNumPageLayout;

/** 智能验证码页面*/
public class SmartVerifyPage extends FakeActivity implements OnClickListener {
	private static final int RETRY_INTERVAL = 60;

	private String phone;
	private String code;
	private String formatedPhone;
	private int time = RETRY_INTERVAL;

	private Dialog pd;

	private EditText etIdentifyNum;
	private TextView tvResend;
	private LinearLayout llVoice;
	private TextView tvPhone;
	private TextView tvIdentifyNotify;
	private ImageView ivClear;
	private Button btnSubmit;
	private boolean showSmart = false;

	public void setPhone(String phone, String code, String formatedPhone) {
		this.phone = phone;
		this.code = code;
		this.formatedPhone = formatedPhone;
	}

	public void onCreate() {
		IdentifyNumPageLayout page = new IdentifyNumPageLayout(activity);
		LinearLayout layout = page.getLayout();

		if (layout != null) {
			activity.setContentView(layout);
			activity.findViewById(ResHelper.getIdRes(activity, "ll_back")).setOnClickListener(this);

			btnSubmit = (Button) activity.findViewById(ResHelper.getIdRes(activity, "btn_submit"));
			btnSubmit.setOnClickListener(this);
			btnSubmit.setEnabled(false);

			etIdentifyNum = (EditText) activity.findViewById(ResHelper.getIdRes(activity, "et_put_identify"));

			tvResend = (TextView) activity.findViewById(ResHelper.getIdRes(activity, "tv_resend"));

			llVoice = (LinearLayout) activity.findViewById(ResHelper.getIdRes(activity, "ll_voice"));

			tvIdentifyNotify = (TextView) activity.findViewById(ResHelper.getIdRes(activity, "tv_identify_notify"));
			int resId = ResHelper.getStringRes(activity, "smssdk_send_mobile_detail");
			if (resId > 0) {
				String text = getContext().getString(resId);
				tvIdentifyNotify.setText(Html.fromHtml(text));
			}

			tvPhone = (TextView) activity.findViewById(ResHelper.getIdRes(activity, "tv_phone"));
			tvPhone.setText(formatedPhone);

			ivClear = (ImageView) activity.findViewById(ResHelper.getIdRes(activity, "iv_clear"));
			ivClear.setOnClickListener(this);

			countDown();
		}

	}

	/** 倒数计时 */
	private void countDown() {
		runOnUIThread(new Runnable() {
			public void run() {
				time--;
				if (time == RETRY_INTERVAL - 2) {
					btnSubmit.setEnabled(true);
					int resId = ResHelper.getBitmapRes(activity, "smssdk_btn_enable");
					if (resId > 0) {
						btnSubmit.setBackgroundResource(resId);
					}
					resId = ResHelper.getStringRes(activity, "smssdk_smart_verify_already");
					etIdentifyNum.setText(resId);
					etIdentifyNum.setEnabled(false);

					tvResend.setVisibility(View.GONE);
					llVoice.setVisibility(View.GONE);

					resId = ResHelper.getStringRes(activity, "smssdk_smart_verify_tips");
					tvIdentifyNotify.setText(resId);

					showSmart = true;
					time = RETRY_INTERVAL;
				} else {
					runOnUIThread(this, 1000);
				}
			}
		}, 1000);
	}

	public void onClick(View v) {
		int id = v.getId();
		int idLlBack = ResHelper.getIdRes(activity, "ll_back");
		int idBtnSubmit = ResHelper.getIdRes(activity, "btn_submit");
		int idIvClear = ResHelper.getIdRes(activity, "iv_clear");

		if (id == idLlBack) {
			if(showSmart) {
				finish();
				return;
			}
			runOnUIThread(new Runnable() {
				public void run() {
					showNotifyDialog();
				}
			});
		} else if (id == idBtnSubmit) {
			// 提交验证码
			HashMap<String, Object> resp = new HashMap<String, Object>();
			resp.put("country", code);
			resp.put("phone", phone);
			afterSubmit(resp);

		} else if (id == idIvClear) {
			etIdentifyNum.getText().clear();
		}
	}


	/**
	 * 提交验证码成功后的执行事件
	 *
	 * @param result
	 * @param data
	 */
	private void afterSubmit(final Object data) {
		runOnUIThread(new Runnable() {
			public void run() {
				if (pd != null && pd.isShowing()) {
					pd.dismiss();
				}

				HashMap<String, Object> res = new HashMap<String, Object>();
				res.put("res", true);
				res.put("page", 2);
				res.put("phone", data);
				setResult(res);
				finish();

			}
		});
	}

	/** 按返回键时，弹出的提示对话框 */
	private void showNotifyDialog() {
		int resId = ResHelper.getStyleRes(activity, "CommonDialog");
		if (resId > 0) {
			final Dialog dialog = new Dialog(getContext(), resId);

			LinearLayout layout = BackVerifyDialogLayout.create(activity);

			if (layout != null) {
				dialog.setContentView(layout);

				resId = ResHelper.getIdRes(activity, "tv_dialog_hint");
				TextView tv = (TextView) dialog.findViewById(resId);
				resId = ResHelper.getStringRes(activity,
						"smssdk_close_identify_page_dialog");
				if (resId > 0) {
					tv.setText(resId);
				}
				resId = ResHelper.getIdRes(activity, "btn_dialog_ok");
				Button waitBtn = (Button) dialog.findViewById(resId);
				resId = ResHelper.getStringRes(activity, "smssdk_wait");
				if (resId > 0) {
					waitBtn.setText(resId);
				}
				waitBtn.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
				resId = ResHelper.getIdRes(activity, "btn_dialog_cancel");
				Button backBtn = (Button) dialog.findViewById(resId);
				resId = ResHelper.getStringRes(activity, "smssdk_back");
				if (resId > 0) {
					backBtn.setText(resId);
				}
				backBtn.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						dialog.dismiss();
						finish();
					}
				});
				dialog.setCanceledOnTouchOutside(true);
				dialog.show();
			}
		}
	}

	@Override
	public boolean onKeyEvent(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			finish();
			return true;
		} else {
			return false;
		}
	}

}
