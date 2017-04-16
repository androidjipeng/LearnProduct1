package com.learn.soft.product.bean;

import java.io.Serializable;

/**
 * **********************************************************
 * <p/>
 * 说明：
 * <p/>
 * 作者：cailin
 * <p/>
 * 创建日期：2014/11/26
 * <p/>
 * 描述： **********************************************************
 */
public class IndexBeanAdvice implements Serializable {
	public String id;
	public String thumb;
	public String money;
	public String circle_code;


	public String getCircle_code() {
		return circle_code;
	}

	public void setCircle_code(String circle_code) {
		this.circle_code = circle_code;
	}

	public int canyurenshu;
	public int zongrenshu;
	public int yunjiage;
}