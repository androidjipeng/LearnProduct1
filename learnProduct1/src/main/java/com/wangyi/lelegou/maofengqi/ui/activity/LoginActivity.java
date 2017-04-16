package com.wangyi.lelegou.maofengqi.ui.activity;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

import com.learn.soft.product.jni.JiaZhengApp;
import com.learn.soft.product.main.activity.ShowWebViewInfoActivity;
import com.learn.soft.product.util.BundleKey;
import com.learn.soft.product.util.MyLog;
import com.learn.soft.product1.R;
import com.wangyi.lelegou.maofengqi.base.BaseActivity;
import com.wangyi.lelegou.maofengqi.bean.CookieBean;
import com.wangyi.lelegou.maofengqi.bean.ResultBean;
import com.wangyi.lelegou.maofengqi.bean.UserBean;
import com.wangyi.lelegou.maofengqi.utils.ApiClass;
import com.wangyi.lelegou.maofengqi.utils.Constant;
import com.wangyi.lelegou.maofengqi.utils.MD5Utils;
import com.wangyi.lelegou.maofengqi.utils.OnLoginListener;
import com.wangyi.lelegou.maofengqi.utils.ResultCallback;
import com.wangyi.lelegou.maofengqi.utils.ShareUtils;
import com.wangyi.lelegou.maofengqi.utils.VerificationUtils;
import com.wangyi.lelegou.maofengqi.view.CustomClearEditText;

/**
 * **********************************************************
 * <p/>
 * 说明:登录
 * <p/>
 * 作者:@Doc.March
 * <p/>
 * 创建日期:2016-9-29
 * <p/>
 * 描述:
 * <p/>
 * **********************************************************
 */
@SuppressWarnings("deprecation")
public class LoginActivity extends BaseActivity implements View.OnClickListener {

	private CustomClearEditText etPhoneNumber;
	private EditText etPassword;
	private TextView tvForgetPassword;
	private TextView tvLogin;
	private TextView tvRegisterByPhoneNumber;

	private TextView tvWeixinLogin;
	private TextView tvQqLogin;

	private TextView tvServiceAgreement;

	private ShareUtils shareUtils;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_login;
	}

	@Override
	protected void afterCreate(Bundle savedInstanceState) {
		ShareSDK.initSDK(this);
		shareUtils = new ShareUtils();

		tvContent.setText(R.string.login);

		etPhoneNumber = (CustomClearEditText) findViewById(R.id.et_phone_number);
		etPassword = (EditText) findViewById(R.id.et_password);

		tvForgetPassword = (TextView) findViewById(R.id.tv_forget_password);
		tvLogin = (TextView) findViewById(R.id.tv_login);
		tvRegisterByPhoneNumber = (TextView) findViewById(R.id.tv_register_by_phone_number);
		tvWeixinLogin = (TextView) findViewById(R.id.tv_weixin_login);
		tvQqLogin = (TextView) findViewById(R.id.tv_qq_login);
		tvServiceAgreement = (TextView) findViewById(R.id.tv_service_agreement);

		tvForgetPassword.setOnClickListener(this);
		tvLogin.setOnClickListener(this);
		tvRegisterByPhoneNumber.setOnClickListener(this);
		tvWeixinLogin.setOnClickListener(this);
		tvQqLogin.setOnClickListener(this);
		tvServiceAgreement.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_forget_password:
			UpdatePasswordActivity.start(this);
			break;
		case R.id.tv_login:
			login();
			break;
		case R.id.tv_register_by_phone_number:
			RegisterActivity.start(this);
			break;
		case R.id.tv_weixin_login:
			Toast.makeText(this, "微信登录", Toast.LENGTH_SHORT).show();
			shareUtils.setPlatformName(Wechat.NAME);
			shareUtils.setOnLoginListener(onLoginListener);
			shareUtils.login(this);
			break;
		case R.id.tv_qq_login:
			Toast.makeText(this, "QQ登录", Toast.LENGTH_SHORT).show();
			shareUtils.setPlatformName(QQ.NAME);
			shareUtils.setOnLoginListener(onLoginListener);
			shareUtils.login(this);
			break;
		case R.id.tv_sina_login:
			Toast.makeText(this, "新浪登录", Toast.LENGTH_SHORT).show();
			shareUtils.setOnLoginListener(onLoginListener);
			shareUtils.setPlatformName(SinaWeibo.NAME);
			// shareUtils.login(this);
			break;
		case R.id.tv_service_agreement:
			String url = "http://91lelegou.com/?/question/question/protocol";
			serviceAgreement("", url);
			break;
		default:
			break;
		}
	}

	public void serviceAgreement(String title, String url) {
		Intent intent = new Intent(mActivity, ShowWebViewInfoActivity.class);
		Bundle args = new Bundle();
		args.putString(BundleKey.Bundle_KEY_Title, title);
		args.putString(BundleKey.Bundle_KEY_Url, url);
		intent.putExtras(args);
		startActivity(intent);
	}

	// 登录
	private void login() {
		String phoneNumber = etPhoneNumber.getText().toString();
		String password = etPassword.getText().toString();

		if (VerificationUtils.loginVerification(this, phoneNumber, password)) {
			showProgressInfo(null);
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("phoneNumber", phoneNumber);
			paramsMap.put("password", MD5Utils.md5(password));
			ApiClass.login(paramsMap, callback);
		}
	}

	// 第三方登录回调实现类
	private OnLoginListener onLoginListener = new OnLoginListener() {

		@Override
		public void onLogin(Platform platform, HashMap<String, Object> res) {
			thirdPartyLogin(platform);

			// if (platform == null) {
			// return;
			// }
			// String str = platform.getDb().getUserId() + "--"
			// + platform.getDb().getUserName() + "--"
			// + platform.getDb().getUserIcon();
			// Toast.makeText(mActivity, str, Toast.LENGTH_SHORT).show();
			// Log.i("info", str);
		}
	};

	// 第三方登录
	private void thirdPartyLogin(Platform platform) {
		if (platform == null) {
			return;
		}

		// 平台(1:微信,2:QQ)
		int platformName = 0;
		if (Wechat.NAME.equals(platform.getName())) {
			platformName = 1;
		} else if (QQ.NAME.equals(platform.getName())) {
			platformName = 2;
		}

		if (platformName == 0) {
			return;
		}

		showProgressInfo(null);
		MyLog.e("jp","QQ--->"+"exportData:"+platform.getDb().exportData()+"  "+platform.getDb());

		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("platformName", platformName);
		paramsMap.put("userId", platform.getDb().getUserId());
		paramsMap.put("userName", platform.getDb().getUserName());
		paramsMap.put("userIcon", platform.getDb().getUserIcon());
		paramsMap.put("channel", Constant.CHANNEL);
		ApiClass.thirdPartyLogin(paramsMap, callback);
	}

	private ResultCallback<UserBean> callback = new ResultCallback<UserBean>() {

		@Override
		public void onSuccess(ResultBean<UserBean> resultBean, int id) {
			dismissProgress();
			if (resultBean.getStatus() == 1) {
				UserBean userBean = resultBean.getJsonMsg();
				MyLog.e("jp","======>userBean:==>Openid:"+userBean.getOpenid());
				// Toast.makeText(mActivity, userBean.toString(), Toast.LENGTH_SHORT).show();
				JiaZhengApp.getInstance().setLogin(true);
				JiaZhengApp.getInstance().setUserId(userBean.getId());
				int invitationStatus = userBean.getInvitation_status();
				// 注意(*)
				JiaZhengApp.getInstance().setInvitationStatus(
						invitationStatus == 0 ? 1 : 0);
				syncCookie(mActivity, Constant.MOBILE_IP
						+ Constant.Web_Index_User_Login, userBean.getCookie());
				// startActivity(new Intent(mActivity, MainActivity.class));
				sendBroadcast(new Intent(Constant.ACTION_LOGN));
				finish();
			} else {
				Toast.makeText(mActivity, resultBean.getInfo(),
						Toast.LENGTH_SHORT).show();
			}
		}

		public void onError(okhttp3.Call call, Exception e, int id) {
			super.onError(call, e, id);
			dismissProgress();
		}
	};

	/**
	 * Sync Cookie
	 */
	private void syncCookie(Context context, String url, CookieBean bean) {
		try {
			CookieSyncManager.createInstance(context);

			CookieManager cookieManager = CookieManager.getInstance();
			cookieManager.setAcceptCookie(true);
			// cookieManager.removeSessionCookie();
			// cookieManager.removeAllCookie();

			cookieManager.setCookie(url, "uid=" + bean.getUid() + ";");
			cookieManager.setCookie(url, "ushell=" + bean.getUshell() + ";");

			CookieSyncManager.getInstance().sync();

			String newCookie = cookieManager.getCookie(url);
			if (newCookie != null) {
				Log.i("info", newCookie);
			}
		} catch (Exception e) {
			Log.i("info", e.toString());
		}
	}

	public static void start(Context context) {
		Intent intent = new Intent(context, LoginActivity.class);
		context.startActivity(intent);
	}
}