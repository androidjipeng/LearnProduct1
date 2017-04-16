package com.wangyi.lelegou.maofengqi.bean;

import java.io.Serializable;

public class QQBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6803795786267995196L;

	/**
	 * 标题
	 */
	private String title;

	/**
	 * 内容文本
	 */
	private String text;

	/**
	 * 地址
	 */
	private String url;

	private String imageUrl;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
}