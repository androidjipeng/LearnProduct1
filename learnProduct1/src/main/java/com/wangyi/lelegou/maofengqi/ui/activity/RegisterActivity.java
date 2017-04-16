package com.wangyi.lelegou.maofengqi.ui.activity;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.learn.soft.product1.R;
import com.wangyi.lelegou.maofengqi.base.BaseActivity;
import com.wangyi.lelegou.maofengqi.bean.ResultBean;
import com.wangyi.lelegou.maofengqi.utils.ApiClass;
import com.wangyi.lelegou.maofengqi.utils.Constant;
import com.wangyi.lelegou.maofengqi.utils.MD5Utils;
import com.wangyi.lelegou.maofengqi.utils.ResultCallback;
import com.wangyi.lelegou.maofengqi.utils.TimeCount;
import com.wangyi.lelegou.maofengqi.utils.VerificationUtils;

/**
 * **********************************************************
 * <p/>
 * 说明:注册
 * <p/>
 * 作者:@Doc.March
 * <p/>
 * 创建日期:2016-9-29
 * <p/>
 * 描述:
 * <p/>
 * **********************************************************
 */
public class RegisterActivity extends BaseActivity implements
		View.OnClickListener {

	private EditText etPhoneNumber, etCode, etPassword;
	private TextView tvSendCode;
	private TextView tvRegister;

	// 计时器
	private TimeCount time;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_register;
	}

	@Override
	protected void afterCreate(Bundle savedInstanceState) {
		tvContent.setText(R.string.register);

		etPhoneNumber = (EditText) findViewById(R.id.et_phone_number);
		etCode = (EditText) findViewById(R.id.et_code);
		etPassword = (EditText) findViewById(R.id.et_password);

		tvSendCode = (TextView) findViewById(R.id.tv_send_code);
		tvRegister = (TextView) findViewById(R.id.tv_register);

		tvSendCode.setOnClickListener(this);
		tvRegister.setOnClickListener(this);

		// 构造CountDownTimer对象
		time = new TimeCount(60000, 1000, tvSendCode);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_send_code:
			sendCode();
			break;
		case R.id.tv_register:
			register();
			break;
		default:
			break;
		}
	}

	// 发送验证码
	private void sendCode() {
		String phoneNumber = etPhoneNumber.getText().toString();
		if (VerificationUtils.verificationPhoneNumber(this, phoneNumber)) {
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("phoneNumber", phoneNumber);
			// 1:注册 2:修改密码 3：修改手机号
			paramsMap.put("type", 1);
			ApiClass.sendVerificationCode(paramsMap,
					sendVerificationCodeCallback);
		}
	}

	// 注册
	public void register() {
		String phoneNumber = etPhoneNumber.getText().toString();
		String code = etCode.getText().toString();
		String password = etPassword.getText().toString();
		if (VerificationUtils.registerVerification(this, phoneNumber, code,
				password)) {
			showProgressInfo(null);
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("phoneNumber", phoneNumber);
			paramsMap.put("code", code);
			paramsMap.put("password", MD5Utils.md5(password));
			// 添加渠道
			paramsMap.put("channel", Constant.CHANNEL);

			ApiClass.register(paramsMap, registerCallback);
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
			super.onError(call, e, id);
			dismissProgress();
		}
	};

	// 注册回调
	private ResultCallback<String> registerCallback = new ResultCallback<String>() {

		@Override
		public void onSuccess(ResultBean<String> resultBean, int id) {
			dismissProgress();
			Toast.makeText(mActivity, resultBean.getInfo(), Toast.LENGTH_SHORT)
					.show();
			if (resultBean.getStatus() == 1) {
				finish();
			}
		}

		public void onError(okhttp3.Call call, Exception e, int id) {
			super.onError(call, e, id);
			dismissProgress();
		}
	};

	// 开启Activity
	public static void start(Context context) {
		Intent intent = new Intent(context, RegisterActivity.class);
		context.startActivity(intent);
	}
}