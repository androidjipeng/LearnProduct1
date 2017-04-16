package com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity.pay;

/**
 * 微信支付实体
 * Created by Cheny on 2017/2/13.
 */

public class WXPayEntity {
    /**
     * 参数：sign（签名）、appid、noncestr、packageValue、partnerid、prepayid(预支付id)、timestamp
     */
        /*应用APPID*/
    private String appid;
    /*签名*/
    private String sign;
    /*随机字符串*/
    private String noncestr;
    /*拓展字段商户号*/
    private String packageValue;
    /*商户号*/
    private String partnerid;
    /*预支付交易会话id*/
    private String prepayid;
    /*时间戳*/
    private String timestamp;
    /*支付单号，唯一标识*/
    private String paymentNo;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getPackageValue() {
        return packageValue;
    }

    public void setPackageValue(String packageValue) {
        this.packageValue = packageValue;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getPaymentNo() {
        return paymentNo;
    }

    public void setPaymentNo(String paymentNo) {
        this.paymentNo = paymentNo;
    }
}
