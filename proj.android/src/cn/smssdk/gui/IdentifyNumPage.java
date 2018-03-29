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
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.IntentFilter;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.tools.FakeActivity;
import com.mob.tools.utils.DeviceHelper;
import com.mob.tools.utils.ResHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.layout.BackVerifyDialogLayout;
import cn.smssdk.gui.layout.IdentifyNumPageLayout;
import cn.smssdk.gui.layout.SendMsgDialogLayout;
import cn.smssdk.utils.SMSLog;


/** 验证码输入页面*/
public class IdentifyNumPage extends FakeActivity implements OnClickListener,
		TextWatcher {
	private static final int RETRY_INTERVAL = 60;
	private static final int MIN_REQUEST_VOICE_VERIFY_INTERVAL = 1000;
	private String phone;
	private String code;
	private String formatedPhone;
	private int time = RETRY_INTERVAL;
	private EventHandler handler;
	private Dialog pd;

	private EditText etIdentifyNum;
	private TextView tvPhone;
	private TextView tvIdentifyNotify;
	private ImageView ivClear;
	private TextView tvResend;
	private Button btnSubmit;
	private TextView btnSounds;
	private BroadcastReceiver smsReceiver;
	private long lastRequestVVTime;
	private String tempCode;

	public void setPhone(String phone, String code, String formatedPhone) {
		this.phone = phone;
		this.code = code;
		this.formatedPhone = formatedPhone;
	}

	public void setTempCode(String tempCode) {
		this.tempCode = tempCode;
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
			etIdentifyNum.addTextChangedListener(this);

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

			tvResend = (TextView) activity.findViewById(ResHelper.getIdRes(activity, "tv_resend"));
			tvResend.setOnClickListener(this);

			btnSounds = (TextView) activity.findViewById(ResHelper.getIdRes(activity, "tv_voice"));
			btnSounds.setOnClickListener(this);

			handler = new EventHandler() {
				public void afterEvent(int event, int result, Object data) {
					if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
						/** 提交验证码 */
						afterSubmit(result, data);
					} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
						/** 获取验证码成功后的执行动作 */
						afterGet(result, data);
					} else if (event == SMSSDK.EVENT_GET_VOICE_VERIFICATION_CODE) {
						/** 获取语音版验证码成功后的执行动作 */
						afterGetVoice(result, data);
					}
				}
			};
			SMSSDK.registerEventHandler(handler);
			countDown();
		}

		/* 注册短信接受Receiver
		 * 如被某鹅误报，可去掉此处代码
		 */
		try {
			if (DeviceHelper.getInstance(activity).checkPermission("android.permission.RECEIVE_SMS")) {
				smsReceiver = new SMSReceiver(new SMSSDK.VerifyCodeReadListener() {
					public void onReadVerifyCode(final String verifyCode) {
						runOnUIThread(new Runnable() {
							public void run() {
								etIdentifyNum.setText(verifyCode);
							}
						});
					}
				});
				activity.registerReceiver(smsReceiver, new IntentFilter(
						"android.provider.Telephony.SMS_RECEIVED"));
			}
		} catch (Throwable t) {
			t.printStackTrace();
			smsReceiver = null;
		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	public boolean onFinish() {
		SMSSDK.unregisterEventHandler(handler);
		if (smsReceiver != null) {
			try {
				activity.unregisterReceiver(smsReceiver);
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
		return super.onFinish();
	}

	/** 倒数计时 */
	private void countDown() {
		runOnUIThread(new Runnable() {
			public void run() {
				time--;
				setResendText(time);
				if (time == 0) {
//					int resId = ResHelper.getStringRes(activity,
//							"smssdk_unreceive_identify_code");
//					if (resId > 0) {
//						String unReceive = getContext().getString(resId, time);
//						tvUnreceiveIdentify.setText(Html.fromHtml(unReceive));
//					}
//					tvUnreceiveIdentify.setEnabled(true);
					time = RETRY_INTERVAL;
				} else {
//					int resId = ResHelper.getStringRes(activity, "smssdk_receive_msg");
//					if (resId > 0) {
//						String unReceive = getContext().getString(resId, time);
//						tvUnreceiveIdentify.setText(Html.fromHtml(unReceive));
//					}
//					tvUnreceiveIdentify.setEnabled(false);
					runOnUIThread(this, 1000);
				}
			}
		}, 1000);
	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// 如果输入框木有，就隐藏delbtn
		if (s.length() > 0) {
			btnSubmit.setEnabled(true);
			ivClear.setVisibility(View.VISIBLE);
			int resId = ResHelper.getBitmapRes(activity, "smssdk_btn_enable");
			if (resId > 0) {
				btnSubmit.setBackgroundResource(resId);
			}
		} else {
			btnSubmit.setEnabled(false);
			ivClear.setVisibility(View.GONE);
			int resId = ResHelper.getBitmapRes(activity, "smssdk_btn_disenable");
			if (resId > 0) {
				btnSubmit.setBackgroundResource(resId);
			}
		}
	}

	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	public void afterTextChanged(Editable s) {
	}

	public void onClick(View v) {
		int id = v.getId();
		int idLlBack = ResHelper.getIdRes(activity, "ll_back");
		int idBtnSubmit = ResHelper.getIdRes(activity, "btn_submit");
		int idIvClear = ResHelper.getIdRes(activity, "iv_clear");
		int idBtnSounds = ResHelper.getIdRes(activity, "tv_voice");
		int idTvResend = ResHelper.getIdRes(activity, "tv_resend");

		if (id == idLlBack) {
			runOnUIThread(new Runnable() {
				public void run() {
					showNotifyDialog();
				}
			});
		} else if (id == idBtnSubmit) {
			// 提交验证码
			String verificationCode = etIdentifyNum.getText().toString().trim();
			if (!TextUtils.isEmpty(code)) {
				if (pd != null && pd.isShowing()) {
					pd.dismiss();
				}
				pd = CommonDialog.ProgressDialog(activity);
				if (pd != null) {
					pd.show();
				}
				SMSSDK.submitVerificationCode(code, phone, verificationCode);
			} else {
				int resId = ResHelper.getStringRes(activity, "smssdk_write_identify_code");
				if (resId > 0) {
					Toast.makeText(getContext(), resId, Toast.LENGTH_SHORT).show();
				}
			}
		} else if (id == idIvClear) {
			etIdentifyNum.getText().clear();
		} else if (id == idBtnSounds) {
			long time = System.currentTimeMillis();
			if (time - lastRequestVVTime > MIN_REQUEST_VOICE_VERIFY_INTERVAL) {
				lastRequestVVTime = time;
				// 发送语音验证码
				String msg = getContext().getResources().getString(
						ResHelper.getStringRes(activity, "smssdk_send_sounds_identify_code"));
				String confirm = getContext().getResources().getString(ResHelper.getStringRes(activity, "smssdk_i_know"));
				OnClickListener positiveOnClick = new OnClickListener() {
					@Override
					public void onClick(View v) {
						PopupDialog.dismissDialog();

						if (pd != null && pd.isShowing()) {
							pd.dismiss();
						}
						pd = CommonDialog.ProgressDialog(activity);
						if (pd != null) {
							pd.show();
						}
						SMSSDK.getVoiceVerifyCode(code,phone);
					}
				};
				PopupDialog.showDialog(getContext(), null, msg, confirm, positiveOnClick,
						null, null, true, true, false);
			}
		} else if (id == idTvResend) {
			if (pd != null && pd.isShowing()) {
				pd.dismiss();
			}
			pd = CommonDialog.ProgressDialog(activity);
			if (pd != null) {
				pd.show();
			}
			// 重新获取验证码短信
			SMSSDK.getVerificationCode(code, phone.trim(), tempCode, null);
		}
	}

	/**
	 * 提交验证码成功后的执行事件
	 *
	 * @param result
	 * @param data
	 */
	private void afterSubmit(final int result, final Object data) {
		runOnUIThread(new Runnable() {
			public void run() {
				if (pd != null && pd.isShowing()) {
					pd.dismiss();
				}

				if (result == SMSSDK.RESULT_COMPLETE) {
					String msg = getContext().getResources().getString(ResHelper.getStringRes(getContext(), "smssdk_identify_success"));
					String confirm = getContext().getResources().getString(ResHelper.getStringRes(getContext(), "smssdk_confirm"));
					OnClickListener positiveClick = new OnClickListener() {
						@Override
						public void onClick(View v) {
							PopupDialog.dismissDialog();
							HashMap<String, Object> res = new HashMap<String, Object>();
							res.put("res", true);
							res.put("page", 2);
							res.put("phone", data);
							setResult(res);
							finish();
						}
					};
					PopupDialog.showDialog(getContext(), null, msg, confirm, positiveClick,
							null, null, false, false, false);
				} else {
					((Throwable) data).printStackTrace();
					// 验证码不正确
					String message = ((Throwable) data).getMessage();
					int resId = 0;
					try {
						JSONObject json = new JSONObject(message);
						int status = json.getInt("status");
						resId = ResHelper.getStringRes(activity,
								"smssdk_error_detail_" + status);
					} catch (JSONException e) {
						e.printStackTrace();
					}
					if(resId == 0) {
						resId = ResHelper.getStringRes(activity,"smssdk_virificaition_code_wrong");
					}
					if (resId > 0) {
//						Toast.makeText(activity, resId, Toast.LENGTH_SHORT).show();
						String msg = getContext().getResources().getString(resId);
						String confirm = getContext().getResources().getString(ResHelper.getStringRes(getContext(), "smssdk_confirm"));
						OnClickListener positiveClick = new OnClickListener() {
							@Override
							public void onClick(View v) {
								PopupDialog.dismissDialog();
							}
						};
						PopupDialog.showDialog(getContext(), null, msg, confirm, positiveClick,
								null, null, true, true, false);
					}
				}
			}
		});
	}

	/**
	 * 获取验证码成功后,的执行动作
	 *
	 * @param result
	 * @param data
	 */
	private void afterGet(final int result, final Object data) {
		runOnUIThread(new Runnable() {
			public void run() {
				if (pd != null && pd.isShowing()) {
					pd.dismiss();
				}

				if (result == SMSSDK.RESULT_COMPLETE) {
					int resId = ResHelper.getStringRes(activity,
							"smssdk_virificaition_code_sent");
					if (resId > 0) {
						Toast.makeText(activity, resId, Toast.LENGTH_SHORT).show();
					}
					time = RETRY_INTERVAL;
					countDown();
				} else {
					((Throwable) data).printStackTrace();
					Throwable throwable = (Throwable) data;
					// 根据服务器返回的网络错误，给toast提示
					int status = 0;
					try {
						JSONObject object = new JSONObject(throwable.getMessage());
						String des = object.optString("detail");
						status = object.optInt("status");
						if (!TextUtils.isEmpty(des)) {
							Toast.makeText(activity, des, Toast.LENGTH_SHORT).show();
							return;
						}
					} catch (JSONException e) {
						SMSLog.getInstance().w(e);
					}
					// / 如果木有找到资源，默认提示
					int resId = 0;
					if(status >= 400) {
						resId = ResHelper.getStringRes(activity, "smssdk_error_desc_" + status);
					} else {
						resId = ResHelper.getStringRes(activity,
								"smssdk_network_error");
					}
					if (resId > 0) {
						Toast.makeText(activity, resId, Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}

	/**
	 * 获取语音验证码成功后,的执行动作
	 *
	 * @param result
	 * @param data
	 */
	private void afterGetVoice(final int result, final Object data) {
		runOnUIThread(new Runnable() {
			public void run() {
				if (pd != null && pd.isShowing()) {
					pd.dismiss();
				}

				if(result == SMSSDK.RESULT_COMPLETE){
					int resId = ResHelper.getStringRes(activity, "smssdk_send_sounds_success");
					if(resId > 0){
						Toast.makeText(activity, resId, Toast.LENGTH_SHORT).show();
					}
				} else {
					((Throwable) data).printStackTrace();
					Throwable throwable = (Throwable) data;
					// 根据服务器返回的网络错误，给toast提示
					int status = 0;
					try {
						JSONObject object = new JSONObject(
								throwable.getMessage());
						String des = object.optString("detail");
						status = object.optInt("status");
						if (!TextUtils.isEmpty(des)) {
							Toast.makeText(activity, des, Toast.LENGTH_SHORT).show();
							return;
						}
					} catch (JSONException e) {
						SMSLog.getInstance().w(e);
					}
					//  如果木有找到资源，默认提示
					int resId = 0;
					if(status >= 400) {
						resId = ResHelper.getStringRes(activity, "smssdk_error_desc_" + status);
					} else {
						resId = ResHelper.getStringRes(activity,
								"smssdk_network_error");
					}

					if (resId > 0) {
						Toast.makeText(activity, resId, Toast.LENGTH_SHORT).show();
					}
				}

			}
		});
	}

	/** 按返回键时，弹出的提示对话框 */
	private void showNotifyDialog() {
		String msg = getContext().getResources().getString(ResHelper.getStringRes(getContext(), "smssdk_close_identify_page_dialog"));
		String confirm = getContext().getResources().getString(ResHelper.getStringRes(getContext(), "smssdk_confirm"));
		OnClickListener positiveClick = new OnClickListener() {
			@Override
			public void onClick(View v) {
				PopupDialog.dismissDialog();
				finish();
			}
		};
		String cancel = getContext().getResources().getString(ResHelper.getStringRes(getContext(), "smssdk_wait"));
		OnClickListener negativeClick = new OnClickListener() {
			@Override
			public void onClick(View v) {
				PopupDialog.dismissDialog();
			}
		};
		PopupDialog.showDialog(getContext(), null, msg, confirm, positiveClick,
				cancel, negativeClick, true, true, false);
	}

	@Override
	public boolean onKeyEvent(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			runOnUIThread(new Runnable() {
				public void run() {
					showNotifyDialog();
				}
			});
			return true;
		} else {
			return false;
		}
	}

	private void setResendText(int time) {
		if (tvResend != null) {
			String text = getContext().getResources().getString(ResHelper.getStringRes(getContext(), "smssdk_identify_num_page_resend"));
			if (time == 0) {
				tvResend.setText(text);
				tvResend.setTextColor(getContext().getResources().getColor(ResHelper.getColorRes(getContext(), "smssdk_main_color")));
				tvResend.setClickable(true);
			} else {
				text += "(" + time + ")";
				tvResend.setText(text);
				tvResend.setTextColor(getContext().getResources().getColor(ResHelper.getColorRes(getContext(), "smssdk_tv_light_gray")));
				tvResend.setClickable(false);
			}
		}
	}

}
