package com.wangyi.lelegou.maofengqi.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CircleBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4622385699437193550L;

	private int uid;

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	private int number;

	@SerializedName("is_delete")
	public int deleteFlag;

	private int productId;

	private boolean isVisibility;

	private String pictureUrl;

	private String price;

	private int shengyurenshu;

	private int canyurenshu;

	private String circle_id;

	public String circle_code;

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public boolean isVisibility() {
		return isVisibility;
	}

	public void setVisibility(boolean isVisibility) {
		this.isVisibility = isVisibility;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public int getShengyurenshu() {
		return shengyurenshu;
	}

	public void setShengyurenshu(int shengyurenshu) {
		this.shengyurenshu = shengyurenshu;
	}

	public int getCanyurenshu() {
		return canyurenshu;
	}

	public void setCanyurenshu(int canyurenshu) {
		this.canyurenshu = canyurenshu;
	}

	public String getCircle_id() {
		return circle_id;
	}

	public void setCircle_id(String circle_id) {
		this.circle_id = circle_id;
	}

	public String getCircle_code() {
		return circle_code;
	}

	public void setCircle_code(String circle_code) {
		this.circle_code = circle_code;
	}

	public int getDeleteFlag() {
		return deleteFlag;
	}

	public void setDeleteFlag(int deleteFlag) {
		this.deleteFlag = deleteFlag;
	}
}