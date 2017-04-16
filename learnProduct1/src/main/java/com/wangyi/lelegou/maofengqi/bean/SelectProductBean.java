package com.wangyi.lelegou.maofengqi.bean;

import java.io.Serializable;

public class SelectProductBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8366261389407765570L;

	private String productId;

	private String productName;

	private String pictureUrl;

	private String productPrice;

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public String getProductPrice() {
		return productPrice;
	}

	public void setProductPrice(String productPrice) {
		this.productPrice = productPrice;
	}
}