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
package cn.smssdk.gui;

import static com.mob.tools.utils.R.getBitmapRes;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

//#if def{lang} == cn
/** 自定义联系人的列表控件*/
//#elif def{lang} == en
/** A listview of contacts*/
//#endif
public class ContactsListView extends RelativeLayout {
	private ListView lvBody;
	private InnerAdapter innerAdapter;
	private GroupAdapter adapter;
	private TextView tvTitle;
	private int curFirstItem;
	private int titleHeight;
	private OnScrollListener osListener;
	
	public ContactsListView(Context context) {
		super(context);
		init(context);
	}

	public ContactsListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public ContactsListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	
	private void init(Context context) {
		
		lvBody = new ListView(context);
		lvBody.setCacheColorHint(0);
		lvBody.setSelector(new ColorDrawable());
		int resId = getBitmapRes(context, "smssdk_cl_divider");
		if (resId > 0) {
			lvBody.setDivider(context.getResources().getDrawable(resId));
		}
		lvBody.setDividerHeight(1);
		lvBody.setVerticalScrollBarEnabled(false);
		lvBody.setOnScrollListener(new OnScrollListener() {
			
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (osListener != null) {
					osListener.onScrollStateChanged(view, scrollState);
				}
			}
			
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				curFirstItem = firstVisibleItem;
				if (tvTitle != null) {
					ContactsListView.this.onScroll();
				}
				
				if (osListener != null) {
					osListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
				}
			}
		});
		lvBody.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		addView(lvBody);
	}
	
	public void setAdapter(GroupAdapter adapter) {
		this.adapter = adapter;
		innerAdapter = new InnerAdapter(adapter);
		lvBody.setAdapter(innerAdapter);
		setTitle();
	}
	
	public GroupAdapter getAdapter() {
		return adapter;
	}

	private void notifyDataSetChanged() {
		innerAdapter.notifyDataSetChanged();
		setTitle();
	}
	
	//#if def{lang} == cn
	// 设置标题
	//#elif def{lang} == en
	// Setting the layout of group title
	//#endif
	private void setTitle() {
		if (tvTitle != null) {
			removeView(tvTitle);
		}
		if(innerAdapter.getCount() == 0 ){
			return;
		}
		int group = innerAdapter.getItemGroup(curFirstItem);
		int position = innerAdapter.titleIndex.get(group);
		tvTitle = (TextView) innerAdapter.getView(position, null, this);
		LayoutParams lp = new LayoutParams(
				LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		lp.addRule(ALIGN_PARENT_LEFT);
		lp.addRule(ALIGN_PARENT_TOP);
		addView(tvTitle, lp);
		
		tvTitle.measure(0, 0);
		titleHeight = tvTitle.getMeasuredHeight();
		onScroll();
	}
	
	public void setSelection(int group) {
		setSelection(group, -1);
	}
	
	public void setSelection(int group, int position) {
		int titleIndex = innerAdapter.titleIndex.get(group);
		int selection = titleIndex + position + 1;
		lvBody.setSelection(selection);
	}
	
	//#if def{lang} == cn
	// 滚动时，改变标题栏
	//#elif def{lang} == en
	// Change the group title when scroll 
	//#endif
	private void onScroll() {
		LayoutParams lp = (LayoutParams) tvTitle.getLayoutParams();
		if (innerAdapter.isLastItem(curFirstItem)) { 
			//#if def{lang} == cn
			// 前一组最后一项
			//#elif def{lang} == en
			// the last item of the previous group
			//#endif
			int group = innerAdapter.getItemGroup(curFirstItem);
			String title = adapter.getGroupTitle(group);
			tvTitle.setText(title);
			int top = lvBody.getChildAt(1).getTop();
			if (top < titleHeight) {
				lp.setMargins(0, top - titleHeight, 0, 0);
				tvTitle.setLayoutParams(lp);
				return;
			}
		}
		lp.topMargin = 0;
		tvTitle.setLayoutParams(lp);
		
		if (innerAdapter.isTitle(curFirstItem)) {
			//#if def{lang} == cn
			// 标题项
			//#elif def{lang} == en
			// the group title
			//#endif
			int group = innerAdapter.getItemGroup(curFirstItem);
			String title = adapter.getGroupTitle(group);
			tvTitle.setText(title);
		}
	}
	
	//#if def{lang} == cn
	// 设置滚动监听
	//#elif def{lang} == en
	//#endif
	public void setOnScrollListener(OnScrollListener l) {
		osListener = l;
	}
	//#if def{lang} == cn
	/** 自定义listview的适配器*/
	//#elif def{lang} == en
	/** the adapter of contacts-listview */
	//#endif
	private static class InnerAdapter extends BaseAdapter {
		private GroupAdapter adapter;
		private ArrayList<Object> listData;
		private ArrayList<Integer> titleIndex;
		private ArrayList<Integer> lastItemIndex;
		
		public InnerAdapter(GroupAdapter adapter) {
			this.adapter = adapter;
			listData = new ArrayList<Object>();
			titleIndex = new ArrayList<Integer>();
			lastItemIndex = new ArrayList<Integer>();
			init();
		}
		
		private void init() {
			listData.clear();
			titleIndex.clear();
			lastItemIndex.clear();
			for (int g = 0, gc = adapter.getGroupCount(); g < gc; g++) {
				int c = adapter.getCount(g);
				if (c > 0) {
					titleIndex.add(listData.size());
					listData.add(adapter.getGroupTitle(g));
					for (int i = 0; i < c; i++) {
						listData.add(adapter.getItem(g, i));
					}
					lastItemIndex.add(listData.size() - 1);
				}
			}
		}

		public int getCount() {
			return listData.size();
		}

		public Object getItem(int position) {
			return listData.get(position);
		}
		
		public long getItemId(int position) {
			return position;
		}
		
		public int getItemGroup(int position) {
			int size = titleIndex.size();
			for (int i = 0; i < size; i++) {
				int titleIndex = this.titleIndex.get(i);
				if (position < titleIndex) {
					return i - 1;
				}
			}
			return size - 1;
		}
		
		public boolean isTitle(int position) {
			for (int i = 0, size = titleIndex.size(); i < size; i++) {
				if (titleIndex.get(i) == position) {
					return true;
				}
			}
			return false;
		}
		
		public int getViewTypeCount() {
			return 2;
		}
		
		public int getItemViewType(int position) {
			return isTitle(position) ? 0 : 1;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			int group = getItemGroup(position);
			if (isTitle(position)) {
				if (convertView != null && (convertView instanceof TextView)) {
					convertView = adapter.getTitleView(group, (TextView) convertView, parent);
				} else {
					convertView = adapter.getTitleView(group, null, parent);
				}
				
			} else {
				int item = position - titleIndex.get(group) - 1;
				convertView = adapter.getView(group, item, convertView, parent);
			}
			return convertView;
		}
		
		public void notifyDataSetChanged() {
			init();
			super.notifyDataSetChanged();
		}
		
		public boolean isLastItem(int position) {
			for (int i = 0, size = lastItemIndex.size(); i < size; i++) {
				if (lastItemIndex.get(i) == position) {
					return true;
				}
			}
			return false;
		}
		
	}
	//#if def{lang} == cn
	/** 自定义组的abstract类，用于填充listview的item*/
	//#elif def{lang} == en
	/** A abstract class of group-contacts-adapter*/
	//#endif
	public static abstract class GroupAdapter {
		protected final ContactsListView view;
		
		public GroupAdapter(ContactsListView view) {
			this.view = view;
		}
		//#if def{lang} == cn
		/** 获取组的数据个数*/
		//#elif def{lang} == en
		//#endif
		public abstract int getGroupCount();
		
		//#if def{lang} == cn
		/**
		 * 获取某组的item个数
		 * @param group
		 * @return
		 */
		//#elif def{lang} == en
		/**
		 * get items' count of group 
		 * @param group
		 * @return
		 */
		//#endif
		public abstract int getCount(int group);
		
		//#if def{lang} == cn
		/**
		 * 获取某组的title标签
		 * @param group
		 * @return
		 */
		//#elif def{lang} == en
		//#endif
		public abstract String getGroupTitle(int group);
		
		//#if def{lang} == cn
		/**
		 * 获取某组第positon的位置的数据对象
		 * @param group
		 * @param position
		 * @return
		 */
		//#elif def{lang} == en
		//#endif
		public abstract Object getItem(int group, int position);
		
		//#if def{lang} == cn
		/**
		 * 获取某组的title的View
		 * @param group
		 * @param convertView
		 * @param parent
		 * @return
		 */
		//#elif def{lang} == en
		//#endif
		public abstract TextView getTitleView(int group, TextView convertView, ViewGroup parent);
		//#if def{lang} == cn
		/**
		 * 获取某组第positon的位置的item View
		 * @param group
		 * @param position
		 * @param convertView
		 * @param parent
		 * @return
		 */
		//#elif def{lang} == en
		//#endif
		public abstract View getView(int group, int position, View convertView, ViewGroup parent);
		
		public void notifyDataSetChanged() {
			view.notifyDataSetChanged();
		}
		
	}

}
