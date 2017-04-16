package com.learn.soft.product.util.cache;


import com.learn.soft.product.bean.UserInfo;

/**
 * **********************************************************
 * <p/>
 * 说明：
 * <p/>
 * 作者：cailin
 * <p/>
 * 创建日期：2014/9/29
 * <p/>
 * 描述：
 * **********************************************************
 */
public class UserInfoProxy extends BaseModelProxy<String, UserInfo> {

    private static final String TAG_USER_INFO_CACHE = "MyLoginInfoCache_New_2";
    private UserInfo mLoginInfo ;

    public UserInfoProxy() {
        super(TAG_USER_INFO_CACHE);
    }

    private static UserInfoProxy sUserInfoProxy = new UserInfoProxy();

    public static UserInfoProxy getInstance() {
        return sUserInfoProxy;
    }

    @Override
    protected String getKey(String... args) {
        return "UserInfoCache_Key_";
    }

    public UserInfo getLoginInfo() {
        if (mLoginInfo == null) {
            mLoginInfo = hitLocal();
        }
//        if (mLoginInfo!=null){
//            Gson gson=new Gson();
//            MyLog.i("xiaocai", "userinfo gson=" + gson.toJson(mLoginInfo));
//        }
        return mLoginInfo;
    }

    public boolean isLoginSuccess() {
        if (mLoginInfo == null) {
            getLoginInfo();
        }
        return mLoginInfo != null ? mLoginInfo.isLoginSuccess() : false;
    }

    public UserInfo setLoginUserInfo(UserInfo info) {
        putLocal(info);
        mLoginInfo = info;
        return info;
    }

}
