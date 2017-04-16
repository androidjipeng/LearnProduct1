package com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.learn.soft.product.jni.JiaZhengApp;
import com.learn.soft.product.util.ActivityFrgManager;
import com.learn.soft.product.util.MyLog;
import com.learn.soft.product.util.ToastHelper;
import com.learn.soft.product1.R;
import com.switfpass.pay.MainApplication;
import com.switfpass.pay.activity.PayPlugin;
import com.switfpass.pay.bean.RequestMsg;
import com.switfpass.pay.handle.PayHandlerManager;
import com.switfpass.pay.utils.MD5;
import com.switfpass.pay.utils.SignUtils;
import com.switfpass.pay.utils.Util;
import com.switfpass.pay.utils.XmlUtils;
import com.wangyi.lelegou.maofengqi.base.BaseActivity;
import com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity.pay.AliPayResult;
import com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity.pay.Pay;
import com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity.pay.PayConstants;
import com.wangyi.lelegou.maofengqi.utils.GetDeviceInformaton;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import okhttp3.Call;

import static com.switfpass.pay.MainApplication.mchId;

public class AccountRechargeActivity extends BaseActivity implements View.OnClickListener {

    String TAG = "AccountRechargeActivity";
    private TextView current_leledouNum;//您的当前乐豆
    private LinearLayout ll_Nun10, ll_Nun20, ll_Nun30, ll_Nun100, ll_Nun200,ll_Nums;
    private EditText et_get_Num;
    private CheckBox alipay_Check,weixin_Check;
    private Button btn_recharge;

    private Context context;

    private String orderCode;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_account_recharge;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        context = this;
        tvContent = (TextView) findViewById(R.id.tv_content);
        tvContent.setText("账户充值");
//        tvRight = (TextView) findViewById(R.id.tv_right);
//        tvRight.setText("账户明细");
//        tvRight.setOnClickListener(this);

        current_leledouNum = (TextView) findViewById(R.id.current_leledouNum);
        ll_Nun10 = (LinearLayout) findViewById(R.id.ll_Nun10);
        ll_Nun10.setOnClickListener(this);
        ll_Nun20 = (LinearLayout) findViewById(R.id.ll_Nun20);
        ll_Nun20.setOnClickListener(this);
        ll_Nun30 = (LinearLayout) findViewById(R.id.ll_Nun30);
        ll_Nun30.setOnClickListener(this);
        ll_Nun100 = (LinearLayout) findViewById(R.id.ll_Nun100);
        ll_Nun100.setOnClickListener(this);
        ll_Nun200 = (LinearLayout) findViewById(R.id.ll_Nun200);
        ll_Nun200.setOnClickListener(this);
        ll_Nums=(LinearLayout) findViewById(R.id.ll_Nums);
        et_get_Num = (EditText) findViewById(R.id.et_get_Num);
        et_get_Num.setFocusableInTouchMode(true);
        et_get_Num.setFocusable(true);
        alipay_Check = (CheckBox) findViewById(R.id.alipay_Check);
        weixin_Check = (CheckBox) findViewById(R.id.weixin_Check);
        alipay_Check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    weixin_Check.setChecked(false);
                }
            }
        });
        weixin_Check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    alipay_Check.setChecked(false);
                }
            }
        });

        btn_recharge = (Button) findViewById(R.id.btn_recharge);
        btn_recharge.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        if (intent != null) {
            String lelegou = intent.getStringExtra("leledou");
            current_leledouNum.setText(lelegou);
        }

    }

    private String money="0";
    @Override
    public void onClick(View v) {
        et_get_Num.setFocusable(true);
        et_get_Num.setFocusableInTouchMode(true);
        switch (v.getId()) {
            case R.id.btn_recharge:
                recharge();
                break;
            case R.id.ll_Nun10:
                money="10";
                ll_Nun10.setBackgroundResource(R.drawable.red_corners);
                ll_Nun20.setBackgroundResource(R.drawable.white_corners);
                ll_Nun30.setBackgroundResource(R.drawable.white_corners);
                ll_Nun100.setBackgroundResource(R.drawable.white_corners);
                ll_Nun200.setBackgroundResource(R.drawable.white_corners);
                ll_Nums.setBackgroundResource(R.drawable.white_corners);
                break;
            case R.id.ll_Nun20:
                money="20";
                ll_Nun10.setBackgroundResource(R.drawable.white_corners);
                ll_Nun20.setBackgroundResource(R.drawable.red_corners);
                ll_Nun30.setBackgroundResource(R.drawable.white_corners);
                ll_Nun100.setBackgroundResource(R.drawable.white_corners);
                ll_Nun200.setBackgroundResource(R.drawable.white_corners);
                ll_Nums.setBackgroundResource(R.drawable.white_corners);
                break;
            case R.id.ll_Nun30:
                money="30";
                ll_Nun10.setBackgroundResource(R.drawable.white_corners);
                ll_Nun20.setBackgroundResource(R.drawable.white_corners);
                ll_Nun30.setBackgroundResource(R.drawable.red_corners);
                ll_Nun100.setBackgroundResource(R.drawable.white_corners);
                ll_Nun200.setBackgroundResource(R.drawable.white_corners);
                ll_Nums.setBackgroundResource(R.drawable.white_corners);
                break;
            case R.id.ll_Nun100:
                money="100";
                ll_Nun10.setBackgroundResource(R.drawable.white_corners);
                ll_Nun20.setBackgroundResource(R.drawable.white_corners);
                ll_Nun30.setBackgroundResource(R.drawable.white_corners);
                ll_Nun100.setBackgroundResource(R.drawable.red_corners);
                ll_Nun200.setBackgroundResource(R.drawable.white_corners);
                ll_Nums.setBackgroundResource(R.drawable.white_corners);
                break;
            case R.id.ll_Nun200:
                money="200";
                ll_Nun10.setBackgroundResource(R.drawable.white_corners);
                ll_Nun20.setBackgroundResource(R.drawable.white_corners);
                ll_Nun30.setBackgroundResource(R.drawable.white_corners);
                ll_Nun100.setBackgroundResource(R.drawable.white_corners);
                ll_Nun200.setBackgroundResource(R.drawable.red_corners);
                ll_Nums.setBackgroundResource(R.drawable.white_corners);
                break;
            case R.id.ll_Nums:
                money="0";
                et_get_Num.requestFocus();
                ll_Nun10.setBackgroundResource(R.drawable.white_corners);
                ll_Nun20.setBackgroundResource(R.drawable.white_corners);
                ll_Nun30.setBackgroundResource(R.drawable.white_corners);
                ll_Nun100.setBackgroundResource(R.drawable.white_corners);
                ll_Nun200.setBackgroundResource(R.drawable.white_corners);
                ll_Nums.setBackgroundResource(R.drawable.red_corners);
                break;
//            case R.id.tv_right:
//
//                break;
            default:
//                money="0";
//                ll_Nun10.setBackgroundResource(R.drawable.white_corners);
//                ll_Nun20.setBackgroundResource(R.drawable.white_corners);
//                ll_Nun30.setBackgroundResource(R.drawable.white_corners);
//                ll_Nun100.setBackgroundResource(R.drawable.white_corners);
//                ll_Nun200.setBackgroundResource(R.drawable.white_corners);
//                ll_Nums.setBackgroundResource(R.drawable.red_corners);

                break;
        }
    }

    private void recharge() {
        if (alipay_Check.isChecked()) {
            if (money.equals("0"))
            {
                money=et_get_Num.getText().toString();
            }
            showProgressInfo("");
            OkHttpUtils
                    .post()
                    .url(Config.testurl + Config.doAliPay)
                    .addParams("uid", JiaZhengApp.getInstance().getUserId())
                    .addParams("money", money)
                    .addParams("payType","1")
                    .addParams("deviceInfo", GetDeviceInformaton.deviceInfo())
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int i) {
                            dismissProgress();
                        }

                        @Override
                        public void onResponse(String s, int i) {
                            dismissProgress();
                            MyLog.e("jp", "去充值======>onResponse:" + s);
                            try {
                                JSONObject json = new JSONObject(s);
                                String result = json.getString("result");
                                String info = json.getString("info");

                                if (result.equals("0")) {
                                    String orderString = json.getString("orderString");
                                    orderCode = json.getString("orderCode");
                                    Pay.init(context).alipay(orderString, alipayHandler);
                                } else if (result.equals("1")) {
                                    ToastHelper.toast(info);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }

        /**微信支付*/
        if (weixin_Check.isChecked()) {
            if (money.equals("0"))
            {
                money=et_get_Num.getText().toString();
            }
            showProgressInfo("");
            OkHttpUtils
                    .post()
                    .url(Config.testurl + Config.doAliPay)
                    .addParams("uid", JiaZhengApp.getInstance().getUserId())
                    .addParams("money", money)
                    .addParams("payType","2")
                    .addParams("deviceInfo", GetDeviceInformaton.deviceInfo())
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int i) {
                            dismissProgress();
                        }

                        @Override
                        public void onResponse(String s, int i) {
                            dismissProgress();
                            MyLog.e("jp", "去充值======>onResponse:" + s);
                            try {
                                JSONObject json = new JSONObject(s);
                                String result = json.getString("result");
                                String info = json.getString("info");

                                if (result.equals("0")) {
                                    String orderString = json.getString("orderString");
                                    orderCode = json.getString("orderCode");
                                    MyLog.e("jp","======>>orderCode:"+orderCode);
                                    JiaZhengApp.getInstance().setWxorderCode(orderCode);
                                    HashMap parse = XmlUtils.parse(orderString);
                                    String token_id= (String) parse.get("token_id");

                                    RequestMsg msg=new RequestMsg();
                                    msg.setTokenId(token_id);//token_id为服务端预下单返回
                                    msg.setTradeType(MainApplication.WX_APP_TYPE);
                                    msg.setAppId(PayConstants.APP_ID);//appid为商户自己在微信开放平台的应用
                                    PayPlugin.unifiedAppPay(mActivity,msg);

//                                    Pay.init(context).alipay(orderString, alipayHandler);
                                } else if (result.equals("1")) {
                                    ToastHelper.toast(info);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }


    private Handler alipayHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PayConstants.ALI_PAY_FLAG: {
                    AliPayResult payResult = new AliPayResult((Map<String, String>) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
                     * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
                     * docType=1) 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档

                    switch (resultStatus) {
                        case PayConstants.STATUS_SUCCESS:
                            Toast.makeText(context, "支付成功", Toast.LENGTH_SHORT).show();

                            /**支付成功之后传ordercode*/
                            setordercodephp();

                            break;
                        case PayConstants.STATUS_CONFIRMING:
                            Toast.makeText(context, "支付结果确认中", Toast.LENGTH_SHORT).show();
                            break;
                        case PayConstants.STATUS_FAILED:
                            Toast.makeText(context, "支付失败" + resultInfo, Toast.LENGTH_SHORT).show();
                            break;
                        case PayConstants.STATUS_CANCEL:
                            Toast.makeText(context, "取消付款", Toast.LENGTH_SHORT).show();
                            break;
                        case PayConstants.STATUS_NET_ERROR:
                            Toast.makeText(context, "网络连接错误", Toast.LENGTH_SHORT).show();
                            break;
                        case PayConstants.STATUS_RESULT_UNKNOWN:
                            Toast.makeText(context, "支付结果未知,请确认是否支付成功", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(context, "未知错误,请确认是否支付成功", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }
        }
    };

    private void setordercodephp() {
        showProgressInfo("");
        OkHttpUtils
                .post()
                .url(Config.ordercode)
                .addParams("out_trade_no", orderCode)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        dismissProgress();
                        MyLog.e("jp", "====支付宝支付完成之后===onError:" + e.getMessage());
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        dismissProgress();
                        MyLog.e("jp", "====支付宝支付完成之后===onResponse:" + s);
                        try {
                            JSONObject object=new JSONObject(s);
                            String result = object.getString("result");
                            if (result.equals("0"))
                            {
                                ActivityFrgManager.getInstance().finishActivities();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }






}
