package com.learn.soft.product.bean;

import com.google.gson.annotations.SerializedName;
import com.learn.soft.product.util.StringUtil;

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
public class UserInfo implements Serializable {

    @SerializedName("ID")
    public String userId;
    @SerializedName("ProjectID")
    public String projectID;
    @SerializedName("AccountID")
    public String accountID;
    @SerializedName("NickName")
    public String nickName;
    @SerializedName("RealName")
    public String realName;
    @SerializedName("VirtualCoin")
    public String virtualCoin;
    @SerializedName("Score")
    public String score;
    @SerializedName("FundMoney")
    public String fundMoney;
    @SerializedName("NetCallBalance")
    public String netCallBalance;
    @SerializedName("IndianaConin")
    public String indianaConin;
    @SerializedName("HeadImage")
    public String headImage;
    @SerializedName("Identification")
    public String identification;
    @SerializedName("AccountLevelID")
    public String accountLevelID;
    @SerializedName("PersonalRemark")
    public String personalRemark;
    @SerializedName("PhoneNumber")
    public String phoneNumber;
    @SerializedName("Age")
    public String age;
    @SerializedName("Sex")
    public String sex;
    @SerializedName("Birthday")
    public String birthday;
    @SerializedName("Job")
    public String job;
    @SerializedName("Province")
    public String province;
    @SerializedName("City")
    public String city;
    @SerializedName("District")
    public String district;
    @SerializedName("Address")
    public String address;
    @SerializedName("CreateTime")
    public String createTime;
    @SerializedName("UpdateTime")
    public String updateTime;
    @SerializedName("Remark")
    public String remark;


    public boolean isLoginSuccess() {
        return StringUtil.isNotEmpty(userId);
    }

}
