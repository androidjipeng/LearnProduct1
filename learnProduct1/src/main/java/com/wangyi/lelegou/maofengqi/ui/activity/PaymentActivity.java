//package com.wangyi.lelegou.maofengqi.ui.activity;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.CheckBox;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.learn.soft.product.jni.JiaZhengApp;
//import com.learn.soft.product1.R;
//import com.szfpay.tools.SZFPayInit;
//import com.tencent.mm.sdk.openapi.IWXAPI;
//import com.tencent.mm.sdk.openapi.WXAPIFactory;
//import com.wangyi.lelegou.maofengqi.base.BaseActivity;
//import com.wangyi.lelegou.maofengqi.bean.ResultBean;
//import com.wangyi.lelegou.maofengqi.utils.ApiClass;
//import com.wangyi.lelegou.maofengqi.utils.Constant2;
//import com.wangyi.lelegou.maofengqi.utils.ResultCallback;
//import com.wangyi.lelegou.maofengqi.utils.Utils;
//
///**
// * 结算支付
// *
// * @author Doc.March
// *
// */
//public class PaymentActivity extends BaseActivity implements OnClickListener {
//
//	private int number;
//	private String productName;
//	private String productPrice;
//	private boolean isVisibility;
//
//	private String orderNumber;
//	private String accountBalance;
//
//	private TextView tvCircleNumber;
//	private TextView tvProductName;
//	private TextView tvProductPrice;
//	private CheckBox checkBox;
//
//	private TextView tvBalance;
//	private TextView tvRecharge;
//
//	private LinearLayout llZhifubao;
//	private CheckBox cbZhifubao;
//
//	private LinearLayout llWeixin;
//	private CheckBox cbWeixin;
//
//	private int payType = -1;
//
//	private TextView tvPayment;
//
//	SZFPayInit tools;
//	// i聚合微信
//	private IWXAPI api;
//
//	private static PaymentActivity instance;
//
//	@Override
//	protected int getLayoutId() {
//		return R.layout.activity_payment;
//	}
//
//	@Override
//	protected void afterCreate(Bundle savedInstanceState) {
//		instance = this;
//		tvContent.setText("结算支付");
//
//		Bundle bundle = getIntent().getExtras();
//		number = bundle.getInt("number");
//		productName = bundle.getString("productName");
//		productPrice = bundle.getString("productPrice");
//		isVisibility = bundle.getBoolean("isVisibility");
//		orderNumber = bundle.getString("orderNumber");
//		accountBalance = bundle.getString("accountBalance");
//
//		tvCircleNumber = (TextView) findViewById(R.id.tv_circle_number);
//		tvProductName = (TextView) findViewById(R.id.tv_product_name);
//		tvProductPrice = (TextView) findViewById(R.id.tv_product_price);
//		checkBox = (CheckBox) findViewById(R.id.check_box);
//
//		tvBalance = (TextView) findViewById(R.id.tv_balance);
//		tvRecharge = (TextView) findViewById(R.id.tv_recharge);
//
//		llZhifubao = (LinearLayout) findViewById(R.id.ll_zhifubao);
//		cbZhifubao = (CheckBox) findViewById(R.id.cb_zhifubao);
//
//		llWeixin = (LinearLayout) findViewById(R.id.ll_weixin);
//		cbWeixin = (CheckBox) findViewById(R.id.cb_weixin);
//
//		tvPayment = (TextView) findViewById(R.id.tv_payment);
//
//		tvCircleNumber.setText(String.valueOf(number));
//		tvProductName.setText(productName);
//		tvProductPrice.setText("商品价值：¥ " + productPrice);
//		checkBox.setChecked(isVisibility);
//
//		tvBalance.setText("您的余额不足（账户余额：" + accountBalance + "元）");
//
//		tvRecharge.setOnClickListener(this);
//		llZhifubao.setOnClickListener(this);
//		llWeixin.setOnClickListener(this);
//		tvPayment.setOnClickListener(this);
//
//		// 初始化微信支付
//		tools = new SZFPayInit(this);
//		api = WXAPIFactory.createWXAPI(this, Constant2.wxappid);
//		api.registerApp(Constant2.wxappid);
//	}
//
//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.ll_zhifubao:
//			cbZhifubao.setChecked(true);
//			cbWeixin.setChecked(false);
//			break;
//		case R.id.ll_weixin:
//			cbZhifubao.setChecked(false);
//			cbWeixin.setChecked(true);
//			break;
//		case R.id.tv_payment:
//			payment();
//			break;
//		}
//	}
//
//	private void payment() {
//		if (cbZhifubao.isChecked()) {
//			payType = 2;
//		}
//		if (cbWeixin.isChecked()) {
//			payType = 3;
//		}
//		if (payType == -1) {
//			Toast.makeText(this, "请选择支付方式", Toast.LENGTH_SHORT).show();
//			return;
//		}
//		showProgressInfo("");
//		Map<String, Object> paramsMap = new HashMap<String, Object>();
//		paramsMap.put("id", JiaZhengApp.getInstance().getUserId());
//		paramsMap.put("orderNumber", orderNumber);// 订单编号
//		paramsMap.put("payType", payType);// 支付方式(1:余额支付 2:支付宝 3:微信支付)
//		ApiClass.payment(paramsMap, callback);
//	}
//
//	private ResultCallback<String> callback = new ResultCallback<String>() {
//
//		@Override
//		public void onSuccess(ResultBean<String> resultBean, int id) {
//			dismissProgress();
//			if (resultBean.getStatus() != 1) {
//				Toast.makeText(mActivity, resultBean.getInfo(),
//						Toast.LENGTH_SHORT).show();
//				return;
//			}
//			try {
//				JSONObject json = new JSONObject(resultBean.getJsonMsg());
//				String url = json.getString("paramenter");
//				// Constant.PAY_SHOW = 1;
//				Utils.wexin_ijuhe(mActivity, url, api);
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//		}
//
//		public void onError(okhttp3.Call call, Exception e, int id) {
//			super.onError(call, e, id);
//			dismissProgress();
//		}
//	};
//
//	// 启动PaymentActivity
//	public static void start(Context context, int number, String productName,
//			String productPrice, boolean isVisibility, String orderNumber,
//			String accountBalance) {
//		Intent intent = new Intent(context, PaymentActivity.class);
//		Bundle bundle = new Bundle();
//		bundle.putInt("number", number);
//		bundle.putString("productName", productName);
//		bundle.putString("productPrice", productPrice);
//		bundle.putBoolean("isVisibility", isVisibility);
//		bundle.putString("orderNumber", orderNumber);
//		bundle.putString("accountBalance", accountBalance);
//		intent.putExtras(bundle);
//		context.startActivity(intent);
//	}
//
//	public static PaymentActivity getInstance() {
//		return instance;
//	}
//}