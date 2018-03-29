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
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mob.tools.utils.ResHelper;

/**联系人详情页面布局*/
public class ContactDetailPageLayout extends BasePageLayout {

	public ContactDetailPageLayout(Context c) {
		super(c,null);
	}

	protected void onCreateContent(LinearLayout parent) {
		SizeHelper.prepare(context);

		// 名字
		TextView contactName = new TextView(context);
		contactName.setId(ResHelper.getIdRes(context, "tv_contact_name"));
		LinearLayout.LayoutParams contactNameParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		contactNameParams.setMargins(SizeHelper.fromPxWidth(26), SizeHelper.fromPxWidth(60), SizeHelper.fromPxWidth(26), 0);
		contactName.setLayoutParams(contactNameParams);
		contactName.setGravity(Gravity.CENTER);
		contactName.setTextColor(context.getResources().getColor(ResHelper.getColorRes(context, "smssdk_main_color")));
		contactName.setTextSize(TypedValue.COMPLEX_UNIT_PX,SizeHelper.fromPxWidth(52));
		parent.addView(contactName);

		// 手机号Container
		LinearLayout llPhone = new LinearLayout(context);
		LinearLayout.LayoutParams llPhoneParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		llPhoneParams.setMargins(SizeHelper.fromPxWidth(26), SizeHelper.fromPxWidth(60), SizeHelper.fromPxWidth(26), 0);
		llPhone.setLayoutParams(llPhoneParams);
		llPhone.setOrientation(LinearLayout.HORIZONTAL);
		parent.addView(llPhone);

		int labelWidth = ResHelper.dipToPx(context, 80);
		int textSize = ResHelper.dipToPx(context, 14);
		int textColor = context.getResources().getColor(ResHelper.getColorRes(context, "smssdk_black"));
		TextView tvPhoneLabel = new TextView(context);
		LinearLayout.LayoutParams tvPhoneLabelParams = new LinearLayout.LayoutParams(labelWidth,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		tvPhoneLabel.setLayoutParams(tvPhoneLabelParams);
		int resid = ResHelper.getStringRes(context, "smssdk_label_phone");
		tvPhoneLabel.setText(resid);
		tvPhoneLabel.setTextColor(textColor);
		tvPhoneLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
		llPhone.addView(tvPhoneLabel);

		TextView phone = new TextView(context);
		phone.setId(ResHelper.getIdRes(context, "tv_phone"));
		LinearLayout.LayoutParams phoneParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		phone.setLayoutParams(phoneParams);
		phone.setGravity(Gravity.RIGHT);
		phone.setTextColor(context.getResources().getColor(ResHelper.getColorRes(context, "smssdk_tv_light_gray")));
		phone.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
		llPhone.addView(phone);

		// 分割线
		View linePhone = new View(context);
		LinearLayout.LayoutParams linePhoneParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				ResHelper.dipToPx(context, 1));
		linePhoneParams.setMargins(SizeHelper.fromPxWidth(26), SizeHelper.fromPxWidth(10), SizeHelper.fromPxWidth(26), 0);
		linePhone.setLayoutParams(linePhoneParams);
		resid = ResHelper.getColorRes(context, "smssdk_line_light_gray");
		linePhone.setBackgroundResource(resid);
		parent.addView(linePhone);

		// 手机号2Container
		LinearLayout llPhone2 = new LinearLayout(context);
		LinearLayout.LayoutParams llPhone2Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		llPhone2Params.setMargins(SizeHelper.fromPxWidth(26), SizeHelper.fromPxWidth(22), SizeHelper.fromPxWidth(26), 0);
		llPhone2.setId(ResHelper.getIdRes(context, "ll_phone2"));
		llPhone2.setLayoutParams(llPhone2Params);
		llPhone2.setOrientation(LinearLayout.HORIZONTAL);
		llPhone2.setVisibility(View.GONE);
		parent.addView(llPhone2);

		TextView tvPhoneLabel2 = new TextView(context);
		LinearLayout.LayoutParams tvPhoneLabel2Params = new LinearLayout.LayoutParams(labelWidth,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		tvPhoneLabel2.setLayoutParams(tvPhoneLabel2Params);
		resid = ResHelper.getStringRes(context, "smssdk_label_phone2");
		tvPhoneLabel2.setText(resid);
		tvPhoneLabel2.setTextColor(textColor);
		tvPhoneLabel2.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
		llPhone2.addView(tvPhoneLabel2);

		TextView phone2 = new TextView(context);
		phone2.setId(ResHelper.getIdRes(context, "tv_phone2"));
		LinearLayout.LayoutParams phone2Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		phone2.setLayoutParams(phone2Params);
		phone2.setGravity(Gravity.RIGHT);
		phone2.setTextColor(context.getResources().getColor(ResHelper.getColorRes(context, "smssdk_tv_light_gray")));
		phone2.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
		llPhone2.addView(phone2);

		// 分割线2
		View linephone2 = new View(context);
		LinearLayout.LayoutParams linephone2Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				ResHelper.dipToPx(context, 1));
		linephone2Params.setMargins(SizeHelper.fromPxWidth(26), SizeHelper.fromPxWidth(10), SizeHelper.fromPxWidth(26), 0);
		linephone2.setLayoutParams(linephone2Params);
		linephone2.setId(ResHelper.getIdRes(context, "vw_divider2"));
		resid = ResHelper.getColorRes(context, "smssdk_line_light_gray");
		linephone2.setBackgroundResource(resid);
		linephone2.setVisibility(View.GONE);
		parent.addView(linephone2);

		Button inviteBtn = new Button(context);
		inviteBtn.setId(ResHelper.getIdRes(context, "btn_invite"));
		LinearLayout.LayoutParams inviteParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				SizeHelper.fromPxWidth(72));
		inviteParams.setMargins(SizeHelper.fromPxWidth(26), SizeHelper.fromPxWidth(40), SizeHelper.fromPxWidth(26), 0);
		inviteBtn.setLayoutParams(inviteParams);
		resid = ResHelper.getBitmapRes(context, "smssdk_btn_enable");
		inviteBtn.setBackgroundResource(resid);
		resid = ResHelper.getStringRes(context, "smssdk_send_invitation");
		inviteBtn.setText(resid);
		inviteBtn.setTextColor(0xffffffff);
		inviteBtn.setTextSize(TypedValue.COMPLEX_UNIT_PX,SizeHelper.fromPxWidth(28));
		inviteBtn.setPadding(0, 0, 0, 0);
		parent.addView(inviteBtn);
	}

}
