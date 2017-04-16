package com.wangyi.lelegou.maofengqi.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * **********************************************************
 * <p/>
 * 说明:输入信息验证
 * <p/>
 * 作者:@Doc.March
 * <p/>
 * 创建日期:2016-9-29
 * <p/>
 * 描述:
 * <p/>
 * **********************************************************
 */
public class VerificationUtils {

	public static boolean verificationPhoneNumber(Context context, String phoneNumber) {
		if (phoneNumber != null && !phoneNumber.equals("") && isMobileNO(phoneNumber)) {
			return true;
		} else {
			Toast.makeText(context, "请输入有效的手机号码", Toast.LENGTH_SHORT).show();
			return false;
		}
	}

	private static boolean verificationCode(Context context, String code) {
		if (code != null && !code.equals("")) {
			return true;
		} else {
			Toast.makeText(context, "请输入正确的验证码", Toast.LENGTH_SHORT).show();
			return false;
		}
	}

	private static boolean verificationPassword(Context context, String password) {
		if (password != null && !password.equals("")) {
			return true;
		} else {
			Toast.makeText(context, "请输入有效的密码", Toast.LENGTH_SHORT).show();
			return false;
		}
	}

	// 登录验证信息
	public static boolean loginVerification(Context context, String phoneNumber, String password) {
		if (verificationPhoneNumber(context, phoneNumber)
				&& verificationPassword(context, password)) {
			return true;
		}
		return false;
	}

	// 注册验证信息
	public static boolean registerVerification(Context context, String phoneNumber, String code,
			String password) {
		if (verificationPhoneNumber(context, phoneNumber) && verificationCode(context, code)
				&& verificationPassword(context, password)) {
			return true;
		}
		return false;
	}

	// 修改昵称验证信息
	public static boolean updateNickNameVerification(Context context, String nickName) {
		if (nickName != null && !nickName.equals("")) {
			return true;
		} else {
			Toast.makeText(context, "请输入昵称", Toast.LENGTH_SHORT).show();
			return false;
		}
	}

	// 修改电话号码验证信息
	public static boolean updatePhoneVerification(Context context, String phoneNumber, String code) {
		if (verificationPhoneNumber(context, phoneNumber) && verificationCode(context, code)) {
			return true;
		}
		return false;
	}

	private static boolean verificationConsignee(Context context, String consignee) {
		if (consignee != null && !consignee.equals("")) {
			return true;
		} else {
			Toast.makeText(context, "请输入收货人姓名", Toast.LENGTH_SHORT).show();
			return false;
		}
	}

	private static boolean verificationArea(Context context, int provincedId, int cityId,
			int districtId) {
		if (provincedId != -1 && cityId != -1 && districtId != -1) {
			return true;
		} else {
			Toast.makeText(context, "请选择地区", Toast.LENGTH_SHORT).show();
			return false;
		}
	}

	private static boolean verificationAddress(Context context, String address) {
		if (address != null && !address.equals("")) {
			return true;
		} else {
			Toast.makeText(context, "请输入详细地址", Toast.LENGTH_SHORT).show();
			return false;
		}
	}

	// 编辑地址验证
	public static boolean editAddressVerification(Context context, String consignee,
			String phoneNumber, int provincedId, int cityId, int districtId, String address) {
		if (verificationConsignee(context, consignee)
				&& verificationPhoneNumber(context, phoneNumber)
				&& verificationArea(context, provincedId, cityId, districtId)
				&& verificationAddress(context, address)) {
			return true;
		}
		return false;
	}

	// 验证手机格式
	public static boolean isMobileNO(String mobiles) {
		/*
		 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、(1349卫通)
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
		// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
		// String telRegex = "[1][358]\\d{9}"
		String telRegex = "[1]\\d{10}";
		if (TextUtils.isEmpty(mobiles)) {
			return false;
		} else {
			return mobiles.matches(telRegex);
		}
	}
}