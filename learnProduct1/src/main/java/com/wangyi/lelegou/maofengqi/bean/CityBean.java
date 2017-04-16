package com.wangyi.lelegou.maofengqi.bean;

import java.io.Serializable;
import java.util.List;

/**
 * **********************************************************
 * <p/>
 * 说明:城市信息实体类
 * <p/>
 * 作者:@Doc.March
 * <p/>
 * 创建日期:2016-9-29
 * <p/>
 * 描述:
 * <p/>
 * **********************************************************
 */
public class CityBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4621063383071634403L;

	/**
	 * 城市id
	 */
	private int cityId;

	/**
	 * 城市名称
	 */
	private String cityName;

	private List<DistrictBean> districtBeans;

	public int getCityId() {
		return cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public List<DistrictBean> getDistrictBeans() {
		return districtBeans;
	}

	public void setDistrictBeans(List<DistrictBean> districtBeans) {
		this.districtBeans = districtBeans;
	}
}