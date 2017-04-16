package com.wangyi.lelegou.maofengqi.utils;

import okhttp3.Call;

import android.util.Log;
import android.widget.Toast;

import com.learn.soft.product.jni.JiaZhengApp;
import com.zhy.http.okhttp.callback.StringCallback;

public class OldResultCallback extends StringCallback {

	@Override
	public void onError(Call call, Exception e, int id) {
		Toast.makeText(JiaZhengApp.getInstance(), e.getMessage(),
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onResponse(String response, int id) {
		Log.i("info", "response:" + response);
	}
}