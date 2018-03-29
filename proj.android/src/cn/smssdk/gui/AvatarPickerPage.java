/*
 * 官网地站:http://www.mob.com
 * 技术支持QQ: 4006852216
 * 官方微信:ShareSDK   （如果发布新版本的话，我们将会第一时间通过微信将版本更新内容推送给您。如果使用过程中有任何问题，
 * 也可以通过微信与我们取得联系，我们将会在24小时内给予回复）
 *
 * Copyright (c) 2014年 mob.com. All rights reserved.
 */
package cn.smssdk.gui;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.mob.tools.FakeActivity;
import com.mob.tools.MobUIShell;
import com.mob.tools.gui.AsyncImageView;
import com.mob.tools.utils.ResHelper;

import java.util.HashMap;

import cn.smssdk.gui.util.Const;


public class AvatarPickerPage extends FakeActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

	public static final String INTENT_PICK_URL = "INTENT_PICK_URL";

	private GridAdapter adapter;

	@Override
	public void onCreate() {
		super.onCreate();
		activity.setContentView(ResHelper.getLayoutRes(getContext(), "smssdk_avatar_picker_page"));
		initView();
	}

	public void show(Context context) {
		Intent intent = new Intent(context, MobUIShell.class);
		show(context, intent);
	}

	@Override
	public void show(Context context, Intent i) {
		super.show(context, i);
	}

	private void initView()	{
		TextView tv = findViewById(ResHelper.getIdRes(getContext(), "tv_left"));
		tv.setText(ResHelper.getStringRes(getContext(), "smssdk_cancel"));
		tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
		tv.setOnClickListener(this);

		tv = findViewById(ResHelper.getIdRes(getContext(), "tv_title"));
		tv.setText(ResHelper.getStringRes(getContext(), "smssdk_pick_avatar"));

		tv = findViewById(ResHelper.getIdRes(getContext(), "tv_right"));
		tv.setText("");
		tv.setOnClickListener(this);

		GridView gridView = findViewById(ResHelper.getIdRes(getContext(), "gv_avator"));
		adapter = new GridAdapter();
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		String url = (String)adapter.getItem(position);
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put(INTENT_PICK_URL, url);
		setResult(params);
		sendResult();
		finish();
	}

	class GridAdapter extends BaseAdapter {

		private String[] data = Const.AVATOR_ARR;

		@Override
		public int getCount() {
			return null != data ? data.length : 0;
		}

		@Override
		public Object getItem(int position) {
			return data[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (null == convertView) {
				convertView = LayoutInflater.from(activity).inflate(ResHelper.getLayoutRes(getContext(), "smssdk_avatar_picker_item"), parent, false);
			}
			final Context context = getContext();
			final AsyncImageView iv = (AsyncImageView)convertView.findViewById(ResHelper.getIdRes(getContext(), "iv_avator_item"));
			iv.setRound(ResHelper.dipToPx(context, 65 / 2));
			String url = (String)getItem(position);
			iv.execute(url, ResHelper.getBitmapRes(context, "smssdk_cp_default_avatar"));
			return convertView;
		}
	}

	@Override
	public void onClick(View v) {
		final int id = v.getId();
		if (ResHelper.getIdRes(getContext(), "tv_left") == id) {
			finish();
		}
	}
}
