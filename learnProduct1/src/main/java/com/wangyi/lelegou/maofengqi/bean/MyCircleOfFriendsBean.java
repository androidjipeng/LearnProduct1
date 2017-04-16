package com.wangyi.lelegou.maofengqi.bean;

import java.io.Serializable;
import java.util.List;

public class MyCircleOfFriendsBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9218925456729425196L;

	/**
	 * 累计分成
	 */
	private String accumulaticeCut;

	private List<CircleOfFriendBean> contacts;

	public String getAccumulaticeCut() {
		return accumulaticeCut;
	}

	public void setAccumulaticeCut(String accumulaticeCut) {
		this.accumulaticeCut = accumulaticeCut;
	}

	public List<CircleOfFriendBean> getContacts() {
		return contacts;
	}

	public void setContacts(List<CircleOfFriendBean> contacts) {
		this.contacts = contacts;
	}
}