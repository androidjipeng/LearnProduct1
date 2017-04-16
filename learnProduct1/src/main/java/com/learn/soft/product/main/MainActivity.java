package com.learn.soft.product.main;

import java.net.URLDecoder;

import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;


import com.learn.soft.product.jni.BaseActivity;
import com.learn.soft.product.jni.JiaZhengApp;
import com.learn.soft.product.main.activity.ShowWebViewInfoActivity;
import com.learn.soft.product.util.ApiWebCommon;
import com.learn.soft.product.util.BundleKey;
import com.learn.soft.product.util.MyLog;
import com.learn.soft.product.util.StringUtil;
import com.learn.soft.product.util.ToastHelper;
import com.learn.soft.product1.R;
import com.umeng.analytics.MobclickAgent;
import com.wangyi.lelegou.maofengqi.ui.activity.LoginActivity;
import com.wangyi.lelegou.maofengqi.ui.activity.SearchProductActivity;
import com.wangyi.lelegou.maofengqi.utils.Constant2;

/**
 * **********************************************************
 * <p/>
 * 说明：首页
 * <p/>
 * 作者：cailin
 * <p/>
 * 创建日期：2016/4/19
 * <p/>
 * 描述： **********************************************************
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {
	private static final int TAG_FRGMENT_SIZE = 5;
	private boolean isShowExitTip = false;

	private TextView mBtnView[];
	private ImageView mIvView[];
	private int mSelectPosition = -1;
	private BaseTabFragment mCurrentShowFrg;
	private TextView mTvShowBuyNumber;
	private String mRefreshUrl;

	private int secondPosition;
	private int cateId;
	private String cateName;

	@Override
	protected void onBaseCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_main);
		mRefreshUrl = ApiWebCommon.API_COMMON.Api_Common_Url
				+ ApiWebCommon.API_COMMON.Web_Index_Cart_Number;
		mTvShowBuyNumber = (TextView) findViewById(R.id.tv_show_buy_number);
		mSelectPosition = -1;
		mBtnView = new TextView[TAG_FRGMENT_SIZE];
		mIvView = new ImageView[TAG_FRGMENT_SIZE];
		for (int i = 0; i < mBtnView.length; i++) {
			mBtnView[i] = (TextView) findViewById(R.id.tv_btn1 + i);
			mIvView[i] = (ImageView) findViewById(R.id.iv_btn1 + i);
			mBtnView[i].setOnClickListener(this);
		}
		initDataSelectPosition(0,0,0,"");
		findViewById(R.id.iv_search).setOnClickListener(this);
		findViewById(R.id.iv_title_head_buy).setOnClickListener(this);
		findViewById(R.id.iv_title_head_login).setOnClickListener(this);
		findViewById(R.id.iv_new).setOnClickListener(this);
		MobclickAgent.setDebugMode(false);
		MobclickAgent.openActivityDurationTrack(false);

		/**
         * SDK初始化 ，请放在游戏启动界面
         */
//        IAppPay.init(this, IAppPay.PORTRAIT, Constant2.appid);//接入时！不要使用Demo中的appid
	}

	@Override
	protected void initViews() {

	}

	public void initDataSelectPosition(int position,int secondPosition,int cateId,String cateName) {
		if (mSelectPosition == position) {
			return;
		} else {
			mSelectPosition = position;
			this.secondPosition=secondPosition;
			this.cateId = cateId;
			this.cateName=cateName;
		}
		initFrgsAndShow();
		resetSelectBtnBg();
	}

	private void initFrgsAndShow() {
		if (mSelectPosition < 0 || mSelectPosition > TAG_FRGMENT_SIZE) {
			throw new IllegalStateException("position error");
		}
		switch (mSelectPosition) {
		case 0:
			mCurrentShowFrg = new TabIndexFragment();
			break;
		case 1:
			mCurrentShowFrg = new TabAllProductFragment();
			Bundle bundle=new Bundle();
			//bundle.putInt(key, value);
			bundle.putInt(BundleKey.Bundle_KEY_POSITION, secondPosition);
			bundle.putInt(BundleKey.BUNDLE_KEY_CATE_ID, cateId);
			bundle.putString(BundleKey.BUNDLE_KEY_CATE_NAME, cateName);
			mCurrentShowFrg.setArguments(bundle);
			break;
		case 2:
			mCurrentShowFrg = new TabNewsInfoFragment();
			break;
		case 3:
			mCurrentShowFrg = new TabShowHistoryFragment();
			break;
		case 4:
			mCurrentShowFrg = new TabMineFragment();
			break;
		default:
			mCurrentShowFrg = new TabMineFragment();
			break;
		}
		FragmentTransaction mTransaction = getSupportFragmentManager()
				.beginTransaction();
		// mTransaction.setCustomAnimations(R.anim.slide_in_from_left, 0);
		mTransaction.replace(R.id.area_show_content, mCurrentShowFrg, "frg"
				+ mSelectPosition);
		mTransaction.commit();
	}

	private void resetSelectBtnBg() {
		for (int i = 0; i < mBtnView.length; i++) {
			if (mSelectPosition == i) {
				mIvView[i].setVisibility(View.VISIBLE);
				mBtnView[i]
						.setTextColor(getResources().getColor(R.color.white));
				mBtnView[i]
						.setBackgroundResource(R.drawable.bg_tab_title_circle_corners);
			} else {
				mIvView[i].setVisibility(View.INVISIBLE);
				mBtnView[i].setTextColor(getResources().getColor(
						R.color.tab_color_default));
				mBtnView[i]
						.setBackgroundResource(R.drawable.bg_common_selector);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_btn1:
			initDataSelectPosition(0,0,0,"");
			break;
		case R.id.tv_btn2:
			initDataSelectPosition(1,0,0,"");
			break;
		case R.id.tv_btn3:
			initDataSelectPosition(2, 0, 0, "");
			break;
		case R.id.tv_btn4:
			initDataSelectPosition(3, 0, 0, "");
			break;
		case R.id.tv_btn5:
		// initDataSelectPosition(4);
		{
			StringBuffer sb = new StringBuffer();
			sb.append(ApiWebCommon.API_COMMON.Api_Common_Url);
			sb.append(ApiWebCommon.API_COMMON.Web_Index_Time_Limit);
			String url = sb.toString();
			setCommonWebViewShow("", url);
		}
			break;
		case R.id.iv_title_head_buy: {
			StringBuffer sb = new StringBuffer();
			sb.append(ApiWebCommon.API_COMMON.Api_Common_Url);
			sb.append(ApiWebCommon.API_COMMON.Web_Index_Cart_List);
			String url = sb.toString();
			setCommonWebViewShow("", url);
		}
			break;
		case R.id.iv_title_head_login: {

			if (JiaZhengApp.getInstance().isLogin()) {
				// PersonalDataActivity.start(this);
				StringBuffer sb = new StringBuffer();
				sb.append(ApiWebCommon.API_COMMON.Api_Common_Url);
				sb.append(ApiWebCommon.API_COMMON.Web_Index_User_Login);
				String url = sb.toString();
				setCommonWebViewShow("", url);
			} else {
				LoginActivity.start(this);
			}
		}
			break;
		case R.id.iv_search:
			SearchProductActivity.start(this);
			break;
		case R.id.iv_new:
			//setCommonWebViewShow("", "http://91lelegou.com/?/carousel/carousel/novice_raiders");
			if (JiaZhengApp.getInstance().isLogin()) {
				setCommonWebViewShow("", "http://91lelegou.com/?/mobile/home/orderlist");
			}else{
				LoginActivity.start(this);
			}
			break;
		default:
			break;
		}
	}

	public void setCommonWebViewShow(String title, String url) {
		Intent intent = new Intent(getApplication(),
				ShowWebViewInfoActivity.class);
		Bundle args = new Bundle();
		args.putString(BundleKey.Bundle_KEY_Title, title);
		MyLog.i("xiaocai", "url=" + url);
		args.putString(BundleKey.Bundle_KEY_Url, url);
		intent.putExtras(args);
		startActivity(intent);
	}

	@Override
	public void onBackPressed() {
		if (!isShowExitTip) {
			isShowExitTip = true;
			ToastHelper.toast(this, R.string.app_exit_tip);
			mBtnView[0].postDelayed(new Runnable() {
				@Override
				public void run() {
					isShowExitTip = false;
				}
			}, 2000);
			return;
		}
		super.onBackPressed();
		finish();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		// onBaseCreate(null);
	}

	@Override
	protected void onResume() {
		super.onResume();
		refreshShowUI();
	}

	public void refreshShowUI() {
		beginRefreshData();
	}

	private WebView mWvShow = null;
	private int mCountBuyNumber = 0;

	private void beginRefreshData() {
		if (StringUtil.isEmptyOrNull(mRefreshUrl)) {
			mRefreshUrl = ApiWebCommon.API_COMMON.Api_Common_Url
					+ ApiWebCommon.API_COMMON.Web_Index_Cart_Number;
		}
		if (mWvShow == null) {
			mWvShow = new WebView(getApplication());
			mWvShow.getSettings().setJavaScriptEnabled(true);// 设置页面支持Javascript
			mWvShow.getSettings().setLoadWithOverviewMode(true);
			mWvShow.getSettings().setSupportZoom(true);// 支持缩放
			mWvShow.getSettings().setDefaultTextEncodingName("utf-8");
			mWvShow.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);// 解决缓存问题

			mWvShow.getSettings().setUseWideViewPort(true);
			mWvShow.getSettings().setLoadWithOverviewMode(true);
			mWvShow.getSettings().setSavePassword(true);
			mWvShow.getSettings().setSaveFormData(true);
			mWvShow.getSettings().setJavaScriptEnabled(true);
			// enable Web Storage: localStorage, sessionStorage
			mWvShow.getSettings().setDomStorageEnabled(true);

			mWvShow.setWebViewClient(new SelfWebViewClient());
			mWvShow.setWebChromeClient(new SelfChromeClient());
		}

		mWvShow.loadUrl(mRefreshUrl);
	}

	class SelfWebViewClient extends WebViewClient {

		@Override
		public void onReceivedSslError(WebView view, SslErrorHandler handler,
				SslError error) {
			// 如果加载网页失败的话就接受证书
			handler.proceed();
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			if (url.indexOf("tel:") < 0) {
				view.loadUrl(url);
			}

			return true;
		}

	}

	class SelfChromeClient extends WebChromeClient {
		public SelfChromeClient() {
		}

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			if (newProgress == 100) {
				CookieManager cookieManager = CookieManager.getInstance();
				String str = cookieManager.getCookie(mRefreshUrl);
				MyLog.i("xiaocai", "Cookies CookieManager = " + str);
				try {
					mCountBuyNumber = 0;
					str = URLDecoder.decode(str, "utf-8");
					MyLog.i("xiaocai", "Cookies CookieManager = " + str);
					if (StringUtil.isNotEmpty(str)) {
						String[] arrays = str.split(";");
						int number = arrays != null ? arrays.length : 0;
						if (number > 0) {
							for (int i = 0; i < number; i++) {
								String[] keyPair = arrays[i].split("=");
								int allNumber = keyPair != null ? keyPair.length
										: 0;
								if (allNumber >= 2) {
									MyLog.i("xiaocai", "keyPair[0] = "
											+ keyPair[0]);
									String key = keyPair[0];
									if (StringUtil.isNotEmpty(key)) {
										key = key.trim();
									}
									if ("Cartlist".equals(key)) {

										String data = keyPair[1];
										MyLog.i("xiaocai", "keyPair[1] = "
												+ data);
										if (StringUtil.isNotEmpty(data)) {
											String str1 = data;
											String str2 = "num";
											int total = 0;
											for (String tmp = str1; tmp != null
													&& tmp.length() >= str2
															.length();) {
												if (tmp.indexOf(str2) == 0) {
													mCountBuyNumber++;
													tmp = tmp.substring(str2
															.length());
												} else {
													tmp = tmp.substring(1);
												}
											}
										}
										MyLog.i("xiaocai", "mCountBuyNumber = "
												+ mCountBuyNumber);
										break;
									}
								}
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (mHandler != null) {
				mHandler.sendEmptyMessage(newProgress);
			}
			super.onProgressChanged(view, newProgress);
		}

	}

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 100) {
				mTvShowBuyNumber.setText(String.valueOf(mCountBuyNumber));
				if (mCountBuyNumber > 0) {
					mTvShowBuyNumber.setVisibility(View.VISIBLE);
				} else {
					mTvShowBuyNumber.setVisibility(View.GONE);
				}
			}
		}
	};

}
