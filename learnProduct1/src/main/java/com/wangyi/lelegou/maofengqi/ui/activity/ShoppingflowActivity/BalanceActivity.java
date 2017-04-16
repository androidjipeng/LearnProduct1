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
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.wangyi.lelegou.maofengqi.bean.AddressBean;
import com.wangyi.lelegou.maofengqi.ui.activity.AddressManagerActivity;
import com.wangyi.lelegou.maofengqi.ui.activity.MainActivity;
import com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity.bean.AddressModel;
import com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity.bean.VirtualshopModel;
import com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity.pay.AliPayResult;
import com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity.pay.Pay;
import com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity.pay.PayConstants;
import com.wangyi.lelegou.maofengqi.utils.GetDeviceInformaton;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

import static com.learn.soft.product1.R.id.leledou_allNum;

public class BalanceActivity extends BaseActivity implements View.OnClickListener {

   public final static int rescode=0;

    private LinearLayout please_address, ll_tip, ll_account_beizhu, ll_alipay,ll_wechatpay, ll_leledou, ll_item2, ll_item1;
    private TextView tip_content;
    private EditText et_account, et_remark;
    private TextView money_num, pay_leledou_num;
    private CheckBox check_leledou, check_alipay,check_wechat;
    private Button submit;
    private TextView address;
    private Context context;
    private TextView name;
    TextView leledou_allNum;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_balance;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        context=this;
        initview();
    }

    private void initview() {
        name= (TextView) findViewById(R.id.name);
        tvContent = (TextView) findViewById(R.id.tv_content);
        tvContent.setText("结算支付");
        address= (TextView) findViewById(R.id.address);
        please_address = (LinearLayout) findViewById(R.id.please_address);
        please_address.setOnClickListener(this);
        ll_tip = (LinearLayout) findViewById(R.id.ll_tip);
        ll_account_beizhu = (LinearLayout) findViewById(R.id.ll_account_beizhu);
        ll_alipay = (LinearLayout) findViewById(R.id.ll_alipay);
        ll_wechatpay = (LinearLayout) findViewById(R.id.ll_wechatpay);
        ll_leledou = (LinearLayout) findViewById(R.id.ll_leledou);
        ll_item2 = (LinearLayout) findViewById(R.id.ll_item2);
        ll_item1 = (LinearLayout) findViewById(R.id.ll_item1);


        tip_content = (TextView) findViewById(R.id.tip_content);
        money_num = (TextView) findViewById(R.id.money_num);
        pay_leledou_num = (TextView) findViewById(R.id.pay_leledou_num);
        et_account = (EditText) findViewById(R.id.et_account);
        et_remark = (EditText) findViewById(R.id.et_remark);
        check_leledou = (CheckBox) findViewById(R.id.check_leledou);
        check_alipay = (CheckBox) findViewById(R.id.check_alipay);
        check_wechat= (CheckBox) findViewById(R.id.check_wechat);
        check_leledou.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    check_alipay.setChecked(false);
                    check_wechat.setChecked(false);
                }
            }
        });

        check_alipay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    check_leledou.setChecked(false);
                    check_wechat.setChecked(false);
                }
            }
        });

        check_wechat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    check_alipay.setChecked(false);
                    check_leledou.setChecked(false);
                }
            }
        });

        leledou_allNum= (TextView) findViewById(R.id.leledou_allNum);
        leledou_allNum.setText("(账户乐豆"+JiaZhengApp.getInstance().getLeledouNum()+"个)");
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(this);
        data();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    /**
     * tag=0 圈子的，tag=1 非圈子的
     */

    private String tag;
    private boolean isvirtual;
    private String circleId;
    private boolean ispaycircle;
    private String shopid;
    private String addressid="";
    private void data() {
        final Intent intent = getIntent();
        if (intent != null) {
            tag = intent.getStringExtra("tag");
            isvirtual = intent.getBooleanExtra("isvirtual", false);
            /**圈子的*/
            if (tag.equals("0")) {
                circleId = intent.getStringExtra("circleId");
                ispaycircle = intent.getBooleanExtra("ispaycircle", false);
                if (isvirtual)//虚拟的
                {
                    if (ispaycircle) {
                        MyLog.e("jp", "圈子==虚拟的=已付费的");
                        ll_tip.setVisibility(View.VISIBLE);
                        ll_account_beizhu.setVisibility(View.VISIBLE);
                    } else {
                        MyLog.e("jp", "圈子==虚拟的=未付费的");
                        ll_tip.setVisibility(View.VISIBLE);
                        ll_account_beizhu.setVisibility(View.VISIBLE);
                        ll_item1.setVisibility(View.VISIBLE);
                        ll_item2.setVisibility(View.VISIBLE);
                        ll_leledou.setVisibility(View.VISIBLE);
                        ll_alipay.setVisibility(View.VISIBLE);
                        ll_wechatpay.setVisibility(View.VISIBLE);
                    }

                    OkHttpUtils
                            .post()
                            .url(Config.testurl + Config.queryVirtualAcconut)
                            .addParams("uid", JiaZhengApp.getInstance().getUserId())
                            .addParams("circleId", circleId)
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int i) {
                                    MyLog.e("jp", "圈子==虚拟的------------->onError:" + e.getMessage());
                                }

                                @Override
                                public void onResponse(String s, int i) {
                                    MyLog.e("jp", "圈子==虚拟的------------->onResponse:" + s);
                                    Gson gson=new Gson();
                                    VirtualshopModel virtualshopModel = gson.fromJson(s, VirtualshopModel.class);
                                    if (virtualshopModel.getResult()==0)
                                    {
                                        List<VirtualshopModel.ObjListBean> datalist = virtualshopModel.getObjList();
                                        et_account.setText(datalist.get(0).getAccountCode()+"");
                                        et_remark.setText(datalist.get(0).getRemarks()+"");

                                    }else
                                    {
                                        ToastHelper.toast(virtualshopModel.getInfo());
                                    }


                                }
                            });

                } else //非虚拟的
                {
                    if (ispaycircle) {//已付费的
                        MyLog.e("jp", "圈子==非虚拟的=已付费");
                        please_address.setVisibility(View.VISIBLE);
                    } else {//未付费的
                        MyLog.e("jp", "圈子==非虚拟的=未付费");
                        please_address.setVisibility(View.VISIBLE);
                        ll_item1.setVisibility(View.VISIBLE);
                        ll_item2.setVisibility(View.VISIBLE);
                        ll_leledou.setVisibility(View.VISIBLE);
                        ll_alipay.setVisibility(View.VISIBLE);
                        ll_wechatpay.setVisibility(View.VISIBLE);
                    }

                    OkHttpUtils
                            .post()
                            .url(Config.testurl + Config.queryMyShopingDeliveryAddr)
                            .addParams("uid", JiaZhengApp.getInstance().getUserId())
                            .addParams("circleId", circleId)
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int i) {
                                    MyLog.e("jp", "圈子==非虚拟的------------->onError:" + e.getMessage());
                                }

                                @Override
                                public void onResponse(String s, int i) {
                                    MyLog.e("jp", "圈子==非虚拟的------------->onResponse:" + s);
                                    Gson gson=new Gson();
                                    AddressModel addressModel = gson.fromJson(s, AddressModel.class);
                                    List<AddressModel.ObjListBean> objList = addressModel.getObjList();
                                    if (addressModel.getResult()==0)
                                    {

                                        if (objList.size()<=0)
                                        {
                                            //// TODO: 2017/3/31 没有地址 的时候
//                                            Intent address=new Intent(context,AddressActivity.class);
//                                            startActivityForResult(address,rescode);
                                        }else
                                        {
                                            address.setText(objList.get(0).getAddr());
                                            addressid=objList.get(0).getId()+"";
                                        }
                                    }else
                                    {
                                        ToastHelper.toast(addressModel.getInfo());
                                    }

                                }
                            });
                }

            }

            /**非圈子*/
            if (tag.equals("1")) {
                shopid = intent.getStringExtra("shopid");

                if (isvirtual)//虚拟的
                {
                    MyLog.e("jp", "非圈子==虚拟的");
                    ll_tip.setVisibility(View.VISIBLE);
                    ll_account_beizhu.setVisibility(View.VISIBLE);

                    OkHttpUtils
                            .post()
                            .url(Config.testurl + Config.queryVirtualAcconut)
                            .addParams("uid", JiaZhengApp.getInstance().getUserId())
                            .addParams("shopid", shopid)
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int i) {
                                    MyLog.e("jp", "非圈子==虚拟的------------->onError:" + e.getMessage());
                                }

                                @Override
                                public void onResponse(String s, int i) {
                                    MyLog.e("jp", "非圈子==虚拟的------------->onResponse:" + s);
                                    Gson gson=new Gson();
                                    VirtualshopModel virtualshopModel = gson.fromJson(s, VirtualshopModel.class);
                                    if (virtualshopModel.getResult()==0)
                                    {
                                        List<VirtualshopModel.ObjListBean> datalist = virtualshopModel.getObjList();
                                        et_account.setText(datalist.get(0).getAccountCode()+"");
                                        et_remark.setText(datalist.get(0).getRemarks()+"");

                                    }else
                                    {
                                        ToastHelper.toast(virtualshopModel.getInfo());
                                    }

                                }
                            });


                } else //非虚拟的
                {
                    MyLog.e("jp", "非圈子==非虚拟的");
                    please_address.setVisibility(View.VISIBLE);

                    OkHttpUtils
                            .post()
                            .url(Config.testurl + Config.queryMyShopingDeliveryAddr)
                            .addParams("uid", JiaZhengApp.getInstance().getUserId())
                            .addParams("shopid", shopid)
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int i) {
                                    MyLog.e("jp", "非圈子==非虚拟的------------->onError:" + e.getMessage());
                                }

                                @Override
                                public void onResponse(String s, int i) {
                                    MyLog.e("jp", "非圈子==非虚拟的------------->onResponse:" + s);
                                    Gson gson=new Gson();
                                    AddressModel addressModel = gson.fromJson(s, AddressModel.class);
                                    List<AddressModel.ObjListBean> objList = addressModel.getObjList();
                                    addressid=objList.get(0).getId()+"";
                                    if (addressModel.getResult()==0)
                                    {

                                        if (objList.size()<=0)
                                        {
                                            //// TODO: 2017/3/31 没有地址 的时候
//                                            Intent address=new Intent(context,AddressActivity.class);
//                                            startActivityForResult(address, rescode);
                                        }else
                                        {
                                            address.setText(objList.get(0).getAddr());
                                        }
                                    }else
                                    {
                                        ToastHelper.toast(addressModel.getInfo());
                                    }
                                }
                            });

                }

            }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:
                /**圈子的*/
                if (tag.equals("0")) {
                    if (isvirtual)//虚拟的
                    {
                        if (ispaycircle) {//已付费的
                            MyLog.e("jp", "圈子==虚拟的=已付费的");
                            showProgressInfo("");
                            OkHttpUtils
                                    .post()
                                    .url(Config.testurl+Config.doMyShopingDeliveryAddr)
                                    .addParams("uid",JiaZhengApp.getInstance().getUserId())
                                    .addParams("circleId",circleId)
                                    .addParams("accountCode",et_account.getText().toString())
                                    .addParams("remarks",et_remark.getText().toString())
                                    .build()
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onError(Call call, Exception e, int i) {
                                            dismissProgress();
                                            MyLog.e("jp","==onclick==非圈子==虚拟的onResponse:"+e.getMessage());
                                        }

                                        @Override
                                        public void onResponse(String s, int i) {
                                            dismissProgress();
                                            MyLog.e("jp","==onclick==非圈子==虚拟的onResponse:"+s);
                                            try {
                                                JSONObject object=new JSONObject(s);
                                                if (object.getString("result").equals("0"))
                                                {
                                                    ToastHelper.toast("提交成功");
                                                    Intent intent=new Intent(context, MainActivity.class);
                                                    startActivity(intent);
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                        } else {//未付费的
                            MyLog.e("jp", "圈子==虚拟的=未付费的");

                            OkHttpUtils
                                    .post()
                                    .url(Config.testurl+Config.doMyShopingDeliveryAddr)
                                    .addParams("uid",JiaZhengApp.getInstance().getUserId())
                                    .addParams("circleId",circleId)
                                    .addParams("accountCode",et_account.getText().toString())
                                    .addParams("remarks",et_remark.getText().toString())
                                    .build()
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onError(Call call, Exception e, int i) {

                                            MyLog.e("jp","==onclick==非圈子==虚拟的onResponse:"+e.getMessage());
                                        }

                                        @Override
                                        public void onResponse(String s, int i) {

                                            try {
                                                JSONObject object=new JSONObject(s);
                                                if (object.getString("result").equals("0"))
                                                {
                                                    /**去支付*/
                                                    payCricleMoney();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });


                        }

                    } else //非虚拟的
                    {
                        if (!"".equals(addressid)) {


                            if (ispaycircle) {//已付费的

                                MyLog.e("jp", "圈子==非虚拟的=已付费");
                                showProgressInfo("");
                                OkHttpUtils
                                        .post()
                                        .url(Config.testurl + Config.doMyShopingDeliveryAddr)
                                        .addParams("uid", JiaZhengApp.getInstance().getUserId())
                                        .addParams("circleId", circleId)
                                        .addParams("addressid", addressid)
                                        .build()
                                        .execute(new StringCallback() {
                                            @Override
                                            public void onError(Call call, Exception e, int i) {
                                                dismissProgress();
                                                MyLog.e("jp", "==onclick==非圈子==虚拟的onResponse:" + e.getMessage());
                                            }

                                            @Override
                                            public void onResponse(String s, int i) {
                                                dismissProgress();
                                                MyLog.e("jp", "==onclick==非圈子==虚拟的onResponse:" + s);
                                                try {
                                                    JSONObject object = new JSONObject(s);
                                                    if (object.getString("result").equals("0")) {
                                                        ToastHelper.toast("提交成功");
                                                        Intent intent = new Intent(context, MainActivity.class);
                                                        startActivity(intent);
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });

                            } else {//未付费的
                                MyLog.e("jp", "圈子==非虚拟的=未付费");



                                OkHttpUtils
                                        .post()
                                        .url(Config.testurl + Config.doMyShopingDeliveryAddr)
                                        .addParams("uid", JiaZhengApp.getInstance().getUserId())
                                        .addParams("circleId", circleId)
                                        .addParams("addressid", addressid)
                                        .build()
                                        .execute(new StringCallback() {
                                            @Override
                                            public void onError(Call call, Exception e, int i) {

                                                MyLog.e("jp", "==onclick==onError:" + e.getMessage());
                                            }

                                            @Override
                                            public void onResponse(String s, int i) {

                                                MyLog.e("jp", "==onclick==onResponse:" + s);
                                                try {
                                                    JSONObject object = new JSONObject(s);
                                                    if (object.getString("result").equals("0")) {
                                                        /**去支付*/
                                                        payCricleMoney();
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });

                            }
                        }else
                        {
                            ToastHelper.toast("请输入选择地址");
                        }

                    }

                }

                /**非圈子*/
                if (tag.equals("1")) {

                    if (isvirtual)//虚拟的
                    {
                        MyLog.e("jp", "=======================================================非圈子==虚拟的");
                        showProgressInfo("");
                        OkHttpUtils
                                .post()
                                .url(Config.testurl+Config.doMyShopingDeliveryAddr)
                                .addParams("uid",JiaZhengApp.getInstance().getUserId())
                                .addParams("shopid",shopid)
                                .addParams("accountCode",et_account.getText().toString())
                                .addParams("remarks",et_remark.getText().toString())
                                .build()
                                .execute(new StringCallback() {
                                    @Override
                                    public void onError(Call call, Exception e, int i) {
                                         dismissProgress();
                                        MyLog.e("jp","==onclick==非圈子==虚拟的onResponse:"+e.getMessage());
                                    }

                                    @Override
                                    public void onResponse(String s, int i) {
                                         dismissProgress();
                                        MyLog.e("jp","==onclick==非圈子==虚拟的onResponse:"+s);
                                        try {
                                            JSONObject object=new JSONObject(s);
                                            if (object.getString("result").equals("0"))
                                            {
                                                ToastHelper.toast("提交成功");
                                                Intent intent=new Intent(context, MainActivity.class);
                                                startActivity(intent);
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });




                    } else //非虚拟的
                    {

                        if (!"".equals(addressid)) {
                            MyLog.e("jp", "===========================================非圈子==非虚拟的");
                            showProgressInfo("");
                            OkHttpUtils
                                    .post()
                                    .url(Config.testurl + Config.doMyShopingDeliveryAddr)
                                    .addParams("uid", JiaZhengApp.getInstance().getUserId())
                                    .addParams("shopid", shopid)
                                    .addParams("addressid", addressid)
                                    .build()
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onError(Call call, Exception e, int i) {
                                            dismissProgress();
                                            MyLog.e("jp", "==onclick==非圈子==虚拟的onResponse:" + e.getMessage());
                                        }

                                        @Override
                                        public void onResponse(String s, int i) {
                                            dismissProgress();
                                            MyLog.e("jp", "==onclick==非圈子==虚拟的onResponse:" + s);
                                            try {
                                                JSONObject object = new JSONObject(s);
                                                if (object.getString("result").equals("0")) {
                                                    ToastHelper.toast("提交成功");
                                                    Intent intent = new Intent(context, MainActivity.class);
                                                    startActivity(intent);
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });
                        }else
                        {
                            ToastHelper.toast("请输入选择地址");
                        }
                    }


                }

                break;
            case R.id.please_address:
                Intent address=new Intent(context,AddressActivity.class);
                startActivityForResult(address, rescode);
                break;
            default:
                break;
        }
    }
    private String orderCode;

    private void payCricleMoney() {

        if (check_leledou.isChecked()||check_alipay.isChecked()||check_wechat.isChecked()) {
            /**leledou*/
            if (check_leledou.isChecked()) {
                showProgressInfo("");
                OkHttpUtils.post()
                        .url(Config.ledoupay)
                        .addParams("type", "4")
                        .addParams("circle_id", circleId)
                        .addParams("uid", JiaZhengApp.getInstance().getUserId())
                        .addParams("money", "2")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int i) {
                                dismissProgress();
                                MyLog.e("jp","leledou========>onError:"+e.getMessage());
                            }

                            @Override
                            public void onResponse(String s, int i) {
                                dismissProgress();
                                MyLog.e("jp","leledou========>onResponse:"+s);
                                try {
                                    JSONObject object = new JSONObject(s);
                                    if (object.getString("result").equals("0")) {
                                        ToastHelper.toast("提交成功");
                                        Intent intent = new Intent(context, MainActivity.class);
                                        startActivity(intent);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

            }
            /**alipay*/
            if (check_alipay.isChecked()) {
                showProgressInfo("");
                MyLog.e("jp","circleId:"+circleId);
                OkHttpUtils
                        .post()
                        .url(Config.testurl + Config.doAliPayCircle)
                        .addParams("uid", JiaZhengApp.getInstance().getUserId())
                        .addParams("circleId", circleId)
                        .addParams("type", "3")
                        .addParams("payType","1")
                        .addParams("deviceInfo", GetDeviceInformaton.deviceInfo())
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int i) {
                                dismissProgress();
                                MyLog.e("jp", "alipay----onError======>onResponse:" + e.getMessage());

                            }

                            @Override
                            public void onResponse(String s, int i) {
                                dismissProgress();
                                MyLog.e("jp", "alipay======>onResponse:" + s);
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

            if (check_wechat.isChecked()) {
                showProgressInfo("");
                MyLog.e("jp","circleId:"+circleId);
                OkHttpUtils
                        .post()
                        .url(Config.testurl + Config.doAliPayCircle)
                        .addParams("uid", JiaZhengApp.getInstance().getUserId())
                        .addParams("circleId", circleId)
                        .addParams("type", "3")
                        .addParams("payType","2")
                        .addParams("deviceInfo", GetDeviceInformaton.deviceInfo())
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int i) {
                                dismissProgress();
                                MyLog.e("jp", "weixin----onError======>onResponse:" + e.getMessage());

                            }

                            @Override
                            public void onResponse(String s, int i) {
                                dismissProgress();
                                MyLog.e("jp", "weixin======>onResponse:" + s);
                                try {
                                    JSONObject json = new JSONObject(s);
                                    String result = json.getString("result");
                                    String info = json.getString("info");

                                    if (result.equals("0")) {

                                        String orderString = json.getString("orderString");
                                        orderCode = json.getString("orderCode");
                                        JiaZhengApp.getInstance().setWxorderCode(orderCode);
                                        HashMap parse = XmlUtils.parse(orderString);
                                        String token_id= (String) parse.get("token_id");

                                        RequestMsg msg=new RequestMsg();
                                        msg.setTokenId(token_id);//token_id为服务端预下单返回
                                        msg.setTradeType(MainApplication.WX_APP_TYPE);
                                        msg.setAppId(PayConstants.APP_ID);//appid为商户自己在微信开放平台的应用
                                        PayPlugin.unifiedAppPay(mActivity,msg);

//                                        Pay.init(context).alipay(orderString, alipayHandler);
                                    } else if (result.equals("1")) {
                                        ToastHelper.toast(info);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            }

        }else
        {
            ToastHelper.toast("请选择一种支付");
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
                                ToastHelper.toast("提交成功");
                                Intent intent=new Intent(context, MainActivity.class);
                                startActivity(intent);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
               if (resultCode==RESULT_OK)
               {
                   String staddress = data.getStringExtra("address");
                   MyLog.e("jp","---->address:"+staddress);
                   address.setText(staddress);
                   String names = data.getStringExtra("name");
                   name.setVisibility(View.VISIBLE);
                   name.setText("收货人:"+names);
                   MyLog.e("jp","---->name:"+names);
                   addressid = data.getStringExtra("addressId");
                   MyLog.e("jp","---->addressId:"+addressid);
               }
    }
}
