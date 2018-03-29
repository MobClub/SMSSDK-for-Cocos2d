package cn.smssdk.gui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mob.tools.gui.AsyncImageView;
import com.mob.tools.utils.ResHelper;

import cn.smssdk.gui.entity.Profile;
import cn.smssdk.gui.util.GUISPDB;

/**
 * Created by weishj on 2018/3/9.
 */

public class PersonalInfoView {
	private static View view;
	private AsyncImageView ivAvatar;
	private TextView tvNickname;
	private LinearLayout llPhone;
	private TextView tvPhone;
	private TextView tvBind;

	public static View create(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		view = inflater.inflate(ResHelper.getLayoutRes(context, "smssdk_personal_info"), null);
		// 为保证视觉美观，先隐藏，等通讯录列表数据加载完毕后再和通讯录列表一起显示（ContactsPage.initData()中显示）
		view.setVisibility(View.GONE);

		// 若本地有缓存的用户资料，更新UI
		Profile cachedProfile = GUISPDB.getProfile();
		if (cachedProfile != null) {
			// 头像
			AsyncImageView ivAvatar = ((AsyncImageView) view.findViewById(ResHelper.getIdRes(context, "iv_avatar")));
			ivAvatar.setRound(ResHelper.dipToPx(context, 60 / 2));
			ivAvatar.execute(cachedProfile.getAvatar(), ResHelper.getBitmapRes(context, "smssdk_cp_default_avatar"));
			// 昵称
			((TextView)view.findViewById(ResHelper.getIdRes(context, "tv_nickname"))).setText(cachedProfile.getNickName());
			// 手机号
			view.findViewById(ResHelper.getIdRes(context, "ll_phone_container")).setVisibility(View.VISIBLE);
			((TextView)view.findViewById(ResHelper.getIdRes(context, "tv_profile_phone"))).setText(cachedProfile.getPhoneNum());
			// 绑定按钮
			((TextView)view.findViewById(ResHelper.getIdRes(context, "tv_profile_rebind"))).setText(
					ResHelper.getStringRes(context, "smssdk_rebind_profile"));
		}

		return view;
	}

	public static void updateUI(Context context, Profile profile) {
		AsyncImageView ivAvatar = ((AsyncImageView) view.findViewById(ResHelper.getIdRes(context, "iv_avatar")));
		ivAvatar.setRound(ResHelper.dipToPx(context, 60 / 2));
		if (profile != null) {
			// 头像
			ivAvatar.execute(profile.getAvatar(), ResHelper.getBitmapRes(context, "smssdk_cp_default_avatar"));
			// 昵称
			((TextView)view.findViewById(ResHelper.getIdRes(context, "tv_nickname"))).setText(profile.getNickName());
			// 手机号
			view.findViewById(ResHelper.getIdRes(context, "ll_phone_container")).setVisibility(View.VISIBLE);
			((TextView)view.findViewById(ResHelper.getIdRes(context, "tv_profile_phone"))).setText(profile.getPhoneNum());
			// 绑定按钮
			((TextView)view.findViewById(ResHelper.getIdRes(context, "tv_profile_rebind"))).setText(
					ResHelper.getStringRes(context, "smssdk_rebind_profile"));
		} else {
			// 头像
			ivAvatar.execute(null, ResHelper.getBitmapRes(context, "smssdk_cp_default_avatar"));
			// 昵称
			((TextView)view.findViewById(ResHelper.getIdRes(context, "tv_nickname"))).setText(ResHelper.getStringRes(context, "smssdk_my_profile"));
			// 手机号
			view.findViewById(ResHelper.getIdRes(context, "ll_phone_container")).setVisibility(View.GONE);
			// 绑定按钮
			((TextView)view.findViewById(ResHelper.getIdRes(context, "tv_profile_rebind"))).setText(
					ResHelper.getStringRes(context, "smssdk_bind_profile"));
		}
	}
}
