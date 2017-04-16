package com.wangyi.lelegou.maofengqi.ui.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.learn.soft.product.jni.JiaZhengApp;
import com.learn.soft.product.main.activity.fragment.ShowConfirmDlg;
import com.learn.soft.product.util.LoadDataType;
import com.learn.soft.product.util.NetStateManager;
import com.learn.soft.product.util.ToastHelper;
import com.learn.soft.product.widget.pulltorefresh.PullToRefreshBase;
import com.learn.soft.product.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.learn.soft.product.widget.pulltorefresh.PullToRefreshGridView;
import com.learn.soft.product1.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wangyi.lelegou.maofengqi.base.BaseActivity;
import com.wangyi.lelegou.maofengqi.bean.AllCirclesBean;
import com.wangyi.lelegou.maofengqi.bean.CircleBean;
import com.wangyi.lelegou.maofengqi.bean.ResultBean;
import com.wangyi.lelegou.maofengqi.ui.adapter.CircleAdapter;
import com.wangyi.lelegou.maofengqi.utils.ApiClass;
import com.wangyi.lelegou.maofengqi.utils.Constant;
import com.wangyi.lelegou.maofengqi.utils.DialogManager;
import com.wangyi.lelegou.maofengqi.utils.ResultCallback;
import com.wangyi.lelegou.maofengqi.utils.Utils;
import com.wangyi.lelegou.maofengqi.view.adapter.CommonAdapter;
import com.wangyi.lelegou.maofengqi.view.adapter.ViewHolder;

/**
 * 可参与的圈子
 * 
 * @author Doc.March
 * 
 */
public class ParticipateCirclesActivity extends BaseActivity implements
		PullToRefreshBase.OnRefreshListener2<GridView> {

	private PullToRefreshGridView pgvCommonPulltofresh;
	private GridView gridView;
	private List<CircleBean> list = new ArrayList<CircleBean>();
//	private CircleAdapter adapter;
	private CircleAdapter2 adapter;

	private LoadDataType mLoadDataType = LoadDataType.FirstLoad;
	private int pageIndex = 1;
	private int pageCount;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_participate_circles;
	}

	@Override
	protected void afterCreate(Bundle savedInstanceState) {
		tvContent.setText("可参与的圈子");

		pgvCommonPulltofresh = (PullToRefreshGridView) findViewById(R.id.pgv_common_pulltofresh);
		pgvCommonPulltofresh.setMode(Mode.PULL_FROM_START);
		pgvCommonPulltofresh.setOnRefreshListener(this);
		pgvCommonPulltofresh.setEmptyView(LayoutInflater.from(this).inflate(
				R.layout.common_empty_show, null));
		gridView = pgvCommonPulltofresh.getRefreshableView();
		gridView.setNumColumns(2);

		mLoadDataType = LoadDataType.FirstLoad;
		pageIndex = 1;

		getData();
	}

	private void getData() {
		showProgressInfo("");
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("id", JiaZhengApp.getInstance().getUserId());
		paramsMap.put("flag", 2);
		paramsMap.put("pageIndex", pageIndex);
		paramsMap.put("pageSize", 10);
		ApiClass.allCircle(paramsMap, callback);
	}

	private ResultCallback<String> callback = new ResultCallback<String>() {

		@Override
		public void onSuccess(ResultBean<String> resultBean, int id) {
			dismissProgress();
			if (resultBean.getStatus() == 1) {
				try {
					AllCirclesBean bean = new Gson().fromJson(
							resultBean.getJsonMsg(),
							new TypeToken<AllCirclesBean>() {
							}.getType());
					if (mLoadDataType == LoadDataType.FirstLoad
							|| mLoadDataType == LoadDataType.RefreshLoad) {
						list.clear();
					}
					if (bean != null) {
						pageCount = bean.getPageCount();
						if (pageIndex >= pageCount) {
							pgvCommonPulltofresh.setMode(Mode.PULL_FROM_START);
						} else {
							pgvCommonPulltofresh.setMode(Mode.BOTH);
						}

						int number = bean.getCircles() != null ? bean
								.getCircles().size() : 0;
						if (number > 0) {
							list.addAll(bean.getCircles());
						}
					}
					setAdapter();
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (mLoadDataType != LoadDataType.FirstLoad) {
					stopLoadingRefreshState();
				}
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

	private void setAdapter() {
		if (adapter == null) {
			adapter = new CircleAdapter2(mActivity, list,
					R.layout.activity_main_tab_index_third_item);
			gridView.setAdapter(adapter);
		} else {
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
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
	public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
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
		if (pgvCommonPulltofresh != null) {
			pgvCommonPulltofresh.postDelayed(new Runnable() {
				@Override
				public void run() {
					pgvCommonPulltofresh.onRefreshComplete();
				}
			}, 100);
		}
	}

	// 启动ParticipateCirclesActivity
	public static void start(Context context) {
		Intent intent = new Intent(context, ParticipateCirclesActivity.class);
		context.startActivity(intent);
	}



    class CircleAdapter2 extends CommonAdapter<CircleBean> implements
            View.OnClickListener {

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
                    CircleBean bean = (CircleBean) v.getTag();
                    // http://91lelegou.com/?/mobile
                    String url = Constant.MOBILE_IP + "/mobile/item/"
                            + bean.getProductId() + "/" + bean.getCircle_id();
//                    Toast.makeText(mContext, url, Toast.LENGTH_SHORT).show();
                    Utils.startWebViewShow(mContext, "", url);
                }
                break;
                case R.id.iv_index_third_item_delete:{
                    final CircleBean bean = (CircleBean) v.getTag();
					DialogManager.Instense(mActivity).showDialog("提示！", "确定解散圈子？", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							//确定
							deleteInfoJoin(bean, 2);
						}
					}, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							//取消
						}
					});

                }
            }
        }
    }

    private void deleteInfoJoin(final CircleBean bean, final int type) {
        if (bean!=null){
//            String info="";
//            if (type==1&&bean.getCanyurenshu()>0&&bean.getShengyurenshu()>0){
////                            info="该圈子已经有人参与，无法解散";
//                info="圈子已解散，如已付款，则支付金额将返还乐豆充值账户。";
//            }else{
////                            info="您确认要解散？";
//                info="您确定要解散圈子吗？";
//            }
//            ShowConfirmDlg.showDlg(getSupportFragmentManager(), new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    deleteNewCircle(bean.getCircle_id(), type);
//                }
//            }, info);

            deleteNewCircle(bean.getCircle_id(), type);
        }
    }

    // 删除圈子
    public void deleteNewCircle(String circleid ,final int type ) {
        showProgressInfo(null);
        Map<String, Object> paramsMap = new HashMap<String, Object>();
        paramsMap.put("user_id", JiaZhengApp.getInstance().getUserId());
        paramsMap.put("circle_id", circleid);
        paramsMap.put("type", type);
        ResultCallback<String> callback = new ResultCallback<String>() {

            @Override
            public void onSuccess(ResultBean<String> resultBean, int id) {
                dismissProgress();
                if (resultBean!=null&&resultBean.getStatus() == 1) {
                    getData();
                }else{
                    if (resultBean!=null){
                        ToastHelper.toast(resultBean.getInfo());
                    }else{
                        ToastHelper.toast("删除失败");
                    }
                }
            }

            public void onError(okhttp3.Call call, Exception e, int id) {
                super.onError(call, e, id);
                dismissProgress();
            }
        };
        ApiClass.deleteNewCircle(paramsMap, callback);
    }



}