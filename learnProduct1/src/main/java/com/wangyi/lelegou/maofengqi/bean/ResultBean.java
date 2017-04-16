package com.wangyi.lelegou.maofengqi.bean;

import java.io.Serializable;

/**
 * **********************************************************
 * <p/>
 * 说明:服务器数据实体类
 * <p/>
 * 作者:@Doc.March
 * <p/>
 * 创建日期:2016-9-29
 * <p/>
 * 描述:
 * <p/>
 * **********************************************************
 */
public class ResultBean<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4860093732166990010L;

	private int status;

	private String info;

	private T jsonMsg;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public T getJsonMsg() {
		return jsonMsg;
	}

	public void setJsonMsg(T jsonMsg) {
		this.jsonMsg = jsonMsg;
	}
}