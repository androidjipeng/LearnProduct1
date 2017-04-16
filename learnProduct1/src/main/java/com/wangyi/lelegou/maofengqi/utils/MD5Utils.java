package com.wangyi.lelegou.maofengqi.utils;

import java.security.MessageDigest;

/**
 * **********************************************************
 * <p/>
 * 说明:android MD5加密帮助类
 * <p/>
 * 作者:@Doc.March
 * <p/>
 * 创建日期:2016-9-29
 * <p/>
 * 描述:TODO(用一句话描述该文件做什么)
 * <p/>
 * **********************************************************
 */
public final class MD5Utils {

	public static String md5(String txt) {
		if (txt == null || txt.length() == 0) {
			return null;
		}

		try {
			MessageDigest e = MessageDigest.getInstance("MD5");
			e.update(txt.getBytes("UTF-8"));

			StringBuffer buf = new StringBuffer();
			byte[] var3 = e.digest();
			int var4 = var3.length;

			for (int i = 0; i < var4; ++i) {
				byte b = var3[i];
				buf.append(String.format("%02x", new Object[] { Integer.valueOf(b & 255) }));
			}

			return buf.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}