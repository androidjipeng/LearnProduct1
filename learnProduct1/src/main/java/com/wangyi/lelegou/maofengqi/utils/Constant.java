package com.wangyi.lelegou.maofengqi.utils;

import android.os.Environment;

/**
 * **********************************************************
 * <p/>
 * 说明:静态常量类
 * <p/>
 * 作者:@Doc.March
 * <p/>
 * 创建日期:2016-9-29
 * <p/>
 * 描述:TODO(用一句话描述该文件做什么)
 * <p/>
 * **********************************************************
 */
public class Constant {

	// 内定版本
	// public static final int VERSION_CODE = 8;
	// 2016.12.2 测试
	// public static final int VERSION_CODE = 11;
	// 2016.12.28 正式
	public static final int VERSION_CODE = 14;

	// 渠道
	// 1,测试
	// 2,北京渠道
	// 3,上海渠道
	// 4,广州渠道
	// 5,深圳渠道
	// 6,二维码
	public static final int CHANNEL = 1;

	// http://91lelegou.com
    /**正式的地址*/
	public static final String IP = "http://91lelegou.com/?v1/member/";
	public static final String MOBILE_IP = "http://91lelegou.com/?/mobile";
	public static final String CIRCLE_IP = "http://91lelegou.com/?/mobile/circle/";
	public static final String NEW_CREATE_IP = "http://91lelegou.com/?v1/member/";


//	===============================================================================测试
	/**测试地址*/
//    public static final String IP = "http://101.201.115.226:8011/index.php/?/v1/member/";
//    public static final String MOBILE_IP = "http://101.201.115.226:8011/index.php/?/mobile";
//	public static final String CIRCLE_IP = "http://101.201.115.226:8011/index.php/?/mobile/circle/";
//	public static final String NEW_CREATE_IP = "http://101.201.115.226:8011/index.php/?/v1/member/";







//	public static final String CIRCLE_IP = "http://91lelegou.com/?v1/member/";

	public static final String Api_Http_Sub = "http://91lelegou.com";

	// 登录
	public static final String LOGIN = "login";
	// 注册
	public static final String REGISTER = "register";
	// 发送验证码
	public static final String SEND_VERIFICATION_CODE = "sendVerificationCode";
	// 忘记密码
	public static final String FORGET_PASSWORD = "forgetPassword";
	// 获取个人资料
	public static final String GET_PERSONAL_DATA = "getPersonalData";
	// 上传图片
	public static final String UPLOAD_IMAGE = "uploadImage";
	// 修改头像
	public static final String UPDATE_HEAD = "updateHead";
	// 修改昵称，手机号
	public static final String UPDATE_PERSONAL_DATA = "updatePersonalData";
	// 获取获取所有地址
	public static final String GET_ALL_ADDRESS = "getAllAddress";
	// 添加,编辑,删除地址
	public static final String EDIT_ADDRESS = "editAddress";
	// 获取省市区
	public static final String GET_PROVINCE_CITY_DISTRICT = "getProvinceCityDistrict";
	// 搜索商品
	public static final String SEARCH_PRODUCT = "searchProduct";
	// 中奖公告
	public static final String GET_ADVERTISEMENT_INFO = "getAdvertisementInfo";
	// 晒单
	public static final String SHAI_DAN = "shaidan";
	// 确认地址
	public static final String CONFIRM_ADDRESS = "confirm_address";
	// 更新APP
	public static final String UPDATE_APP = "updateAPP";
	// 激活APP
	public static final String ACTIVATION_APP = "activationApp";
	// 第三方登录
	public static final String THIRD_PARTY_LOGIN = "thirdPartyLogin";

	// 新建圈子
	public static final String CREATE_NEW_CIRCLE = "createNewCircle";
	// 加入圈子
	public static final String JOIN_NEW_CIRCLE = "mayCircle";
	// 删除圈子
	public static final String DELETE_NEW_CIRCLE = "delCircle";
	// 宝贝列表
	public static final String SELECT_LIST = "selectList";
	// 结算支付
	public static final String PAYMENT = "payment";
	// 圈中宝
	public static final String CIRCLE = "circle";
	// 获取未读的通知
	public static final String GETUNREADNOTIFICATION = "getUnreadNotification";
	// 通知标记已读
	public static final String MARKREAD = "markRead";
	// 获取用户信息
	public static final String GETUSERINFO = "getUserInfo";
	// 我建的圈子(所有)或者可参与的圈子(所有)
	public static final String ALL_CIRCLE = "allCircle";
	// 我的圈友
	public static final String MY_CIRCLE_OF_FRIENDS = "myCircleOfFriends";
	// 分享
	public static final String SHARE = "share";
	public static final String SHARE_NEW = "shareCircle";
	// 新手礼包
	public static final String NOVICE_PACKS = "novicePacks";

	// 首页接口
	public static final String INDEX = "/mobile/api";
	// 商品分类
	public static final String GET_PRODUCT_CATEGORY = "/mobile/categoryapi";

	// Web加载购物车
	public static final String MOBILE_CART_LIST = "/cart/cartlist";
	// Web加载登录界面
	public static final String MOBILE_USER_LOGIN = "/user/login";
	// Web加载我的页面
	public static final String MOBILE_HOME = "/home/";

	// 首页接口
	public static final String Api_Index_First = "/mobile/api";
	// 商品分类
	public static final String Api_Index_Product_Category = "/mobile/categoryapi";
	// 商品列表
	public static final String Api_Index_Product_List = "/mobile/listapi/%s/%s/%s";
	// 最新揭晓列表
	public static final String Api_NewsInfo_Product_List = "/mobile/xsapi/";
	// 加入购物车
	public static final String Api_Add_Cart_Shop = "/ajax/addShopCart/%s/1";
	// 最新晒单
	public static final String Api_ShaiDan_List_New = "/shaidan/sdapi/new/";
	// 人气晒单
	public static final String Api_ShaiDan_List_People = "/shaidan/sdapi/renqi/";
	// 评论最多
	public static final String Api_ShaiDan_List_Comment = "/shaidan/sdapi/pinglun/";

	// Web加载首页
	public static final String Web_Index = "/mobile";

	// Web加载首页的最新揭晓详情
	public static final String Web_Index_Product_NewsInfo = "/mobile/dataserver/%s";
	// Web加载首页的热门推荐详情,所有商品列表
	public static final String Web_Index_Product_Advice = "/mobile/item/%s";
	// Web加载限时揭晓
	public static final String Web_Index_Time_Limit = "/autolottery";
	// Web加载登录界面
	public static final String Web_Index_User_Login = "/user/login";
	// Web加载购物车
	public static final String Web_Index_Cart_List = "/cart/cartlist";
	// Web加载购物车数量
	public static final String Web_Index_Cart_Number = "/cart/api";
	// Web加载晒单详情
	public static final String Web_Show_History_Detail = "/shaidan/detail/%s";
	// Web用户详情
	public static final String Web_Show_UserInfo_Detail = "/mobile/userindex/%s";
	// Web展示Ad详情
	public static final String Web_Show_Ad_Detail = "/mobile/item/%s";
	// Web展示账号明细
	public static final String Web_Show_User_Balance = "/home/userbalance";

	/**
	 * 路径
	 */
	public static final String DIRECTORY = Environment
			.getExternalStorageDirectory().toString();

	public static final String HEAD_IMAGE_DIR = "/LeLeGou/head";

	public static final String PIC_CACHE_DIR = "/LeLeGou/PicCache";

	/**
	 * requestCode
	 */
	public static final int REQUESTCODE_ADDRESS_MANAGER = 1000;

	public static final int REQUESTCODE_CHOOSE_PICTURE = 1001;

	public static final int REQUESTCODE_TAKE_PHONO = 1002;

	public static final int REQUESTCODE_CROP_SMALL_PICTURE = 1003;

	public static final int REQUESTCODE_UPDATE_NICK_NAME = 1004;

	public static final int REQUESTCODE_UPDATE_PHONE_NUMBER = 1005;

	/**
	 * IntentFilter
	 */
	public static final String ACTION_RELOAD = "android.action.march.reload";

	public static final String ACTION_LOGN = "android.action.march.login";

	public static final String ACTION_CREATE_NEW_CIRCLE = "android.action.march.create.new.circle";

	// 支付回调页面
	// public static int PAY_SHOW = 0;

	// 支付成功回调地址
	public static String PAYMENT_CALLBACK_SUCCESS_URL = "";

	// 支付失败回调地址
	public static String PAYMENT_CALLBACK_FAIL_URL = "";
}