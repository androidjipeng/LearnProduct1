package com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity;

/**
 * Created by jipeng on 2017/3/23.
 */

public class Config {
    /**测试地址*/
    public static final String testurl="http://60.205.148.81:8082/yyg_app/";
    /**正式的地址*/
//    public static final String testurl="http://app.91lelegou.com/";


    /**商品详情*/
    public static final String queryShopDetail="queryShopDetail";

    /**加入购物车*/
    public static final String addShopingCart="addShopingCart";
    /**查询购物车*/
    public static final String queryShopingCart="doQueryShopingCart";//queryShopingCart
    /**删除购物车商品*/
    public static final String delShopingCart="delShopingCart";
    /**购物车立即乐乐购*/
    public static final String doShoppingCartClearing="doShoppingCartClearing";
    /**立即购买---二人专区，三人专区的结算*/
    public static final String doClearing="doClearing";
    /**去充值*/
    public static final String doAliPay="doAliPay";
    /**支付-购物车商品*/
    public static final String doAliPayShoppingCart="doAliPayShoppingCart";
    /**支付-圈子商品和折扣商品*/
    public static final String doAliPayCircle="doAliPayCircle";

    //==========================================================
    public static final String ledoupay="http://101.201.115.226:8011/index.php/v1/pay/balance_pay";//乐豆

    public static final String ordercode="http://101.201.115.226:8011/index.php/v1/pay/payment";//ordercode

    //==========================================================

    /**我的-获得商品*/
    public static final String queryMyShoplist="queryMyShoplist";

    /**提交获得商品收货信息*/
    public static final String doMyShopingDeliveryAddr="doMyShopingDeliveryAddr";

    /**查询获得非虚拟商品收货信息*/
    public static final String queryMyShopingDeliveryAddr="queryMyShopingDeliveryAddr";

    /**查询获得虚拟商品收货信息*/
    public static final String queryVirtualAcconut="queryVirtualAcconut";
}
