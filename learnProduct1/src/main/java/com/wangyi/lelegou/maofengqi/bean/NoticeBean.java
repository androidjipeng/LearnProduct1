package com.wangyi.lelegou.maofengqi.bean;

import java.io.Serializable;

/**
 * **********************************************************
 * <p/>
 * 说明:提示信息实体类
 * <p/>
 * 作者:@Doc.March
 * <p/>
 * 创建日期:2016-9-29
 * <p/>
 * 描述:TODO(用一句话描述该文件做什么)
 * <p/>
 * **********************************************************
 */
public class NoticeBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6811643718260667000L;

	private String nickName;

	private String time;

	private String productName;

	private String shopid;

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getShopid() {
		return shopid;
	}

	public void setShopid(String shopid) {
		this.shopid = shopid;
	}

	@Override
	public String toString() {
		return "NoticeBean{" +
				"nickName='" + nickName + '\'' +
				", time='" + time + '\'' +
				", productName='" + productName + '\'' +
				", shopid='" + shopid + '\'' +
				'}';
	}
}