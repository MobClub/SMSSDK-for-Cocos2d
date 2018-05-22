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
import com.mob.tools.utils.ResHelper;


public class SizeHelper {
	public static final float DESIGNED_DENSITY = 1.5f;
	public static final int DESIGNED_SCREEN_WIDTH = 540;

	/**
	 * 根据density转换设计的px到目标机器，返回px大小
	 * @return 像素大小
	 */
	public static int fromPx(Context context, int px) {
		return ResHelper.designToDevice(context, DESIGNED_DENSITY, px);
	}

	/**
	* 根据屏幕宽度转换设计的px到目标机器，返回px大小
	* @return 像素大小
	*/
	public static int fromPxWidth(Context context, int px) {
		return ResHelper.designToDevice(context, DESIGNED_SCREEN_WIDTH, px);
	}

	/**
	* 根据density转换设计的dp到目标机器，返回px大小
	* @return 像素大小
	*/
	public static int fromDp(Context context, int dp) {
		int px = ResHelper.dipToPx(context, dp);
		return ResHelper.designToDevice(context, DESIGNED_DENSITY, px);
	}

}
