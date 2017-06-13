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


//#if def{lang} == cn
/** 验证码输入页面*/
//#elif def{lang} == en
/** the page of input verification code */
//#endif
public class IdentifyNumPage extends FakeActivity implements OnClickListener,
		TextWatcher {
	//#if def{sdk.debugable}
	private static final int RETRY_INTERVAL = 40;
	//#else
	//#=private static final int RETRY_INTERVAL = 60;
	//#endif
	private static final int MIN_REQUEST_VOICE_VERIFY_INTERVAL = 1000;
	private String phone;
	private String code;
	private String formatedPhone;
	private int time = RETRY_INTERVAL;
	private EventHandler handler;
	private Dialog pd;

	private EditText etIdentifyNum;
	private TextView tvTitle;
	private TextView tvPhone;
	private TextView tvIdentifyNotify;
	private TextView tvUnreceiveIdentify;
	private ImageView ivClear;
	private Button btnSubmit;
	private Button btnSounds;
	private BroadcastReceiver smsReceiver;
	private int showDialogType = 1;
	private long lastRequestVVTime;

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

			tvTitle = (TextView) activity.findViewById(ResHelper.getIdRes(activity, "tv_title"));
			int resId = ResHelper.getStringRes(activity, "smssdk_write_identify_code");
			if (resId > 0) {
				tvTitle.setText(resId);
			}
			
			etIdentifyNum = (EditText) activity.findViewById(ResHelper.getIdRes(activity, "et_put_identify"));
			etIdentifyNum.addTextChangedListener(this);
		
			tvIdentifyNotify = (TextView) activity.findViewById(ResHelper.getIdRes(activity, "tv_identify_notify"));
			resId = ResHelper.getStringRes(activity, "smssdk_send_mobile_detail");
			if (resId > 0) {
				String text = getContext().getString(resId);
				tvIdentifyNotify.setText(Html.fromHtml(text));
			}
			
			tvPhone = (TextView) activity.findViewById(ResHelper.getIdRes(activity, "tv_phone"));
			tvPhone.setText(formatedPhone);
			
			tvUnreceiveIdentify = (TextView) activity.findViewById(ResHelper.getIdRes(activity, "tv_unreceive_identify"));
			resId = ResHelper.getStringRes(activity, "smssdk_receive_msg");
			if (resId > 0) {
				String unReceive = getContext().getString(resId, time);
				tvUnreceiveIdentify.setText(Html.fromHtml(unReceive));
			}
			tvUnreceiveIdentify.setOnClickListener(this);
			tvUnreceiveIdentify.setEnabled(false);
			
			ivClear = (ImageView) activity.findViewById(ResHelper.getIdRes(activity, "iv_clear"));
			ivClear.setOnClickListener(this);
			
			btnSounds = (Button) findViewById(ResHelper.getIdRes(activity, "btn_sounds"));
			btnSounds.setOnClickListener(this);

			handler = new EventHandler() {
				public void afterEvent(int event, int result, Object data) {
					if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
						//#if def{lang} == cn
						/** 提交验证码 */
						//#elif def{lang} == en
						/** submit verification code */
						//#endif
						afterSubmit(result, data);
					} else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
						//#if def{lang} == cn
						/** 获取验证码成功后的执行动作 */
						//#elif def{lang} == en
						/** excute the event after get submit verification code */
						//#endif
						afterGet(result, data);
					} else if (event == SMSSDK.EVENT_GET_VOICE_VERIFICATION_CODE) {
						//#if def{lang} == cn
						/** 获取语音版验证码成功后的执行动作 */
						//#elif def{lang} == en
						/** excute the event after get submit voice verification code */
						//#endif
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

	//#if def{lang} == cn
	/** 倒数计时 */
	//#elif def{lang} == en
	/** countdown when get verification code from server */
	//#endif
	private void countDown() {
		runOnUIThread(new Runnable() {
			public void run() {
				time--;
				if (time == 0) {
					int resId = ResHelper.getStringRes(activity,
							"smssdk_unreceive_identify_code");
					if (resId > 0) {
						String unReceive = getContext().getString(resId, time);
						tvUnreceiveIdentify.setText(Html.fromHtml(unReceive));
					}
					tvUnreceiveIdentify.setEnabled(true);
					btnSounds.setVisibility(View.VISIBLE);
					time = RETRY_INTERVAL;
				} else {
					int resId = ResHelper.getStringRes(activity, "smssdk_receive_msg");
					if (resId > 0) {
						String unReceive = getContext().getString(resId, time);
						tvUnreceiveIdentify.setText(Html.fromHtml(unReceive));
					}
//					if (time == 30){
//						btnSounds.setVisibility(View.VISIBLE);
//					}
					tvUnreceiveIdentify.setEnabled(false);
					runOnUIThread(this, 1000);
				}
			}
		}, 1000);
	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
		//#if def{lang} == cn
		// 如果输入框木有，就隐藏delbtn
		//#elif def{lang} == en
		// hide the delete button when the input view is null
		//#endif
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
//		btnSounds.setVisibility(View.GONE);
	}

	public void onClick(View v) {
		int id = v.getId();
		int idLlBack = ResHelper.getIdRes(activity, "ll_back");
		int idBtnSubmit = ResHelper.getIdRes(activity, "btn_submit");
		int idTvUnreceiveIdentify = ResHelper.getIdRes(activity, "tv_unreceive_identify");
		int idIvClear = ResHelper.getIdRes(activity, "iv_clear");
		int idBtnSounds = ResHelper.getIdRes(activity, "btn_sounds");

		if (id == idLlBack) {
			runOnUIThread(new Runnable() {
				public void run() {
					showNotifyDialog();
				}
			});
		} else if (id == idBtnSubmit) {
			//#if def{lang} == cn
			// 提交验证码
			//#elif def{lang} == en
			// submit the verification code
			//#endif
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
		} else if (id == idTvUnreceiveIdentify) {
			showDialogType = 1;
			//#if def{lang} == cn
			// 没有接收到短信
			//#elif def{lang} == en
			// can not receive the sms of verification code
			//#endif
			showDialog(showDialogType);
		} else if (id == idIvClear) {
			etIdentifyNum.getText().clear();
		} else if (id == idBtnSounds) {
			long time = System.currentTimeMillis();
			if (time - lastRequestVVTime > MIN_REQUEST_VOICE_VERIFY_INTERVAL) {
				lastRequestVVTime = time;
				showDialogType = 2;
				//#if def{lang} == cn
				// 发送语音验证码
				//#elif def{lang} == en
				// send voice verification code
				//#endif
				showDialog(showDialogType);
			}
		}
	}

	//#if def{lang} == cn
	/** 弹出重新发送短信对话框,或发送语音窗口 */
	//#elif def{lang} == en
	/** show the dialog of resend verification-code-sms */
	//#endif
	private void showDialog(int type) {
		if (type == 1) {
			int resId = ResHelper.getStyleRes(activity, "CommonDialog");
			if (resId > 0) {
				final Dialog dialog = new Dialog(getContext(), resId);
				TextView tv = new TextView(getContext());
				if (type == 1) {
					resId = ResHelper.getStringRes(activity,
							"smssdk_resend_identify_code");
				} else {
					resId = ResHelper.getStringRes(activity,
							"smssdk_send_sounds_identify_code");
				}
				if (resId > 0) {
					tv.setText(resId);
				}
				tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
				resId = ResHelper.getColorRes(activity, "smssdk_white");
				if (resId > 0) {
					tv.setTextColor(getContext().getResources().getColor(resId));
				}
				int dp10 = ResHelper.dipToPx(getContext(), 10);
				tv.setPadding(dp10, dp10, dp10, dp10);

				dialog.setContentView(tv);
				tv.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						dialog.dismiss();
						tvUnreceiveIdentify.setEnabled(false);

						if (pd != null && pd.isShowing()) {
							pd.dismiss();
						}
						pd = CommonDialog.ProgressDialog(activity);
						if (pd != null) {
							pd.show();
						}
						//#if def{lang} == cn
						// 重新获取验证码短信
						//#elif def{lang} == en
						// to get sms verification code
						//#endif
						SMSSDK.getVerificationCode(code, phone.trim(), null);
					}
				});

				dialog.setCanceledOnTouchOutside(true);
				dialog.setOnCancelListener(new OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						tvUnreceiveIdentify.setEnabled(true);
					}
				});
				dialog.show();
			}
		} else if (type == 2) {
			int resId = ResHelper.getStyleRes(activity, "CommonDialog");
			if (resId > 0) {
				final Dialog dialog = new Dialog(getContext(), resId);
				LinearLayout layout = SendMsgDialogLayout.create(activity);
				
				if (layout != null) {
					dialog.setContentView(layout);
					
					TextView tvTitle = (TextView) dialog.findViewById(ResHelper.getIdRes(activity, "tv_dialog_title"));
					resId = ResHelper.getStringRes(activity,
							"smssdk_make_sure_send_sounds");
					if (resId > 0) {
						tvTitle.setText(resId);
					}
					
					TextView tv = (TextView) dialog.findViewById(ResHelper.getIdRes(activity, "tv_dialog_hint"));
					resId = ResHelper.getStringRes(activity,
							"smssdk_send_sounds_identify_code");
					if (resId > 0) {
						String text = getContext().getString(resId);
						tv.setText(text);
					}
					
					((Button) dialog.findViewById(ResHelper.getIdRes(activity, "btn_dialog_ok"))).setOnClickListener(new OnClickListener() {
									public void onClick(View v) {
										// TODO 发送语言
										dialog.dismiss();
										SMSSDK.getVoiceVerifyCode(code,phone);
									}
					});
					
					((Button) dialog.findViewById(ResHelper.getIdRes(activity, "btn_dialog_cancel"))).setOnClickListener(new OnClickListener() {
									public void onClick(View v) {
										dialog.dismiss();
									}
					});
					dialog.setCanceledOnTouchOutside(true);
					dialog.show();
				}
			}
		}

	}

	//#if def{lang} == cn
	/**
	 * 提交验证码成功后的执行事件
	 * 
	 * @param result
	 * @param data
	 */
	//#elif def{lang} == en
	/** execute the event after sumbit verification code sucessfully */
	//#endif
	private void afterSubmit(final int result, final Object data) {
		runOnUIThread(new Runnable() {
			public void run() {
				if (pd != null && pd.isShowing()) {
					pd.dismiss();
				}

				if (result == SMSSDK.RESULT_COMPLETE) {
					HashMap<String, Object> res = new HashMap<String, Object>();
					res.put("res", true);
					res.put("page", 2);
					res.put("phone", data);
					setResult(res);
					finish();
				} else {
					((Throwable) data).printStackTrace();
					//#if def{lang} == cn
					// 验证码不正确
					//#elif def{lang} == en
					// the wrong verification code
					//#endif
					String message = ((Throwable) data).getMessage();
					int resId = 0;
					try {
						JSONObject json = new JSONObject(message);
						int status = json.getInt("status");
						resId = ResHelper.getStringRes(activity,
								"smssdk_error_detail_" + status);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(resId == 0) {
						resId = ResHelper.getStringRes(activity,"smssdk_virificaition_code_wrong");
					}
					if (resId > 0) {
						Toast.makeText(activity, resId, Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
	}

	//#if def{lang} == cn
	/**
	 * 获取验证码成功后,的执行动作
	 * 
	 * @param result
	 * @param data
	 */
	//#elif def{lang} == en
	/** excute the event after get submit verification code */
	//#endif
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
					resId = ResHelper.getStringRes(activity, "smssdk_receive_msg");
					if (resId > 0) {
						String unReceive = getContext().getString(resId, time);
						tvUnreceiveIdentify.setText(Html.fromHtml(unReceive));
					}
					btnSounds.setVisibility(View.GONE);
					time = RETRY_INTERVAL;
					countDown();
				} else {
					((Throwable) data).printStackTrace();
					Throwable throwable = (Throwable) data;
					//#if def{lang} == cn
					// 根据服务器返回的网络错误，给toast提示
					//#elif def{lang} == en
					// show toast according to the error code from server
					//#endif
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
					//#if def{lang} == cn
					// / 如果木有找到资源，默认提示
					//#elif def{lang} == en
					// network error
					//#endif
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
	
	//#if def{lang} == cn
	/**
	 * 获取语音验证码成功后,的执行动作
	 *
	 * @param result
	 * @param data
	 */
	//#elif def{lang} == en
	/** excute the event after get submit voice verification code */
	//#endif
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
					btnSounds.setVisibility(View.GONE);
				} else {
					((Throwable) data).printStackTrace();
					Throwable throwable = (Throwable) data;
					//#if def{lang} == cn
					// 根据服务器返回的网络错误，给toast提示
					//#elif def{lang} == en
					// show toast according to the error code from server
					//#endif
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
					//#if def{lang} == cn
					//  如果木有找到资源，默认提示
					//#elif def{lang} == en
					// network error
					//#endif
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

	//#if def{lang} == cn
	/** 按返回键时，弹出的提示对话框 */
	//#elif def{lang} == en
	/** show dialog when you click back button */
	//#endif
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

}
