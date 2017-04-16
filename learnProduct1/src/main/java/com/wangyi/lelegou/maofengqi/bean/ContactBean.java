package com.wangyi.lelegou.maofengqi.bean;

import java.io.Serializable;

public class ContactBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3071648006064519386L;

	private String name;

	private String number;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
}