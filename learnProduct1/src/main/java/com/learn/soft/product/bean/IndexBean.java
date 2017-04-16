package com.learn.soft.product.bean;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * **********************************************************
 * <p/>
 * 说明：
 * <p/>
 * 作者：cailin
 * <p/>
 * 创建日期：2014/11/26
 * <p/>
 * 描述： **********************************************************
 */
public class IndexBean implements Serializable {

	public int status;

	// 广告
	@SerializedName("slides")
	public List<IndexBeanAd> listAd;

	// 热门推荐
	@SerializedName("shoplistrenqi")
	public List<IndexBeanAdvice> listAdvice;

	// 最新揭晓
	// @SerializedName("shopqishu")
	// public List<IndexBeanNewsInfo> listNewsInfo;

	// 今日限时
	// @SerializedName("jinri_shoplist")
	// public List<IndexBeanToday> listToday;

	@SerializedName("shoplistliangyuan")
	public List<IndexBeanAdvice> liangYuanBeans;

	@SerializedName("shoplistsanyuan")
	public List<IndexBeanAdvice> sanYuanBeans;
}