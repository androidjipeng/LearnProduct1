package com.learn.soft.product.main.activity;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;


import com.learn.soft.product.jni.BaseSwipeBackActivity;
import com.learn.soft.product.jni.JiaZhengApp;
import com.learn.soft.product.main.activity.fragment.ShowCalTipDlg;
import com.learn.soft.product.util.ActivityFrgManager;
import com.learn.soft.product.util.ApiWebCommon;
import com.learn.soft.product.util.BundleKey;
import com.learn.soft.product.util.MyLog;
import com.learn.soft.product.util.StringUtil;
import com.learn.soft.product.util.UtilConversionHelper;
import com.learn.soft.product.widget.ObservableWebView;
import com.learn.soft.product1.R;
import com.wangyi.lelegou.maofengqi.bean.ResultBean;
import com.wangyi.lelegou.maofengqi.ui.activity.AddressManagerActivity;
import com.wangyi.lelegou.maofengqi.ui.activity.LoginActivity;
import com.wangyi.lelegou.maofengqi.ui.activity.MainActivity;
import com.wangyi.lelegou.maofengqi.ui.activity.MyCircleOfFriendsActivity;
import com.wangyi.lelegou.maofengqi.ui.activity.PersonalDataActivity;
import com.wangyi.lelegou.maofengqi.ui.activity.ShaiDanActivity;
import com.wangyi.lelegou.maofengqi.utils.*;

/**
 * **********************************************************
 * <p/>
 * 说明：
 * <p/>
 * 作者：cailin
 * <p/>
 * 创建日期：2016/7/2
 * <p/>
 * 描述： **********************************************************
 */
public class ShowWebViewInfoActivity extends BaseSwipeBackActivity implements
		View.OnClickListener {
	private String mTitle;
	private String mUrl;

	// private ProgressBar mPbShow;
	private ObservableWebView mWvShow;
	private View mBtnBack2;
	private View mBtnLoginOut;
	private TextView mTvTitle;

	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 100) {

				dismissProgress();
				if (mTvBuyNumber != null) {
					mTvBuyNumber.setText(String.valueOf(mCountBuyNumber));
					if (mCountBuyNumber > 0) {
						mTvBuyNumber.setVisibility(View.VISIBLE);
					} else {
						mTvBuyNumber.setVisibility(View.GONE);
					}
				}
			} else if (msg.what == 200) {
				dismissProgress();
				finish();
			} else if (msg.what == 300) {
				// mWvShow.loadUrl(mUrl);
				refreshLoadNumber();
			}/*
			 * else if (msg.what==500){ if (mAreaZhmx!=null){
			 * mAreaZhmx.setVisibility(View.VISIBLE); } }else if
			 * (msg.what==600){ if (mAreaZhmx!=null){
			 * mAreaZhmx.setVisibility(View.GONE); } }
			 */
		}
	};

	private View mAreaBuyContent;
	private TextView mTvBuyNumber;
	private int mCountBuyNumber = 0;
	private View mAreaCalResult;
	private View mAreaZhmx;

	public static ShowWebViewInfoActivity instance = null;

//	SZFPayInit tools;
	// i聚合微信
//	private IWXAPI api;
	private String mark;

	@Override
	protected void onBaseCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_show_webinfo);
		// 初始化微信支付
//		tools = new SZFPayInit(this);
//		api = WXAPIFactory.createWXAPI(this, Constant2.wxappid);
//		api.registerApp(Constant2.wxappid);

		instance = this;
		Bundle args = getIntent().getExtras() != null ? getIntent().getExtras()
				: savedInstanceState;
		if (args != null) {
			mTitle = args.getString(BundleKey.Bundle_KEY_Title);
			mUrl = args.getString(BundleKey.Bundle_KEY_Url);
//			id=args.getString("id");
			mark=args.getString("mark");
		}
		MyLog.i("xiaocai", "webview url="+mUrl);
		MyLog.i("xiaocai", "webview mTitle="+mTitle);
		String url1=Constant.MOBILE_IP+"/home/orderlist";
		String url2=Constant.MOBILE_IP+"/home/userbuylist";
		if (url1.equals(mUrl)){
			makeRead(1);
		}else if (url2.equals(mUrl)){
			makeRead(2);
		}

		mAreaZhmx = findViewById(R.id.area_show_zhmx);
		mAreaCalResult = findViewById(R.id.area_show_calResult);
		mAreaBuyContent = findViewById(R.id.area_show_buy_content);
		mTvBuyNumber = (TextView) findViewById(R.id.tv_head_buy_number);

		mTvTitle = (TextView) findViewById(R.id.tv_head_title);
		mBtnBack2 = findViewById(R.id.btn_head_go_back);
		mBtnLoginOut = findViewById(R.id.btn_login_out);
		mAreaBuyContent.setOnClickListener(this);
		mBtnBack2.setOnClickListener(this);
		mAreaCalResult.setOnClickListener(this);
		mAreaZhmx.setOnClickListener(this);
		mBtnLoginOut.setOnClickListener(this);
		mWvShow = (ObservableWebView) findViewById(R.id.wv_main_show);

		WebSettings settings = mWvShow.getSettings();
		// 设置页面支持JavaScript
		settings.setJavaScriptEnabled(true);
		// 设置webView自适应屏幕大小
		settings.setUseWideViewPort(true);// 设置此属性，可任意比例缩放。
		settings.setLoadWithOverviewMode(true);
		// 设置网页默认编码
		settings.setDefaultTextEncodingName("gbk");
		// 支持缩放
		settings.setSupportZoom(true);
		// settings.setBuiltInZoomControls(true); // 显示放大缩小
		// 解决缓存问题
		settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		// 使用localStorage则必须打开 这个主要是用在html5中的本地存储。
		settings.setDomStorageEnabled(true);
		settings.setSaveFormData(true);

		settings.setSavePassword(true);

		settings.setAllowFileAccess(true);
		settings.setDatabaseEnabled(true);

		mWvShow.setWebViewClient(new SelfWebViewClient());
		mWvShow.setWebChromeClient(new SelfChromeClient());
		// 设置支持JS调用java
		mWvShow.addJavascriptInterface(new JSCallAndroidInterface(), "Android");

		showProgressInfo("");
		//// TODO: 2017/3/17 要做的

		mWvShow.loadUrl(mUrl);


		IntentFilter filter = new IntentFilter(Constant.ACTION_RELOAD);
		receiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.getAction().equals(Constant.ACTION_RELOAD)) {
					isReload = true;
				}
			}
		};
		registerReceiver(receiver, filter);
	}

	@Override
	protected void initViews() {

	}

	public void makeRead(final int type) {
		if (JiaZhengApp.getInstance().isLogin()) {
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("userId", JiaZhengApp.getInstance().getUserId());
			paramsMap.put("winnerBySelfType", type);
			ResultCallback<String> callback = new ResultCallback<String>() {
				@Override
				public void onSuccess(ResultBean<String> resultBean, int id) {
					if (resultBean!=null&&resultBean.getStatus() == 1) {
						try {
							if (type==1){

							}
							if (type==2){

							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}

				public void onError(okhttp3.Call call, Exception e, int id) {
					super.onError(call, e, id);

				}
			};
			ApiClass.markRead(paramsMap, callback);
		}
	}
	@Override
	protected void onDestroy() {
		try {
			if (mWvShow != null) {
				mWvShow.destroy();
				mWvShow = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onDestroy();
		if (receiver != null)
			unregisterReceiver(receiver);
		instance = null;
		System.gc();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_head_go_back:
			try {
				if ((mWvShow != null) && mWvShow.canGoBack()) {
					mWvShow.goBack();
				} else {
						finish();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case R.id.area_show_buy_content:
			try {
				StringBuffer sb = new StringBuffer();
				sb.append(ApiWebCommon.API_COMMON.Api_Common_Url);
				sb.append(ApiWebCommon.API_COMMON.Web_Index_Cart_List);
				String url = sb.toString();
				mWvShow.loadUrl(url);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case R.id.area_show_calResult:
			showSendTypeDlg();
			break;
		case R.id.btn_login_out: {
			JiaZhengApp.getInstance().setLogin(false);
			JiaZhengApp.getInstance().setUserId("");
			removeCookie(getApplication());
			// StringBuffer sb=new StringBuffer();
			// sb.append(ApiWebCommon.API_COMMON.Api_Common_Url);
			// sb.append(ApiWebCommon.API_COMMON.Web_Index_User_Login);
			// String url=sb.toString();
			// mWvShow.loadUrl(url);
			finish();
		}
			break;
		case R.id.area_show_zhmx:
			try {
				StringBuffer sb = new StringBuffer();
				sb.append(ApiWebCommon.API_COMMON.Api_Common_Url);
				sb.append(ApiWebCommon.API_COMMON.Web_Show_User_Balance);
				String url = sb.toString();
				mWvShow.loadUrl(url);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
	}

	private void showSendTypeDlg() {
		if (mShowCalTipDlg == null) {
			mShowCalTipDlg = new ShowCalTipDlg();
			getSupportFragmentManager().beginTransaction().add(mShowCalTipDlg,
					TAG_SHOW_SENDTYPE_MSG_DLG);
		}
		if (!mShowCalTipDlg.isVisible()) {
			int[] location = new int[2];
			mAreaBuyContent.getLocationOnScreen(location);// 那个用getLocationInWindow是整个窗口内的绝对坐标不准与getLocationOnScreen值一样，与现在返回的值一样。可以去测试一下
			int x = location[0];
			int y = location[1];
			Bundle args = new Bundle();
			args.putInt(BundleKey.BUNDLE_KEY_X,
					UtilConversionHelper.dip2px(getApplication(), 15));
			args.putInt(BundleKey.BUNDLE_KEY_Y,
					UtilConversionHelper.dip2px(getApplication(), 50));
			mShowCalTipDlg.setArguments(args);
			mShowCalTipDlg.show(getSupportFragmentManager(),
					TAG_SHOW_SENDTYPE_MSG_DLG);
		} else {
			mShowCalTipDlg.dismiss();
		}

	}

	private ShowCalTipDlg mShowCalTipDlg = null;
	private static final String TAG_SHOW_SENDTYPE_MSG_DLG = "TAG_SHOW_SENDTYPE_MSG_DLG";

	public static final String sTagShow = ApiWebCommon.API_COMMON.Api_Common_Url
			+ ApiWebCommon.API_COMMON.Web_Index;
	public static final String sTagShow2 = ApiWebCommon.API_COMMON.Api_Common_Url
			+ ApiWebCommon.API_COMMON.Web_Index + "/";

	class SelfWebViewClient extends WebViewClient {

		@Override
		public void onReceivedSslError(WebView view, SslErrorHandler handler,
				SslError error) {
			// 如果加载网页失败的话就接受证书
			handler.proceed();
		}

		@Override
		public WebResourceResponse shouldInterceptRequest(WebView view,
				String url) {
			if (StringUtil.isNotEmpty(url)) {
				MyLog.i("xiaocai", "url1=" + url);
				MyLog.i("xiaocai", "url2=" + sTagShow);
				if (url.equals(sTagShow) || url.equals(sTagShow2)) {
					Message m = new Message();
					m.what = 200;
					mHandler.sendMessage(m);
				} else if (url.contains("addShopCart") && url.contains("ajax")) {
					beginRefreshInfo();
				}/*
				 * else if (url.contains("userrecharge")){ Message m=new
				 * Message(); m.what=500; mHandler.sendMessage(m); }else if
				 * (url.contains("userbalance")||url.contains("home")){ Message
				 * m=new Message(); m.what=600; mHandler.sendMessage(m); }
				 */
			}
			return super.shouldInterceptRequest(view, url);
		}

		@Override
		public boolean shouldOverrideUrlLoading(final WebView view, String url) {
			// Toast.makeText(ShowWebViewInfoActivity.this, url, Toast.LENGTH_LONG).show();
			if (url.startsWith("weixin:")) {
				Intent intent = new Intent();
				intent.setAction(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(url));
				startActivity(intent);
				return true;
			}
			Log.i("info1", url);
			if (url.indexOf("http://91lelegou.com/?/mobile/user/login") != -1) {
				if (!JiaZhengApp.getInstance().isLogin()) {
					view.stopLoading();
					LoginActivity.start(ShowWebViewInfoActivity.this);
					return true;
				}
			}
			if (url.indexOf("http://91lelegou.com/?/mobile/mobile/MyPersonHomePage/") != -1) {
				view.stopLoading();
				PersonalDataActivity.start(view.getContext());
				return true;
			}
			if (url.indexOf("http://91lelegou.com/?/mobile/mobile/singlelist") != -1) {
				view.stopLoading();
				return true;
			}
			if (url.indexOf("http://91lelegou.com/?/mobile/mobile/sure_address") != -1) {
				view.stopLoading();
				return true;
			}
			// 购买圈子宝贝
			if (url.indexOf("http://91lelegou.com/?/mobile/circle/circleGoodDesc/") != -1) {
				
			}
			// 我的圈友
			if (url.indexOf("http://91lelegou.com/?/mobile/home/quanyou") != -1) {
				view.stopLoading();
				MyCircleOfFriendsActivity.start(ShowWebViewInfoActivity.this);
				return true;
			}
			// 爱贝支付
			if (url.indexOf("http://91lelegou.com/?/mobile/cart/paysubmit_iapppay") != -1) {
				view.stopLoading();
				// Log.i("info1", url);
				// Toast.makeText(ShowWebViewInfoActivity.this, "点击 来了",
				// Toast.LENGTH_SHORT).show();
//				String param = getTransdata(url);
//				IAppPay.startPay(ShowWebViewInfoActivity.this, param,
//						iPayResultCallback);
				return true;
			}
			// 微信支付
			
//			if (url.indexOf("http://91lelegou.com/?/mobile/cart/paysubmit_weixin") != -1) {
//				view.stopLoading();
//				weixin(url);
//				return true;
//			}
			
			// i聚合微信支付
			// 购买商品
			if (url.indexOf("http://91lelegou.com/?/mobile/cart/paysubmit_ijuhe") != -1) {
				view.stopLoading();
				String str = url
						.substring("http://91lelegou.com/?/mobile/cart/paysubmit_ijuhe?getUrl="
								.length());
//				Utils.wexin_ijuhe(ShowWebViewInfoActivity.this, str, api);
				return true;
			}
			// 建圈
			if (url.indexOf("http://91lelegou.com/?/mobile/circle/paysubmit_ijuhe") != -1) {
				view.stopLoading();
				String str = url
						.substring("http://91lelegou.com/?/mobile/circle/paysubmit_ijuhe?getUrl="
								.length());
//				Utils.wexin_ijuhe(ShowWebViewInfoActivity.this, str, api);
				return true;
			}
			// 支付宝支付
			if (url.startsWith("alipays:") || url.startsWith("alipay")) {
				Utils.zhifubao(ShowWebViewInfoActivity.this, url);
				return true;
			}
			return false;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			if (view != null) {
				String title = view.getTitle();
				MyLog.i("xiaocai", "webview title=" + title);
				if (StringUtil.isNotEmpty(title)) {
					title = title.replaceAll(" ", "");
					title = title.replaceAll("-", "");
					title = title.replaceAll("乐乐购触屏版", "");
					title = title.replaceAll("触屏版", "");
					// if (title.equals("限时")){
					// title="限时揭晓";
					// }

					if ("商品详情".equals(title) || "限时".equals(title)) {
						mAreaBuyContent.setVisibility(View.VISIBLE);
					} else {
						mAreaBuyContent.setVisibility(View.GONE);
					}

					if ("计算结果".equals(title)) {
						mAreaCalResult.setVisibility(View.VISIBLE);
					} else {
						mAreaCalResult.setVisibility(View.GONE);
					}
					if ("我的乐乐购".equals(title)) {
						mBtnLoginOut.setVisibility(View.VISIBLE);
					} else {
						mBtnLoginOut.setVisibility(View.GONE);
					}
					if (title.contains("http:")
							|| title.contains("https:")
							|| title.contains(ApiWebCommon.API_COMMON.Api_Http_Sub)) {
						title = "";
					}

					if ("帐户充值".equals(title)) {
						mAreaZhmx.setVisibility(View.VISIBLE);
					} else {
						mAreaZhmx.setVisibility(View.GONE);
					}

					mTvTitle.setText(title);
				} else {
					mTvTitle.setText("");
				}

			}
		}
	}

	private void weixin(String url) {
		// http://91lelegou.com/index.php/mobile/cart/paysubmit_weixin?
		// customer=517_C14818585142379695&
		// payMoney=1&
		// goodsName=乐乐购&
		// goodsDetail=乐乐购&
		// notify_url=http://localhost/yyg/index.php/pay/weixin_url/houtai/
		String str = url
				.substring("http://91lelegou.com/?/mobile/cart/paysubmit_weixin"
						.length() + 1);
		String[] arr = str.split("&");
		String[] params = new String[arr.length];
		for (int i = 0; i < arr.length; i++) {
			params[i] = arr[i].split("=")[1];
		}

		Toast.makeText(this, "微信支付", Toast.LENGTH_SHORT).show();
//		tools.StartPay(SZFPayInit.PAY_TYPE_WX, params[0], params[2], params[1],
//				params[3], null, new TM_Event() {
//					@Override
//					public void on_Result(int code, String... arg1) {
//						// TODO Auto-generated method stub
//						if (code == 0) {// 充值成功
//							Toast.makeText(ShowWebViewInfoActivity.this,
//									"充值成功!", Toast.LENGTH_SHORT).show();
//						} else {// 充值失败
//							Toast.makeText(ShowWebViewInfoActivity.this,
//									"充值失败!", Toast.LENGTH_SHORT).show();
//						}
//					}
//				});
	}

	/**
	 * 支付结果回调
	 */
//	IPayResultCallback iPayResultCallback = new IPayResultCallback() {
//
//		@Override
//		public void onPayResult(int resultCode, String signvalue,
//				String resultInfo) {
//			// TODO Auto-generated method stub
//			switch (resultCode) {
//			case IAppPay.PAY_SUCCESS:
//				// 调用 IAppPayOrderUtils 的验签方法进行支付结果验证
//				boolean payState = IAppPayOrderUtils.checkPayResult(signvalue,
//						Constant2.publicKey);
//				if (payState) {
//					if (mWvShow != null && sucUrl != null) {
//						mWvShow.loadUrl(sucUrl);
//					}
//					Toast.makeText(ShowWebViewInfoActivity.this, "支付成功",
//							Toast.LENGTH_LONG).show();
//				}
//				break;
//			case IAppPay.PAY_ING:
//				if (mWvShow != null && failUrl != null) {
//					mWvShow.loadUrl(failUrl);
//				}
//				Toast.makeText(ShowWebViewInfoActivity.this, resultInfo,
//						Toast.LENGTH_LONG).show();
//				// Toast.makeText(ShowWebViewInfoActivity.this, "成功下单",
//				// Toast.LENGTH_LONG).show();
//				break;
//			default:
//				if (mWvShow != null && failUrl != null) {
//					mWvShow.loadUrl(failUrl);
//				}
//				Toast.makeText(ShowWebViewInfoActivity.this, resultInfo,
//						Toast.LENGTH_LONG).show();
//				break;
//			}
//			Log.d("MainDemoActivity", "requestCode:" + resultCode
//					+ ",signvalue:" + signvalue + ",resultInfo:" + resultInfo);
//		}
//	};

	

	

	private String sucUrl = null;
	private String failUrl = null;

	/** 获取收银台参数 */
//	private String getTransdata(String url) {
//		// appid=3008870986
//		// &waresid=1
//		// &cporderid=C14812663968569961
//		// &price=1
//		// &currency=RMB
//		// &appuserid=7365
//		// &notifyurl=http://91lelegou.com/index.php/pay/iapppay_url/houtai/
//		String str = url
//				.substring("http://91lelegou.com/?/mobile/cart/paysubmit"
//						.length() + 1);
//		String[] arr = str.split("&");
//		String[] params = new String[arr.length];
//		for (int i = 0; i < arr.length; i++) {
//			params[i] = arr[i].split("=")[1];
//		}
//
//		// 调用 IAppPayOrderUtils getTransdata() 获取支付参数
//		IAppPayOrderUtils orderUtils = new IAppPayOrderUtils();
//		orderUtils.setAppid(params[0]);
//		orderUtils.setWaresid(Integer.parseInt(params[1]));// 传入您商户后台创建的商品编号
//		orderUtils.setCporderid(params[2]);
//		orderUtils.setAppuserid(params[5]);
//		orderUtils.setPrice(Float.parseFloat(params[3]));// 单位 元
//		// orderUtils.setWaresname("自定义名称");//开放价格名称(用户可自定义，如果不传以后台配置为准)
//		// orderUtils.setCpprivateinfo("cpprivateinfo123456");
//		orderUtils.setNotifyurl(params[6]);
//		sucUrl = params[7];
//		failUrl = params[8];
//		return orderUtils.getTransdata(Constant2.privateKey);
//	}

	private BroadcastReceiver receiver;
	private boolean isReload = false;

	@Override
	protected void onResume() {
		super.onResume();
		if (isReload && mWvShow != null) {
			mWvShow.reload();
		}
	}

	private void beginRefreshInfo() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(100);
					Message message = new Message();
					message.what = 300;
					mHandler.sendMessage(message);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
		}).start();
	}

	class SelfChromeClient extends WebChromeClient {
		public SelfChromeClient() {
		}

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			Log.i("info", "onProgressChanged--");
			if (newProgress == 100) {
				Log.i("info", "onProgressChanged--100");
				CookieManager cookieManager = CookieManager.getInstance();
				String str = cookieManager.getCookie(view.getUrl());
				Log.i("info", "Cookies CookieManager = " + str);
				try {
					mCountBuyNumber = 0;
					str = URLDecoder.decode(str, "utf-8");
					Log.i("info", "Cookies CookieManager = " + str);
					if (StringUtil.isNotEmpty(str)) {
						String[] arrays = str.split(";");
						int number = arrays != null ? arrays.length : 0;
						if (number > 0) {
							for (int i = 0; i < number; i++) {
								String[] keyPair = arrays[i].split("=");
								int allNumber = keyPair != null ? keyPair.length
										: 0;
								if (allNumber >= 2) {
									Log.i("info", "keyPair[0] = " + keyPair[0]);
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
										Log.i("info", "mCountBuyNumber = "
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			try {
				if ((mWvShow != null) && mWvShow.canGoBack()) {
					mWvShow.goBack();
					return true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	private void refreshLoadNumber() {
		String mRefreshUrl = ApiWebCommon.API_COMMON.Api_Common_Url
				+ ApiWebCommon.API_COMMON.Web_Index_Cart_Number;
		WebView mWvShow = new WebView(getApplication());
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

		mWvShow.setWebViewClient(new SelfWebViewClient2());
		mWvShow.setWebChromeClient(new SelfChromeClient());

		mWvShow.loadUrl(mRefreshUrl);
	}

	class SelfWebViewClient2 extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			if (StringUtil.isNotEmpty(url) && url.indexOf("tel:") < 0) {
				view.loadUrl(url);
			}
			return true;
		}

	}

	private void removeCookie(Context context) {
		CookieSyncManager.createInstance(context);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();
		CookieSyncManager.getInstance().sync();
	}

	// JS调用该类的方法
	public class JSCallAndroidInterface {

		@JavascriptInterface
		public void shaidan(int id) {
			ShaiDanActivity.start(ShowWebViewInfoActivity.this, id);
		}

		@JavascriptInterface
		public void qrdizhi(int shopId, int circleId) {
			MyLog.i("xiaocai", "1111 shopid="+shopId+", circleId="+circleId);
			AddressManagerActivity.start(ShowWebViewInfoActivity.this, shopId, circleId);
		}

		@JavascriptInterface
		public void showToastTel(String number) {
			String action = Intent.ACTION_DIAL;// android.intent.action.DIAL
			Intent intent = new Intent(action);
			intent.setData(Uri.parse("tel:" + number));
			startActivity(intent);
		}

		@JavascriptInterface
		public void showToastQQ(String number) {
			String url = "mqqwpa://im/chat?chat_type=wpa&uin=" + number;
			startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
		}

		@JavascriptInterface
		public String getAndroidPackageName() {
			return getPackageName();
		}

		// 传入支付回调地址
		@JavascriptInterface
		public void setPaymentCallbackTag(int tag) {
			// 1:购买商品 ,2：建圈,3:圈子中的商品
			if (tag == 1) {
				Constant.PAYMENT_CALLBACK_SUCCESS_URL = "http://91lelegou.com/?/mobile/cart/paysuccess";
				Constant.PAYMENT_CALLBACK_FAIL_URL = "http://91lelegou.com/?/mobile/cart/fail";
			} else if (tag == 2) {
				Constant.PAYMENT_CALLBACK_SUCCESS_URL = "http://91lelegou.com/?/mobile/circle/paysuccess";
				Constant.PAYMENT_CALLBACK_FAIL_URL = "http://91lelegou.com/?/mobile/circle/payfail";
			}
			// Toast.makeText(ShowWebViewInfoActivity.this, Constant.PAYMENT_CALLBACK_FAIL_URL , Toast.LENGTH_SHORT).show();
		}
	}
}
