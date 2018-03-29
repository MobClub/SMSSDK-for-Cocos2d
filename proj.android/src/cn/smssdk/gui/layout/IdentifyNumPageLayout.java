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
import android.graphics.Typeface;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mob.tools.utils.ResHelper;

/**验证码输入页面布局*/
public class IdentifyNumPageLayout extends BasePageLayout {

	public IdentifyNumPageLayout(Context c) {
		super(c,null);
	}

	protected void onCreateContent(LinearLayout parent) {
		LinearLayout wrapperLayout =  new LinearLayout(context);
		LinearLayout.LayoutParams wrapperParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		wrapperLayout.setLayoutParams(wrapperParams);
		wrapperLayout.setBackgroundColor(0xffffffff);
		wrapperLayout.setOrientation(LinearLayout.VERTICAL);
		wrapperLayout.setPadding(SizeHelper.fromPxWidth(26), 0, SizeHelper.fromPxWidth(26), 0);
		parent.addView(wrapperLayout);

		TextView identifyNotify = new TextView(context);
		identifyNotify.setId(ResHelper.getIdRes(context, "tv_identify_notify"));
		LinearLayout.LayoutParams identifyNotifyParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		identifyNotifyParams.topMargin = SizeHelper.fromPxWidth(32);
		identifyNotify.setGravity(Gravity.CENTER);
		identifyNotify.setLayoutParams(identifyNotifyParams);
		int resid = ResHelper.getStringRes(context, "smssdk_make_sure_mobile_detail");
		identifyNotify.setText(resid);
		identifyNotify.setTextColor(0xff000000);
		identifyNotify.setTextSize(TypedValue.COMPLEX_UNIT_PX,ResHelper.dipToPx(context, 24));
		wrapperLayout.addView(identifyNotify);

		// 手机号Container
		LinearLayout llPhone = new LinearLayout(context);
		LinearLayout.LayoutParams llPhoneParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		llPhoneParams.topMargin = SizeHelper.fromPxWidth(60);
		llPhone.setLayoutParams(llPhoneParams);
		llPhone.setOrientation(LinearLayout.HORIZONTAL);
		wrapperLayout.addView(llPhone);

		int labelWidth = ResHelper.dipToPx(context, 80);
		int textSize = ResHelper.dipToPx(context, 14);
		int textColor = context.getResources().getColor(ResHelper.getColorRes(context, "smssdk_black"));
		TextView tvPhoneLabel = new TextView(context);
		LinearLayout.LayoutParams tvPhoneLabelParams = new LinearLayout.LayoutParams(labelWidth,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		tvPhoneLabel.setLayoutParams(tvPhoneLabelParams);
		resid = ResHelper.getStringRes(context, "smssdk_label_phone");
		tvPhoneLabel.setText(resid);
		tvPhoneLabel.setTextColor(textColor);
		tvPhoneLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
		llPhone.addView(tvPhoneLabel);

		TextView phone = new TextView(context);
		phone.setId(ResHelper.getIdRes(context, "tv_phone"));
		LinearLayout.LayoutParams phoneParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		phone.setLayoutParams(phoneParams);
		phone.setTextColor(textColor);
		phone.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
		llPhone.addView(phone);

		// 分割线
		View linePhone = new View(context);
		LinearLayout.LayoutParams linePhoneParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				ResHelper.dipToPx(context, 1));
		linePhoneParams.topMargin = ResHelper.dipToPx(context, 10);
		linePhone.setLayoutParams(linePhoneParams);
		resid = ResHelper.getColorRes(context, "smssdk_line_light_gray");
		linePhone.setBackgroundResource(resid);
		wrapperLayout.addView(linePhone);

		// 验证码Container
		LinearLayout llCode = new LinearLayout(context);
		LinearLayout.LayoutParams llCodeParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		llCodeParams.topMargin = ResHelper.dipToPx(context, 15);
		llCode.setLayoutParams(llCodeParams);
		llCode.setOrientation(LinearLayout.HORIZONTAL);
		wrapperLayout.addView(llCode);

		TextView tvCodeLabel = new TextView(context);
		LinearLayout.LayoutParams tvCodeLabelParams = new LinearLayout.LayoutParams(labelWidth,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		tvCodeLabel.setLayoutParams(tvCodeLabelParams);
		resid = ResHelper.getStringRes(context, "smssdk_identify_code");
		tvCodeLabel.setText(resid);
		tvCodeLabel.setTextColor(textColor);
		tvCodeLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
		llCode.addView(tvCodeLabel);

		LinearLayout.LayoutParams putIdentifyParams = new LinearLayout.LayoutParams(0,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		putIdentifyParams.weight = 1;
		EditText putIdentify = new EditText(context);
		putIdentify.setLayoutParams(putIdentifyParams);
		putIdentify.setId(ResHelper.getIdRes(context, "et_put_identify"));
		resid = ResHelper.getStringRes(context, "smssdk_write_identify_code");
		putIdentify.setPadding(0, 0, 0, 0);
		putIdentify.setHint(resid);
		putIdentify.setBackgroundColor(0xffffffff);
		putIdentify.setSingleLine(true);
		putIdentify.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
		putIdentify.setInputType(InputType.TYPE_CLASS_NUMBER);
		putIdentify.setGravity(Gravity.CENTER_VERTICAL);
		llCode.addView(putIdentify);

		LinearLayout.LayoutParams clearImageParams = new LinearLayout.LayoutParams(ResHelper.dipToPx(context, 18),
				ResHelper.dipToPx(context, 18));
		ImageView clearImage = new ImageView(context);
		clearImage.setLayoutParams(clearImageParams);
		clearImage.setId(ResHelper.getIdRes(context, "iv_clear"));
		resid = ResHelper.getBitmapRes(context, "smssdk_clear_search");
		clearImage.setImageResource(resid);
		clearImage.setScaleType(ScaleType.CENTER_INSIDE);
		clearImage.setVisibility(View.GONE);
		llCode.addView(clearImage);

		TextView tvResend = new TextView(context);
		LinearLayout.LayoutParams tvResendParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		tvResendParams.leftMargin = ResHelper.dipToPx(context, 10);
		tvResend.setLayoutParams(tvResendParams);
		tvResend.setId(ResHelper.getIdRes(context, "tv_resend"));
		resid = ResHelper.getStringRes(context, "smssdk_identify_num_page_resend");
		tvResend.setText(resid);
		tvResend.setTextColor(context.getResources().getColor(ResHelper.getColorRes(context, "smssdk_tv_light_gray")));
		tvResend.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
		llCode.addView(tvResend);

		// 分割线
		View lineCode = new View(context);
		LinearLayout.LayoutParams lineCodeParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				ResHelper.dipToPx(context, 1));
		lineCodeParams.topMargin = ResHelper.dipToPx(context, 10);
		lineCode.setLayoutParams(lineCodeParams);
		resid = ResHelper.getColorRes(context, "smssdk_line_light_gray");
		lineCode.setBackgroundResource(resid);
		wrapperLayout.addView(lineCode);

		// 提交按钮
		Button submitBtn = new Button(context);
		submitBtn.setId(ResHelper.getIdRes(context, "btn_submit"));
		LinearLayout.LayoutParams submitParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				SizeHelper.fromPxWidth(72));
		submitParams.topMargin = ResHelper.dipToPx(context, 40);
		submitBtn.setLayoutParams(submitParams);
		resid = ResHelper.getBitmapRes(context, "smssdk_btn_disenable");
		submitBtn.setBackgroundResource(resid);
		resid = ResHelper.getStringRes(context, "smssdk_next");
		submitBtn.setText(resid);
		submitBtn.setTextColor(0xffffffff);
		submitBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX,SizeHelper.fromPxWidth(24));
		submitBtn.setPadding(SizeHelper.fromPxWidth(10), 0, SizeHelper.fromPxWidth(10), 0);
		wrapperLayout.addView(submitBtn);

		// 语音验证码Container
		LinearLayout llVoice = new LinearLayout(context);
		LinearLayout.LayoutParams llVoiceParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		llVoiceParams.topMargin = SizeHelper.fromPxWidth(20);
		llVoice.setId(ResHelper.getIdRes(context, "ll_voice"));
		llVoice.setLayoutParams(llVoiceParams);
		llVoice.setOrientation(LinearLayout.HORIZONTAL);
		llVoice.setGravity(Gravity.CENTER);
		wrapperLayout.addView(llVoice);

		textSize = ResHelper.dipToPx(context, 16);
		TextView tvUnReceive = new TextView(context);
		LinearLayout.LayoutParams tvUnReceiveParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		tvUnReceive.setLayoutParams(tvUnReceiveParams);
		resid = ResHelper.getStringRes(context, "smssdk_unreceive_identify_code");
		tvUnReceive.setText(resid);
		tvUnReceive.setTextColor(textColor);
		tvUnReceive.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
		llVoice.addView(tvUnReceive);

		TextView tvVoice = new TextView(context);
		tvVoice.setId(ResHelper.getIdRes(context, "tv_voice"));
		LinearLayout.LayoutParams tvVoiceParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		tvVoice.setLayoutParams(tvVoiceParams);
		resid = ResHelper.getStringRes(context, "smssdk_voice_code");
		tvVoice.setText(resid);
		textColor = context.getResources().getColor(ResHelper.getColorRes(context, "smssdk_main_color"));
		tvVoice.setTextColor(textColor);
		tvVoice.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
		llVoice.addView(tvVoice);
	}

}
