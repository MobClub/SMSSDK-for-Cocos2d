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
import android.view.ViewGroup;
import android.widget.LinearLayout;

//#if def{lang} == cn
/**页面布局基类*/
//#elif def{lang} == en
/** page basic layout*/
//#endif
public abstract class BasePageLayout {
	LinearLayout layout = null;
	Context context = null;
	
	public BasePageLayout(Context c,boolean isSearch) {
		context = c;
		layout = new LinearLayout(context);
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		layout.setLayoutParams(params);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setBackgroundColor(0xffffffff);
		
		LinearLayout title = TitleLayout.create(context,isSearch);
		layout.addView(title);
		onCreateContent(layout);
	}
	
	//#if def{lang} == cn
	/**获取生成的布局*/
	//#elif def{lang} == en
	/** get the layout created*/
	//#endif
	public LinearLayout getLayout() {
		return layout;
	}
	
	protected abstract void onCreateContent(LinearLayout parent);
}
