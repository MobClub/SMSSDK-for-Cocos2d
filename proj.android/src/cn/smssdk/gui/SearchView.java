package cn.smssdk.gui;

import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.mob.tools.utils.ResHelper;

/**
 * Created by weishj on 2018/3/9.
 */

public class SearchView extends RelativeLayout {
	private Context context;
	private boolean enableClear;
	private EditText etSearch;

	public SearchView(Context context, boolean enableClear) {
		super(context);
		this.context = context;
		this.enableClear = enableClear;

		initView();
	}

	private void initView() {
		// 设置Container的属性
		setBackgroundResource(ResHelper.getColorRes(context, "smssdk_bg_gray"));
		setPadding(ResHelper.dipToPx(context, 10), ResHelper.dipToPx(context, 8),
				ResHelper.dipToPx(context, 10), ResHelper.dipToPx(context, 8));
		setGravity(Gravity.CENTER_VERTICAL);
		RelativeLayout.LayoutParams rlSearchParams = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ResHelper.dipToPx(context, 55));
		setLayoutParams(rlSearchParams);

		// 搜索框
		etSearch = new EditText(context);
		etSearch.setId(ResHelper.getIdRes(context, "et_put_identify"));
		etSearch.setHint(ResHelper.getStringRes(context, "smssdk_search"));
		int resId = ResHelper.getColorRes(context, "smssdk_tv_light_gray");
		etSearch.setHintTextColor(context.getResources().getColor(resId));
		etSearch.setGravity(Gravity.CENTER);
		etSearch.setSingleLine(true);
		etSearch.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
		etSearch.setBackgroundResource(ResHelper.getBitmapRes(context, "smssdk_conners_edittext_bg"));
		RelativeLayout.LayoutParams etSearchParams = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		etSearch.setLayoutParams(etSearchParams);
		addView(etSearch);

		// 清除按钮
		if (enableClear) {
			final ImageView ivClear = new ImageView(context);
			ivClear.setId(ResHelper.getIdRes(context, "iv_clear"));
			int res = ResHelper.getBitmapRes(context, "smssdk_clear_search");
			ivClear.setImageResource(res);
			ivClear.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
			ivClear.setVisibility(View.GONE);
			RelativeLayout.LayoutParams ivClearParams = new RelativeLayout.LayoutParams(
					ResHelper.dipToPx(context, 40), ViewGroup.LayoutParams.MATCH_PARENT);
			ivClearParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			ivClearParams.setMargins(0, 0, ResHelper.dipToPx(context, 10), 0);
			ivClear.setLayoutParams(ivClearParams);
			addView(ivClear);
			etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						ivClear.setVisibility(View.VISIBLE);
					}
				}
			});
		}
	}

	public void setEditTextBackgroundResource(int resId) {
		if (resId >= 0) {
			etSearch.setBackgroundResource(resId);
		}
	}

	public void setEditTextGravity(int gravity) {
		etSearch.setGravity(gravity);
	}
}
