package com.wangyi.lelegou.maofengqi.view.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * **********************************************************
 * <p/>
 * 说明:自定义通用CommonAdapter继承BaseAdapter
 * <p/>
 * 作者:@Doc.March
 * <p/>
 * 创建日期:2016-9-29
 * <p/>
 * 描述:
 * <p/>
 * **********************************************************
 */
public abstract class CommonAdapter<T> extends BaseAdapter {

	/**
	 * 上下文环境
	 */
	protected Context mContext;

	/**
	 * 数据
	 */
	protected List<T> mDatas;

	/**
	 * 布局文件资源Id
	 */
	protected int mItemLayoutId;

	public CommonAdapter(Context context, List<T> mDatas, int itemLayoutId) {
		this.mContext = context;
		this.mDatas = mDatas;
		this.mItemLayoutId = itemLayoutId;
	}

	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public T getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// 得到ViewHolder
		ViewHolder viewHolder = ViewHolder.getViewHolder(mContext, convertView, parent,
				mItemLayoutId);
		convert(viewHolder, position, getItem(position));
		return viewHolder.getConvertView();
	}

	/**
	 * 需要重写的方法
	 * 
	 * @param holder
	 *            ViewHolder
	 * @param position
	 *            索引
	 * @param item
	 *            对应索引的数据
	 */
	public abstract void convert(ViewHolder holder, int position, T item);

	public void setData(List<T> data) {
		this.mDatas = data;
	}

	public void setData(T[] data) {
		List<T> list = new ArrayList<T>();
		for (int i = 0; i < data.length; i++) {
			list.add(data[i]);
		}
		this.mDatas = list;
	}
}