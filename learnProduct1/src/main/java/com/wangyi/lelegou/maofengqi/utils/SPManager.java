package com.wangyi.lelegou.maofengqi.utils;

import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wangyi.lelegou.maofengqi.bean.HistoryRecordBean;

/**
 * **********************************************************
 * <p/>
 * 说明:SharedPreferences管理
 * <p/>
 * 作者:@Doc.March
 * <p/>
 * 创建日期:2016-9-29
 * <p/>
 * 描述:
 * <p/>
 * **********************************************************
 */
public class SPManager {

	// 获取SPManager实例
	private static SPManager mInstance;
	private static SharedPreferences preferences;

	// 私有构造方法,初始化
	private SPManager(Context context) {
		// 实例化SharedPreferences对象
		preferences = context.getSharedPreferences("sp", Context.MODE_PRIVATE);
	}

	// 获取SPManager对象
	public static SPManager getInstance(Context context) {
		if (mInstance == null) {
			synchronized (SPManager.class) {
				if (mInstance == null) {
					mInstance = new SPManager(context);
				}
			}
		}
		return mInstance;
	}

	// 设置是否是第一次激活
	public static void setActivationApp(boolean isActivationApp) {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean("isActivationApp", isActivationApp);
		editor.commit();
	}

	// 师傅是第一次激活
	public static boolean isActivationApp() {
		return preferences.getBoolean("isActivationApp", false);
	}

	// 设置是否删除了Cookies
	public static void setRemoveCookie(boolean isRemoveCookie) {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean("removeCookie", isRemoveCookie);
		editor.commit();
	}

	// 是否已经移除Cookies
	public static boolean isRemoveCookie() {
		return preferences.getBoolean("removeCookie", false);
	}

	// 设置是否登录
	public static void setLogin(boolean isLogin) {
		// 实例化SharedPreferences.Editor对象
		SharedPreferences.Editor editor = preferences.edit();
		// 用putString的方法保存数据
		editor.putBoolean("isLogin", isLogin);
		// 提交当前数据
		editor.commit();
	}

	// 是否登录
	public static boolean isLogin() {
		return preferences.getBoolean("isLogin", false);
	}

	// 设置用户Id
	public static void setUserId(String userId) {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("userId", userId);
		editor.commit();
	}

	// 获取用户Id
	public static String getUserId() {
		return preferences.getString("userId", "");
	}

	// 设置搜索历史记录
	public static void setHistoryRecord(List<HistoryRecordBean> list) {
		String historyRecord = new Gson().toJson(list);
		Log.i("info", historyRecord);

		SharedPreferences.Editor editor = preferences.edit();
		editor.putString("historyRecord", historyRecord);
		editor.commit();
	}

	public static int getInvitationStatus() {
		return preferences.getInt("invitationStatus", 0);
	}

	public static void setInvitationStatus(int invitationStatus) {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt("invitationStatus", invitationStatus);
		editor.commit();
	}

	// 得到搜索历史记录
	public static List<HistoryRecordBean> getHistoryRecord() {
		String historyRecord = preferences.getString("historyRecord", "");
		Log.i("info", "!!!" + historyRecord);

		List<HistoryRecordBean> list = new Gson().fromJson(historyRecord,
				new TypeToken<List<HistoryRecordBean>>() {
				}.getType());
		return list;
	}
}