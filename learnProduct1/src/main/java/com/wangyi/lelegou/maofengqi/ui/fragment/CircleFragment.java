package com.wangyi.lelegou.maofengqi.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.learn.soft.product.jni.JiaZhengApp;
import com.learn.soft.product.main.activity.fragment.ShowConfirmDlg;
import com.learn.soft.product.main.activity.fragment.ShowConfirmInputDlg;
import com.learn.soft.product.main.activity.fragment.ShowSelectTypeDlg;
import com.learn.soft.product.util.BundleKey;
import com.learn.soft.product.util.*;
import com.learn.soft.product.util.UtilConversionHelper;
import com.learn.soft.product.widget.MyGridView;
import com.learn.soft.product.widget.pulltorefresh.PullToRefreshBase;
import com.learn.soft.product.widget.pulltorefresh.PullToRefreshScrollView;
import com.learn.soft.product1.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wangyi.lelegou.maofengqi.base.BaseFragment;
import com.wangyi.lelegou.maofengqi.bean.CircleBean;
import com.wangyi.lelegou.maofengqi.bean.CircleFragmentBean;
import com.wangyi.lelegou.maofengqi.bean.ResultBean;
import com.wangyi.lelegou.maofengqi.bean.ShareBean;
import com.wangyi.lelegou.maofengqi.ui.activity.CreateNewCircleActivity;
import com.wangyi.lelegou.maofengqi.ui.activity.IBuiltCirclesActivity;
import com.wangyi.lelegou.maofengqi.ui.activity.LoginActivity;
import com.wangyi.lelegou.maofengqi.ui.activity.ParticipateCirclesActivity;
import com.wangyi.lelegou.maofengqi.ui.activity.SearchProductActivity;
import com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity.Config;
import com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity.NoShopActivity;
import com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity.ShopDetailActivity;
import com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity.bean.HomeShopInformation;
import com.wangyi.lelegou.maofengqi.ui.adapter.CircleAdapter;
import com.wangyi.lelegou.maofengqi.utils.*;
import com.wangyi.lelegou.maofengqi.view.adapter.CommonAdapter;
import com.wangyi.lelegou.maofengqi.view.adapter.ViewHolder;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

public class CircleFragment extends BaseFragment implements OnClickListener,
		PullToRefreshBase.OnRefreshListener2<ScrollView> {

	// protected ImageButton ivSearch;
	protected ImageButton ivBack;
	protected TextView tvContent;
	/**添加按钮*/
	protected ImageButton ivCreateNewCircle;

	private LinearLayout llNoLogin;
	private TextView tvLogin;
	private boolean isReload;

	private PullToRefreshScrollView refreshView;

	private LinearLayout llContent;
	private RelativeLayout rlIbuiltCircles;
	private TextView tvIbuiltCircles;
	private MyGridView mgvIbuiltCircles;
	private RelativeLayout rlParticipateCircles;
	private TextView tvParticipateCircles;
	private MyGridView mgvParticipateCircles;


	private RelativeLayout rl_Createcircle;//创建圈子

	private RelativeLayout rl_join_circle;//可加入的圈子
	/**
	 * Use this factory method to create a new instance of this fragment using
	 * the provided parameters.
	 * 
	 * @return A new instance of fragment CircleFragment.
	 */

	private Context context;

	public static CircleFragment newInstance() {
		CircleFragment fragment = new CircleFragment();
		return fragment;
	}

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_circle;
	}

	@Override
	protected void afterCreate(Bundle savedInstanceState) {
		context=getActivity();
		rl_Createcircle= (RelativeLayout) findViewById(R.id.rl_Createcircle);
		rl_join_circle= (RelativeLayout) findViewById(R.id.rl_join_circle);
		rl_Createcircle.setOnClickListener(this);
		rl_join_circle.setOnClickListener(this);
	
		// ivSearch = (ImageButton) findViewById(R.id.iv_search);
		ivBack = (ImageButton) findViewById(R.id.iv_back);
		tvContent = (TextView) findViewById(R.id.tv_content);
		/**原来的添加按钮*/
		ivCreateNewCircle = (ImageButton) findViewById(R.id.iv_create_new_circle);
        /**未登录状态布局*/
		llNoLogin = (LinearLayout) findViewById(R.id.ll_no_login);
		/**登陆按钮*/
		tvLogin = (TextView) findViewById(R.id.tv_login);

		refreshView = (PullToRefreshScrollView) findViewById(R.id.refresh_view);
		llContent = (LinearLayout) findViewById(R.id.ll_content);
		rlIbuiltCircles = (RelativeLayout) findViewById(R.id.rl_ibuilt_circles);
		tvIbuiltCircles = (TextView) findViewById(R.id.tv_ibuilt_circles);
		mgvIbuiltCircles = (MyGridView) findViewById(R.id.mgv_ibuilt_circles);
		rlParticipateCircles = (RelativeLayout) findViewById(R.id.rl_participate_circles);
		tvParticipateCircles = (TextView) findViewById(R.id.tv_participate_circles);
		mgvParticipateCircles = (MyGridView) findViewById(R.id.mgv_participate_circles);

		// ivSearch.setVisibility(View.VISIBLE);
		// ivSearch.setOnClickListener(this);
		ivBack.setVisibility(View.INVISIBLE);
		tvContent.setText("圈中宝");
		/**原来布局中的圈中宝中的加号*/
//		ivCreateNewCircle.setVisibility(View.VISIBLE);
//		ivCreateNewCircle.setOnClickListener(this);

		refreshView.setOnRefreshListener(this);

		//setData();
	}

	@Override
	public void onStart() {
		super.onStart();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constant.ACTION_LOGN);
		filter.addAction(Constant.ACTION_CREATE_NEW_CIRCLE);
		getActivity().registerReceiver(receiver, filter);
	}

	@Override
	public void onResume() {
		super.onResume();
		// if (isReload) {
		setData();
		// }
	}

	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(Constant.ACTION_LOGN)) {
				isReload = true;
			} else if (action.equals(Constant.ACTION_CREATE_NEW_CIRCLE)) {
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

	public void setData() {
		if (JiaZhengApp.getInstance().isLogin()) {
			llContent.setVisibility(View.VISIBLE);
			llNoLogin.setVisibility(View.GONE);

			rlIbuiltCircles.setOnClickListener(this);
			rlParticipateCircles.setOnClickListener(this);

			getData();
		} else {
//			rlIbuiltCircles.setVisibility(View.GONE);//我创建的圈子
//			mgvIbuiltCircles.setVisibility(View.GONE);//我创建的圈子的gridview
//			rlParticipateCircles.setVisibility(View.GONE);//可参与的圈子
//			mgvParticipateCircles.setVisibility(View.GONE);//可参与的圈子gridview
			llContent.setVisibility(View.GONE);
			//// TODO: 2017/3/13  
//			rl_Createcircle.setVisibility(View.VISIBLE);
//			rl_join_circle.setVisibility(View.VISIBLE);
			llNoLogin.setVisibility(View.VISIBLE);
			tvLogin.setOnClickListener(this);
		}
	}

	private void getData() {
		showProgressInfo(null);
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("id", JiaZhengApp.getInstance().getUserId());
		ApiClass.circle(paramsMap, callback);
	}

	private ResultCallback<String> callback = new ResultCallback<String>() {

		@Override
		public void onSuccess(ResultBean<String> resultBean, int id) {
			dismissProgress();
			if (resultBean.getStatus() != 1) {
				Toast.makeText(getActivity(), resultBean.getInfo(),
						Toast.LENGTH_SHORT).show();
				return;
			}
			try {
				MyLog.i("xiaocai", "data="+resultBean.getJsonMsg());
				CircleFragmentBean circleFragmentBean = new Gson().fromJson(
						resultBean.getJsonMsg(),
						new TypeToken<CircleFragmentBean>() {
						}.getType());
				setAdapter(circleFragmentBean);
			} catch (Exception e) {
				e.printStackTrace();
			}
			stopLoadingRefreshState();
		}

		public void onError(okhttp3.Call call, Exception e, int id) {
			super.onError(call, e, id);
			dismissProgress();
			ToastHelper.toast(e.getMessage());
			stopLoadingRefreshState();
		}
	};

	private void setAdapter(CircleFragmentBean bean) {
		if (bean == null) {
			return;
		}
		tvIbuiltCircles.setText("我建的圈子（" + bean.getIbuiltCirclesCount() + "）");
		tvParticipateCircles.setText("可参与的圈子（" + bean.getParticipateCirclesCount() + "）");
		/**我建的圈子的数据*/
		List<CircleBean> ibuiltCircles = bean.getIbuiltCircles();
		if (ibuiltCircles != null) {
			CircleSelfAdapter adapter = new CircleSelfAdapter(getActivity(),
					ibuiltCircles, R.layout.activity_main_tab_index_third_item);
			mgvIbuiltCircles.setAdapter(adapter);
		}
		/**可参与的圈子*/
		List<CircleBean> participateCircles = bean.getParticipateCircles();
		if (participateCircles != null) {
			CircleAdapter2 adapter = new CircleAdapter2(getActivity(),
					participateCircles,
					R.layout.activity_main_tab_index_third_item);
			mgvParticipateCircles.setAdapter(adapter);
		}
	}

	public void showInputInfoDlg(){
		if (JiaZhengApp.getInstance().isLogin()) {
			ShowConfirmInputDlg.showDlg(getActivity().getSupportFragmentManager(), null);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_search:
			SearchProductActivity.start(getActivity());
			break;
		case R.id.iv_create_new_circle:
			/**原来的新建圈子加号图标*/
//			if (JiaZhengApp.getInstance().isLogin()) {
//
//				showSendTypeDlg();
//			} else {
//				Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT)
//						.show();
//			}
			break;
		case R.id.tv_login:
			/**登陆*/
			LoginActivity.start(getActivity());
			break;
		case R.id.rl_ibuilt_circles:
			/**我建的圈子*/
			IBuiltCirclesActivity.start(getActivity());
			break;
		case R.id.rl_participate_circles:
			/**可参与的圈子*/
			ParticipateCirclesActivity.start(getActivity());
			break;
			case R.id.rl_join_circle:
				/**可加入圈子*/
				if (JiaZhengApp.getInstance().isLogin()) {

					ShowConfirmInputDlg.showDlg(getActivity().getSupportFragmentManager(), null);
				} else {
					Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT)
							.show();
				}

				break;
			case R.id.rl_Createcircle:
				/**创建圈子*/
				if (JiaZhengApp.getInstance().isLogin()) {

					CreateNewCircleActivity.start(getActivity());
				} else {
					Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT)
							.show();
				}

				break;
			default:break;


		}
	}

	private ShowSelectTypeDlg mSendTypeMsgDlg = null;
	private static final String TAG_SHOW_SENDTYPE_MSG_DLG = "TAG_SHOW_SENDTYPE_MSG_DLG";

	/**dialog窗口*/
	private void showSendTypeDlg() {
		if (mSendTypeMsgDlg == null) {
			mSendTypeMsgDlg = new ShowSelectTypeDlg();
			getActivity().getSupportFragmentManager().beginTransaction().add(
					mSendTypeMsgDlg, TAG_SHOW_SENDTYPE_MSG_DLG);
		}
		if (!mSendTypeMsgDlg.isVisible()) {
			int[] location = new int[2];
			ivCreateNewCircle.getLocationOnScreen(location);// 那个用getLocationInWindow是整个窗口内的绝对坐标不准与getLocationOnScreen值一样，与现在返回的值一样。可以去测试一下
			Rect rect = new Rect();
			getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
			int statusBarHeight = rect.top;
			int x = location[0];
			int y = location[1];
			Bundle args = new Bundle();
			args.putInt(BundleKey.BUNDLE_KEY_X, x+ivCreateNewCircle.getWidth()-UtilConversionHelper.dip2px(getActivity(), 10));
			args.putInt(BundleKey.BUNDLE_KEY_Y, y - statusBarHeight + ivCreateNewCircle.getHeight() - UtilConversionHelper.dip2px(getActivity(), 5));
			mSendTypeMsgDlg.setArguments(args);
			mSendTypeMsgDlg.show(getActivity().getSupportFragmentManager(),
					TAG_SHOW_SENDTYPE_MSG_DLG);
		} else {
			mSendTypeMsgDlg.dismiss();
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
		getData();
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		stopLoadingRefreshState();
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

    
    //// TODO: 2017/3/13 我建的圈子 
	class CircleSelfAdapter extends CommonAdapter<CircleBean> implements
			OnClickListener {

		private DisplayImageOptions options;

		public CircleSelfAdapter(Context context, List<CircleBean> mDatas,
							 int itemLayoutId) {
			super(context, mDatas, itemLayoutId);
			options = JiaZhengApp.getInstance().getDefaultImgeOptions();
		}

		@Override
		public void convert(ViewHolder holder, int position, CircleBean item) {
			View mArea = holder.getViewById(R.id.area_index_third_item);
            View mTran5Area = holder.getViewById(R.id.area_index_tran5_item);
			ImageView mIvPic = (ImageView) holder
					.getViewById(R.id.iv_index_third_item_pic);
			ProgressBar mPbShow = (ProgressBar) holder
					.getViewById(R.id.pb_index_third_item_bar);
			TextView mTvPrice = (TextView) holder
					.getViewById(R.id.tv_index_third_item_price);
			TextView mTvAll = (TextView) holder
					.getViewById(R.id.tv_index_third_item_all);
			TextView mTvJoin = (TextView) holder
					.getViewById(R.id.tv_index_third_item_join);
			TextView mTvRelease = (TextView) holder
					.getViewById(R.id.tv_index_third_item_release);
			TextView mTvYunJiaGe = (TextView) holder
					.getViewById(R.id.tv_yun_jia_ge);
			TextView mTvId = (TextView) holder
					.getViewById(R.id.tv_index_third_item_id);
			ImageView mIvShare =  (ImageView)holder
					.getViewById(R.id.iv_index_third_item_invite);
			ImageView mIvDelete = (ImageView) holder
					.getViewById(R.id.iv_index_third_item_delete);
            TextView mTvDelete2 = (TextView) holder
                    .getViewById(R.id.tv_index_third_item_deletetip);
			mIvShare.setVisibility(View.VISIBLE);
			mIvDelete.setVisibility(View.VISIBLE);
			mTvYunJiaGe.setVisibility(View.GONE);
			if (item != null) {
                if (item.getShengyurenshu()==0){
                    mTran5Area.setVisibility(View.VISIBLE);
                    mTvDelete2.setText("已结束");
                }else{

					mTvDelete2.setText("");
					mTran5Area.setVisibility(View.GONE);
                }
				ImageLoader.getInstance().displayImage(item.getPictureUrl(),
						mIvPic, options);
				mTvId.setText(String.format("ID:%s", item.getCircle_code()));
				mTvPrice.setText(String.format("￥:%s", item.getPrice()));
				mTvJoin.setText(String.valueOf(item.getCanyurenshu()));
				mTvAll.setText(String.valueOf(item.getNumber()));
				mTvRelease.setText(String.valueOf(item.getShengyurenshu()));
				int progress = 0;
				try {
					progress = item.getCanyurenshu() * 100 / item.getNumber();
				} catch (Exception e) {
					e.printStackTrace();
				}
				mPbShow.setProgress(progress);
				mArea.setTag(item);
				mArea.setOnClickListener(this);
				mIvDelete.setTag(item);
				mIvDelete.setOnClickListener(this);
				mIvShare.setTag(item);
				mIvShare.setOnClickListener(this);
			}
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.area_index_third_item:{
					//// TODO: 2017/3/16 我自己的圈子进入详情
					MyLog.e("jp","CircleFragment===============area_index_third_item");
					final CircleBean bean = (CircleBean) v.getTag();

					MyLog.e("jp","我自己的圈子Uid:"+bean.getUid()+"  ProductId:"+bean.getProductId()+" Circle_id:"+bean.getCircle_id());
					OkHttpUtils.post().url(Config.testurl+Config.queryShopDetail)
							.addParams("itemId",bean.getProductId()+"")
							.addParams("circleId",bean.getCircle_id())
                            .addParams("uid",bean.getUid()+"")
							.build()
							.execute(new StringCallback() {
								@Override
								public void onError(Call call, Exception e, int id) {
									Log.e("jp","订单详情==onError:"+e.getMessage());
								}

								@Override
								public void onResponse(String response, int id) {
									Log.e("jp","CircleSelfAdapter---->订单详情==onResponse:"+response);
									Gson gson=new Gson();
									HomeShopInformation homeShopInformation = gson.fromJson(response, HomeShopInformation.class);
									JiaZhengApp.getInstance().setBean(homeShopInformation);
									if (homeShopInformation.getResult()==1)
									{
                                        Intent intent1=new Intent(getActivity(), NoShopActivity.class);
										startActivity(intent1);
									}else
									{
										Intent intent=new Intent(getActivity(), ShopDetailActivity.class);
                                        intent.putExtra("tag","1");
									    intent.putExtra("circleid",bean.getCircle_id());
										startActivity(intent);
									}

								}
							});


//					// http://91lelegou.com/?/mobile
//					String url = Constant.MOBILE_IP + "/mobile/item/"
//							+ bean.getProductId() + "/" + bean.getCircle_id();
////					Toast.makeText(mContext, url, Toast.LENGTH_SHORT).show();
//					Utils.startWebViewShow(mContext, "", url);
				}
					break;
				case R.id.iv_index_third_item_delete:{
					MyLog.e("jp","======删除");
					final CircleBean bean = (CircleBean) v.getTag();
					DialogManager.Instense(context).showDialog("提示！", "确定解散圈子？", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							//确定
							deleteInfoJoin(bean, 1);
						}
					}, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
                             //取消
						}
					});


				}
					break;
				case R.id.iv_index_third_item_invite:{
					CircleBean bean = (CircleBean) v.getTag();
                    showProgressInfo("");
                    Map<String, Object> paramsMap = new HashMap<String, Object>();
                    paramsMap.put("user_id", JiaZhengApp.getInstance().getUserId());
                    paramsMap.put("circle_code", bean.circle_code);
                    ApiClass.shareNew(paramsMap, shareCallback);
				}
					break;
			}
		}
	}

	private void deleteInfoJoin(final CircleBean bean, final int type) {
		if (bean!=null){
            String info="";
            if (type==1&&bean.getCanyurenshu()>0&&bean.getShengyurenshu()>0){
//                            info="该圈子已经有人参与，无法解散";
                info="圈子已解散，如已付款，则支付金额将返还乐豆充值账户。";
                ShowConfirmDlg.showDlg(getActivity().getSupportFragmentManager(), new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        deleteNewCircle(bean.getCircle_id(), type);
                    }
                }, info);
            }else{
                deleteNewCircle(bean.getCircle_id(), type);
            }

        }
	}


	// 删除圈子
	public void deleteNewCircle(String circleid ,final int type ) {
//		showProgressInfo("");
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("user_id", JiaZhengApp.getInstance().getUserId());
		paramsMap.put("circle_id", circleid);
		paramsMap.put("type", type);
		ResultCallback<String> callback = new ResultCallback<String>() {

			@Override
			public void onSuccess(ResultBean<String> resultBean, int id) {
//				dismissProgress();
				if (resultBean!=null&&resultBean.getStatus() == 1) {
					setData();
				}else{
                    if (resultBean!=null){
                        ToastHelper.toast(getActivity(), resultBean.getInfo());
                    }else{
                        ToastHelper.toast(getActivity(), "删除失败");
                    }
                }
			}

			public void onError(okhttp3.Call call, Exception e, int id) {
				super.onError(call, e, id);
//				dismissProgress();
			}
		};
		ApiClass.deleteNewCircle(paramsMap, callback);
	}


    private ResultCallback<String> shareCallback = new ResultCallback<String>() {

        @Override
        public void onSuccess(ResultBean<String> resultBean, int id) {
            dismissProgress();
            if (resultBean.getStatus() != 1) {
                Toast.makeText(mActivity, resultBean.getInfo(),
                        Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                ShareBean bean = new Gson().fromJson(resultBean.getJsonMsg(),
                        new TypeToken<ShareBean>() {
                        }.getType());
                ShareUtils.show(mActivity, bean);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void onError(okhttp3.Call call, Exception e, int id) {
            super.onError(call, e, id);
            dismissProgress();
            ToastHelper.toast(e.getMessage());
        }
    };



	class CircleAdapter2 extends CommonAdapter<CircleBean> implements
			OnClickListener {

		private DisplayImageOptions options;

		public CircleAdapter2(Context context, List<CircleBean> mDatas,
							 int itemLayoutId) {
			super(context, mDatas, itemLayoutId);
			options = JiaZhengApp.getInstance().getDefaultImgeOptions();
		}

		@Override
		public void convert(ViewHolder holder, int position, CircleBean item) {
			View mArea = holder.getViewById(R.id.area_index_third_item);
			View mTran5Area = holder.getViewById(R.id.area_index_tran5_item);
			ImageView mIvPic = (ImageView) holder
					.getViewById(R.id.iv_index_third_item_pic);
			ProgressBar mPbShow = (ProgressBar) holder
					.getViewById(R.id.pb_index_third_item_bar);
			TextView mTvPrice = (TextView) holder
					.getViewById(R.id.tv_index_third_item_price);
			TextView mTvAll = (TextView) holder
					.getViewById(R.id.tv_index_third_item_all);
			TextView mTvJoin = (TextView) holder
					.getViewById(R.id.tv_index_third_item_join);
			TextView mTvRelease = (TextView) holder
					.getViewById(R.id.tv_index_third_item_release);
			TextView mTvYunJiaGe = (TextView) holder
					.getViewById(R.id.tv_yun_jia_ge);
			TextView mTvId = (TextView) holder
					.getViewById(R.id.tv_index_third_item_id);
			TextView mTvDelete2 = (TextView) holder
					.getViewById(R.id.tv_index_third_item_deletetip);

			ImageView mIvDelete = (ImageView) holder
					.getViewById(R.id.iv_index_third_item_delete);

			mTvYunJiaGe.setVisibility(View.GONE);
			if (item != null) {
				if (item.getShengyurenshu()==0||item.getDeleteFlag()==1){
					mTran5Area.setVisibility(View.VISIBLE);
					mIvDelete.setVisibility(View.VISIBLE);
					if (item.getShengyurenshu()==0){
						mTvDelete2.setText("已结束");
					}else if (item.getDeleteFlag()==1){

						mTvDelete2.setText("圈子已解散");
					}
				}else{
					mTran5Area.setVisibility(View.GONE);
					mIvDelete.setVisibility(View.GONE);
					mTvDelete2.setText("");
				}
				ImageLoader.getInstance().displayImage(item.getPictureUrl(),
						mIvPic, options);
				mTvId.setText(String.format("ID:%s", item.getCircle_code()));
				mTvPrice.setText(String.format("价格:￥%s", item.getPrice()));
				mTvJoin.setText(String.valueOf(item.getCanyurenshu()));
				mTvAll.setText(String.valueOf(item.getNumber()));
				mTvRelease.setText(String.valueOf(item.getShengyurenshu()));
				int progress = 0;
				try {
					progress = item.getCanyurenshu() * 100 / item.getNumber();
				} catch (Exception e) {
					e.printStackTrace();
				}
				mPbShow.setProgress(progress);
				mArea.setTag(item);
				mArea.setOnClickListener(this);
				mIvDelete.setTag(item);
				mIvDelete.setOnClickListener(this);
			}
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.area_index_third_item:{
					//// TODO: 2017/3/16 别人的圈子点击详情 
					MyLog.i("jp","==============area_index_third_item");
					final CircleBean bean = (CircleBean) v.getTag();
                    MyLog.e("jp","别人的圈子点击详情Uid:"+bean.getUid());
					OkHttpUtils.post().url(Config.testurl+Config.queryShopDetail)
							.addParams("itemId",bean.getProductId()+"")
							.addParams("circleId",bean.getCircle_id())
                            .addParams("uid",bean.getUid()+"")
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

									if (homeShopInformation.getResult()==1)
									{
										Intent noshop=new Intent(getActivity(),NoShopActivity.class);
										startActivity(noshop);
									}else {
										Intent intent=new Intent(getActivity(), ShopDetailActivity.class);
										intent.putExtra("tag","1");
										intent.putExtra("circleid",bean.getCircle_id());
										startActivity(intent);
									}


								}
							});

//					// http://91lelegou.com/?/mobile
//					String url = Constant.MOBILE_IP + "/mobile/item/"
//							+ bean.getProductId() + "/" + bean.getCircle_id();
////					Toast.makeText(mContext, url, Toast.LENGTH_SHORT).show();
//					Utils.startWebViewShow(mContext, "", url);
				}
					break;
				case R.id.iv_index_third_item_delete:{
					final CircleBean bean = (CircleBean) v.getTag();
					deleteInfoJoin(bean, 2);
				}
			}
		}
	}

}