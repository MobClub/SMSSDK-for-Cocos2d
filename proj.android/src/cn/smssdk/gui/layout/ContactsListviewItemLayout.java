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
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mob.tools.gui.AsyncImageView;
import com.mob.tools.utils.ResHelper;

/**联系人列表表项布局*/
public class ContactsListviewItemLayout {

	public static LinearLayout create(Context context) {
		SizeHelper.prepare(context);

		LinearLayout root = new LinearLayout(context);
		root.setId(ResHelper.getIdRes(context, "rl_lv_item_bg"));
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
				SizeHelper.fromPxWidth(95));
		root.setLayoutParams(params);
		int padding = SizeHelper.fromPxWidth(14);
		root.setPadding(padding, padding, padding, padding);
		root.setGravity(Gravity.CENTER_VERTICAL);
		root.setBackgroundColor(0xffffffff);

		AsyncImageView contactImage = new AsyncImageView(context);
		contactImage.setId(ResHelper.getIdRes(context, "iv_contact"));
		int width= SizeHelper.fromPxWidth(64);
		LinearLayout.LayoutParams contactImageParams = new LinearLayout.LayoutParams(width, width);
		float radius = width / 2;
		contactImage.setRound(radius);
		contactImage.setLayoutParams(contactImageParams);
		contactImage.setScaleType(ScaleType.FIT_CENTER);
		root.addView(contactImage);

		LinearLayout wrapper = new LinearLayout(context);
		LinearLayout.LayoutParams wrapperParams = new LinearLayout.LayoutParams(0,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		wrapperParams.weight = 1;
		wrapperParams.leftMargin = SizeHelper.fromPxWidth(12);
		wrapper.setLayoutParams(wrapperParams);
		wrapper.setOrientation(LinearLayout.VERTICAL);
		root.addView(wrapper);

		TextView name = new TextView(context);
		name.setId(ResHelper.getIdRes(context, "tv_name"));
		LinearLayout.LayoutParams nameParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		name.setLayoutParams(nameParams);
		name.setTextColor(0xff333333);
		name.setTextSize(TypedValue.COMPLEX_UNIT_PX,SizeHelper.fromPxWidth(28));
		name.setSingleLine(true);
		name.setEllipsize(TextUtils.TruncateAt.END);
		wrapper.addView(name);

		// 通讯录显示名
		TextView contact = new TextView(context);
		contact.setId(ResHelper.getIdRes(context, "tv_contact"));
		LinearLayout.LayoutParams contactParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		contact.setLayoutParams(contactParams);
		contact.setTextColor(0xff999999);
		contact.setTextSize(TypedValue.COMPLEX_UNIT_PX,SizeHelper.fromPxWidth(22));
		name.setSingleLine(true);
		name.setEllipsize(TextUtils.TruncateAt.END);
		// 新UI中去掉了该组件(隐藏）
//		contact.setVisibility(View.GONE);
		wrapper.addView(contact);

		Button add = new Button(context);
		add.setId(ResHelper.getIdRes(context, "btn_add"));
		LinearLayout.LayoutParams addParams = new LinearLayout.LayoutParams(SizeHelper.fromPxWidth(100),
				SizeHelper.fromPxWidth(46));
		addParams.leftMargin = ResHelper.dipToPx(context, 5);
		add.setLayoutParams(addParams);
		int resid = ResHelper.getStringRes(context, "smssdk_add_contact");
		add.setText(resid);
		add.setTextSize(TypedValue.COMPLEX_UNIT_PX, SizeHelper.fromPxWidth(22));
		add.setTextColor(0xffffffff);
		add.setBackgroundResource(ResHelper.getBitmapRes(context, "smssdk_btn_enable"));
		add.setPadding(0, 0, 0, 0);
		root.addView(add);

		return root;
	}
}
