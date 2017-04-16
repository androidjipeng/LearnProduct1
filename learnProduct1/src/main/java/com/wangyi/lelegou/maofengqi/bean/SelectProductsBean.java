package com.wangyi.lelegou.maofengqi.bean;

import java.io.Serializable;
import java.util.List;

public class SelectProductsBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3927158070642420046L;

	private int pageCount;

	private List<SelectProductBean> products;

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public List<SelectProductBean> getProducts() {
		return products;
	}

	public void setProducts(List<SelectProductBean> products) {
		this.products = products;
	}
}