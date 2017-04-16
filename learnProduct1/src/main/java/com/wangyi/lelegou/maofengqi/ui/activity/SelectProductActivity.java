package com.wangyi.lelegou.maofengqi.ui.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.learn.soft.product.bean.ProductTypeBean;
import com.learn.soft.product.bean.ProductTypeChildBean;
import com.learn.soft.product.util.LoadDataType;
import com.learn.soft.product.util.NetStateManager;
import com.learn.soft.product.util.StringUtil;
import com.learn.soft.product.util.ToastHelper;
import com.learn.soft.product.widget.pulltorefresh.PullToRefreshBase;
import com.learn.soft.product.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.learn.soft.product.widget.pulltorefresh.PullToRefreshListView;
import com.learn.soft.product1.R;
import com.wangyi.lelegou.maofengqi.base.BaseActivity;
import com.wangyi.lelegou.maofengqi.bean.ResultBean;
import com.wangyi.lelegou.maofengqi.bean.SelectProductBean;
import com.wangyi.lelegou.maofengqi.bean.SelectProductsBean;
import com.wangyi.lelegou.maofengqi.ui.adapter.SelectProductAdapter;
import com.wangyi.lelegou.maofengqi.ui.fragment.ShowArrayTypeChoiceDlg2;
import com.wangyi.lelegou.maofengqi.utils.ApiClass;
import com.wangyi.lelegou.maofengqi.utils.OldResultCallback;
import com.wangyi.lelegou.maofengqi.utils.ResultCallback;

/**
 * 选择宝贝
 * 
 * @author Doc.March
 * 
 */
public class SelectProductActivity extends BaseActivity implements
		OnClickListener, PullToRefreshBase.OnRefreshListener2<ListView> {

	private LinearLayout llSelectCate;
	private ImageView iv;

	private PullToRefreshListView plvCommonPulltofresh;
	private ListView listView;
	private List<SelectProductBean> list = new ArrayList<SelectProductBean>();
	private SelectProductAdapter adapter;

	private LoadDataType mLoadDataType = LoadDataType.FirstLoad;
	private int cateId = 0;
	private int pageIndex = 1;
	private int pageCount;

	private List<ProductTypeChildBean> listType = new ArrayList<ProductTypeChildBean>();

	@Override
	protected int getLayoutId() {
		return R.layout.activity_select_product;
	}

	@Override
	protected void afterCreate(Bundle savedInstanceState) {
		llSelectCate = (LinearLayout) findViewById(R.id.ll_select_cate);
		iv = (ImageView) findViewById(R.id.iv);
		tvContent.setText("全部商品");
		llSelectCate.setOnClickListener(this);

		plvCommonPulltofresh = (PullToRefreshListView) findViewById(R.id.plv_common_pulltofresh);
		plvCommonPulltofresh.setMode(Mode.PULL_FROM_START);
		plvCommonPulltofresh.setOnRefreshListener(this);
		plvCommonPulltofresh.setEmptyView(LayoutInflater.from(this).inflate(
				R.layout.common_empty_show, null));
		listView = plvCommonPulltofresh.getRefreshableView();

		mLoadDataType = LoadDataType.FirstLoad;
		pageIndex = 1;

		getData();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_select_cate:
			if (mFindDlg == null) {
				getProductType();
			} else {
				showDlgChoice();
			}
			break;
		}
	}

	private void getProductType() {
		showProgressInfo("");
		ApiClass.getProductCategory(null, new OldResultCallback() {
			@Override
			public void onResponse(String response, int id) {
				dismissProgress();
				if (StringUtil.isNotEmpty(response)) {
					try {
						ProductTypeBean bean = new Gson().fromJson(response,
								new TypeToken<ProductTypeBean>() {
								}.getType());
						if (bean != null && bean.status == 1) {
							listType.clear();
							int number = bean.listType != null ? bean.listType
									.size() : 0;
							if (number > 0) {
								typeAddFirst();
								listType.addAll(bean.listType);
							}
						}
						showDlgChoice();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			@Override
			public void onError(Call call, Exception e, int id) {
				dismissProgress();
			}
		});
	}

	private void typeAddFirst() {
		ProductTypeChildBean b = new ProductTypeChildBean();
		b.cateid = "0";
		b.name = "全部商品";
		listType.add(b);
	}

	private ShowArrayTypeChoiceDlg2 mFindDlg;
	public final String TAG_SHOW_FIND_DLG = "TAG_SHOW_FIND_DLG";
	private int mPosition;

	private void showDlgChoice() {
		mFindDlg = new ShowArrayTypeChoiceDlg2();
		mFindDlg.registerTVInfoListener(new ShowArrayTypeChoiceDlg2.ResetTVShowInfo() {
			@Override
			public void refreshTVInfo(String s, int position) {
				mPosition = position;

				tvContent.setText(listType.get(mPosition).name);
				iv.setImageResource(R.drawable.ic_select_product_normal);

				cateId = Integer.parseInt(listType.get(mPosition).cateid);

				mLoadDataType = LoadDataType.FirstLoad;
				pageIndex = 1;

				getData();
			}

			@Override
			public void cancel(DialogInterface dialog) {
				iv.setImageResource(R.drawable.ic_select_product_normal);
			}
		});
		getSupportFragmentManager().beginTransaction().add(mFindDlg,
				TAG_SHOW_FIND_DLG);
		if (!mFindDlg.isVisible()) {
			iv.setImageResource(R.drawable.ic_select_product_checked);
			ArrayList<String> listArr = new ArrayList<String>();
			for (int i = 0; i < listType.size(); i++) {
				listArr.add(listType.get(i).name);
			}

			Bundle args = new Bundle();
			args.putString(ShowArrayTypeChoiceDlg2.BUNDLE_KEY_TITLE, "请选择商品分类");
			args.putString(ShowArrayTypeChoiceDlg2.BUNDLE_KEY_SELECT_ITEM,
					listType.get(mPosition).name);// mSelectTypeId
			args.putSerializable(ShowArrayTypeChoiceDlg2.BUNDLE_KEY_ALL_ITEM,
					listArr);
			mFindDlg.setArguments(args);
			mFindDlg.show(getSupportFragmentManager(), TAG_SHOW_FIND_DLG);
		} else {
			iv.setImageResource(R.drawable.ic_select_product_normal);
			mFindDlg.dismiss();
		}
	}

	private void getData() {
		showProgressInfo("");
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("cateId", cateId);
		paramsMap.put("pageIndex", pageIndex);
		paramsMap.put("pageSize", 10);
		ApiClass.selectList(paramsMap, callback);
	}

	private ResultCallback<String> callback = new ResultCallback<String>() {

		@Override
		public void onSuccess(ResultBean<String> resultBean, int id) {
			dismissProgress();
			if (resultBean.getStatus() != 1) {
				Toast.makeText(mActivity, resultBean.getInfo(),
						Toast.LENGTH_SHORT).show();
				return;
			}

			try {
				SelectProductsBean bean = new Gson().fromJson(
						resultBean.getJsonMsg(),
						new TypeToken<SelectProductsBean>() {
						}.getType());
				if (mLoadDataType == LoadDataType.FirstLoad
						|| mLoadDataType == LoadDataType.RefreshLoad) {
					list.clear();
				}
				if (bean != null) {
					pageCount = bean.getPageCount();
					if (pageIndex >= pageCount) {
						plvCommonPulltofresh.setMode(Mode.PULL_FROM_START);
					} else {
						plvCommonPulltofresh.setMode(Mode.BOTH);
					}

					int number = bean.getProducts() != null ? bean
							.getProducts().size() : 0;
					if (number > 0) {
						list.addAll(bean.getProducts());
					}
				}
				setAdapter();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (mLoadDataType != LoadDataType.FirstLoad) {
				stopLoadingRefreshState();
			}
		}

		public void onError(okhttp3.Call call, Exception e, int id) {
			super.onError(call, e, id);
			dismissProgress();
		}
	};

	private void setAdapter() {
		if (adapter == null) {
			adapter = new SelectProductAdapter(mActivity, list,
					R.layout.item_select_product);
			listView.setAdapter(adapter);
		} else {
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (!NetStateManager.OnNet()) {
			stopLoadingRefreshState();
			ToastHelper.toast(R.string.no_network_tip);
			return;
		}

		String label = DateUtils.formatDateTime(mActivity,
				System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
						| DateUtils.FORMAT_SHOW_DATE
						| DateUtils.FORMAT_ABBREV_ALL);
		refreshView.getLoadingLayoutProxy()
				.setLastUpdatedLabel("最后更新：" + label);

		mLoadDataType = LoadDataType.RefreshLoad;
		pageIndex = 1;
		getData();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		if (!NetStateManager.OnNet()) {
			stopLoadingRefreshState();
			ToastHelper.toast(R.string.no_network_tip);
			return;
		}

		mLoadDataType = LoadDataType.MoreLoad;
		pageIndex++;
		getData();
	}

	private void stopLoadingRefreshState() {
		if (plvCommonPulltofresh != null) {
			plvCommonPulltofresh.postDelayed(new Runnable() {
				@Override
				public void run() {
					plvCommonPulltofresh.onRefreshComplete();
				}
			}, 100);
		}
	}

	// 启动SelectProductActivity
	public static void start(Activity activity, int requestCode) {
		Intent intent = new Intent(activity, SelectProductActivity.class);
		activity.startActivityForResult(intent, requestCode);
	}
}