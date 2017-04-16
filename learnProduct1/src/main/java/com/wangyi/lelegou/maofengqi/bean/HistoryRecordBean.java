package com.wangyi.lelegou.maofengqi.bean;

import java.io.Serializable;

/**
 * **********************************************************
 * <p/>
 * 说明:历史信息实体类
 * <p/>
 * 作者:@Doc.March
 * <p/>
 * 创建日期:2016-9-29
 * <p/>
 * 描述:
 * <p/>
 * **********************************************************
 */
public class HistoryRecordBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6156435614998409286L;

	private String searchContent;

	public HistoryRecordBean(String searchContent) {
		super();
		this.searchContent = searchContent;
	}

	public String getSearchContent() {
		return searchContent;
	}

	public void setSearchContent(String searchContent) {
		this.searchContent = searchContent;
	}
}