package com.wangyi.lelegou.maofengqi.view.adapter;

import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * **********************************************************
 * <p/>
 * 说明:RecyclerView适配器
 * <p/>
 * 作者:@Doc.March
 * <p/>
 * 创建日期:2016-9-29
 * <p/>
 * 描述:TODO(用一句话描述该文件做什么)
 * <p/>
 * **********************************************************
 */
public abstract class RecyclerViewAdapter<T> extends
		RecyclerView.Adapter<RecyclerViewAdapter<T>.MyViewHolder> {

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

	private OnItemClickListener<T> mOnItemClickListener;

	private OnItemLongClickListener<T> mOnItemLongClickListener;

	public RecyclerViewAdapter(Context context, List<T> mDatas, int itemLayoutId) {
		this.mContext = context;
		this.mDatas = mDatas;
		this.mItemLayoutId = itemLayoutId;
	}

	@Override
	public int getItemCount() {
		return mDatas.size();
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		// 这里是创建ViewHolder的地方，RecyclerAdapter内部已经实现了ViewHolder的重用
		return new MyViewHolder(LayoutInflater.from(mContext).inflate(mItemLayoutId, parent, false));
	}

	@Override
	public void onBindViewHolder(final MyViewHolder holder, final int position) {
		final View itemView = holder.viewHolder.getConvertView();
		final T t = getItem(position);
		// 单击事件
		if (mOnItemClickListener != null) {
			itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mOnItemClickListener.onItemClick(itemView, position, t);
				}
			});
		}
		// 长点击事件
		if (mOnItemLongClickListener != null) {
			itemView.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					mOnItemLongClickListener.onItemLongClick(itemView, position, t);
					return true;
				}
			});
		}
		convert(holder.viewHolder, position, t);
	}

	public T getItem(int position) {
		return mDatas.get(position);
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

	class MyViewHolder extends RecyclerView.ViewHolder {

		ViewHolder viewHolder;

		public MyViewHolder(View convertView) {
			super(convertView);
			viewHolder = ViewHolder.getViewHolder(mContext, convertView);
		}
	}

	// 点击事件接口
	public interface OnItemClickListener<T> {

		void onItemClick(View view, int position, T t);
	}

	// 长按事件接口
	public interface OnItemLongClickListener<T> {

		void onItemLongClick(View view, int position, T t);
	}

	public void setOnItemClickListener(OnItemClickListener<T> listener) {
		mOnItemClickListener = listener;
	}

	public void setOnItemLongClickListener(OnItemLongClickListener<T> listener) {
		mOnItemLongClickListener = listener;
	}

	public void add(int location, T t) {
		mDatas.add(location, t);
		notifyItemInserted(location);
	}

	public boolean add(T t) {
		int lastIndex = mDatas.size();
		if (mDatas.add(t)) {
			notifyItemInserted(lastIndex);
			return true;
		} else {
			return false;
		}
	}

	public boolean addAll(int location, Collection<? extends T> collection) {
		if (mDatas.addAll(location, collection)) {
			notifyItemRangeInserted(location, collection.size());
			return true;
		} else {
			return false;
		}
	}

	public boolean addAll(Collection<? extends T> collection) {
		int lastIndex = mDatas.size();
		if (mDatas.addAll(collection)) {
			notifyItemRangeInserted(lastIndex, collection.size());
			return true;
		} else {
			return false;
		}
	}
}