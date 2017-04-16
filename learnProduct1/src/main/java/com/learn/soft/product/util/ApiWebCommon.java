package com.learn.soft.product.util;

/**
 * **********************************************************
 * <p/>
 * 说明：
 * <p/>
 * 作者：cailin
 * <p/>
 * 创建日期：2015/12/31
 * <p/>
 * 描述：
 * **********************************************************
 */
public class ApiWebCommon {

    public static interface API_COMMON {

//        public static final String Api_Img_File = "http://test.youliao.com.cn:85/statics/uploads/";
//        public static final String Api_Common_Url = "http://test.youliao.com.cn:85/?/mobile";

//        public static final String Api_Http_Sub = "120.26.64.47:81";
//        public static final String Api_Img_File = "http://120.26.64.47:81/statics/uploads/";
//        public static final String Api_Common_Url = "http://120.26.64.47:81/?/mobile";

    	public static final String Api_Http_Sub = "http://91lelegou.com";
        //public static final String Api_Img_File = "http://91lelegou.com/statics/uploads/";
        public static final String Api_Common_Url = "http://91lelegou.com/?/mobile";

        //首页接口
        public static final String Api_Index_First = "/mobile/api";
        //商品分类
        public static final String Api_Index_Product_Category = "/mobile/categoryapi";
        //商品列表
        public static final String Api_Index_Product_List = "/mobile/listapi/%s/%s/%s";
        //最新揭晓列表
        public static final String Api_NewsInfo_Product_List = "/mobile/xsapi/";
        //加入购物车
        public static final String Api_Add_Cart_Shop = "/ajax/addShopCart/%s/1";

        /*支付*/
        public static final String Api_circle_pay="/circle/paymentGoodsCircle/";

        //最新晒单
        public static final String Api_ShaiDan_List_New = "/shaidan/sdapi/new/";
        //人气晒单
        public static final String Api_ShaiDan_List_People = "/shaidan/sdapi/renqi/";
        //评论最多
        public static final String Api_ShaiDan_List_Comment = "/shaidan/sdapi/pinglun/";


        //Web加载首页
        public static final String Web_Index = "/mobile";

        //Web加载首页的最新揭晓详情
        public static final String Web_Index_Product_NewsInfo = "/mobile/dataserver/%s";
        //Web加载首页的热门推荐详情,所有商品列表
        public static final String Web_Index_Product_Advice = "/mobile/item/%s";
        //Web加载限时揭晓
        public static final String Web_Index_Time_Limit = "/autolottery";
        //Web加载登录界面
        public static final String Web_Index_User_Login = "/user/login";
        //Web加载购物车
        public static final String Web_Index_Cart_List = "/cart/cartlist";
        //Web加载购物车数量
        public static final String Web_Index_Cart_Number = "/cart/api";
        //Web加载晒单详情
        public static final String Web_Show_History_Detail = "/shaidan/detail/%s";
        //Web用户详情
        public static final String Web_Show_UserInfo_Detail = "/mobile/userindex/%s";
        //Web展示Ad详情
        public static final String Web_Show_Ad_Detail = "/mobile/item/%s";
        //Web展示账号明细
        public static final String Web_Show_User_Balance = "/home/userbalance";



    }



}
