package com.wangyi.lelegou.maofengqi.bean;

import java.io.Serializable;
import java.util.List;

public class AllCirclesBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1989959023097033267L;

	private int pageCount;

	private List<CircleBean> circles;

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public List<CircleBean> getCircles() {
		return circles;
	}

	public void setCircles(List<CircleBean> circles) {
		this.circles = circles;
	}
}