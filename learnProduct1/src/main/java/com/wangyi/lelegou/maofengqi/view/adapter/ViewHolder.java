package com.wangyi.lelegou.maofengqi.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * **********************************************************
 * <p/>
 * 说明:适配器通用的ViewHolder
 * <p/>
 * 作者:@Doc.March
 * <p/>
 * 创建日期:2016-9-29
 * <p/>
 * 描述:
 * <p/>
 * **********************************************************
 */
public class ViewHolder {

	// 布局文件
	private View mConvertView;

	// 和Map类似，但是比Map效率高，不过键只能为Integer.用于存储控件.
	private SparseArray<View> mViews;

	private ViewHolder(Context mContext, ViewGroup parent, int layoutId) {
		this.mViews = new SparseArray<View>();
		mConvertView = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
		// 设置Tag
		mConvertView.setTag(this);
	}

	/**
	 * 拿到一个ViewHolder对象
	 * 
	 * @param context
	 *            上下文对象
	 * @param convertView
	 * @param parent
	 * @param layoutId
	 *            自己定义的 布局文件
	 * @return 返回ViewHolder
	 */
	public static ViewHolder getViewHolder(Context context, View convertView, ViewGroup parent,
			int layoutId) {
		// 判断convertView否为空，为空初始化
		if (convertView == null) {
			return new ViewHolder(context, parent, layoutId);
		}
		return (ViewHolder) convertView.getTag();
	}

	private ViewHolder(Context mContext, View convertView) {
		this.mViews = new SparseArray<View>();
		mConvertView = convertView;
		// 设置Tag
		mConvertView.setTag(this);
	}

	public static ViewHolder getViewHolder(Context context, View convertView) {
		return new ViewHolder(context, convertView);
	}

	/**
	 * 返回视图
	 * 
	 * @return mConvertView
	 */
	public View getConvertView() {
		return mConvertView;
	}

	/**
	 * 通过控件的Id获取对于的控件，如果没有则加入views
	 * 
	 * @param viewId
	 *            控件的Id
	 * @return View
	 */
	@SuppressWarnings("unchecked")
	public <T extends View> T getViewById(int viewId) {
		// 根据viewId在mViews中得到View
		View view = mViews.get(viewId);
		if (view == null) {
			view = mConvertView.findViewById(viewId);
			mViews.put(viewId, view);
		}
		return (T) view;
	}

	/**
	 * 为TextView设置字符串
	 * 
	 * @param viewId
	 *            资源Id
	 * @param text
	 *            需要显示的文字内容
	 * @return
	 */
	public ViewHolder setText(int viewId, String text) {
		TextView view = getViewById(viewId);
		view.setText(text);
		return this;
	}

	/**
	 * 为TextView设置字符串
	 * 
	 * @param viewId
	 *            资源Id
	 * @param textId
	 *            需要显示的文字资源Id
	 * @return
	 */
	public ViewHolder setText(int viewId, int textId) {
		TextView view = getViewById(viewId);
		view.setText(textId);
		return this;
	}

	/**
	 * 为ImageView设置图片
	 * 
	 * @param viewId
	 *            资源Id
	 * @param drawableId
	 *            图片资源Id
	 * @return
	 */
	public ViewHolder setImageResource(int viewId, int drawableId) {
		ImageView view = getViewById(viewId);
		view.setImageResource(drawableId);
		return this;
	}

	/**
	 * 为ImageView设置图片
	 * 
	 * @param viewId
	 *            资源Id
	 * @param bitmap
	 *            Bitmap图片资源
	 * @return
	 */
	public ViewHolder setImageBitmap(int viewId, Bitmap bitmap) {
		ImageView view = getViewById(viewId);
		view.setImageBitmap(bitmap);
		return this;
	}
}