package com.wangyi.lelegou.maofengqi.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.learn.soft.product.jni.JiaZhengApp;
import com.learn.soft.product.main.activity.ShowWebViewInfoActivity;
import com.learn.soft.product.util.BundleKey;
import com.learn.soft.product.util.MyLog;
import com.learn.soft.product.util.ToastHelper;
import com.learn.soft.product1.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wangyi.lelegou.maofengqi.base.BaseFragment;
import com.wangyi.lelegou.maofengqi.bean.ProductBean;
import com.wangyi.lelegou.maofengqi.bean.ResultBean;
import com.wangyi.lelegou.maofengqi.bean.SearchProductBean;
import com.wangyi.lelegou.maofengqi.ui.activity.SearchProductActivity;
import com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity.Config;
import com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity.bean.AddCartInformation;
import com.wangyi.lelegou.maofengqi.utils.ApiClass;
import com.wangyi.lelegou.maofengqi.utils.Constant;
import com.wangyi.lelegou.maofengqi.utils.ResultCallback;
import com.wangyi.lelegou.maofengqi.view.adapter.RecyclerViewAdapter;
import com.wangyi.lelegou.maofengqi.view.adapter.RecyclerViewAdapter.OnItemClickListener;
import com.wangyi.lelegou.maofengqi.view.adapter.ViewHolder;
import com.wangyi.lelegou.maofengqi.view.recyclerview.DividerItemDecoration;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

/**
 * **********************************************************
 * <p/>
 * 说明:商品列表
 * <p/>
 * 作者:@Doc.March
 * <p/>
 * 创建日期:2016-9-29
 * <p/>
 * 描述:
 * <p/>
 * **********************************************************
 */
public class ProductListFragment extends BaseFragment implements
		OnClickListener {

	private TextView tvCount;
	private RecyclerView recyclerView;
	private List<ProductBean> list = new ArrayList<ProductBean>();
	private RecyclerViewAdapter<ProductBean> adapter;

	private int pageIndex = 1;
	private int pageSize = -1;

	private DisplayImageOptions options = JiaZhengApp.getInstance()
			.getDefaultImgeOptions();

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_product_list;
	}

	@Override
	protected void afterCreate(Bundle savedInstanceState) {
		tvCount = (TextView) findViewById(R.id.tv_count);
		recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
		// 设置布局管理器
		recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
		// 如果可以确定每个item的高度是固定的,设置这个选项可以提高性能
		recyclerView.setHasFixedSize(true);
		adapter = new RecyclerViewAdapter<ProductBean>(mActivity, list,
				R.layout.item_product) {

			@Override
			public void convert(ViewHolder holder, int position,
					ProductBean item) {
				ImageView ivPic = holder
						.getViewById(R.id.iv_product_info_item_pic);
				ImageLoader.getInstance().displayImage(item.getThumb(), ivPic,
						options);

				TextView tvTitle = holder
						.getViewById(R.id.tv_product_info_item_title);
				tvTitle.setText(Html.fromHtml(item.getTitle()));

				holder.setText(R.id.tv_product_info_item_all,
						item.getZongrenshu());
				holder.setText(R.id.tv_product_info_item_join,
						item.getCanyurenshu());
				String release = String.valueOf(Integer.parseInt(item
						.getZongrenshu())
						- Integer.parseInt(item.getCanyurenshu()));
				holder.setText(R.id.tv_product_info_item_release, release);

				ProgressBar pbShow = holder
						.getViewById(R.id.pb_product_info_item_bar);

				int progress = 0;
				try {
					progress = Integer.parseInt(item.getCanyurenshu()) * 100
							/ Integer.parseInt(item.getZongrenshu());
				} catch (Exception e) {
					e.printStackTrace();
				}
				pbShow.setProgress(progress);

				View itemView = holder.getViewById(R.id.area_product_info_item);
				itemView.setTag(item);
				itemView.setOnClickListener(ProductListFragment.this);
				ImageView ivAdd = holder
						.getViewById(R.id.iv_product_info_item_add);
				ivAdd.setTag(item);
				ivAdd.setOnClickListener(ProductListFragment.this);
			}
		};
		// 设置adapter
		recyclerView.setAdapter(adapter);
		// 设置Item增加、移除动画
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		// 添加分割线
		recyclerView.addItemDecoration(new DividerItemDecoration(mActivity,
				DividerItemDecoration.VERTICAL_LIST));
		adapter.setOnItemClickListener(new OnItemClickListener<ProductBean>() {

			@Override
			public void onItemClick(View view, int position, ProductBean t) {
				Toast.makeText(mActivity, "", Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public void onClick(View v) {// 需要一个id
		ProductBean bean;
		StringBuffer sb;
		switch (v.getId()) {
		case R.id.area_product_info_item:
			bean = (ProductBean) v.getTag();
			String title = "商品信息";
			sb = new StringBuffer();
			sb.append(Constant.MOBILE_IP);
			sb.append(String.format(Constant.Web_Index_Product_Advice,
					bean.getId()));
			setCommonWebViewShow(title, sb.toString());
			break;
		case R.id.iv_product_info_item_add:
			bean = (ProductBean) v.getTag();

			OkHttpUtils.post()
					.url(Config.testurl+Config.addShopingCart)
					.addParams("uid",JiaZhengApp.getInstance().getUserId())
					.addParams("shopid",bean.getId())
					.addParams("num","1")
					.addParams("type","1")
					.build().execute(new StringCallback() {
				@Override
				public void onError(Call call, Exception e, int i) {
//                dismissProgress();
					MyLog.e("jp","AddCart====>onError:"+e.getMessage());
				}

				@Override
				public void onResponse(String s, int i) {
//              dismissProgress();
					MyLog.e("jp","AddCart====>onResponseon:"+s);
					Gson gson=new Gson();
					AddCartInformation addCartInformation = gson.fromJson(s, AddCartInformation.class);
					ToastHelper.toast(addCartInformation.getInfo());
				}
			});

             /**======================================h5*/
//			bean = (ProductBean) v.getTag();
//			sb = new StringBuffer();
//			sb.append(Constant.MOBILE_IP);
//			sb.append(String.format(Constant.Api_Add_Cart_Shop, bean.getId()));
//			beginLoadData(sb.toString());
			break;
		}
	}

	public void setCommonWebViewShow(String title, String url) {
		Intent intent = new Intent(getActivity(), ShowWebViewInfoActivity.class);
		Bundle args = new Bundle();
		args.putString(BundleKey.Bundle_KEY_Title, title);
		MyLog.i("xiaocai", "url=" + url);
		args.putString(BundleKey.Bundle_KEY_Url, url);
		intent.putExtras(args);
		startActivity(intent);
	}

	private WebView mWvShow = null;

	@SuppressWarnings("deprecation")
	@SuppressLint("SetJavaScriptEnabled")
	private void beginLoadData(String url) {
		// showProgressInfo("");
		if (mWvShow == null) {
			mWvShow = new WebView(getActivity());
			mWvShow.getSettings().setJavaScriptEnabled(true);// 设置页面支持Javascript
			mWvShow.getSettings().setLoadWithOverviewMode(true);
			mWvShow.getSettings().setSupportZoom(true);// 支持缩放
			// mWvShow.getSettings().setBuiltInZoomControls(true); // 显示放大缩小
			mWvShow.getSettings().setDefaultTextEncodingName("utf-8");
			mWvShow.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);// 解决缓存问题

			mWvShow.getSettings().setUseWideViewPort(true);
			mWvShow.getSettings().setLoadWithOverviewMode(true);
			mWvShow.getSettings().setSavePassword(true);
			mWvShow.getSettings().setSaveFormData(true);
			// enable Web Storage: localStorage, sessionStorage
			mWvShow.getSettings().setDomStorageEnabled(true);

			mWvShow.setWebViewClient(new SelfWebViewClient());
			mWvShow.setWebChromeClient(new SelfChromeClient());
		}
		mWvShow.loadUrl(url);
	}

	// 创建ProductListFragment
	public static ProductListFragment newInstance() {
		Bundle bundle = new Bundle();
		ProductListFragment fragment = new ProductListFragment();
		fragment.setArguments(bundle);
		return fragment;
	}

	// 更新ProductListFragment数据
	public void updateData(String keyword) {
		showProgressInfo(null);
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("keyword", keyword);
		paramsMap.put("pageIndex", pageIndex);
		paramsMap.put("pageSize", pageSize);
		ApiClass.searchProduct(paramsMap, callback);
	}

	// 搜索结果回调
	private ResultCallback<SearchProductBean> callback = new ResultCallback<SearchProductBean>() {

		@Override
		public void onSuccess(ResultBean<SearchProductBean> resultBean, int id) {
			dismissProgress();
			if (resultBean.getStatus() == 1) {
				tvCount.setText("共" + resultBean.getJsonMsg().getCount()
						+ "件商品");

				if (list.size() != 0)
					adapter.notifyItemRangeRemoved(0, list.size());

				list.clear();
				if (resultBean.getJsonMsg() != null
						&& resultBean.getJsonMsg().getList().size() > 0) {
					list.addAll(resultBean.getJsonMsg().getList());
				}

				if (list.size() != 0)
					adapter.notifyItemRangeInserted(0, list.size());
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
			if (mHandler != null) {
				mHandler.sendEmptyMessage(newProgress);
			}
			super.onProgressChanged(view, newProgress);
		}

	}

	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == 100) {
				dismissProgress();
				refreshShow();
				ToastHelper.toast("添加成功");
			}
		}
	};

	private void refreshShow() {
		if (getActivity() instanceof SearchProductActivity) {
			SearchProductActivity activity = (SearchProductActivity) getActivity();
			activity.refreshShowUI();
		}
	}
}