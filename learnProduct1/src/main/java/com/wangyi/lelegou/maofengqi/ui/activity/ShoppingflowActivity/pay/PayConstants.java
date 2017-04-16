package com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity.pay;

/**
 * 支付功能相关配置
 * Created by Cheny on 2017/2/16.
 */

public class PayConstants {
    /**
     * 微信支付模块
     */
    // APP_ID 替换为你的应用从官方网站申请到的合法appId
    public static final String APP_ID = "wxfaf120a5bc0a0c9d";

    //支付类型，微信支付方式
    public static final String TYPE_WXPAY = "wxpay";


    /*微信支付结果返回码，错误码*/
    //支付成功
    public static final int CODE_SUCCESS = 0;
    //支付失败，发生错误 可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
    public static final int CODE_FAILED = -1;
    //用户取消 无需处理。发生场景：用户不支付了，点击取消，返回APP。
    public static final int CODE_CANCEL = -2;


    /**
     * 阿里（支付宝）支付模块
     */
    //阿里支付标识
    public static final int ALI_PAY_FLAG = 1;
    //支付类型，支付宝支付方式
    public static final String TYPE_ALIPAY = "alipay";

    /*支付宝支付结果返回码*/
    //订单支付成功
    public static final String STATUS_SUCCESS = "9000";
    //正在处理中，代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
    public static final String STATUS_CONFIRMING = "8000";
    // 订单支付失败
    public static final String STATUS_FAILED = "4000";
    // 用户中途取消
    public static final String STATUS_CANCEL = "6001";
    // 网络连接出错
    public static final String STATUS_NET_ERROR = "6002";
    // 支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
    public static final String STATUS_RESULT_UNKNOWN = "6004";
}
