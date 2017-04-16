package com.learn.soft.product1.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.learn.soft.product.jni.JiaZhengApp;
import com.learn.soft.product.util.ActivityFrgManager;
import com.learn.soft.product.util.MyLog;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity.Config;
import com.wangyi.lelegou.maofengqi.utils.Constant2;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler
{

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
    private IWXAPI api;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        api = WXAPIFactory.createWXAPI(this, Constant2.wxappid);//appid需换成商户自己开放平台appid
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    public void onReq(BaseReq req)
    {
        MyLog.e("jp","WXPayEntryActivity=================================onReq:>"+req.toString());
    }

    @Override
    public void onResp(BaseResp resp)
    {
        MyLog.e("jp","WXPayEntryActivity=================================onResp:>"+resp.toString());
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX)
        {
            // resp.errCode == -1 原因：支付错误,可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等
            // resp.errCode == -2 原因 用户取消,无需处理。发生场景：用户不支付了，点击取消，返回APP
            if (resp.errCode == 0) // 支付成功
            {
                Toast.makeText(this, "支付成功", Toast.LENGTH_SHORT).show();
                setordercodephp();


            }
            if (resp.errCode == -1)
            {
                Toast.makeText(this, "支付错误", Toast.LENGTH_SHORT).show();
                finish();
            }
            if (resp.errCode == -2)
            {
                Toast.makeText(this, "用户支付取消", Toast.LENGTH_SHORT).show();
                finish();
            }

//            else
//            {
//                Toast.makeText(this, resp.errCode + "test", Toast.LENGTH_SHORT)
//                        .show();
//
//                finish();
//            }
        }
    }

    private void setordercodephp() {

        OkHttpUtils
                .post()
                .url(Config.ordercode)
                .addParams("out_trade_no", JiaZhengApp.getInstance().getWxorderCode())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {

                        MyLog.e("jp", "====微信支付完成之后===onError:" + e.getMessage());
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        MyLog.e("jp", "====微信支付完成之后===onResponse:" + s);
                        try {
                            JSONObject object=new JSONObject(s);
                            String result = object.getString("result");
                            MyLog.e("jp","乐乐豆结算返回值==============>result:"+result);
                            if (result.equals("0"))
                            {
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }
}