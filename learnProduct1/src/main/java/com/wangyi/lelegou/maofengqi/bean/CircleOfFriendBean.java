package com.wangyi.lelegou.maofengqi.bean;

import java.io.Serializable;

public class CircleOfFriendBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8431758122525663977L;

	/**
	 * 用户Id
	 */
	private String id;

	/**
	 * 头像
	 */
	private String headUrl;

	private String nickname;

	/**
	 * 手机号码
	 */
	private String phoneNumber;

	/**
	 * 本月消费
	 */
	private String consumption;

	/**
	 * 本月分成
	 */
	private String monthCut;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getHeadUrl() {
		return headUrl;
	}

	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getConsumption() {
		return consumption;
	}

	public void setConsumption(String consumption) {
		this.consumption = consumption;
	}

	public String getMonthCut() {
		return monthCut;
	}

	public void setMonthCut(String monthCut) {
		this.monthCut = monthCut;
	}
}