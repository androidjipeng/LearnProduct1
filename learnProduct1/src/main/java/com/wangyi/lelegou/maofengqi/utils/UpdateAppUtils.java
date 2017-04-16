package com.wangyi.lelegou.maofengqi.utils;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import com.wangyi.lelegou.maofengqi.bean.ResultBean;
import com.wangyi.lelegou.maofengqi.bean.UpdateAppBean;

public class UpdateAppUtils {

	// 检查更新
	public static void updateApp(final Activity activity) {
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("platform", "Android");
		paramsMap.put("packageName", activity.getPackageName());
		paramsMap.put("versionCode", Constant.VERSION_CODE);
		paramsMap.put("versionName", AppUtils.getVersionName(activity));
		ApiClass.updateApp(paramsMap, new ResultCallback<UpdateAppBean>() {

			@Override
			public void onSuccess(ResultBean<UpdateAppBean> resultBean, int id) {
				if (resultBean != null
						&& resultBean.getJsonMsg().getUpdateCode() == 1) {
					showUpdateIno(activity, resultBean.getJsonMsg());
				}
			}
		});
	}

	// 显示更新信息
	private static void showUpdateIno(final Activity activity, final UpdateAppBean bean) {
		new AlertDialog.Builder(activity).setTitle("更新提示")
				.setMessage(bean.getUpdateInfo().replaceAll("---", "\n"))
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Uri uri = Uri.parse(bean.getUpdateUrl());
						Intent intent = new Intent(Intent.ACTION_VIEW, uri);
						activity.startActivity(intent);
					}
				}).setNegativeButton("取消", null)
				.setCancelable(false)
				.create()
				.show();
	}
}