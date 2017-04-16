package com.learn.soft.product.jni;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;
import com.learn.soft.product.util.AppConfigUse;
import com.learn.soft.product.util.NetStateManager;
import com.learn.soft.product1.R;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.utils.L;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.umeng.analytics.MobclickAgent;
import com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity.bean.HomeShopInformation;
import com.wangyi.lelegou.maofengqi.utils.SPManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.cookie.store.PersistentCookieStore;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class JiaZhengApp extends BaseApp {

	// public static String notifyUrl=null;

	// 超时时间
	public static final int TIMEOUT = 1000 * 60;

	private DisplayImageOptions mDefaultoptions;
	private DisplayImageOptions mUserPicoptions;
	private static JiaZhengApp sUpApp;

	// 是否登录
	private boolean isLogin;
	// 用户Id
	private String userId;
	// 0:表示没有
	private int invitationStatus;


	//商品详情
	private HomeShopInformation bean;

	public HomeShopInformation getBean() {
		return bean;
	}

	public void setBean(HomeShopInformation bean) {
		this.bean = bean;
	}

	public static JiaZhengApp getInstance() {
		return sUpApp;
	}

	@Override
	protected void afterCreate() {
		sUpApp = (JiaZhengApp) getApp();
		NetStateManager.init(getBaseContext());

		boolean isNotDebug = true;
		L.writeDebugLogs(isNotDebug);
		L.writeLogs(isNotDebug);
		MobclickAgent.setDebugMode(isNotDebug);
		initImageLoader(getApplicationContext());

		// 初始化
//		TM_Application.on_AppInit(getApplicationContext());

		// 初始化SPManager
		SPManager.getInstance(getApplicationContext());
		isLogin = SPManager.isLogin();
		userId = SPManager.getUserId();
		invitationStatus = SPManager.getInvitationStatus();

		OkHttpClient okHttpClient = new OkHttpClient.Builder()
				.connectTimeout(TIMEOUT, TimeUnit.SECONDS)
				.readTimeout(TIMEOUT, TimeUnit.SECONDS)
				.writeTimeout(TIMEOUT, TimeUnit.SECONDS)
				.cookieJar(new CookiesManager())
				.addInterceptor(new Interceptor() {

					@Override
					public Response intercept(Chain chain) throws IOException {
						long t1 = System.nanoTime();
						Request request = chain.request();
						// Log.i("info",
						// String.format("Sending request %s on %s%n%s",
						// request.url(),
						// chain.connection(), request.headers()));
						Response response = chain.proceed(request);
						long t2 = System.nanoTime();
						Log.i("info", String.format(
								"Received response for %s in %.1fms%n%s",
								request.url(), (t2 - t1) / 1e6d,
								response.headers()));
						return response;
					}
				}).build();
		OkHttpUtils.initClient(okHttpClient);



		JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
		JPushInterface.init(this);     		// 初始化 JPush
	}

	/**
	 * 自动管理Cookies
	 */
	private class CookiesManager implements CookieJar {

		// PersistentCookieStore //持久化cookie
		// SerializableHttpCookie //持久化cookie
		// MemoryCookieStore //cookie信息存在内存中
		private final PersistentCookieStore cookieStore = new PersistentCookieStore(
				getApplicationContext());

		@Override
		public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
			Log.i("info", "============");
			if (cookies != null && cookies.size() > 0) {
				cookieStore.add(url, cookies);
				for (Cookie item : cookies) {
					Log.i("info", item.toString());
				}
			}
		}

		@Override
		public List<Cookie> loadForRequest(HttpUrl url) {
			List<Cookie> cookies = cookieStore.get(url);
			Log.i("info", "+++++++++++");
			if (cookies != null && cookies.size() > 0) {
				for (Cookie item : cookies) {
					Log.i("info", item.toString());
				}
			}
			return cookies;
		}
	}

	@Override
	protected void beforeExit() {
	}

	private void initImageLoader(Context context) {
		File cacheDir = StorageUtils.getOwnCacheDirectory(
				getApplicationContext(), AppConfigUse.PATH_PIC_CACHE);
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.diskCache(new UnlimitedDiscCache(cacheDir))
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.diskCacheSize(50 * 1024 * 1024)
				// 50 Mb
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs().build();
		ImageLoader.getInstance().init(config);
	}

	public DisplayImageOptions getDefaultImgeOptions() {
		if (mDefaultoptions == null) {
			mDefaultoptions = new DisplayImageOptions.Builder()
					.showImageOnLoading(R.drawable.ic_begin_loading)
					.showImageForEmptyUri(R.drawable.ic_no_loading_default)
					.showImageOnFail(R.drawable.ic_no_loading_default)
					.cacheInMemory(true).cacheOnDisk(true)
					.considerExifParams(true)
					.bitmapConfig(Bitmap.Config.RGB_565).build();
		}
		return mDefaultoptions;
	}

	public DisplayImageOptions getUserPicOptions() {
		if (mUserPicoptions == null) {
			mUserPicoptions = new DisplayImageOptions.Builder()
					.bitmapConfig(Bitmap.Config.RGB_565)
					.showImageOnLoading(R.drawable.ic_default_head_old)
					.showImageForEmptyUri(R.drawable.ic_default_head_old)
					.showImageOnFail(R.drawable.ic_default_head_old)
					.cacheInMemory(true).cacheOnDisk(true)
					.considerExifParams(true)
					.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
					.displayer(new RoundedBitmapDisplayer(360)).build();
		}
		return mUserPicoptions;
	}

	public boolean isLogin() {
		return isLogin;
	}

	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
		SPManager.setLogin(isLogin);
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
		SPManager.setUserId(userId);
	}

	public int getInvitationStatus() {
		return invitationStatus;
	}

	public void setInvitationStatus(int invitationStatus) {
		this.invitationStatus = invitationStatus;
		SPManager.setInvitationStatus(invitationStatus);
	}

	private String leledouNum;

	public String getLeledouNum() {
		return leledouNum;
	}

	public void setLeledouNum(String leledouNum) {
		this.leledouNum = leledouNum;
	}

	private String wxorderCode;

	public String getWxorderCode() {
		return wxorderCode;
	}

	public void setWxorderCode(String wxorderCode) {
		this.wxorderCode = wxorderCode;
	}
}