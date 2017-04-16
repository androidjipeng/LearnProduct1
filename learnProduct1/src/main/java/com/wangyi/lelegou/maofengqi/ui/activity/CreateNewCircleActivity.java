package com.wangyi.lelegou.maofengqi.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.learn.soft.product.jni.JiaZhengApp;
import com.learn.soft.product.util.ToastHelper;
import com.learn.soft.product1.R;
import com.wangyi.lelegou.maofengqi.base.BaseActivity;
import com.wangyi.lelegou.maofengqi.bean.ResultBean;
import com.wangyi.lelegou.maofengqi.utils.ApiClass;
import com.wangyi.lelegou.maofengqi.utils.ResultCallback;
import com.wangyi.lelegou.maofengqi.view.CustomClearEditText;

import java.util.HashMap;
import java.util.Map;

/**
 * 建立圈子
 * 
 * @author Doc.March
 * 
 */
public class CreateNewCircleActivity extends BaseActivity implements
		OnClickListener {

	private CustomClearEditText etCircleNumber;
	private LinearLayout rlSelectProduct;
	private TextView tvProductName;
	private TextView tvProductPrice;
	private CheckBox checkBox;

	private int number;
	private int productId = -1;
	private String productName;
	private String productPrice;
	private boolean isVisibility;

	private int circleId;

	private final int REQUEST_CODE = 1;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_create_new_circle;
	}

	@Override
	protected void afterCreate(Bundle savedInstanceState) {
		tvContent.setText("建立圈子");
		tvRight.setText("下一步");
		tvRight.setOnClickListener(this);

		etCircleNumber = (CustomClearEditText) findViewById(R.id.et_circle_number);
		rlSelectProduct = (LinearLayout) findViewById(R.id.rl_select_product);
		tvProductName = (TextView) findViewById(R.id.tv_product_name);
		tvProductPrice = (TextView) findViewById(R.id.tv_product_price);
		checkBox = (CheckBox) findViewById(R.id.check_box);

		rlSelectProduct.setOnClickListener(this);

		// productId = 3760;
		// productName = "话费充值20元";
		// productPrice = "24.00";
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_right:
			next();
			break;
		case R.id.rl_select_product:
			SelectProductActivity.start(this, REQUEST_CODE);
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
			if (data != null) {
				Bundle bundle = data.getExtras();
				productId = bundle.getInt("productId");
				productName = bundle.getString("productName");
				productPrice = bundle.getString("productPrice");

				tvProductName.setText(productName);
				tvProductPrice.setText("商品价格：¥ " + productPrice);
			}
		}
	}

	private void next() {
		if (TextUtils.isEmpty(etCircleNumber.getText())
				|| Integer.parseInt(etCircleNumber.getText().toString()) <= 0) {
			Toast.makeText(this, "请输入需要加入的人次", Toast.LENGTH_SHORT).show();
			return;
		}
		if (productId == -1) {
			Toast.makeText(this, "请选择宝贝", Toast.LENGTH_SHORT).show();
			return;
		}

		number = Integer.parseInt(etCircleNumber.getText().toString());
		isVisibility = checkBox.isChecked();

//		new AlertDialog.Builder(this).setMessage("建立圈子需要支付两个乐豆的费用，是否继续？")
//				.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//						dialog.dismiss();
//						createNewCircle();
//					}
//				}).setNegativeButton("取消", null).setCancelable(false).create()
//				.show();
		createNewCircle();
	}

	// 新建圈子
	private void createNewCircle() {
		showProgressInfo(null);
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("id", JiaZhengApp.getInstance().getUserId());
		paramsMap.put("number", number);
		paramsMap.put("productId", productId);
//		paramsMap.put("isVisibility", isVisibility);
		ApiClass.createNewCircle(paramsMap, callback);
	}

	private ResultCallback<String> callback = new ResultCallback<String>() {

		@Override
		public void onSuccess(ResultBean<String> resultBean, int id) {
			dismissProgress();
			if (resultBean.getStatus() != 1) {
				Toast.makeText(mActivity, resultBean.getInfo(),
						Toast.LENGTH_SHORT).show();
				return;
			}
			success(resultBean.getJsonMsg());
		}

		public void onError(okhttp3.Call call, Exception e, int id) {
			super.onError(call, e, id);
			dismissProgress();
		}
	};

	private void success(String jsonMsg) {
//		ToastHelper.toast(jsonMsg);
		finish();
//		try {
//			CreateNewCircleBean bean = new Gson().fromJson(jsonMsg,
//					new TypeToken<CreateNewCircleBean>() {
//					}.getType());
//			circleId = bean.getCircle_id();
//			sendBroadcast(new Intent(Constant.ACTION_CREATE_NEW_CIRCLE));
//			String url = Constant.CIRCLE_IP + "payCircle/" + circleId;
//			Utils.startWebViewShow(this, "", url);
//			finish();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	// 启动CreateNewCircleActivity
	public static void start(Context context) {
		Intent intent = new Intent(context, CreateNewCircleActivity.class);
		context.startActivity(intent);
	}
}