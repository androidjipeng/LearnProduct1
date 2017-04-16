package com.learn.soft.product.jni;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.WindowManager;

public class Loading {
    private ProgressDialog mDialog;
    private Context mContext;

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
        title=title==null||"".equals(title)? "正在加载...": title;
        mDialog.setMessage(title);
        mDialog.show();
    }

    /**
     * 取消等待窗
     */
    public void dismiss() {
        if (null != mDialog) {
            mDialog.cancel();
        }

    }

    private void initDialog() {
        mDialog = new ProgressDialog(mContext);
        mDialog.setCancelable(true);
        WindowManager.LayoutParams params = mDialog.getWindow().getAttributes();
        mDialog.getWindow().setAttributes(params);
    }
    
    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
