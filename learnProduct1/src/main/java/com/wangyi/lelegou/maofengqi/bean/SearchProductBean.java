package com.wangyi.lelegou.maofengqi.bean;

import java.io.Serializable;
import java.util.List;

/**
 * **********************************************************
 * <p/>
 * 说明:搜索商品实体类
 * <p/>
 * 作者:@Doc.March
 * <p/>
 * 创建日期:2016-9-29
 * <p/>
 * 描述:
 * <p/>
 * **********************************************************
 */
public class SearchProductBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3061329747021441942L;

	private int count;

	private List<ProductBean> list;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<ProductBean> getList() {
		return list;
	}

	public void setList(List<ProductBean> list) {
		this.list = list;
	}
}