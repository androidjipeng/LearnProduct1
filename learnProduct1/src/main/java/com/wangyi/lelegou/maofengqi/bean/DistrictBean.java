package com.wangyi.lelegou.maofengqi.bean;

import java.io.Serializable;

/**
 * **********************************************************
 * <p/>
 * 说明:街道信息实体类
 * <p/>
 * 作者:@Doc.March
 * <p/>
 * 创建日期:2016-9-29
 * <p/>
 * 描述:
 * <p/>
 * **********************************************************
 */
public class DistrictBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -752984743396118244L;

	/**
	 * 县Id
	 */
	private int districtId;

	/**
	 * 县名
	 */
	private String districtName;

	public int getDistrictId() {
		return districtId;
	}

	public void setDistrictId(int districtId) {
		this.districtId = districtId;
	}

	public String getDistrictName() {
		return districtName;
	}

	public void setDistrictName(String districtName) {
		this.districtName = districtName;
	}
}