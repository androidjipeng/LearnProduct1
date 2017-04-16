package com.wangyi.lelegou.maofengqi.ui.activity;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.learn.soft.product.jni.JiaZhengApp;
import com.learn.soft.product.main.activity.ShowWebViewInfoActivity;
import com.learn.soft.product1.R;
import com.wangyi.lelegou.maofengqi.base.BaseActivity;
import com.wangyi.lelegou.maofengqi.bean.ResultBean;
import com.wangyi.lelegou.maofengqi.utils.ApiClass;
import com.wangyi.lelegou.maofengqi.utils.ResultCallback;
import com.wangyi.lelegou.maofengqi.utils.TimeCount;
import com.wangyi.lelegou.maofengqi.utils.VerificationUtils;
import com.wangyi.lelegou.maofengqi.view.CustomClearEditText;

/**
 * **********************************************************
 * <p/>
 * 说明:修改电话号码
 * <p/>
 * 作者:@Doc.March
 * <p/>
 * 创建日期:2016-9-29
 * <p/>
 * 描述:
 * <p/>
 * **********************************************************
 */
@SuppressWarnings("deprecation")
public class UpdatePhoneNumberActivity extends BaseActivity implements
		OnClickListener {

	private CustomClearEditText etPhoneNumber;
	private EditText etCode;
	private TextView tvSendCode;

	// 计时器
	private TimeCount time;

	private String phoneNumber;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_update_phone_number;
	}

	@Override
	protected void afterCreate(Bundle savedInstanceState) {
		tvContent.setText(R.string.update_phone_number);

		tvRight.setText(R.string.save);
		tvRight.setOnClickListener(this);

		etPhoneNumber = (CustomClearEditText) findViewById(R.id.et_phone_number);
		etCode = (EditText) findViewById(R.id.et_code);
		tvSendCode = (TextView) findViewById(R.id.tv_send_code);

		tvSendCode.setOnClickListener(this);

		// 构造CountDownTimer对象
		time = new TimeCount(60000, 1000, tvSendCode);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null && bundle.getString("phoneNumber") != null) {
			// phoneNumber = bundle.getString("phoneNumber");
			// etPhoneNumber.setText(phoneNumber);
			// etPhoneNumber.setSelection(etPhoneNumber.getText().length());
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_right:
			save();
			break;
		case R.id.tv_send_code:
			sendCode();
			break;
		default:
			break;
		}
	}

	// 保存修改信息
	private void save() {
		phoneNumber = etPhoneNumber.getText().toString();
		String code = etCode.getText().toString();
		if (VerificationUtils.updatePhoneVerification(this, phoneNumber, code)) {
			showProgressInfo(null);
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("id", JiaZhengApp.getInstance().getUserId());
			paramsMap.put("type", 2);
			paramsMap.put("nickName", "");
			paramsMap.put("phoneNumber", phoneNumber);
			paramsMap.put("code", code);
			ApiClass.updatePersonalData(paramsMap, updatePersonalDataCallback);
		}
	}

	// 发送验证码
	private void sendCode() {
		String phoneNumber = etPhoneNumber.getText().toString();
		if (VerificationUtils.verificationPhoneNumber(this, phoneNumber)) {
			showProgressInfo(null);
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("phoneNumber", phoneNumber);
			// 1:注册 2:修改密码 3：修改手机号
			paramsMap.put("type", 3);
			ApiClass.sendVerificationCode(paramsMap,
					sendVerificationCodeCallback);
		}
	}

	// 发送验证码回调
	private ResultCallback<String> sendVerificationCodeCallback = new ResultCallback<String>() {

		@Override
		public void onSuccess(ResultBean<String> resultBean, int id) {
			dismissProgress();
			Toast.makeText(mActivity, resultBean.getInfo(), Toast.LENGTH_SHORT)
					.show();
			if (resultBean.getStatus() == 1) {
				// 开始计时
				time.start();
			}
		}

		public void onError(okhttp3.Call call, Exception e, int id) {
			dismissProgress();
		}
	};

	// 保存修改信息回调
	private ResultCallback<String> updatePersonalDataCallback = new ResultCallback<String>() {

		@Override
		public void onSuccess(ResultBean<String> resultBean, int id) {
			dismissProgress();
			Toast.makeText(mActivity, resultBean.getInfo(), Toast.LENGTH_SHORT)
					.show();
			if (resultBean.getStatus() == 1) {
				relogin();
			}
		}

		public void onError(okhttp3.Call call, Exception e, int id) {
			super.onError(call, e, id);
			dismissProgress();
		}
	};

	private void relogin() {
		JiaZhengApp.getInstance().setLogin(false);
		JiaZhengApp.getInstance().setUserId("");
		removeCookie(getApplication());
		new AlertDialog.Builder(mActivity).setTitle("温馨提示").setMessage("请重新登录")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						if (PersonalDataActivity.instance != null) {
							PersonalDataActivity.instance.finish();
						}
						if (ShowWebViewInfoActivity.instance != null) {
							ShowWebViewInfoActivity.instance.finish();
						}
						LoginActivity.start(mActivity);
						finish();
					}
				}).setCancelable(false).create().show();
	}

	private void removeCookie(Context context) {
		CookieSyncManager.createInstance(context);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();
		CookieSyncManager.getInstance().sync();
	}

	public static void start(Activity activity, String phoneNumber,
			int requestCode) {
		Intent intent = new Intent(activity, UpdatePhoneNumberActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("phoneNumber", phoneNumber);
		intent.putExtras(bundle);
		activity.startActivityForResult(intent, requestCode);
	}
}