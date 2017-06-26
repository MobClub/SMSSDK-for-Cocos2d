//#if def{lang} == cn
/*
 * 官网地站:http://www.mob.com
 * 技术支持QQ: 4006852216
 * 官方微信:ShareSDK   （如果发布新版本的话，我们将会第一时间通过微信将版本更新内容推送给您。如果使用过程中有任何问题，也可以通过微信与我们取得联系，我们将会在24小时内给予回复）
 * 
 * Copyright (c) 2014年 mob.com. All rights reserved.
 */
//#elif def{lang} == en
/*
 * Offical Website:http://www.mob.com
 * Support QQ: 4006852216
 * Offical Wechat Account:ShareSDK   (We will inform you our updated news at the first time by Wechat, if we release a new version. If you get any problem, you can also contact us with Wechat, we will reply you within 24 hours.)
 * 
 * Copyright (c) 2013 mob.com. All rights reserved.
 */
//#endif
package cn.smssdk.gui.layout;

import android.content.Context;
import com.mob.tools.utils.ResHelper;


public class SizeHelper {
	public static float designedDensity = 1.5f;
	public static int designedScreenWidth = 540;
	private static Context context = null;
	
	protected static SizeHelper helper;
	
	private SizeHelper() {
	}
	
	public static void prepare(Context c) {
		if(context == null || context != c.getApplicationContext()) {
			context = c;
		}
	}
	
	//#if def{lang} == cn
	/**
	 * 根据density转换设计的px到目标机器，返回px大小
	 * @return 像素大小
	 */
	//#elif def{lang} == en
	/**
	 * convert to target pixel from designed pixel by density
	 * @return size in pixel
 	 */
	//#endif
	public static int fromPx(int px) {
		return ResHelper.designToDevice(context, designedDensity, px);
	}
	
	//#if def{lang} == cn
	/**
	* 根据屏幕宽度转换设计的px到目标机器，返回px大小
	* @return 像素大小
	*/
	//#elif def{lang} == en
	/**
	* convert to target pixel from designed pixel by screen width
	* @return size in pixel
	*/
	//#endif
	public static int fromPxWidth(int px) {
		return ResHelper.designToDevice(context, designedScreenWidth, px);
	}
	
	//#if def{lang} == cn
	/**
	* 根据density转换设计的dp到目标机器，返回px大小
	* @return 像素大小
	*/
	//#elif def{lang} == en
	/**
	* convert to target pixel from designed dp by density
	* @return size in pixel
	*/
	//#endif
	public static int fromDp(int dp) {
		int px = ResHelper.dipToPx(context, dp);
		return ResHelper.designToDevice(context, designedDensity, px);
	}
	
}
