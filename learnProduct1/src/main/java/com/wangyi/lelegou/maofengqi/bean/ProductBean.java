package com.wangyi.lelegou.maofengqi.bean;

import java.io.Serializable;

/**
 * **********************************************************
 * <p/>
 * 说明:商品实体类
 * <p/>
 * 作者:@Doc.March
 * <p/>
 * 创建日期:2016-9-29
 * <p/>
 * 描述:
 * <p/>
 * **********************************************************
 */
public class ProductBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4542009282367461069L;

	public String id;

	private String title;

	private String thumb;

	private String zongrenshu;

	private String canyurenshu;

	private String shenyurenshu;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getThumb() {
		return thumb;
	}

	public void setThumb(String thumb) {
		this.thumb = thumb;
	}

	public String getZongrenshu() {
		return zongrenshu;
	}

	public void setZongrenshu(String zongrenshu) {
		this.zongrenshu = zongrenshu;
	}

	public String getCanyurenshu() {
		return canyurenshu;
	}

	public void setCanyurenshu(String canyurenshu) {
		this.canyurenshu = canyurenshu;
	}

	public String getShenyurenshu() {
		return shenyurenshu;
	}

	public void setShenyurenshu(String shenyurenshu) {
		this.shenyurenshu = shenyurenshu;
	}
}