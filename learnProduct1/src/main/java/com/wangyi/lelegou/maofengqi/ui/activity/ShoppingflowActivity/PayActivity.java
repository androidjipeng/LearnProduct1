package com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.learn.soft.product.jni.JiaZhengApp;
import com.learn.soft.product.util.ActivityFrgManager;
import com.learn.soft.product.util.MyLog;
import com.learn.soft.product.util.ToastHelper;
import com.learn.soft.product1.R;
import com.switfpass.pay.MainApplication;
import com.switfpass.pay.activity.PayPlugin;
import com.switfpass.pay.bean.RequestMsg;
import com.switfpass.pay.utils.XmlUtils;
import com.wangyi.lelegou.maofengqi.base.BaseActivity;
import com.wangyi.lelegou.maofengqi.ui.activity.MainActivity;
import com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity.bean.PayMoneyModel;
import com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity.pay.AliPayResult;
import com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity.pay.Pay;
import com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity.pay.PayConstants;
import com.wangyi.lelegou.maofengqi.ui.adapter.ShopPayListShowAdapter;
import com.wangyi.lelegou.maofengqi.utils.GetDeviceInformaton;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class PayActivity extends BaseActivity implements View.OnClickListener {

    private ListView shops_listview;
    private TextView money;
    private TextView leledouNum;
    private CheckBox leledouCheck;
    private CheckBox alipayCheck;
    private Button btn_paymoney;

    private Context context;

    private PayMoneyModel payMoneyModel;
    private String tag;
    private String shopidss;

    private String circleid;
    private String type;
    private String shopid;

    private CheckBox wx_check;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pay;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        context = this;


        ivBack = (ImageButton) findViewById(R.id.iv_back);
        tvContent = (TextView) findViewById(R.id.tv_content);
//        ivBack.setVisibility(View.VISIBLE);
        tvContent.setText("获取宝贝");

        shops_listview = (ListView) findViewById(R.id.shops_listview);
        money = (TextView) findViewById(R.id.money);
        leledouNum = (TextView) findViewById(R.id.leledouNum);
        leledouCheck = (CheckBox) findViewById(R.id.leledouCheck);
        alipayCheck = (CheckBox) findViewById(R.id.alipayCheck);
        wx_check= (CheckBox) findViewById(R.id.wx_check);
        leledouCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    alipayCheck.setChecked(false);
                    wx_check.setChecked(false);
                }
            }
        });
        alipayCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    leledouCheck.setChecked(false);
                    wx_check.setChecked(false);
                }
            }
        });

        wx_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    alipayCheck.setChecked(false);
                    leledouCheck.setChecked(false);
                }
            }
        });


        btn_paymoney = (Button) findViewById(R.id.btn_paymoney);
        btn_paymoney.setOnClickListener(this);

        /**获取计算数据  tag=1 代表从购物车来的，tag=2 圈中宝来的 tag=3 立即购买  */
        Intent intent = getIntent();
        if (intent != null) {
            tag = intent.getStringExtra("tag");
            if (tag.equals("1")) {
                tvContent.setText("结算支付");
                String shopids = intent.getStringExtra("shopids");
                shopidss = intent.getStringExtra("shopidss");
                showProgressInfo("");
                OkHttpUtils.post()
                        .url(Config.testurl + Config.doShoppingCartClearing)
                        .addParams("uid", JiaZhengApp.getInstance().getUserId())
                        .addParams("shopids", shopids)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int i) {
                                MyLog.e("jp", "PayActivity=====1=====onError:" + e.getMessage());
                                dismissProgress();
                            }

                            @Override
                            public void onResponse(String s, int i) {
                                dismissProgress();
                                MyLog.e("jp", "PayActivity=====1=====onResponse:" + s);
                                Gson gson = new Gson();
                                payMoneyModel = gson.fromJson(s, PayMoneyModel.class);
                                if (payMoneyModel.getResult() == 0) {
                                    List<PayMoneyModel.ObjListBean> objList = payMoneyModel.getObjList();
                                    ShopPayListShowAdapter adapter = new ShopPayListShowAdapter(context, objList);
                                    shops_listview.setAdapter(adapter);
                                    money.setText(payMoneyModel.getTotalMoney() + "元");
                                    leledouNum.setText("乐豆支付" + payMoneyModel.getTotalMoney() + "个" + "(账户乐豆：" + payMoneyModel.getLedou() + "个)");

                                }else {
                                    ToastHelper.toast(payMoneyModel.getInfo());
                                }

                            }
                        });

            }

            if (tag.equals("2")) {
                showProgressInfo("");
                String uid = JiaZhengApp.getInstance().getUserId();
                circleid = intent.getStringExtra("circleid");
                type = intent.getStringExtra("type");
                MyLog.e("jp","-------->uid:"+uid+"==>circleid:"+circleid+"===>type:"+type);
                OkHttpUtils.post()
                        .url(Config.testurl + Config.doClearing)
                        .addParams("uid", uid)
                        .addParams("circleId", circleid)
                        .addParams("type", type)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int i) {
                                MyLog.e("jp", "PayActivity=====2=====onError:" + e.getMessage());
                                dismissProgress();
                            }

                            @Override
                            public void onResponse(String s, int i) {
                                dismissProgress();
                                MyLog.e("jp", "PayActivity=======结算---2===onResponse:" + s);
                                Gson gson = new Gson();
                                payMoneyModel = gson.fromJson(s, PayMoneyModel.class);
                                if (payMoneyModel.getResult() == 0) {
                                    List<PayMoneyModel.ObjListBean> objList = payMoneyModel.getObjList();
                                    ShopPayListShowAdapter adapter = new ShopPayListShowAdapter(context, objList);
                                    shops_listview.setAdapter(adapter);
                                    money.setText(payMoneyModel.getTotalMoney() + "元");
                                    leledouNum.setText("乐豆支付" + payMoneyModel.getTotalMoney() + "个" + "(账户乐豆：" + payMoneyModel.getLedou() + "个)");
                                } else {
                                    ToastHelper.toast(payMoneyModel.getInfo());
                                }

                            }

                        });

            }
            if (tag.equals("3")) {
                showProgressInfo("");
                String uid = JiaZhengApp.getInstance().getUserId();
                shopid = intent.getStringExtra("shopid");
//                String circleid = intent.getStringExtra("circleid");
                type = intent.getStringExtra("type");
                OkHttpUtils.post()
                        .url(Config.testurl + Config.doClearing)
                        .addParams("uid", uid)
                        .addParams("shopid", shopid)
                        .addParams("type", type)
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int i) {
                                MyLog.e("jp", "PayActivity=====3=====onError:" + e.getMessage());
                                dismissProgress();
                            }

                            @Override
                            public void onResponse(String s, int i) {
                                dismissProgress();
                                MyLog.e("jp", "PayActivity=====3=====onResponse:" + s);
                                Gson gson = new Gson();
                                payMoneyModel = gson.fromJson(s, PayMoneyModel.class);
                                if (payMoneyModel.getResult() == 0) {
                                    List<PayMoneyModel.ObjListBean> objList = payMoneyModel.getObjList();
                                    ShopPayListShowAdapter adapter = new ShopPayListShowAdapter(context, objList);
                                    shops_listview.setAdapter(adapter);
                                    money.setText(payMoneyModel.getTotalMoney() + "元");
                                    leledouNum.setText("乐豆支付" + payMoneyModel.getTotalMoney() + "个" + "(账户乐豆：" + payMoneyModel.getLedou() + "个)");
                                }else {
                                    ToastHelper.toast(payMoneyModel.getInfo());
                                }
                            }
                        });

            }

        }

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_paymoney:

                paymony();
                break;
            default:
                break;
        }
    }

    private String orderCode;

    private void paymony() {
        /**用乐乐豆*/
        if (leledouCheck.isChecked()) {
            if ( payMoneyModel.getLedou()>=payMoneyModel.getTotalMoney())
            {


            /**购物车*/
            if (tag.equals("1")) {
                showProgressInfo("");
                OkHttpUtils
                        .post()
                        .url(Config.ledoupay)
                        .addParams("type","1")
                        .addParams("uid", JiaZhengApp.getInstance().getUserId())
                        .addParams("shoplist", shopidss)
                        .addParams("deviceInfo", GetDeviceInformaton.deviceInfo())
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int i) {
                                dismissProgress();
                                MyLog.e("jp", "购物车-tag=1=乐豆支付===onError：" + e.getMessage());
                            }

                            @Override
                            public void onResponse(String s, int i) {
                                dismissProgress();
                                MyLog.e("jp", "购物车-tag=1=乐豆支付===onResponse：" + s);
                                try {
                                    JSONObject object=new JSONObject(s);
                                    String result = object.getString("result");
                                    if (result.equals("0"))
                                    {
                                        ToastHelper.toast("支付成功");
                                        Intent intent=new Intent(context, MainActivity.class);
                                        startActivity(intent);
                                    }else
                                    {
                                        ToastHelper.toast("支付失败");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });


            }
            /**圈中宝来的*/
            if (tag.equals("2")) {

                showProgressInfo("");
                OkHttpUtils
                        .post()
                        .url(Config.ledoupay)
                        .addParams("type","3")
                        .addParams("uid", JiaZhengApp.getInstance().getUserId())
                        .addParams("circle_id", circleid)
                        .addParams("money",payMoneyModel.getTotalMoney()+"")
                        .addParams("deviceInfo", GetDeviceInformaton.deviceInfo())
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int i) {
                                dismissProgress();
                                MyLog.e("jp", "圈中宝来的-tag=2=乐豆支付===onError：" + e.getMessage());
                            }

                            @Override
                            public void onResponse(String s, int i) {
                                dismissProgress();
                                MyLog.e("jp", "圈中宝来的-tag=2=乐豆支付===onResponse：" + s);
                                try {
                                    JSONObject object=new JSONObject(s);
                                    String result = object.getString("result");
                                    if (result.equals("0"))
                                    {
                                        ToastHelper.toast("支付成功");
                                        Intent intent=new Intent(context, MainActivity.class);
                                        startActivity(intent);
                                    }else
                                    {
                                        ToastHelper.toast("支付失败");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

            }
            /**立即购买(折扣商品)*/
            if (tag.equals("3")) {
                showProgressInfo("");
                OkHttpUtils
                        .post()
                        .url(Config.ledoupay)
                        .addParams("type","2")
                        .addParams("uid", JiaZhengApp.getInstance().getUserId())
                        .addParams("shopid", shopid)
                        .addParams("money",payMoneyModel.getTotalMoney()+"")
                        .addParams("deviceInfo", GetDeviceInformaton.deviceInfo())
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int i) {
                                dismissProgress();
                                MyLog.e("jp", "立即购买(折扣商品)-tag=3=乐豆支付===onError：" + e.getMessage());
                            }

                            @Override
                            public void onResponse(String s, int i) {
                                dismissProgress();
                                MyLog.e("jp", "立即购买(折扣商品)-tag=3=乐豆支付===onResponse：" + s);
                                try {
                                    JSONObject object=new JSONObject(s);
                                    String result = object.getString("result");
                                    if (result.equals("0"))
                                    {   ToastHelper.toast("支付成功");
                                        Intent intent=new Intent(context, MainActivity.class);
                                        startActivity(intent);
                                    }else
                                    {
                                        ToastHelper.toast("支付失败");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            }

            }else
            {
                ToastHelper.toast("乐豆不够，请充值，或选择其他支付");
            }
        }


        /**用支付宝*/
        if (alipayCheck.isChecked()) {
            if (tag.equals("1"))//tag=1 代表从购物车来的
            {
                showProgressInfo("");
                OkHttpUtils
                        .post()
                        .url(Config.testurl + Config.doAliPayShoppingCart)
                        .addParams("uid", JiaZhengApp.getInstance().getUserId())
                        .addParams("shopids", shopidss)
                        .addParams("payType","1")
                        .addParams("deviceInfo", GetDeviceInformaton.deviceInfo())
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int i) {
                                dismissProgress();
                                MyLog.e("jp", "payactivity=用支付宝===tag=1----->onError===>" + e.getMessage());
                            }

                            @Override
                            public void onResponse(String s, int i) {
                                dismissProgress();
                                MyLog.e("jp", "payactivity=用支付宝===tag=1----->onResponse===>" + s);
                                try {
                                    JSONObject jsonObject = new JSONObject(s);
                                    String result = jsonObject.getString("result");
                                    String info = jsonObject.getString("info");
                                    if (result.equals("0")) {
                                        String orderString = jsonObject.getString("orderString");
                                        Pay.init(context).alipay(orderString, alipayHandler);
                                        orderCode = jsonObject.getString("orderCode");
                                        Pay.init(context).alipay(orderString);
                                    } else {
                                        ToastHelper.toast(info);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            }

            if (tag.equals("2"))//tag=2 圈中宝来的
            {
                showProgressInfo("");
                OkHttpUtils
                        .post()
                        .url(Config.testurl + Config.doAliPayCircle)
                        .addParams("uid", JiaZhengApp.getInstance().getUserId())
                        .addParams("circleId", circleid)
                        .addParams("type", type)
                        .addParams("payType","1")
                        .addParams("deviceInfo", GetDeviceInformaton.deviceInfo())
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int i) {
                                dismissProgress();
                                MyLog.e("jp", "payactivity=用支付宝===tag=2----->onError===>" + e.getMessage());
                            }

                            @Override
                            public void onResponse(String s, int i) {
                                dismissProgress();
                                MyLog.e("jp", "payactivity=用支付宝===tag=2----->onResponse===>" + s);
                                try {
                                    JSONObject jsonObject = new JSONObject(s);
                                    String result = jsonObject.getString("result");
                                    String info = jsonObject.getString("info");
                                    if (result.equals("0")) {
                                        String orderString = jsonObject.getString("orderString");
                                        Pay.init(context).alipay(orderString, alipayHandler);
                                        orderCode = jsonObject.getString("orderCode");
                                        Pay.init(context).alipay(orderString);
                                    } else {
                                        ToastHelper.toast(info);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
            }

            if (tag.equals("3"))// tag=3 立即购买(折扣商品)
            {

                showProgressInfo("");
                OkHttpUtils
                        .post()
                        .url(Config.testurl + Config.doAliPayCircle)
                        .addParams("uid", JiaZhengApp.getInstance().getUserId())
                        .addParams("shopid", shopid)
                        .addParams("type", type)
                        .addParams("payType","1")
                        .addParams("deviceInfo", GetDeviceInformaton.deviceInfo())
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int i) {
                                dismissProgress();
                                MyLog.e("jp", "payactivity=用支付宝===tag=3----->onError===>" + e.getMessage());
                            }

                            @Override
                            public void onResponse(String s, int i) {
                                dismissProgress();
                                MyLog.e("jp", "payactivity=用支付宝===tag=3----->onResponse===>" + s);
                                try {
                                    JSONObject jsonObject = new JSONObject(s);
                                    String result = jsonObject.getString("result");
                                    String info = jsonObject.getString("info");
                                    if (result.equals("0")) {
                                        String orderString = jsonObject.getString("orderString");
                                        Pay.init(context).alipay(orderString, alipayHandler);
                                        orderCode = jsonObject.getString("orderCode");
//                                        Pay.init(context).alipay(orderString);
                                    } else {
                                        ToastHelper.toast(info);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

            }
        }


        /**微信*/
        if (wx_check.isChecked())
        {
            if (tag.equals("1"))//tag=1 代表从购物车来的
            {
                showProgressInfo("");
                OkHttpUtils
                        .post()
                        .url(Config.testurl + Config.doAliPayShoppingCart)
                        .addParams("uid", JiaZhengApp.getInstance().getUserId())
                        .addParams("shopids", shopidss)
                        .addParams("payType","2")
                        .addParams("deviceInfo", GetDeviceInformaton.deviceInfo())
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int i) {
                                dismissProgress();
                                MyLog.e("jp", "payactivity=用支付宝===tag=1----->onError===>" + e.getMessage());
                            }

                            @Override
                            public void onResponse(String s, int i) {
                                dismissProgress();
                                MyLog.e("jp", "payactivity=用支付宝===tag=1----->onResponse===>" + s);
                                try {
                                    JSONObject jsonObject = new JSONObject(s);
                                    String result = jsonObject.getString("result");
                                    String info = jsonObject.getString("info");
                                    if (result.equals("0")) {
                                        String orderString = jsonObject.getString("orderString");
//                                        Pay.init(context).alipay(orderString, alipayHandler);
                                        orderCode = jsonObject.getString("orderCode");
                                        JiaZhengApp.getInstance().setWxorderCode(orderCode);
                                        HashMap parse = XmlUtils.parse(orderString);
                                        String token_id= (String) parse.get("token_id");

                                        RequestMsg msg=new RequestMsg();
                                        msg.setTokenId(token_id);//token_id为服务端预下单返回
                                        msg.setTradeType(MainApplication.WX_APP_TYPE);
                                        msg.setAppId(PayConstants.APP_ID);//appid为商户自己在微信开放平台的应用
                                        PayPlugin.unifiedAppPay(mActivity,msg);

                                    } else {
                                        ToastHelper.toast(info);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            }

            if (tag.equals("2"))//tag=2 圈中宝来的
            {
                showProgressInfo("");
                OkHttpUtils
                        .post()
                        .url(Config.testurl + Config.doAliPayCircle)
                        .addParams("uid", JiaZhengApp.getInstance().getUserId())
                        .addParams("circleId", circleid)
                        .addParams("type", type)
                        .addParams("payType","2")
                        .addParams("deviceInfo", GetDeviceInformaton.deviceInfo())
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int i) {
                                dismissProgress();
                                MyLog.e("jp", "payactivity=用支付宝===tag=2----->onError===>" + e.getMessage());
                            }

                            @Override
                            public void onResponse(String s, int i) {
                                dismissProgress();
                                MyLog.e("jp", "payactivity=用支付宝===tag=2----->onResponse===>" + s);
                                try {
                                    JSONObject jsonObject = new JSONObject(s);
                                    String result = jsonObject.getString("result");
                                    String info = jsonObject.getString("info");
                                    if (result.equals("0")) {
                                        String orderString = jsonObject.getString("orderString");
//                                        Pay.init(context).alipay(orderString, alipayHandler);
                                        orderCode = jsonObject.getString("orderCode");

                                        JiaZhengApp.getInstance().setWxorderCode(orderCode);
                                        HashMap parse = XmlUtils.parse(orderString);
                                        String token_id= (String) parse.get("token_id");

                                        RequestMsg msg=new RequestMsg();
                                        msg.setTokenId(token_id);//token_id为服务端预下单返回
                                        msg.setTradeType(MainApplication.WX_APP_TYPE);
                                        msg.setAppId(PayConstants.APP_ID);//appid为商户自己在微信开放平台的应用
                                        PayPlugin.unifiedAppPay(mActivity,msg);

                                    } else {
                                        ToastHelper.toast(info);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
            }

            if (tag.equals("3"))// tag=3 立即购买(折扣商品)
            {

                showProgressInfo("");
                OkHttpUtils
                        .post()
                        .url(Config.testurl + Config.doAliPayCircle)
                        .addParams("uid", JiaZhengApp.getInstance().getUserId())
                        .addParams("shopid", shopid)
                        .addParams("type", type)
                        .addParams("payType","2")
                        .addParams("deviceInfo", GetDeviceInformaton.deviceInfo())
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int i) {
                                dismissProgress();
                                MyLog.e("jp", "payactivity=用支付宝===tag=3----->onError===>" + e.getMessage());
                            }

                            @Override
                            public void onResponse(String s, int i) {
                                dismissProgress();
                                MyLog.e("jp", "payactivity=用支付宝===tag=3----->onResponse===>" + s);
                                try {
                                    JSONObject jsonObject = new JSONObject(s);
                                    String result = jsonObject.getString("result");
                                    String info = jsonObject.getString("info");
                                    if (result.equals("0")) {
                                        String orderString = jsonObject.getString("orderString");
//                                        Pay.init(context).alipay(orderString, alipayHandler);
                                        orderCode = jsonObject.getString("orderCode");

                                        JiaZhengApp.getInstance().setWxorderCode(orderCode);
                                        HashMap parse = XmlUtils.parse(orderString);
                                        String token_id= (String) parse.get("token_id");

                                        RequestMsg msg=new RequestMsg();
                                        msg.setTokenId(token_id);//token_id为服务端预下单返回
                                        msg.setTradeType(MainApplication.WX_APP_TYPE);
                                        msg.setAppId(PayConstants.APP_ID);//appid为商户自己在微信开放平台的应用
                                        PayPlugin.unifiedAppPay(mActivity,msg);

                                    } else {
                                        ToastHelper.toast(info);
                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

            }
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
                        MyLog.e("jp", "====支付宝支付完成之后===onResponse:" + e.getMessage());
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
                                Intent intent=new Intent(context, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

}
