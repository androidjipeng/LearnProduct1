package com.learn.soft.product.main.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.Toast;
import com.learn.soft.product.jni.BaseActivity;
import com.learn.soft.product.main.activity.fragment.SplashImageFragment;
import com.learn.soft.product.util.ActivityFrgManager;
import com.learn.soft.product.util.MyLog;
import com.learn.soft.product.util.StringUtil;
import com.learn.soft.product.util.ToastHelper;
import com.learn.soft.product1.R;
import com.umeng.analytics.MobclickAgent;
import com.wangyi.lelegou.maofengqi.bean.ResultBean;
import com.wangyi.lelegou.maofengqi.ui.activity.MainActivity;
import com.wangyi.lelegou.maofengqi.utils.ApiClass;
import com.wangyi.lelegou.maofengqi.utils.Constant;
import com.wangyi.lelegou.maofengqi.utils.ResultCallback;
import com.wangyi.lelegou.maofengqi.utils.SPManager;

import java.util.HashMap;
import java.util.Map;

/**
 * **********************************************************
 * <p/>
 * 说明：
 * <p/>
 * 作者：cailin
 * <p/>
 * 创建日期：2016/4/19
 * <p/>
 * 描述： **********************************************************
 */
public class SplashActivity extends BaseActivity implements
		View.OnClickListener {

	private boolean isFirstStartMain;

	protected boolean isAddToAllExit(){
		return false;
	}

	@Override
	protected void onBaseCreate(Bundle savedInstanceState) {
		MainActivity.sIsFromShareOpen=false;
		MainActivity.sShareId=null;
		Intent i_getvalue = getIntent();
		if (i_getvalue!=null){
			String action = i_getvalue.getAction();
			if(Intent.ACTION_VIEW.equals(action)){
				Uri uri = i_getvalue.getData();
				if(uri != null){
					String shareId = uri.getQueryParameter("circle_code");
					String showtabflag = uri.getQueryParameter("showtabflag");
					if("1".equals(showtabflag)){//处理跳到首页
						MyLog.e("jp","=====================>SplashActivity");
//						ToastHelper.toast("shareId="+shareId+", showtabflag="+showtabflag);
					}else{//否则继续原来的
						if (StringUtil.isNotEmpty(shareId)){
							MainActivity.sIsFromShareOpen=true;
							MainActivity.sShareId=shareId;
							ActivityFrgManager.getInstance().finishActivities();
							MyLog.i("xiaocai", "shareId="+shareId);
						}
					}
				}
			}
		}


		MobclickAgent.setDebugMode(false);
		MobclickAgent.openActivityDurationTrack(false);
		// PrefrenceUtil util=new PrefrenceUtil();
		// if (util.getFirstFlag()){
		// util.setFirstFlag(false);
		// showFirstDisplay();
		// }else{
		// }
		showDefaultShow();

	}

	private void showFirstDisplay() {
		setContentView(R.layout.activity_splash_first);
		findViewById(R.id.tv_skip_over).setOnClickListener(this);
		ViewPager vp = (ViewPager) findViewById(R.id.vp_show_splash);
		vp.setAdapter(new SplashFragmentDataAdapter(getSupportFragmentManager()));
	}

	private void updateCookie() {
		try {
			CookieSyncManager.createInstance(SplashActivity.this);

			CookieManager cookieManager = CookieManager.getInstance();
			cookieManager.setAcceptCookie(true);

			String old = cookieManager
					.getCookie("http://91lelegou.com/?/mobile/user/login");
			if (old != null && !old.equals("")) {
				String[] arr = old.split(";");
				for (int i = 0; i < arr.length; i++) {
					cookieManager.setCookie(
							"http://91lelegou.com/?/mobile/user/login",
							arr[i].trim() + ";");
				}
				CookieSyncManager.getInstance().sync();
			}

			String newCookie = cookieManager
					.getCookie("http://91lelegou.com/?/mobile/user/login");
			if (newCookie != null) {
				Log.i("info1", "newCookie:sss" + newCookie);
			}
		} catch (Exception e) {
			Log.i("info", e.toString());
		}
	}

	private void showDefaultShow() {
		final View view = View.inflate(this, R.layout.activity_splash_default,
				null);
		setContentView(view);

		if (!SPManager.isRemoveCookie()) {
			// 如果有cookie,把cookie换到另外一个地址下
			updateCookie();
			SPManager.setRemoveCookie(true);
		}

		if (!SPManager.isActivationApp()) {
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			// 添加渠道
			paramsMap.put("channel", Constant.CHANNEL);
			ApiClass.activationApp(paramsMap, callback);
		} else {
			handler.sendEmptyMessageDelayed(1, 3000);
		}

//		AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
//		aa.setDuration(100);
//		view.startAnimation(aa);
//		aa.setAnimationListener(new Animation.AnimationListener() {
//
//			@Override
//			public void onAnimationEnd(Animation arg0) {
//				try {
//					Thread.sleep(3000);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//				redirectTo();
//			}
//
//			@Override
//			public void onAnimationRepeat(Animation animation) {
//			}
//
//			@Override
//			public void onAnimationStart(Animation animation) {
//			}
//		});
	}

	private ResultCallback<String> callback = new ResultCallback<String>() {

		@Override
		public void onSuccess(ResultBean<String> resultBean, int id) {
			// dismissProgress();
			if (resultBean.getStatus() == 1) {
				SPManager.setActivationApp(true);
				handler.sendEmptyMessageDelayed(1, 2000);
			} else {
				Toast.makeText(SplashActivity.this, resultBean.getInfo(),
						Toast.LENGTH_SHORT).show();
				finish();
			}
		}

		public void onError(okhttp3.Call call, Exception e, int id) {
			super.onError(call, e, id);
			dismissProgress();
		}
	};
	
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			redirectTo();
		}
	};

	@Override
	protected void initViews() {

	}

	@Override
	protected boolean isShowTranStatus() {
		return false;
	}

	private void redirectTo() {
		if (!isFirstStartMain) {
			isFirstStartMain = true;
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			finish();
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.tv_skip_over:
			redirectTo();
			break;
		}
	}

	private class SplashFragmentDataAdapter extends FragmentStatePagerAdapter {

		public SplashFragmentDataAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			return SplashImageFragment.instance(i);
		}

		@Override
		public int getCount() {
			return 4;
		}
	}
}