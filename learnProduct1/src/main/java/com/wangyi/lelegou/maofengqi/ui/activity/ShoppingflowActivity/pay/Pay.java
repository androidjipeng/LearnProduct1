package com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity.pay;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;

import java.util.Map;

/**
 * 支付服务
 * Created by Cheny on 2017/2/13.
 */

public class Pay {
    /**
     * 初始化pay对象
     */
    public static Pay init(Context context) {
        return new Pay(context);
    }

    private Pay(Context context) {
        mContext = context;
    }

    private Context mContext;

//    /**
//     * 微信支付
//     *
//     * @param entity
//     */
//    public void wxPay(WXPayEntity entity) {
//        IWXAPI api = WXAPIFactory.createWXAPI(mContext, PayConstants.APP_ID);
//        api.registerApp(PayConstants.APP_ID);
//        PayReq req = new PayReq();
//        req.appId = entity.getAppid();
//        req.partnerId = entity.getPartnerid();
//        req.prepayId = entity.getPrepayid();
//        req.packageValue = entity.getPackageValue();
//        req.nonceStr = entity.getNoncestr();
//        req.timeStamp = entity.getTimestamp();
//        req.sign = entity.getSign();
//        boolean result = api.sendReq(req);
//        if (!result) {
//            Toast.makeText(mContext, "打开微信支付失败!", Toast.LENGTH_SHORT).show();
//        }
//    }

    /**
     * 支付宝支付
     */
    public void aliPay(final AliPayEntity entity) {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask((Activity) mContext);
                // 调用支付接口，获取支付结果
                Map<String, String> result = alipay.payV2(entity.getUrl(), true);
                Log.i("msp", result.toString());
                Message msg = new Message();
                msg.what = PayConstants.ALI_PAY_FLAG;
                msg.obj = result;
                alipayHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    public void alipay(final String info)
    {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask((Activity) mContext);
                // 调用支付接口，获取支付结果
                Map<String, String> result = alipay.payV2(info, true);
                Log.i("msp", result.toString());
                Message msg = new Message();
                msg.what = PayConstants.ALI_PAY_FLAG;
                msg.obj = result;
                alipayHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

     /**乐乐购特殊处理*/
    public void alipay(final String info, final Handler handlers)
    {
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask((Activity) mContext);
                // 调用支付接口，获取支付结果
                Map<String, String> result = alipay.payV2(info, true);
                Log.i("msp", result.toString());
                Message msg = new Message();
                msg.what = PayConstants.ALI_PAY_FLAG;
                msg.obj = result;
                handlers.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
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
                            Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
                            break;
                        case PayConstants.STATUS_CONFIRMING:
                            Toast.makeText(mContext, "支付结果确认中", Toast.LENGTH_SHORT).show();
                            break;
                        case PayConstants.STATUS_FAILED:
                            Toast.makeText(mContext, "支付失败" + resultInfo, Toast.LENGTH_SHORT).show();
                            break;
                        case PayConstants.STATUS_CANCEL:
                            Toast.makeText(mContext, "取消付款", Toast.LENGTH_SHORT).show();
                            break;
                        case PayConstants.STATUS_NET_ERROR:
                            Toast.makeText(mContext, "网络连接错误", Toast.LENGTH_SHORT).show();
                            break;
                        case PayConstants.STATUS_RESULT_UNKNOWN:
                            Toast.makeText(mContext, "支付结果未知,请确认是否支付成功", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            Toast.makeText(mContext, "未知错误,请确认是否支付成功", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }
        }
    };
}
