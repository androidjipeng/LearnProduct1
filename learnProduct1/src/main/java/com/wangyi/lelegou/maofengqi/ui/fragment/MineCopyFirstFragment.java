package com.wangyi.lelegou.maofengqi.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.*;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.learn.soft.product.jni.JiaZhengApp;
import com.learn.soft.product1.R;
import com.wangyi.lelegou.maofengqi.base.BaseFragment;
import com.wangyi.lelegou.maofengqi.ui.activity.LoginActivity;
import com.wangyi.lelegou.maofengqi.ui.activity.MyCircleOfFriendsActivity;
import com.wangyi.lelegou.maofengqi.ui.activity.PersonalDataActivity;
import com.wangyi.lelegou.maofengqi.ui.activity.SearchProductActivity;
import com.wangyi.lelegou.maofengqi.utils.Constant;
import com.wangyi.lelegou.maofengqi.utils.Utils;

public class MineCopyFirstFragment extends BaseFragment implements OnClickListener {

	// protected ImageButton ivSearch;
	protected ImageButton ivBack;
	protected TextView tvContent;
	protected TextView tvRight;

	private WebView webView;
	private LinearLayout llNoLogin;
	private TextView tvLogin;
	private boolean isReload;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @return A new instance of fragment MineFragment.
	 */
	public static MineCopyFirstFragment newInstance() {
		MineCopyFirstFragment fragment = new MineCopyFirstFragment();
		return fragment;
	}

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_mine_first;
	}

	@Override
	protected void afterCreate(Bundle savedInstanceState) {
		// http://91lelegou.com/?/mobile/home/orderlist
		// ivSearch = (ImageButton) findViewById(R.id.iv_search);
		ivBack = (ImageButton) findViewById(R.id.iv_back);
		tvContent = (TextView) findViewById(R.id.tv_content);
		tvRight = (TextView) findViewById(R.id.tv_right);

		webView = (WebView) findViewById(R.id.web_view);
		llNoLogin = (LinearLayout) findViewById(R.id.ll_no_login);
		tvLogin = (TextView) findViewById(R.id.tv_login);

		// ivSearch.setVisibility(View.VISIBLE);
		// ivSearch.setOnClickListener(this);
		ivBack.setVisibility(View.INVISIBLE);
		tvContent.setText("我的");

		setData();
	}

	@Override
	public void onStart() {
		super.onStart();
		IntentFilter filter = new IntentFilter(Constant.ACTION_LOGN);
		getActivity().registerReceiver(receiver, filter);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (isReload) {
			setData();
		}
	}

	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(Constant.ACTION_LOGN)) {
				isReload = true;
			}
		}
	};

	public void onStop() {
		super.onStop();
		if (receiver != null) {
			getActivity().unregisterReceiver(receiver);
		}
	};

	private void setData() {
		if (JiaZhengApp.getInstance().isLogin()) {
			tvRight.setText("退出");
			tvRight.setOnClickListener(this);

			webView.setVisibility(View.VISIBLE);
			llNoLogin.setVisibility(View.GONE);

			WebSettings settings = webView.getSettings();
			// 设置页面支持JavaScript
			settings.setJavaScriptEnabled(true);

			webView.setWebViewClient(new MyWebViewClient());
			webView.setWebChromeClient(new WebChromeClient());

			showProgressInfo("");
			webView.loadUrl(Constant.MOBILE_IP + Constant.MOBILE_USER_LOGIN);
		} else {
			webView.setVisibility(View.GONE);
			llNoLogin.setVisibility(View.VISIBLE);
			tvLogin.setOnClickListener(this);
		}
	}

	private class MyWebViewClient extends WebViewClient {

		@Override
		public void onPageFinished(WebView view, String url) {
			dismissProgress();
			super.onPageFinished(view, url);
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView webView, String url) {
			if (url.indexOf(Constant.MOBILE_IP + "/mobile/MyPersonHomePage/") != -1) {
				webView.stopLoading();
				PersonalDataActivity.start(webView.getContext());
				return true;
			}
			// 我的圈友
			if (url.indexOf("http://91lelegou.com/?/mobile/home/quanyou") != -1) {
				webView.stopLoading();
				MyCircleOfFriendsActivity.start(webView.getContext());
				return true;
			}
			if (!(url.equals(Constant.MOBILE_IP + Constant.MOBILE_USER_LOGIN) || url
					.equals(Constant.MOBILE_IP + Constant.MOBILE_HOME))) {
				Utils.startWebViewShow(getActivity(), "", url);
				return true;
			}
			return super.shouldOverrideUrlLoading(webView, url);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_search:
			SearchProductActivity.start(getActivity());
			break;
		case R.id.tv_right:
			JiaZhengApp.getInstance().setLogin(false);
			JiaZhengApp.getInstance().setUserId("");
			removeCookie(getActivity().getApplication());
			setData();
			break;
		case R.id.tv_login:
			LoginActivity.start(getActivity());
			break;
		}
	}

	private void removeCookie(Context context) {
		CookieSyncManager.createInstance(context);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();
		CookieSyncManager.getInstance().sync();
	}
}