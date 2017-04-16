package com.wangyi.lelegou.maofengqi.utils;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.wangyi.lelegou.maofengqi.bean.AddressBean;
import com.wangyi.lelegou.maofengqi.bean.JsonMsgBean;
import com.wangyi.lelegou.maofengqi.bean.SearchProductBean;
import com.wangyi.lelegou.maofengqi.bean.UpdateAppBean;
import com.wangyi.lelegou.maofengqi.bean.UploadImageBean;
import com.wangyi.lelegou.maofengqi.bean.UserBean;

/**
 * **********************************************************
 * <p/>
 * 说明:接口
 * <p/>
 * 作者:@Doc.March
 * <p/>
 * 创建日期:2016-9-29
 * <p/>
 * 描述:
 * <p/>
 * **********************************************************
 */
public class ApiClass {

	// 登录
	public static void login(Map<String, Object> paramsMap,
			ResultCallback<UserBean> callback) {
		String url = Constant.IP + Constant.LOGIN;
		OkHttpUtilsManager.getInstance().postAsyn(url, null, paramsMap, null,
				null, callback);
	}

	// 注册
	public static void register(Map<String, Object> paramsMap,
			ResultCallback<String> callback) {
		String url = Constant.IP + Constant.REGISTER;
		paramsMap.put("deviceInfo",GetDeviceInformaton.deviceInfo());
		OkHttpUtilsManager.getInstance().postAsyn(url, null, paramsMap, null,
				null, callback);
	}

	// 发送验证码
	public static void sendVerificationCode(Map<String, Object> paramsMap,
			ResultCallback<String> callback) {
		String url = Constant.IP + Constant.SEND_VERIFICATION_CODE;
		OkHttpUtilsManager.getInstance().postAsyn(url, null, paramsMap, null,
				null, callback);
	}

	// 忘记密码
	public static void forgetPassword(Map<String, Object> paramsMap,
			ResultCallback<String> callback) {
		String url = Constant.IP + Constant.FORGET_PASSWORD;
		OkHttpUtilsManager.getInstance().postAsyn(url, null, paramsMap, null,
				null, callback);
	}

	// 获取个人资料(接口名称:)
	public static void getPersonalData(Map<String, Object> paramsMap,
			ResultCallback<UserBean> callback) {
		String url = Constant.IP + Constant.GET_PERSONAL_DATA;
		OkHttpUtilsManager.getInstance().postAsyn(url, null, paramsMap, null,
				null, callback);
	}

	// 上传图片
	public static void uploadImage(Map<String, Object> paramsMap, File file,
			String filekey, ResultCallback<UploadImageBean> callback) {
		String url = Constant.IP + Constant.UPLOAD_IMAGE;
		OkHttpUtilsManager.getInstance().postAsyn(url, null, paramsMap,
				new File[] { file }, new String[] { filekey }, callback);
	}

	// 修改头像
	public static void updateHead(Map<String, Object> paramsMap,
			ResultCallback<String> callback) {
		String url = Constant.IP + Constant.UPDATE_HEAD;
		OkHttpUtilsManager.getInstance().postAsyn(url, null, paramsMap, null,
				null, callback);
	}

	// 修改昵称，手机号
	public static void updatePersonalData(Map<String, Object> paramsMap,
			ResultCallback<String> callback) {
		String url = Constant.IP + Constant.UPDATE_PERSONAL_DATA;
		OkHttpUtilsManager.getInstance().postAsyn(url, null, paramsMap, null,
				null, callback);
	}

	// 获取获取所有地址
	public static void getAllAddress(Map<String, Object> paramsMap,
			ResultCallback<List<AddressBean>> callback) {
		String url = Constant.IP + Constant.GET_ALL_ADDRESS;
		OkHttpUtilsManager.getInstance().postAsyn(url, null, paramsMap, null,
				null, callback);
	}

	// 添加,编辑,删除地址
	public static void editAddress(Map<String, Object> paramsMap,
			ResultCallback<String> callback) {
		String url = Constant.IP + Constant.EDIT_ADDRESS;
		OkHttpUtilsManager.getInstance().postAsyn(url, null, paramsMap, null,
				null, callback);
	}

	// 获取省市区
	public static void getProvinceCityDistrict(Map<String, Object> paramsMap,
			ResultCallback<JsonMsgBean> callback) {
		String url = Constant.IP + Constant.GET_PROVINCE_CITY_DISTRICT;
		OkHttpUtilsManager.getInstance().postAsyn(url, null, paramsMap, null,
				null, callback);
	}

	// 搜索商品
	public static void searchProduct(Map<String, Object> paramsMap,
			ResultCallback<SearchProductBean> callback) {
		String url = Constant.IP + Constant.SEARCH_PRODUCT;
		OkHttpUtilsManager.getInstance().postAsyn(url, null, paramsMap, null,
				null, callback);
	}

	// 中奖公告
	public static void getAdvertisementInfo(Map<String, Object> paramsMap,
			ResultCallback<String> callback) {
		String url = Constant.IP + Constant.GET_ADVERTISEMENT_INFO;
		OkHttpUtilsManager.getInstance().postAsyn(url, null, paramsMap, null,
				null, callback);
	}

	// 晒单
	public static void shaidan(Map<String, Object> paramsMap,
			ResultCallback<String> callback) {
		String url = Constant.IP + Constant.SHAI_DAN;
		OkHttpUtilsManager.getInstance().postAsyn(url, null, paramsMap, null,
				null, callback);
	}

	// 确认地址
	public static void confirmAddress(Map<String, Object> paramsMap,
			ResultCallback<String> callback) {
		String url = Constant.IP + Constant.CONFIRM_ADDRESS;
		OkHttpUtilsManager.getInstance().postAsyn(url, null, paramsMap, null,
				null, callback);
	}

	// 更新APP
	public static void updateApp(Map<String, Object> paramsMap,
			ResultCallback<UpdateAppBean> callback) {
		String url = Constant.IP + Constant.UPDATE_APP;
		OkHttpUtilsManager.getInstance().postAsyn(url, null, paramsMap, null,
				null, callback);
	}

	// 激活APP
	public static void activationApp(Map<String, Object> paramsMap,
			ResultCallback<String> callback) {
		String url = Constant.IP + Constant.ACTIVATION_APP;
		OkHttpUtilsManager.getInstance().postAsyn(url, null, paramsMap, null,
				null, callback);
	}

	// 第三方登录
	public static void thirdPartyLogin(Map<String, Object> paramsMap,
			ResultCallback<UserBean> callback) {
		String url = Constant.IP + Constant.THIRD_PARTY_LOGIN;
		OkHttpUtilsManager.getInstance().postAsyn(url, null, paramsMap, null,
				null, callback);
	}

	// 2017.1.10
	// 新建圈子
	public static void createNewCircle(Map<String, Object> paramsMap,
			ResultCallback<String> callback) {
		String url = Constant.CIRCLE_IP + Constant.CREATE_NEW_CIRCLE;
		OkHttpUtilsManager.getInstance().postAsyn(url, null, paramsMap, null,
				null, callback);
	}

	// 加入圈子
	public static void joinNewCircle(Map<String, Object> paramsMap,
			ResultCallback<String> callback) {
		String url = Constant.CIRCLE_IP + Constant.JOIN_NEW_CIRCLE;
		OkHttpUtilsManager.getInstance().postAsyn(url, null, paramsMap, null,
				null, callback);
	}

	// 删除圈子
	public static void deleteNewCircle(Map<String, Object> paramsMap,
			ResultCallback<String> callback) {
		String url = Constant.CIRCLE_IP + Constant.DELETE_NEW_CIRCLE;
		OkHttpUtilsManager.getInstance().postAsyn(url, null, paramsMap, null,
				null, callback);
	}

	// 宝贝列表
	public static void selectList(Map<String, Object> paramsMap,
			ResultCallback<String> callback) {
		String url = Constant.CIRCLE_IP + Constant.SELECT_LIST;
		OkHttpUtilsManager.getInstance().postAsyn(url, null, paramsMap, null,
				null, callback);
	}

	// 结算支付
	public static void payment(Map<String, Object> paramsMap,
			ResultCallback<String> callback) {
		String url = Constant.CIRCLE_IP + Constant.PAYMENT;
		OkHttpUtilsManager.getInstance().postAsyn(url, null, paramsMap, null,
				null, callback);
	}

	// 圈中宝
	public static void circle(Map<String, Object> paramsMap,
			ResultCallback<String> callback) {
		String url = Constant.CIRCLE_IP + Constant.CIRCLE;
		OkHttpUtilsManager.getInstance().postAsyn(url, null, paramsMap, null,
				null, callback);
	}

	// 获取未读的通知
	public static void getUnreadNotification(Map<String, Object> paramsMap,
			ResultCallback<String> callback) {
		String url = Constant.CIRCLE_IP + Constant.GETUNREADNOTIFICATION;
		OkHttpUtilsManager.getInstance().postAsyn(url, null, paramsMap, null,
				null, callback);
	}

	// 通知标记已读
	public static void markRead(Map<String, Object> paramsMap,
			ResultCallback<String> callback) {
//		String url = Constant.CIRCLE_IP + Constant.MARKREAD;
//		OkHttpUtilsManager.getInstance().postAsyn(url, null, paramsMap, null,
//				null, callback);
	}

	// 获取用户信息
	public static void getUserInfo(Map<String, Object> paramsMap,
			ResultCallback<String> callback) {
		String url = Constant.NEW_CREATE_IP + Constant.GETUSERINFO;
//		OkHttpUtilsManager.getInstance().postAsyn(url, paramsMap, callback);
		OkHttpUtilsManager.getInstance().postAsyn(url, null, paramsMap, null,
				null, callback);
	}

	// 我建的圈子(所有)或者可参与的圈子(所有)
	public static void allCircle(Map<String, Object> paramsMap,
			ResultCallback<String> callback) {
		String url = Constant.CIRCLE_IP + Constant.ALL_CIRCLE;
		OkHttpUtilsManager.getInstance().postAsyn(url, null, paramsMap, null,
				null, callback);
	}

	// 我的圈友
	public static void myCircleOfFriends(Map<String, Object> paramsMap,
			ResultCallback<String> callback) {
		String url = Constant.CIRCLE_IP + Constant.MY_CIRCLE_OF_FRIENDS;
		OkHttpUtilsManager.getInstance().postAsyn(url, null, paramsMap, null,
				null, callback);
	}

	// 分享
	public static void share(Map<String, Object> paramsMap,
			ResultCallback<String> callback) {
		String url = Constant.CIRCLE_IP + Constant.SHARE;
		OkHttpUtilsManager.getInstance().postAsyn(url, null, paramsMap, null,
				null, callback);
	}

	public static void shareNew(Map<String, Object> paramsMap,
			ResultCallback<String> callback) {
		String url = Constant.CIRCLE_IP + Constant.SHARE_NEW;
		OkHttpUtilsManager.getInstance().postAsyn(url, null, paramsMap, null,
				null, callback);
	}

	// 新手礼包
	public static void novicePacks(Map<String, Object> paramsMap,
			ResultCallback<String> callback) {
		String url = Constant.CIRCLE_IP + Constant.NOVICE_PACKS;
		OkHttpUtilsManager.getInstance().postAsyn(url, null, paramsMap, null,
				null, callback);
	}

	// 2017.1.10修改之前的
	// 首页接口
	public static void getIndex(Map<String, Object> paramsMap,
			OldResultCallback callback) {
		String url = Constant.MOBILE_IP + Constant.INDEX;
		OkHttpUtilsManager.getInstance().postAsyn(url, null, paramsMap, null,
				null, callback);
	}

	// 商品分类
	public static void getProductCategory(Map<String, Object> paramsMap,
			OldResultCallback callback) {
		String url = Constant.MOBILE_IP + Constant.GET_PRODUCT_CATEGORY;
		OkHttpUtilsManager.getInstance().postAsyn(url, null, paramsMap, null,
				null, callback);
	}
}