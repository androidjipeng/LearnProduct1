package com.wangyi.lelegou.maofengqi.bean;

import java.io.Serializable;

public class ShareBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9133939525585437561L;

	private QQBean qqBean;

	private WeiXinBean weiXinBean;

	private MessageBean messageBean;

	public QQBean getQqBean() {
		return qqBean;
	}

	public void setQqBean(QQBean qqBean) {
		this.qqBean = qqBean;
	}

	public WeiXinBean getWeiXinBean() {
		return weiXinBean;
	}

	public void setWeiXinBean(WeiXinBean weiXinBean) {
		this.weiXinBean = weiXinBean;
	}

	public MessageBean getMessageBean() {
		return messageBean;
	}

	public void setMessageBean(MessageBean messageBean) {
		this.messageBean = messageBean;
	}
}