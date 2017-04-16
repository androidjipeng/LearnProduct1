package com.learn.soft.product.bean;

import java.io.Serializable;
import java.util.List;

/**
 * **********************************************************
 * <p/>
 * 说明：
 * <p/>
 * 作者：cailin
 * <p/>
 * 创建日期：2014/11/26
 * <p/>
 * 描述：
 * **********************************************************
 */
public class HistoryBean implements Serializable {

    public int status;
    public String count;
    public List<HistoryChildBean> list;

}
