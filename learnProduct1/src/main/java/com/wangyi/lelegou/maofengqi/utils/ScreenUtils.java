package com.wangyi.lelegou.maofengqi.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * **********************************************************
 * <p/>
 * 说明:屏幕相关辅助类
 * <p/>
 * 作者:@Doc.March
 * <p/>
 * 创建日期:2016-9-29
 * <p/>
 * 描述:TODO(用一句话描述该文件做什么)
 * <p/>
 * **********************************************************
 */
public class ScreenUtils {

	private ScreenUtils() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("cannot be instantiated");
	}

	// 获取手机状态栏高度
	// 使用Resource对象获取(推荐这种方式)
	public static int getStatusBarHeight(Context context) {
		int statusBarHeight = 0;
		int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resId > 0) {
			statusBarHeight = context.getResources().getDimensionPixelSize(resId);
		}
		return statusBarHeight;
	}

	// 获取手机状态栏高度
	public static int getStatusBarHeight2(Context context) {
		Class<?> c = null;
		Object obj = null;
		java.lang.reflect.Field field = null;
		int x = 0;
		int statusBarHeight = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			statusBarHeight = context.getResources().getDimensionPixelSize(x);
			return statusBarHeight;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return statusBarHeight;
	}

	// 获取ActionBar的高度
	public static int getActionBarHeight(Context context) {
		TypedValue tv = new TypedValue();
		int actionBarHeight = 0;
		// 如果资源是存在的、有效的
		if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
			actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, context
					.getResources().getDisplayMetrics());
		}
		return actionBarHeight;
	}

	// 状态栏处理：解决全屏切换非全屏页面被压缩问题
	@TargetApi(19)
	public static void initStatusBar(Activity activity, int statusBarColor) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

			// 获取状态栏高度
			int statusBarHeight = getStatusBarHeight(activity);

			// 绘制一个和状态栏一样高的矩形，并添加到视图中
			View rectView = new View(activity);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
			rectView.setLayoutParams(params);
			// 设置状态栏颜色
			rectView.setBackgroundColor(activity.getResources().getColor(statusBarColor));

			// 添加矩形View到布局中
			ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
			decorView.addView(rectView);
			// ViewGroup rootView = (ViewGroup) ((ViewGroup) activity
			// .findViewById(android.R.id.content)).getChildAt(0);
			// rootView.setFitsSystemWindows(true);
			// rootView.setClipToPadding(true);
		}
	}

	// 获得屏幕宽度
	public static int getScreenWidth(Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.widthPixels;
	}

	// 获得屏幕高度
	public static int getDeviceHeight(Context context) {
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics outMetrics = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(outMetrics);
		return outMetrics.heightPixels;
	}
}