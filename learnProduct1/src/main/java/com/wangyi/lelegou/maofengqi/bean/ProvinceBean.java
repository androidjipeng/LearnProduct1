package com.wangyi.lelegou.maofengqi.bean;

import java.io.Serializable;
import java.util.List;

/**
 * **********************************************************
 * <p/>
 * 说明:省级信息实体类
 * <p/>
 * 作者:@Doc.March
 * <p/>
 * 创建日期:2016-9-29
 * <p/>
 * 描述:
 * <p/>
 * **********************************************************
 */
public class ProvinceBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4729778292291387833L;

	/**
	 * 省Id
	 */
	private int provinceId;

	/**
	 * 省名
	 */
	private String provinceName;

	private List<CityBean> cityBeans;

	public int getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(int provinceId) {
		this.provinceId = provinceId;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public List<CityBean> getCityBeans() {
		return cityBeans;
	}

	public void setCityBeans(List<CityBean> cityBeans) {
		this.cityBeans = cityBeans;
	}
}