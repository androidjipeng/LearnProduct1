package com.wangyi.lelegou.maofengqi.ui.activity;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.learn.soft.product.jni.JiaZhengApp;
import com.learn.soft.product1.R;
import com.wangyi.lelegou.maofengqi.base.BaseActivity;
import com.wangyi.lelegou.maofengqi.bean.ResultBean;
import com.wangyi.lelegou.maofengqi.utils.ApiClass;
import com.wangyi.lelegou.maofengqi.utils.Constant;
import com.wangyi.lelegou.maofengqi.utils.ResultCallback;
import com.wangyi.lelegou.maofengqi.utils.VerificationUtils;
import com.wangyi.lelegou.maofengqi.view.CustomClearEditText;

/**
 * **********************************************************
 * <p/>
 * 说明:修改昵称
 * <p/>
 * 作者:@Doc.March
 * <p/>
 * 创建日期:2016-9-29
 * <p/>
 * 描述:
 * <p/>
 * **********************************************************
 */
public class UpdateNickNameActivity extends BaseActivity implements
		View.OnClickListener {

	private CustomClearEditText etNickName;
	private String nickName;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_update_nick_name;
	}

	@Override
	protected void afterCreate(Bundle savedInstanceState) {
		tvContent.setText(R.string.update_nick_name);

		tvRight.setText(R.string.save);
		tvRight.setOnClickListener(this);

		etNickName = (CustomClearEditText) findViewById(R.id.et_nick_name);
		Bundle bundle = getIntent().getExtras();
		if (bundle != null && bundle.getString("nickName") != null) {
			nickName = bundle.getString("nickName");
			etNickName.setText(nickName);
			etNickName.setSelection(etNickName.getText().length());
		}
	}

	@Override
	public void onClick(View v) {
		nickName = etNickName.getText().toString();
		if (VerificationUtils.updateNickNameVerification(this, nickName)) {
			showProgressInfo(null);
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("id", JiaZhengApp.getInstance().getUserId());
			paramsMap.put("type", 1);// 修改昵称type=1,手机号码type=2
			paramsMap.put("nickName", nickName);
			paramsMap.put("phoneNumber", "");
			paramsMap.put("code", "");
			ApiClass.updatePersonalData(paramsMap, updatePersonalDataCallback);
		}
	}

	// 保存修改信息回调
	private ResultCallback<String> updatePersonalDataCallback = new ResultCallback<String>() {

		@Override
		public void onSuccess(ResultBean<String> resultBean, int id) {
			dismissProgress();
			Toast.makeText(mActivity, resultBean.getInfo(), Toast.LENGTH_SHORT)
					.show();
			if (resultBean.getStatus() == 1) {
				sendBroadcast(new Intent(Constant.ACTION_RELOAD));
				PersonalDataActivity.startForResult(mActivity);
			}
		}

		public void onError(okhttp3.Call call, Exception e, int id) {
			super.onError(call, e, id);
			dismissProgress();
		}
	};

	public static void start(Activity activity, String nickName, int requestCode) {
		Intent intent = new Intent(activity, UpdateNickNameActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("nickName", nickName);
		intent.putExtras(bundle);
		activity.startActivityForResult(intent, requestCode);
	}
}