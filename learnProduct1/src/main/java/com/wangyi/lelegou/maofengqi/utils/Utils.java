package com.wangyi.lelegou.maofengqi.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import okhttp3.Call;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.learn.soft.product.main.activity.ShowWebViewInfoActivity;
import com.learn.soft.product.util.BundleKey;

public class Utils {

	public static void startWebViewShow(Context context, String title,
			String url) {
		Intent intent = new Intent(context, ShowWebViewInfoActivity.class);
		Bundle args = new Bundle();
		args.putString(BundleKey.Bundle_KEY_Title, title);
		args.putString(BundleKey.Bundle_KEY_Url, url);
		intent.putExtras(args);
		context.startActivity(intent);
	}
//
//	public static void wexin_ijuhe(Context context, String url, IWXAPI api) {
//		// http://pay.ijuhepay.com:88/GateWay/Receive.aspx?P_UserId=1626&P_OrderId=C14827397815250904&P_CardId=&P_CardPass=&&P_FaceValue=9.00&&P_ChannelId=1010&P_Subject=&P_Price=9.00&P_Quantity=1&P_Description=&&P_Notic=&&P_PostKey=5306e1419c2b10429df5c095831fe80d&P_Result_url=http://91lelegou.com/index.php/pay/ijuhe_url/houtai&P_Notify_URL=http://91lelegou.com/index.php/pay/ijuhe_url/qiantai
//		if (!isWeixinAvilible(context)) {
//			Toast.makeText(context, "您还没有安装微信", Toast.LENGTH_SHORT).show();
//			return;
//		}
//		boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
//		if (isPaySupported) {
//			OkHttpUtilsManager.getInstance().postAsyn(url, null, null, null,
//					null, new WeiXinStringCallback(context, api));
//		} else {
//			Toast.makeText(context, String.valueOf(isPaySupported),
//					Toast.LENGTH_SHORT).show();
//		}
//	}

	public static boolean isWeixinAvilible(Context context) {
		final PackageManager packageManager = context.getPackageManager();// 获取PackageManager
		List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
		if (pinfo != null) {
			for (int i = 0; i < pinfo.size(); i++) {
				String pn = pinfo.get(i).packageName;
				if (pn.equals("com.tencent.mm")) {
					return true;
				}
			}
		}
		return false;
	}

//	private static final class WeiXinStringCallback extends StringCallback {
//
//		private Context context;
////		private IWXAPI api;
//
////		public WeiXinStringCallback(Context context, IWXAPI api) {
////			super();
////			this.context = context;
////			this.api = api;
////		}
//
//		@Override
//		public void onError(Call call, Exception e, int id) {
//			Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
//		}
//
//		@Override
//		public void onResponse(String response, int id) {
//			try {
//				JSONObject json = new JSONObject(response);
//
//				PayReq req = new PayReq();
//
//				req.appId = json.getString("appid");
//				req.partnerId = json.getString("partnerid");
//				req.prepayId = json.getString("prepayid");
//				req.nonceStr = json.getString("noncestr");
//				req.timeStamp = json.getString("timestamp");
//				req.packageValue = json.getString("package");
//				req.sign = json.getString("sign");
//				req.extData = "app data";
//
//				Toast.makeText(context, "正常调起支付", Toast.LENGTH_SHORT).show();
//
//				// 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
//				api.sendReq(req);
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//		}
//	}

	public static void zhifubao(final Context context, String url) {
		try {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			context.startActivity(intent);
		} catch (Exception e) {
			DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					Uri alipayUrl = Uri.parse("https://d.alipay.com");
					Intent intent = new Intent(Intent.ACTION_VIEW, alipayUrl);
					context.startActivity(intent);
				}
			};
			new AlertDialog.Builder(context).setMessage("未检测到支付宝客户端，请安装后重试。")
					.setPositiveButton("立即安装", listener)
					.setNegativeButton("取消", null).show();
		}
	}

	public static void save(Context context, String content) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			try {
				String sdPath = Environment.getExternalStorageDirectory()
						.getAbsolutePath();
				String fileName = "aaaa.txt";
				String filePath = sdPath + fileName;
				FileOutputStream fos = new FileOutputStream(filePath);
				fos.write(content.getBytes("utf-8"));
				fos.close();
				Toast.makeText(context, "保存完成", Toast.LENGTH_SHORT).show();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			Toast.makeText(context, "sd卡没有挂载", Toast.LENGTH_SHORT).show();
		}
	}
}