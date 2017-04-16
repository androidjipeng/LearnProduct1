package com.wangyi.lelegou.maofengqi.bean;

import java.io.Serializable;

/**
 * **********************************************************
 * <p/>
 * 说明:收货地址实体类
 * <p/>
 * 作者:@Doc.March
 * <p/>
 * 创建日期:2016-9-29
 * <p/>
 * 描述:
 * <p/>
 * **********************************************************
 */
public class AddressBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6988498291432598152L;

	// 地址ID
	private int addressId;

	// 收货人
	private String consignee;

	// 手机号码
	private String phoneNumber;

	// 省实体
	private ProvinceBean provinceBean;

	// 城市实体
	private CityBean cityBean;

	// 县实体
	private DistrictBean districtBean;

	// 详细地址
	private String address;

	// 是否是默认地址(0：不是 1：是)用户默认地址只有一个
	private int isDefault;

	public int getAddressId() {
		return addressId;
	}

	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public ProvinceBean getProvinceBean() {
		return provinceBean;
	}

	public void setProvinceBean(ProvinceBean provinceBean) {
		this.provinceBean = provinceBean;
	}

	public CityBean getCityBean() {
		return cityBean;
	}

	public void setCityBean(CityBean cityBean) {
		this.cityBean = cityBean;
	}

	public DistrictBean getDistrictBean() {
		return districtBean;
	}

	public void setDistrictBean(DistrictBean districtBean) {
		this.districtBean = districtBean;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(int isDefault) {
		this.isDefault = isDefault;
	}
}