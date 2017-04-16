package com.wangyi.lelegou.maofengqi.bean;

import java.io.Serializable;

public class UpdateAppBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2548858094683364319L;

	private int updateCode;

	private String updateTime;

	private String updateUrl;

	private String updateInfo;

	public int getUpdateCode() {
		return updateCode;
	}

	public void setUpdateCode(int updateCode) {
		this.updateCode = updateCode;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateUrl() {
		return updateUrl;
	}

	public void setUpdateUrl(String updateUrl) {
		this.updateUrl = updateUrl;
	}

	public String getUpdateInfo() {
		return updateInfo;
	}

	public void setUpdateInfo(String updateInfo) {
		this.updateInfo = updateInfo;
	}

	@Override
	public String toString() {
		return "UpdateAppBean [updateCode=" + updateCode + ", updateTime="
				+ updateTime + ", updateUrl=" + updateUrl + ", updateInfo="
				+ updateInfo + "]";
	}
}