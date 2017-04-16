package com.wangyi.lelegou.maofengqi.bean;

import java.io.Serializable;

/**
 * **********************************************************
 * <p/>
 * 说明:用户信息实体类
 * <p/>
 * 作者:@Doc.March
 * <p/>
 * 创建日期:2016-9-29
 * <p/>
 * 描述:
 * <p/>
 * **********************************************************
 */
public class UserBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4110848984461240798L;

	private String openid;

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	// 用户Id
	private String id;

	private CookieBean cookie;

	private int invitation_status;

	private String nickName;

	private String phoneNumber;

	private String headUrl;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public CookieBean getCookie() {
		return cookie;
	}

	public void setCookie(CookieBean cookie) {
		this.cookie = cookie;
	}

	public int getInvitation_status() {
		return invitation_status;
	}

	public void setInvitation_status(int invitation_status) {
		this.invitation_status = invitation_status;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getHeadUrl() {
		return headUrl;
	}

	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}

	@Override
	public String toString() {
		return "UserBean{" +
				"openid='" + openid + '\'' +
				", id='" + id + '\'' +
				", cookie=" + cookie +
				", invitation_status=" + invitation_status +
				", nickName='" + nickName + '\'' +
				", phoneNumber='" + phoneNumber + '\'' +
				", headUrl='" + headUrl + '\'' +
				'}';
	}
}