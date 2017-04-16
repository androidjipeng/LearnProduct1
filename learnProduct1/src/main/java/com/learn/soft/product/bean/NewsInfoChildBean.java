package com.learn.soft.product.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

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
public class NewsInfoChildBean implements Serializable {
    public String circle_id;
    public String id;
    public String thumb;
    public String user_photo;
    public String qishu;
    public String q_end_time;
    public String money;
    public String gonumber;
    public String q_uid;
    public String q_user_code;
    @SerializedName("q_user")
    public String user_name;

}
