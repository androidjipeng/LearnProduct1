package com.wangyi.lelegou.maofengqi.bean;

import java.io.Serializable;

public class MessageBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2206820059250918410L;

	/**
	 * 短信内容
	 */
	private String body;

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
}