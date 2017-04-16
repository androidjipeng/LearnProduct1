package com.wangyi.lelegou.maofengqi.ui.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.learn.soft.product.bean.IndexBeanAdvice;
import com.learn.soft.product.jni.JiaZhengApp;
import com.learn.soft.product.util.ApiWebCommon;
import com.learn.soft.product.util.MyLog;
import com.learn.soft.product1.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity.Config;
import com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity.ShopDetailActivity;
import com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity.bean.HomeShopInformation;
import com.wangyi.lelegou.maofengqi.utils.Utils;
import com.wangyi.lelegou.maofengqi.view.adapter.CommonAdapter;
import com.wangyi.lelegou.maofengqi.view.adapter.ViewHolder;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class HomeProductAdapter extends CommonAdapter<IndexBeanAdvice>
		implements OnClickListener {

	private DisplayImageOptions options;
    private Context context;
	public HomeProductAdapter(Context context, List<IndexBeanAdvice> mDatas,
			int itemLayoutId) {
		super(context, mDatas, itemLayoutId);
        this.context=context;
		options = JiaZhengApp.getInstance().getDefaultImgeOptions();
	}

	@Override
	public void convert(ViewHolder holder, int position, IndexBeanAdvice item) {
		View mArea = holder.getViewById(R.id.area_index_third_item);
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

		if (item != null) {
			if (item.yunjiage == 10) {
				mTvYunJiaGe.setVisibility(View.VISIBLE);
				mTvYunJiaGe.setText("十元商品");
			} else if (item.yunjiage == 100) {
				mTvYunJiaGe.setVisibility(View.VISIBLE);
				mTvYunJiaGe.setText("百元商品");
			} else {
				mTvYunJiaGe.setVisibility(View.GONE);
			}
			ImageLoader.getInstance().displayImage(item.thumb, mIvPic, options);
//			mTvId.setText(String.format("ID:%s", item.getCircle_code()));
			mTvId.setText("");
			mTvPrice.setText(String.format("价格:￥%s", item.money));
			mTvJoin.setText(String.valueOf(item.canyurenshu));
			mTvAll.setText(String.valueOf(item.zongrenshu));
			mTvRelease.setText(String.valueOf(item.zongrenshu
					- item.canyurenshu));
			int progress = 0;
			try {
				progress = item.canyurenshu * 100 / item.zongrenshu;
			} catch (Exception e) {
				e.printStackTrace();
			}
			mPbShow.setProgress(progress);
			mArea.setTag(item);
			mArea.setOnClickListener(this);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.area_index_third_item:
			//// TODO: 2017/3/16 订单详情
			MyLog.e("jp","HomeProductAdapter=======area_index_third_item");
			IndexBeanAdvice bean = (IndexBeanAdvice) v.getTag();
			OkHttpUtils.post().url(Config.testurl+Config.queryShopDetail)
					.addParams("itemId",bean.id)
					.build()
					.execute(new StringCallback() {
						@Override
						public void onError(Call call, Exception e, int id) {
							Log.e("jp","订单详情==onError:"+e.getMessage());
						}

						@Override
						public void onResponse(String response, int id) {
							Log.e("jp","HomeProductAdapter--订单详情==onResponse:"+response);
							Gson gson=new Gson();
                            HomeShopInformation homeShopInformation = gson.fromJson(response, HomeShopInformation.class);
                            JiaZhengApp.getInstance().setBean(homeShopInformation);
                            Intent intent=new Intent(context, ShopDetailActivity.class);
                            context.startActivity(intent);

                        }
					});



//			String title = "热门推荐";
//			StringBuffer sb = new StringBuffer();
//			sb.append(ApiWebCommon.API_COMMON.Api_Common_Url);
//			sb.append(String.format(
//					ApiWebCommon.API_COMMON.Web_Index_Product_Advice, bean.id));
//			String url = sb.toString();
//			Utils.startWebViewShow(mContext, title, url);
			break;
		}
	}
}