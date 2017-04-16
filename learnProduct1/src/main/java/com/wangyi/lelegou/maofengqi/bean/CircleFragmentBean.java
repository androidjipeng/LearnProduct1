package com.wangyi.lelegou.maofengqi.bean;

import java.io.Serializable;
import java.util.List;

public class CircleFragmentBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1772069551028988615L;

	private int ibuiltCirclesCount;

	private List<CircleBean> ibuiltCircles;

	private int participateCirclesCount;

	private List<CircleBean> participateCircles;

	public int getIbuiltCirclesCount() {
		return ibuiltCirclesCount;
	}

	public void setIbuiltCirclesCount(int ibuiltCirclesCount) {
		this.ibuiltCirclesCount = ibuiltCirclesCount;
	}

	public List<CircleBean> getIbuiltCircles() {
		return ibuiltCircles;
	}

	public void setIbuiltCircles(List<CircleBean> ibuiltCircles) {
		this.ibuiltCircles = ibuiltCircles;
	}

	public int getParticipateCirclesCount() {
		return participateCirclesCount;
	}

	public void setParticipateCirclesCount(int participateCirclesCount) {
		this.participateCirclesCount = participateCirclesCount;
	}

	public List<CircleBean> getParticipateCircles() {
		return participateCircles;
	}

	public void setParticipateCircles(List<CircleBean> participateCircles) {
		this.participateCircles = participateCircles;
	}
}