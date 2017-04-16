package com.wangyi.lelegou.maofengqi.ui.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.learn.soft.product.bean.IndexBean;
import com.learn.soft.product.bean.IndexBeanAd;
import com.learn.soft.product.bean.IndexBeanAdvice;
import com.learn.soft.product.jni.JiaZhengApp;
import com.learn.soft.product.util.MyLog;
import com.learn.soft.product.util.NetStateManager;
import com.learn.soft.product.util.StringUtil;
import com.learn.soft.product.util.ToastHelper;
import com.learn.soft.product.widget.MyGridView;
import com.learn.soft.product.widget.adapter.AdImagePagerAdapter;
import com.learn.soft.product.widget.adview.AdViewFlow;
import com.learn.soft.product.widget.adview.CircleFlowIndicator;
import com.learn.soft.product.widget.pulltorefresh.PullToRefreshBase;
import com.learn.soft.product.widget.pulltorefresh.PullToRefreshScrollView;
import com.learn.soft.product1.R;
import com.wangyi.lelegou.maofengqi.base.BaseFragment;
import com.wangyi.lelegou.maofengqi.bean.NoticeBean;
import com.wangyi.lelegou.maofengqi.bean.ResultBean;
import com.wangyi.lelegou.maofengqi.ui.activity.*;
import com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity.Config;
import com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity.ShopDetailActivity;
import com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity.bean.HomeShopInformation;
import com.wangyi.lelegou.maofengqi.ui.adapter.HomeProductAdapter;
import com.wangyi.lelegou.maofengqi.utils.*;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends BaseFragment implements OnClickListener,
		PullToRefreshBase.OnRefreshListener2<ScrollView> {

	protected ImageButton ivSearch;
	protected ImageButton ivBack;
	protected TextView tvContent;
	protected ImageButton iv;

	private PullToRefreshScrollView refreshView;
	private AdViewFlow adViewFlow;
	private CircleFlowIndicator circleFlowIndicator;
	private AdImagePagerAdapter mAdPagerAdapter;

	private TextView tvAllProduct, tvCircle, tvNew, tvShaidan;

	private LinearLayout layoutNotice;
	private TextView tvName, tvTime, tvProduct;
	private List<NoticeBean> list;

	public int getNoticePosition() {
		return noticePosition;
	}

	public void setNoticePosition(int noticePosition) {
		this.noticePosition = noticePosition;
	}

	private int noticePosition = 0;

	private LinearLayout llContent;
	private RelativeLayout rlHot;
	private MyGridView mgvHot;
	private RelativeLayout rlTwo;
	private MyGridView mgvTwo;
	private RelativeLayout rlThree;
	private MyGridView mgvThree;

	private int cateId2 = 52;
	private String cateName2 = "二人专区";
	private int cateId3 = 53;
	private String cateName3 = "三人专区";

	private Dialog dialog;

	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @return A new instance of fragment HomeFragment.
	 */
	public static HomeFragment newInstance() {
		HomeFragment fragment = new HomeFragment();
		return fragment;
	}

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_home;
	}

	@Override
	protected void afterCreate(Bundle savedInstanceState) {
		ivSearch = (ImageButton) findViewById(R.id.iv_search);
		ivBack = (ImageButton) findViewById(R.id.iv_back);
		tvContent = (TextView) findViewById(R.id.tv_content);
		iv = (ImageButton) findViewById(R.id.iv_create_new_circle);
//        iv.setVisibility(View.GONE);
		refreshView = (PullToRefreshScrollView) findViewById(R.id.refresh_view);
		adViewFlow = (AdViewFlow) findViewById(R.id.ad_view_flow);
		circleFlowIndicator = (CircleFlowIndicator) findViewById(R.id.circle_flow_indicator);

		tvAllProduct = (TextView) findViewById(R.id.tv_all_product);
		tvCircle = (TextView) findViewById(R.id.tv_circle);
		tvNew = (TextView) findViewById(R.id.tv_new);
		tvShaidan = (TextView) findViewById(R.id.tv_shaidan);

		layoutNotice = (LinearLayout) findViewById(R.id.layout_notice);
		tvName = (TextView) findViewById(R.id.tv_name);
		tvTime = (TextView) findViewById(R.id.tv_time);
		tvProduct = (TextView) findViewById(R.id.tv_product);

		llContent = (LinearLayout) findViewById(R.id.ll_content);
		rlHot = (RelativeLayout) findViewById(R.id.rl_hot);
		mgvHot = (MyGridView) findViewById(R.id.mgv_hot);
		rlTwo = (RelativeLayout) findViewById(R.id.rl_two);
		mgvTwo = (MyGridView) findViewById(R.id.mgv_two);
		rlThree = (RelativeLayout) findViewById(R.id.rl_three);
		mgvThree = (MyGridView) findViewById(R.id.mgv_three);

		ivSearch.setVisibility(View.VISIBLE);
		ivBack.setVisibility(View.GONE);
		tvContent.setText("首页");
		ivSearch.setOnClickListener(this);

		refreshView.setOnRefreshListener(this);
		ScrollView sv = refreshView.getRefreshableView();
		adViewFlow.setScrollView(sv);

		tvAllProduct.setOnClickListener(this);
		tvCircle.setOnClickListener(this);
		tvNew.setOnClickListener(this);
		tvShaidan.setOnClickListener(this);

		layoutNotice.setOnClickListener(this);

		rlHot.setOnClickListener(this);
		rlTwo.setOnClickListener(this);
		rlThree.setOnClickListener(this);

		setData();

		getIndexData();
	}

	@Override
	public void onResume() {
		super.onResume();
		setData();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_search:
			/**搜索*/
			MyLog.e("jipeng","===iv_search");
			SearchProductActivity.start(getActivity());
			break;
		case R.id.iv_create_new_circle:
			MyLog.e("jipeng","===iv_create_new_circle");
			show();
			break;
		case R.id.tv_all_product:
			/**所有商品*/
			MyLog.e("jipeng","===tv_all_product");
			AllProductActivity.start(getActivity(), 0, 0, "");
			break;
		case R.id.tv_circle:
			/**圈中宝*/
			MyLog.e("jipeng","===tv_circle");
			if (getActivity() instanceof MainActivity) {
				MainActivity activity = (MainActivity) getActivity();
				activity.changeOption(1);
			}
			break;
		case R.id.tv_new:
			/**最新揭晓*/
			MyLog.e("jipeng","===tv_new");
			RecentAnnouncedActivity.start(getActivity());
			break;
		case R.id.tv_shaidan:
			/**晒单*/
			MyLog.e("jipeng","===tv_shaidan");
			ShareOrderActivity.start(getActivity());
			break;
		case R.id.layout_notice:
			/**中奖通知*/
			MyLog.e("jipeng","===layout_notice");
			//// TODO: 2017/3/16 通知那边进入商品详情 
			if (list != null && list.size() > 0) {

				OkHttpUtils.post().url(Config.testurl+Config.queryShopDetail)
						.addParams("itemId",list.get(getNoticePosition()-1).getShopid())
						.build()
						.execute(new StringCallback() {
							@Override
							public void onError(Call call, Exception e, int id) {
								Log.e("jp","订单详情==onError:"+e.getMessage());
							}

							@Override
							public void onResponse(String response, int id) {
								Log.e("jp","CircleAdapter2---->订单详情==onResponse:"+response);
								Gson gson=new Gson();
								HomeShopInformation homeShopInformation = gson.fromJson(response, HomeShopInformation.class);
								JiaZhengApp.getInstance().setBean(homeShopInformation);
								Intent intent=new Intent(getActivity(), ShopDetailActivity.class);
								startActivity(intent);
							}
						});


//				String title = "商品信息";
//				StringBuffer sb = new StringBuffer();
//				sb.append(Constant.MOBILE_IP);
//				sb.append(String.format(Constant.Web_Index_Product_Advice, list
//						.get(noticePosition).getShopid()));
//				Utils.startWebViewShow(getActivity(), title, sb.toString());
			}
			break;
		case R.id.rl_hot:
			/**热门推荐*/
			MyLog.e("jipeng","===rl_hot");
			AllProductActivity.start(getActivity(), 2, 0, "");
			break;
		case R.id.rl_two:
			/**两人专区*/
			MyLog.e("jipeng","===rl_two");
			AllProductActivity.start(getActivity(), 0, cateId2, cateName2);
			break;
		case R.id.rl_three:
			/**三人专区*/
			MyLog.e("jipeng","===rl_three");
			AllProductActivity.start(getActivity(), 0, cateId3, cateName3);
			break;
		case R.id.iv_close:
			MyLog.e("jipeng","===iv_close");
			if (dialog != null) {
				dialog.dismiss();
			}
			break;
		}
	}

	private void setData() {
		if (JiaZhengApp.getInstance().isLogin()) {
			// 1:表示没有
			if (JiaZhengApp.getInstance().getInvitationStatus() == 1) {
				iv.setVisibility(View.VISIBLE);
				iv.setImageResource(R.drawable.novice_packs);
				iv.setOnClickListener(this);
				iv.setVisibility(View.GONE);
			}
		}
	}

	private void show() {
		// 加载布局文件
		View view = LayoutInflater.from(getActivity()).inflate(
				R.layout.layout_novice_packs, null);
		ImageView ivClose = (ImageView) view.findViewById(R.id.iv_close);
		final EditText etInvitationCode = (EditText) view
				.findViewById(R.id.et_invitation_code);
		TextView tvReceive = (TextView) view.findViewById(R.id.tv_receive);

		dialog = new Dialog(getActivity(), R.style.coupon);
		dialog.setContentView(view);
		dialog.setCancelable(false);

		ivClose.setOnClickListener(this);
		tvReceive.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String invitationCode = etInvitationCode.getText().toString()
						.trim();
				if (invitationCode.equals("")) {
					Toast.makeText(getActivity(), "请输入邀请码", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				novicePacks(invitationCode);
			}
		});
		dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN
						&& KeyEvent.KEYCODE_BACK == keyCode) {
					if (dialog != null) {
						dialog.dismiss();
					}
					return true;
				}
				return false;
			}
		});

		dialog.show();
	}

	private void novicePacks(String invitationCode) {
		showProgressInfo(null);
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("id", JiaZhengApp.getInstance().getUserId());
		paramsMap.put("invitationCode", invitationCode);
		ApiClass.novicePacks(paramsMap, new ResultCallback<String>() {

			@Override
			public void onSuccess(ResultBean<String> resultBean, int id) {
				dismissProgress();
				Toast.makeText(getActivity(), resultBean.getInfo(),
						Toast.LENGTH_SHORT).show();
				if (resultBean.getStatus() == 1) {
					if (dialog != null) {
						dialog.dismiss();
					}
					JiaZhengApp.getInstance().setInvitationStatus(0);
					iv.setVisibility(View.GONE);
				}
			}

			@Override
			public void onError(Call call, Exception e, int id) {
				dismissProgress();
				ToastHelper.toast(e.getMessage());
			}
		});
	}

	private void getIndexData() {
		handler.removeMessages(0);
		showProgressInfo("");
		ApiClass.getAdvertisementInfo(null, advertisementInfoCallback);
		ApiClass.getIndex(null, indexCallback);
	}

	// 广告消息
	private ResultCallback<String> advertisementInfoCallback = new ResultCallback<String>() {

		@Override
		public void onSuccess(ResultBean<String> resultBean, int id) {
			if (resultBean.getStatus() == 1) {
				list = new Gson().fromJson(resultBean.getJsonMsg(),
						new TypeToken<List<NoticeBean>>() {
						}.getType());
                setNoticePosition(0);
				handler.sendEmptyMessage(0);
			} else {
				Toast.makeText(getActivity(), resultBean.getInfo(),
						Toast.LENGTH_SHORT).show();
			}
		}
	};

	/**定时去获得中奖人信息*/

	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case 0:
					if (list != null && list.size() > 0) {
						NoticeBean bean = list.get(getNoticePosition());
//						MyLog.e("jp","=============================================开始");
//						MyLog.e("jp","---->getNoticePosition():"+getNoticePosition());
//						MyLog.e("jp","---->list.size:"+list.size());
//						MyLog.e("jp","---->bean.toString():"+bean.toString());
//						MyLog.e("jp","---->bean.getNickName():"+bean.getNickName());
//						MyLog.e("jp","---->bean.getTime():"+bean.getTime());
//						MyLog.e("jp","---->bean.getProductName():"+bean.getProductName());
//						MyLog.e("jp","--->list.get(getNoticePosition()).getShopid():"+"http://101.201.115.226:8082/yyg_app/queryShopDetail/"+list.get(getNoticePosition()).getShopid());
//						MyLog.e("jp","=============================================结束");
						tvName.setText(bean.getNickName());
						tvTime.setText(bean.getTime() + "前获得");
						tvProduct.setText(bean.getProductName());
						if (getNoticePosition() == list.size() - 1) {

							setNoticePosition(0);
						} else {
							int count=noticePosition + 1;
							setNoticePosition(count);

						}
						handler.sendEmptyMessageDelayed(0, 5000);
					}
					break;
				default:
					break;
			}
		}
	};




	// 数据
	private OldResultCallback indexCallback = new OldResultCallback() {

		@Override
		public void onResponse(String response, int id) {
			dismissProgress();
			if (StringUtil.isNotEmpty(response)) {
				showAdapter(response);
			}
			stopLoadingRefreshState();
		}

		@Override
		public void onError(Call call, Exception e, int id) {
			dismissProgress();
			ToastHelper.toast(e.getMessage());
			stopLoadingRefreshState();
		}
	};



	private void showAdapter(String response) {
		try {
            MyLog.i("xiaocai", "data="+response);
			IndexBean mData = new Gson().fromJson(response,
					new TypeToken<IndexBean>() {
					}.getType());
			if (mData != null && mData.status == 1) {
				// 首页的广告
				int number = mData.listAd != null ? mData.listAd.size() : 0;
				if (number > 0) {
					showAdDataAdapter(mData.listAd);
				}
				// 热门推荐
				showAdviceAdapter(mData.listAdvice);
				// 两元区
				showLiangYuanAdapter(mData.liangYuanBeans);
				// 三元区
				showSanYuanpter(mData.sanYuanBeans);
				llContent.setVisibility(View.VISIBLE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    /**广告 轮播图*/
	private void showAdDataAdapter(List<IndexBeanAd> mListAd) {
		int number = mListAd != null ? mListAd.size() : 0;
		if (number > 0) {
			mAdPagerAdapter = new AdImagePagerAdapter(getActivity(), mListAd)
					.setInfiniteLoop(true);
			adViewFlow.setAdapter(mAdPagerAdapter);
			adViewFlow.setmSideBuffer(number);
			if (number > 1) {
				circleFlowIndicator.setVisibility(View.VISIBLE);
				adViewFlow.setFlowIndicator(circleFlowIndicator);
			}
			adViewFlow.setTimeSpan(4500);
			adViewFlow.setSelection(number * 1000); // 设置初始位置
			adViewFlow.startAutoFlowTimer(); // 启动自动播放
			circleFlowIndicator.postInvalidate();
		}
	}

	/**推荐专区的adapter*/
	private void showAdviceAdapter(List<IndexBeanAdvice> listAdvice) {
		HomeProductAdapter adapter = new HomeProductAdapter(getActivity(),
				listAdvice, R.layout.activity_main_tab_indext_item_layout);
		mgvHot.setAdapter(adapter);
	}
    /**两人专区*/
	private void showLiangYuanAdapter(List<IndexBeanAdvice> liangYuanBeans) {
		HomeProductAdapter adapter = new HomeProductAdapter(getActivity(),
				liangYuanBeans, R.layout.activity_main_tab_indext_item_layout);
		mgvTwo.setAdapter(adapter);
	}
	/**三人专区*/
	private void showSanYuanpter(List<IndexBeanAdvice> sanYuanBeans) {
		HomeProductAdapter adapter = new HomeProductAdapter(getActivity(),
				sanYuanBeans, R.layout.activity_main_tab_indext_item_layout);
		mgvThree.setAdapter(adapter);
	}

	private void stopLoadingRefreshState() {
		if (refreshView != null) {
			refreshView.postDelayed(new Runnable() {
				@Override
				public void run() {
					if (refreshView != null) {
						refreshView.onRefreshComplete();
					}
				}
			}, 100);
		}
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		if (!NetStateManager.OnNet()) {
			stopLoadingRefreshState();
			ToastHelper.toast(R.string.no_network_tip);
			return;
		}
		String label = DateUtils.formatDateTime(getActivity(),
				System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
						| DateUtils.FORMAT_SHOW_DATE
						| DateUtils.FORMAT_ABBREV_ALL);
		refreshView.getLoadingLayoutProxy()
				.setLastUpdatedLabel("最后更新：" + label);
		getIndexData();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		stopLoadingRefreshState();
	}
}