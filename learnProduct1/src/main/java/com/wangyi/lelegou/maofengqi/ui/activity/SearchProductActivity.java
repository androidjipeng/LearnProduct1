package com.wangyi.lelegou.maofengqi.ui.activity;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.learn.soft.product.main.activity.ShowWebViewInfoActivity;
import com.learn.soft.product.util.ApiWebCommon;
import com.learn.soft.product.util.BundleKey;
import com.learn.soft.product.util.MyLog;
import com.learn.soft.product.util.StringUtil;
import com.learn.soft.product1.R;
import com.wangyi.lelegou.maofengqi.base.BaseActivity;
import com.wangyi.lelegou.maofengqi.bean.HistoryRecordBean;
import com.wangyi.lelegou.maofengqi.ui.fragment.ProductListFragment;
import com.wangyi.lelegou.maofengqi.utils.SPManager;
import com.wangyi.lelegou.maofengqi.view.adapter.CommonAdapter;
import com.wangyi.lelegou.maofengqi.view.adapter.ViewHolder;

/**
 * **********************************************************
 * <p/>
 * 说明:搜索
 * <p/>
 * 作者:@Doc.March
 * <p/>
 * 创建日期:2016-9-29
 * <p/>
 * 描述:
 * <p/>
 * **********************************************************
 */
public class SearchProductActivity extends BaseActivity implements
		OnFocusChangeListener, OnEditorActionListener, TextWatcher,
		OnClickListener {

	private ImageButton ivBack;
	private EditText etSearch;
	private ImageView ivClear;
	private View mAreaBuyContent;
	private TextView mTvBuyNumber;
	private String keyword;

	private FrameLayout frameLayout;

	private LinearLayout layoutHistory;
	private RelativeLayout layoutClear;
	private TextView tvClear;
	private List<HistoryRecordBean> list;
	private ListView listView;
	private CommonAdapter<HistoryRecordBean> adapter;

	private FrameLayout flContent;
	private ProductListFragment fragment;

	// 控件是否有焦点
	private boolean hasFoucs;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_search_product;
	}

	@Override
	protected void afterCreate(Bundle savedInstanceState) {
		list = SPManager.getHistoryRecord();
		if (list == null)
			list = new ArrayList<HistoryRecordBean>();

		ivBack = (ImageButton) findViewById(R.id.iv_back);
		etSearch = (EditText) findViewById(R.id.et_search);
		ivClear = (ImageView) findViewById(R.id.iv_clear);
		mAreaBuyContent = findViewById(R.id.area_show_buy_content);
		mTvBuyNumber = (TextView) findViewById(R.id.tv_head_buy_number);

		ivBack.setOnClickListener(this);
		ivClear.setOnClickListener(this);
		mAreaBuyContent.setOnClickListener(this);

		etSearch.setHint(R.string.search_product);
		// 默认设置隐藏图标
		setClearIconVisible(false);
		etSearch.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
		// 设置焦点改变的监听
		etSearch.setOnFocusChangeListener(this);
		etSearch.setOnEditorActionListener(this);
		// 设置输入框里面内容发生改变的监听
		etSearch.addTextChangedListener(this);

		frameLayout = (FrameLayout) findViewById(R.id.layout);
		layoutHistory = (LinearLayout) findViewById(R.id.layout_history);
		layoutClear = (RelativeLayout) findViewById(R.id.layout_clear);
		tvClear = (TextView) findViewById(R.id.tv_clear);
		listView = (ListView) findViewById(R.id.list_view);
		flContent = (FrameLayout) findViewById(R.id.fl_content);

		tvClear.setOnClickListener(this);

		if (list.size() == 0) {
			layoutClear.setVisibility(View.GONE);
		} else {
			layoutClear.setVisibility(View.VISIBLE);
		}

		adapter = new CommonAdapter<HistoryRecordBean>(this, list,
				R.layout.item_history_record) {

			@Override
			public void convert(ViewHolder holder, int position,
					HistoryRecordBean item) {
				holder.setText(R.id.tv, item.getSearchContent());
			}
		};
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				HistoryRecordBean t = list.get(position);
				keyword = t.getSearchContent();

				etSearch.setText(keyword);
				etSearch.setSelection(etSearch.getText().length());

				getData();
			}
		});

		fragment = ProductListFragment.newInstance();
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		transaction.replace(R.id.fl_content, fragment);
		transaction.commit();
	}

	protected void setClearIconVisible(boolean visible) {
		ivClear.setVisibility(visible ? View.VISIBLE : View.GONE);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.iv_clear:
			etSearch.setText("");
			break;
		case R.id.area_show_buy_content:
			MyLog.e("jp","--------------->area_show_buy_content");
			StringBuffer sb = new StringBuffer();
			sb.append(ApiWebCommon.API_COMMON.Api_Common_Url);
			sb.append(ApiWebCommon.API_COMMON.Web_Index_Cart_List);
			String url = sb.toString();
			setCommonWebViewShow("", url);
			break;
		case R.id.tv_clear:
			list.clear();
			layoutClear.setVisibility(View.GONE);
			adapter.notifyDataSetChanged();
			SPManager.setHistoryRecord(list);
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
		args.putString(BundleKey.Bundle_KEY_Url, url);
		intent.putExtras(args);
		startActivity(intent);
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		this.hasFoucs = hasFocus;
		if (hasFocus) {
			//setClearIconVisible(etSearch.getText().length() > 0);
			mAreaBuyContent.setVisibility(View.GONE);
			layoutHistory.setVisibility(View.VISIBLE);
			flContent.setVisibility(View.GONE);
		} else {
			//setClearIconVisible(false);
			mAreaBuyContent.setVisibility(View.VISIBLE);
			layoutHistory.setVisibility(View.GONE);
			flContent.setVisibility(View.VISIBLE);
		}
	}

	/**
	 * 当输入框里面内容发生变化的时候回调的方法
	 */
	@Override
	public void onTextChanged(CharSequence s, int start, int count, int after) {
		//if (hasFoucs) {
			setClearIconVisible(s.length() > 0);
		//}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void afterTextChanged(Editable s) {

	}

	@Override
	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		if (actionId == EditorInfo.IME_ACTION_SEARCH) {
			keyword = etSearch.getText().toString();
			if (keyword.equals("")) {
				Toast.makeText(SearchProductActivity.this, "请输入关键字",
						Toast.LENGTH_SHORT).show();
			} else {
				if (list.size() > 0) {
					layoutClear.setVisibility(View.GONE);
				} else {
					layoutClear.setVisibility(View.VISIBLE);
				}
				getData();
			}
			return true;
		}
		return false;
	}

	private void getData() {
		// 隐藏键盘
		((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
				.hideSoftInputFromWindow(mActivity.getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

		frameLayout.setFocusableInTouchMode(true);
		frameLayout.requestFocus();

		int position = -1;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getSearchContent().equals(keyword)) {
				position = i;
				break;
			}
		}

		if (position != -1) {
			list.remove(position);
		}

		list.add(0, new HistoryRecordBean(keyword));
		adapter.notifyDataSetChanged();
		SPManager.setHistoryRecord(list);

		fragment.updateData(keyword);
	}

	public static void start(Context context) {
		Intent intent = new Intent(context, SearchProductActivity.class);
		context.startActivity(intent);
	}

	// ----
	@Override
	protected void onResume() {
		super.onResume();
		refreshShowUI();
	}

	public void refreshShowUI() {
		beginRefreshData();
	}

	private String mRefreshUrl;
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
				mTvBuyNumber.setText(String.valueOf(mCountBuyNumber));
				if (mCountBuyNumber > 0) {
					mTvBuyNumber.setVisibility(View.VISIBLE);
				} else {
					mTvBuyNumber.setVisibility(View.GONE);
				}
			}
		}
	};
}