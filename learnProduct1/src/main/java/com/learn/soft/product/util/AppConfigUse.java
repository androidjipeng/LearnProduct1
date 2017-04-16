package com.learn.soft.product.util;

import android.os.Environment;
import com.learn.soft.product.jni.JiaZhengApp;

import java.io.File;

public class AppConfigUse
{

    private static String sCachePath;

    public static String getCachePath() {
        if (StringUtil.isEmptyOrNull(sCachePath)) {
            sCachePath = JiaZhengApp.getInstance().getCacheDir().getPath() + File.separator;
        }
        return sCachePath;
    }

    // 本地基础路径
    public static final String PATH_BASE = Environment.getExternalStorageDirectory() + "/jiazheng";
    // 下载更新apk存储路径
    public static final String PATH_APK = PATH_BASE + "/Apk/";
    // 下载图片存储路径
    public static final String PATH_PIC_CACHE = PATH_BASE + "/PicCache";


}
