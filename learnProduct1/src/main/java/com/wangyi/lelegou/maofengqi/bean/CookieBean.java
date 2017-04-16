package com.wangyi.lelegou.maofengqi.bean;

import java.io.Serializable;

/**
 * **********************************************************
 * <p/>
 * 说明:Cookie实体类
 * <p/>
 * 作者:@Doc.March
 * <p/>
 * 创建日期:2016-9-29
 * <p/>
 * 描述:
 * <p/>
 * **********************************************************
 */
public class CookieBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4535659066652416943L;

	private String uid;

	private String ushell;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUshell() {
		return ushell;
	}

	public void setUshell(String ushell) {
		this.ushell = ushell;
	}
}