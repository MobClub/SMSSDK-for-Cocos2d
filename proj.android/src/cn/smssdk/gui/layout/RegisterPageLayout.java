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
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mob.tools.utils.ResHelper;

/**注册页面布局*/
public class RegisterPageLayout extends BasePageLayout {

	public RegisterPageLayout(Context context) {
		super(context,null);
	}

	protected void onCreateContent(LinearLayout parent) {
		SizeHelper.prepare(context);

		TextView topLabel = new TextView(context);
		LinearLayout.LayoutParams topLabelParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		topLabelParams.setMargins(0, ResHelper.dipToPx(context, 30), 0, 0);
		topLabel.setLayoutParams(topLabelParams);
		topLabel.setGravity(Gravity.CENTER);
		topLabel.setText(ResHelper.getStringRes(context, "smssdk_input_phone"));
		topLabel.setTextColor(0xff2A2B30);
		topLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, ResHelper.dipToPx(context, 30));
		parent.addView(topLabel);

		LinearLayout rlCountry = new LinearLayout(context);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				SizeHelper.fromPxWidth(96));
		params.setMargins(SizeHelper.fromPx(26), SizeHelper.fromPxWidth(32), SizeHelper.fromPxWidth(26), 0);
		rlCountry.setLayoutParams(params);
		rlCountry.setId(ResHelper.getIdRes(context, "rl_country"));

		int lLabelWidth = ResHelper.dipToPx(context, 110);
		TextView tv = new TextView(context);
		LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(lLabelWidth,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		tvParams.gravity = Gravity.CENTER_VERTICAL;
		tv.setLayoutParams(tvParams);
		int resid = ResHelper.getStringRes(context, "smssdk_country");
		tv.setText(resid);
		tv.setTextColor(0xff000000);
		tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, SizeHelper.fromPxWidth(25));
		rlCountry.addView(tv);

		TextView tvCountry = new TextView(context);
		tvCountry.setId(ResHelper.getIdRes(context, "tv_country"));
		LinearLayout.LayoutParams tvCountryParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		tvCountryParams.gravity = Gravity.CENTER_VERTICAL;
		tvCountryParams.weight = 1;
		tvCountry.setLayoutParams(tvCountryParams);
		tvCountry.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
		tvCountry.setPadding(SizeHelper.fromPxWidth(14), 0, 0, 0);
		Drawable rArrow = context.getResources().getDrawable(ResHelper.getBitmapRes(context, "smssdk_arrow_right"));
		rArrow.setBounds(0, 0, ResHelper.dipToPx(context, 25), ResHelper.dipToPx(context, 25));
		// 使用图片的默认大小
//		rArrow.setBounds(0, 0, rArrow.getIntrinsicWidth(), rArrow.getIntrinsicHeight());
		tvCountry.setCompoundDrawables(null, null, rArrow, null);
		tvCountry.setTextColor(0xff000000);
		tvCountry.setTextSize(TypedValue.COMPLEX_UNIT_PX, SizeHelper.fromPxWidth(25));
		rlCountry.addView(tvCountry);

		parent.addView(rlCountry);

		View line = new View(context);
		LinearLayout.LayoutParams lineParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				SizeHelper.fromPxWidth(1));
		lineParams.leftMargin = SizeHelper.fromPxWidth(26);
		lineParams.rightMargin = SizeHelper.fromPxWidth(26);
		line.setLayoutParams(lineParams);
		line.setBackgroundColor(context.getResources().getColor(ResHelper.getColorRes(context, "smssdk_gray_press")));
		parent.addView(line);

		LinearLayout phoneLayout =  new LinearLayout(context);
		LinearLayout.LayoutParams phoneParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				SizeHelper.fromPxWidth(70));
		phoneParams.setMargins(SizeHelper.fromPxWidth(26), SizeHelper.fromPxWidth(30), SizeHelper.fromPxWidth(26), 0);
		phoneLayout.setLayoutParams(phoneParams);

		TextView countryNum = new TextView(context);
		countryNum.setId(ResHelper.getIdRes(context, "tv_country_num"));
		LinearLayout.LayoutParams countryNumParams = new LinearLayout.LayoutParams(lLabelWidth,
				LinearLayout.LayoutParams.MATCH_PARENT);
		countryNum.setLayoutParams(countryNumParams);
		countryNum.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
		countryNum.setTextColor(0xff353535);
		countryNum.setTextSize(TypedValue.COMPLEX_UNIT_PX, SizeHelper.fromPxWidth(25));
		phoneLayout.addView(countryNum);

		LinearLayout wrapperLayout =  new LinearLayout(context);
		LinearLayout.LayoutParams wrapperParams = new LinearLayout.LayoutParams(0,
				LinearLayout.LayoutParams.MATCH_PARENT);
		wrapperParams.weight = 1;
		wrapperLayout.setLayoutParams(wrapperParams);

		EditText writePhone = new EditText(context);
		writePhone.setId(ResHelper.getIdRes(context, "et_write_phone"));
		LinearLayout.LayoutParams writePhoneParams = new LinearLayout.LayoutParams(0,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		writePhoneParams.gravity = Gravity.CENTER_VERTICAL;
		writePhoneParams.weight = 1;
		writePhone.setLayoutParams(writePhoneParams);
		writePhone.setBackgroundDrawable(null);
		resid = ResHelper.getStringRes(context, "smssdk_write_mobile_phone");
		writePhone.setHint(resid);
		writePhone.setInputType(InputType.TYPE_CLASS_PHONE);
		writePhone.setTextColor(0xff353535);
		writePhone.setTextSize(TypedValue.COMPLEX_UNIT_PX, SizeHelper.fromPxWidth(25));
		wrapperLayout.addView(writePhone);

		ImageView image = new ImageView(context);
		image.setId(ResHelper.getIdRes(context, "iv_clear"));
		LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(SizeHelper.fromPxWidth(24),
				SizeHelper.fromPxWidth(24));
		imageParams.gravity = Gravity.CENTER_VERTICAL;
		imageParams.rightMargin = SizeHelper.fromPxWidth(20);
		image.setLayoutParams(imageParams);
		resid = ResHelper.getBitmapRes(context, "smssdk_clear_search");
		image.setBackgroundResource(resid);
		image.setScaleType(ScaleType.CENTER_INSIDE);
		image.setVisibility(View.GONE);
		wrapperLayout.addView(image);
		phoneLayout.addView(wrapperLayout);
		parent.addView(phoneLayout);

		View phoneLine = new View(context);
		LinearLayout.LayoutParams phoneLineParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				SizeHelper.fromPxWidth(1));
		phoneLineParams.leftMargin = SizeHelper.fromPxWidth(26);
		phoneLineParams.rightMargin = SizeHelper.fromPxWidth(26);
		phoneLine.setLayoutParams(phoneLineParams);
		phoneLine.setBackgroundColor(context.getResources().getColor(ResHelper.getColorRes(context, "smssdk_gray_press")));
		parent.addView(phoneLine);

		Button nextBtn = new Button(context);
		nextBtn.setId(ResHelper.getIdRes(context, "btn_next"));
		LinearLayout.LayoutParams nextParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				SizeHelper.fromPxWidth(72));
		nextParams.setMargins(SizeHelper.fromPxWidth(26), SizeHelper.fromPxWidth(36), SizeHelper.fromPxWidth(26), 0);
		nextBtn.setLayoutParams(nextParams);
		resid = ResHelper.getBitmapRes(context, "smssdk_btn_disenable");
		nextBtn.setBackgroundResource(resid);
		resid = ResHelper.getStringRes(context, "smssdk_next");
		nextBtn.setText(resid);
		nextBtn.setTextColor(0xffffffff);
		nextBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX, SizeHelper.fromPxWidth(25));
		nextBtn.setPadding(0, 0, 0, 0);
		parent.addView(nextBtn);
	}

}
