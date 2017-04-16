package com.wangyi.lelegou.maofengqi.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.learn.soft.product.jni.JiaZhengApp;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jipeng on 2017/4/14.
 */

public class GetDeviceInformaton {


    public static String deviceInfo() {
        String deviceId = "";
        try {
            TelephonyManager manager = (TelephonyManager) JiaZhengApp.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
            deviceId = manager.getDeviceId();

        } catch (Exception e) {
            Toast.makeText(JiaZhengApp.getInstance(), "设备号获取异常", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        String version = "";
        String packageName = "";
        try {
            PackageManager manager = JiaZhengApp.getInstance().getPackageManager();
            PackageInfo info = manager.getPackageInfo(JiaZhengApp.getInstance().getPackageName(), 0);
            packageName = info.packageName;
            version = info.versionName;

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(JiaZhengApp.getInstance(), "未找到版本号", Toast.LENGTH_SHORT).show();
        }


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("deviceName", android.os.Build.MODEL);
            jsonObject.put("systemVersion", version);
            jsonObject.put("deviceNo", deviceId);
            jsonObject.put("bundleId", packageName);
            jsonObject.put("channelCode", "android_1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

//
//    /**
//     * 获取设备串号 IMEI,国际移动设备身份码
//     *
//     * @return
//     */
//    public static String getDeviceId(Context context) {
//        String deviceId = "";
//        try {
//            TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//            deviceId = manager.getDeviceId();
//
//        } catch (Exception e) {
//            Toast.makeText(context, "设备号获取异常", Toast.LENGTH_SHORT).show();
//            e.printStackTrace();
//        }
//        return deviceId;
//    }
//
//    /**
//     * 获取设备名称
//     * */
//    public static String getDeviceName()
//    {
//        return  android.os.Build.MODEL;
//    }
//
//    /**版本号*/
//    public static String getVersion(Context context) {
//        try {
//            PackageManager manager = context.getPackageManager();
//            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
//
//            String version = info.versionName;
//            return version;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "未找到版本号";
//        }
//    }
}
