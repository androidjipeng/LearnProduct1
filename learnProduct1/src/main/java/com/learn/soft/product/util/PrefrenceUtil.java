package com.learn.soft.product.util;

import android.content.SharedPreferences;
import com.learn.soft.product.jni.JiaZhengApp;

/**
 * **********************************************************
 * <p/>
 * 说明：
 * <p/>
 * 作者：cailin
 * <p/>
 * 创建日期：2016/1/29
 * <p/>
 * 描述：
 * **********************************************************
 */
public class PrefrenceUtil {

    private String BAILETONG_PREFER = "kaixinduobao";
    private String TAG_FIRST_LOAD_FLAG="TAG_FIRST_LOAD_FLAG";

    private SharedPreferences preference;

    public boolean getFirstFlag() {
        preference = JiaZhengApp.getInstance().getSharedPreferences(BAILETONG_PREFER, 0);
        return preference.getBoolean(TAG_FIRST_LOAD_FLAG, true);
    }

    public void setFirstFlag(boolean flag) {
        preference = JiaZhengApp.getInstance().getSharedPreferences(BAILETONG_PREFER, 0);
        preference.edit().putBoolean(TAG_FIRST_LOAD_FLAG, flag).commit();
    }



}
