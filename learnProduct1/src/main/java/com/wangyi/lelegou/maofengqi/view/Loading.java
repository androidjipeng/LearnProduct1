package com.wangyi.lelegou.maofengqi.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.WindowManager;

public class Loading {

	private Context mContext;
	private ProgressDialog mDialog;

	public Loading(Context context) {
		mContext = context;
	}

	/**
	 * 显示等待窗
	 */
	public void show() {
		show(null);
	}

	public void show(String title) {
		if (null == mDialog) {
			initDialog();
		}
		title = title == null || "".equals(title) ? "正在加载..." : title;
		mDialog.setMessage(title);
		mDialog.show();
	}

	/**
	 * 取消等待窗
	 */
	public void dismiss() {
		if (!((Activity) mContext).isFinishing()) {
			if (mDialog != null && mDialog.isShowing()) {
				mDialog.cancel();
			}
		}
	}

	private void initDialog() {
		mDialog = new ProgressDialog(mContext);
		mDialog.setCancelable(true);
		WindowManager.LayoutParams params = mDialog.getWindow().getAttributes();
		mDialog.getWindow().setAttributes(params);
	}
}