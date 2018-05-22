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
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mob.tools.FakeActivity;
import com.mob.tools.utils.ResHelper;

import org.json.JSONObject;

import java.util.HashMap;

import cn.smssdk.EventHandler;
import cn.smssdk.OnSendMessageHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.UserInterruptException;
import cn.smssdk.gui.layout.RegisterPageLayout;
import cn.smssdk.gui.layout.SendMsgDialogLayout;
import cn.smssdk.gui.util.GUISPDB;
import cn.smssdk.utils.SMSLog;

/** 短信注册页面*/
public class RegisterPage extends FakeActivity implements OnClickListener,
		TextWatcher {

	// 默认使用中国区号
	private static final String DEFAULT_COUNTRY_ID = "42";

	private EventHandler callback;

	// 国家
	private TextView tvCountry;
	// 手机号码
	private EditText etPhoneNum;
	// 国家编号
	private TextView tvCountryNum;
	// clear 号码
	private ImageView ivClear;
	// 下一步按钮
	private Button btnNext;

	private String currentId;
	private String currentCode;
	private EventHandler handler;
	private Dialog pd;
	private OnSendMessageHandler osmHandler;
	private String tempCode;

	public void setRegisterCallback(EventHandler callback) {
		this.callback = callback;
	}

	public void setOnSendMessageHandler(OnSendMessageHandler h) {
		osmHandler = h;
	}

	public void show(Context context) {
		super.show(context, null);
	}

	public void onCreate() {
		RegisterPageLayout page = new RegisterPageLayout(activity);
		LinearLayout layout = page.getLayout();

		if (layout != null) {
			activity.setContentView(layout);
			currentId = DEFAULT_COUNTRY_ID;

			View llBack = activity.findViewById(ResHelper.getIdRes(activity, "ll_back"));
//			TextView tv = (TextView) activity.findViewById(ResHelper.getIdRes(activity, "tv_title"));
//			int resId = ResHelper.getStringRes(activity, "smssdk_regist");
//			if (resId > 0) {
//				tv.setText(resId);
//			}

			View viewCountry = activity.findViewById(ResHelper.getIdRes(activity, "rl_country"));
			btnNext = (Button) activity.findViewById(ResHelper.getIdRes(activity, "btn_next"));
			tvCountry = (TextView) activity.findViewById(ResHelper.getIdRes(activity, "tv_country"));

			String[] country = getCurrentCountry();
			// String[] country = SMSSDK.getCountry(currentId);
			if (country != null) {
				currentCode = country[1];
				tvCountry.setText(country[0]);
			}

			tvCountryNum = (TextView) activity.findViewById(ResHelper.getIdRes(activity, "tv_country_num"));
			tvCountryNum.setText("+" + currentCode);

			etPhoneNum = (EditText) activity.findViewById(ResHelper.getIdRes(activity, "et_write_phone"));
			etPhoneNum.setText("");
			etPhoneNum.addTextChangedListener(this);
			etPhoneNum.requestFocus();
			if (etPhoneNum.getText().length() > 0) {
				btnNext.setEnabled(true);

				ivClear = (ImageView) activity.findViewById(ResHelper.getIdRes(activity, "iv_clear"));
				ivClear.setVisibility(View.VISIBLE);
				int resId = ResHelper.getBitmapRes(activity, "smssdk_btn_enable");
				if (resId > 0) {
					btnNext.setBackgroundResource(resId);
				}
			}

			ivClear = (ImageView) activity.findViewById(ResHelper.getIdRes(activity, "iv_clear"));

			llBack.setOnClickListener(this);
			btnNext.setOnClickListener(this);
			ivClear.setOnClickListener(this);
			viewCountry.setOnClickListener(this);

			handler = new EventHandler() {
				public void afterEvent(final int event, final int result,
						final Object data) {
					runOnUIThread(new Runnable() {
						public void run() {
							if (pd != null && pd.isShowing()) {
								pd.dismiss();
							}
							if (result == SMSSDK.RESULT_COMPLETE) {
								if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
									// 请求验证码后，跳转到验证码填写页面
									boolean smart = (Boolean)data;
									afterVerificationCodeRequested(smart);
								}
							} else {
								if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE
										&& data != null
										&& (data instanceof UserInterruptException)) {
									// 由于此处是开发者自己决定要中断发送的，因此什么都不用做
									return;
								}

								int status = 0;
								String confirm = getContext().getString(ResHelper.getStringRes(getContext(), "smssdk_confirm"));
								// 根据服务器返回的网络错误，给toast提示
								try {
									((Throwable) data).printStackTrace();
									Throwable throwable = (Throwable) data;

									JSONObject object = new JSONObject(
											throwable.getMessage());
									String des = object.optString("detail");
									status = object.optInt("status");
									if (!TextUtils.isEmpty(des)) {
//										Toast.makeText(activity, des, Toast.LENGTH_SHORT).show();
										OnClickListener positiveClick = new OnClickListener() {
											@Override
											public void onClick(View v) {
												// Nothing to do
											}
										};
										PopupDialog.create(getContext(), null, des, confirm, positiveClick,
												null, null, true, true, false).show();
										return;
									}
								} catch (Exception e) {
									SMSLog.getInstance().w(e);
								}
								// 如果木有找到资源，默认提示
								int resId = 0;
								if(status >= 400) {
									resId = ResHelper.getStringRes(activity, "smssdk_error_desc_" + status);
								} else {
									resId = ResHelper.getStringRes(activity,
											"smssdk_network_error");
								}

								if (resId > 0) {
//									Toast.makeText(activity, resId, Toast.LENGTH_SHORT).show();
									OnClickListener positiveClick = new OnClickListener() {
										@Override
										public void onClick(View v) {
											// Nothing to do
										}
									};
									String msg = getContext().getString(resId);
									PopupDialog.create(getContext(), null, msg, confirm, positiveClick,
											null, null, true, true, false).show();
								}
							}
						}
					});
				}
			};
		}

	}

	private String[] getCurrentCountry() {
		String mcc = getMCC();
		String[] country = null;
		if (!TextUtils.isEmpty(mcc)) {
			country = SMSSDK.getCountryByMCC(mcc);
		}

		if (country == null) {
			SMSLog.getInstance().d("no country found by MCC: " + mcc);
			country = SMSSDK.getCountry(DEFAULT_COUNTRY_ID);
		}
		return country;
	}

	private String getMCC() {
		TelephonyManager tm = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);
		// 返回当前手机注册的网络运营商所在国家的MCC+MNC. 如果没注册到网络就为空.
		String networkOperator = tm.getNetworkOperator();
		if (!TextUtils.isEmpty(networkOperator)) {
			return networkOperator;
		}

		// 返回SIM卡运营商所在国家的MCC+MNC. 5位或6位. 如果没有SIM卡返回空
		return tm.getSimOperator();
	}

	public void onResume() {
		SMSSDK.registerEventHandler(handler);
	}

	@Override
	public void onPause() {
		// 防止在“填写验证码页面”点击重新发送时，SMSSDK.EVENT_GET_VERIFICATION_CODE事件被该页面捕获到，进而新建了一个“填写验证码页面”的实例
		SMSSDK.unregisterEventHandler(handler);
	}

	public void onDestroy() {
		SMSSDK.unregisterEventHandler(handler);
	}

	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (s.length() > 0) {
			btnNext.setEnabled(true);
			ivClear.setVisibility(View.VISIBLE);
			int resId = ResHelper.getBitmapRes(activity, "smssdk_btn_enable");
			if (resId > 0) {
				btnNext.setBackgroundResource(resId);
			}
		} else {
			btnNext.setEnabled(false);
			ivClear.setVisibility(View.GONE);
			int resId = ResHelper.getBitmapRes(activity, "smssdk_btn_disenable");
			if (resId > 0) {
				btnNext.setBackgroundResource(resId);
			}
		}
	}

	public void afterTextChanged(Editable s) {

	}

	public void onClick(View v) {
		int id = v.getId();
		int idLlBack = ResHelper.getIdRes(activity, "ll_back");
		int idRlCountry = ResHelper.getIdRes(activity, "rl_country");
		int idBtnNext = ResHelper.getIdRes(activity, "btn_next");
		int idIvClear = ResHelper.getIdRes(activity, "iv_clear");

		if (id == idLlBack) {
			finish();
		} else if (id == idRlCountry) {
			// 国家列表
			CountryPage countryPage = new CountryPage();
			countryPage.setCountryId(currentId);
			countryPage.showForResult(activity, null, this);
		} else if (id == idBtnNext) {
			// 请求发送短信验证码
			String phone = etPhoneNum.getText().toString().trim().replaceAll("\\s*", "");
			String code = tvCountryNum.getText().toString().trim();
			confirmSend(phone, code);
		} else if (id == idIvClear) {
			// 清除电话号码输入框
			etPhoneNum.getText().clear();
		}
	}

	@SuppressWarnings("unchecked")
	public void onResult(HashMap<String, Object> data) {
		if (data != null) {
			int page = (Integer) data.get("page");
			if (page == 1) {
				// 国家列表返回
				currentId = (String) data.get("id");
				String[] country = SMSSDK.getCountry(currentId);
				if (country != null) {
					currentCode = country[1];
					tvCountryNum.setText("+" + currentCode);
					tvCountry.setText(country[0]);
				}
			} else if (page == 2) {
				// 验证码校验返回
				Object res = data.get("res");
				//Object smart = data.get("smart");

				HashMap<String, Object> phoneMap = (HashMap<String, Object>) data.get("phone");
				if (res != null && phoneMap != null) {
					if (callback != null) {
						callback.afterEvent(
								SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE,
								SMSSDK.RESULT_COMPLETE, phoneMap);
					}
					finish();
				}
			}
		}
	}

	/** 分割电话号码 */
	private String splitPhoneNum(String phone) {
		StringBuilder builder = new StringBuilder(phone);
		builder.reverse();
		for (int i = 4, len = builder.length(); i < len; i += 5) {
			builder.insert(i, ' ');
		}
		builder.reverse();
		return builder.toString();
	}

	/** 是否请求发送验证码，对话框 */
	public void confirmSend(final String phone, final String code) {
		String title = getContext().getResources().getString(ResHelper.getStringRes(activity, "smssdk_make_sure_mobile_num"));
		String phoneStr = code + " " + splitPhoneNum(phone);
		String msg = String.format(getContext().getResources().getString(
				ResHelper.getStringRes(activity, "smssdk_make_sure_mobile_detail")), phoneStr);
		String confirm = getContext().getResources().getString(ResHelper.getStringRes(activity, "smssdk_ok"));
		String cancel = getContext().getResources().getString(ResHelper.getStringRes(activity, "smssdk_cancel"));
		OnClickListener positiveOnClick = new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 跳转到验证码页面
				if (pd != null && pd.isShowing()) {
					pd.dismiss();
				}
				pd = CommonDialog.ProgressDialog(activity);
				if (pd != null) {
					pd.show();
				}
				// 若内存中无tempCode，则从本地SP中获取tempCode
				if (TextUtils.isEmpty(tempCode)) {
					tempCode = GUISPDB.getTempCode();
				}
				SMSLog.getInstance().i("verification phone ==>>" + phone);
				SMSLog.getInstance().i("verification tempCode ==>>" + tempCode);
				SMSSDK.getVerificationCode(code, phone.trim(), tempCode, osmHandler);
			}
		};
		OnClickListener negativeOnClick = new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Nothing to do
			}
		};
		PopupDialog.create(getContext(), title, msg, confirm, positiveOnClick,
				cancel, negativeOnClick, true, true, false).show();
	}

	public void setTempCode(String tempCode) {
		this.tempCode = tempCode;
		GUISPDB.setTempCode(tempCode);
	}

	/** 请求验证码后，跳转到验证码填写页面 */
	private void afterVerificationCodeRequested(boolean smart) {
		String phone = etPhoneNum.getText().toString().trim().replaceAll("\\s*", "");
		String code = tvCountryNum.getText().toString().trim();
		if (code.startsWith("+")) {
			code = code.substring(1);
		}
		String formatedPhone = "+" + code + " " + splitPhoneNum(phone);

		// 验证码页面
		if(smart) {
			SmartVerifyPage smartPage = new SmartVerifyPage();
			smartPage.setPhone(phone, code, formatedPhone);
			smartPage.showForResult(activity, null, this);
		} else {
			IdentifyNumPage page = new IdentifyNumPage();
			page.setPhone(phone, code, formatedPhone);
			page.setTempCode(this.tempCode);
			page.showForResult(activity, null, this);
		}
	}

}
