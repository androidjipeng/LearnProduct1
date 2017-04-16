package com.wangyi.lelegou.maofengqi.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.learn.soft.product.jni.JiaZhengApp;
import com.learn.soft.product1.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wangyi.lelegou.maofengqi.bean.CircleBean;
import com.wangyi.lelegou.maofengqi.utils.Constant;
import com.wangyi.lelegou.maofengqi.utils.Utils;
import com.wangyi.lelegou.maofengqi.view.adapter.CommonAdapter;
import com.wangyi.lelegou.maofengqi.view.adapter.ViewHolder;

import java.util.List;

public class CircleAdapter extends CommonAdapter<CircleBean> implements
		OnClickListener {

	private DisplayImageOptions options;

	public CircleAdapter(Context context, List<CircleBean> mDatas,
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

		mTvYunJiaGe.setVisibility(View.GONE);
		if (item != null) {
			if (item.getDeleteFlag()==1){
				mTran5Area.setVisibility(View.VISIBLE);
			}else{
				mTran5Area.setVisibility(View.GONE);
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
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.area_index_third_item:
			CircleBean bean = (CircleBean) v.getTag();
			// http://91lelegou.com/?/mobile
			String url = Constant.MOBILE_IP + "/mobile/item/"
					+ bean.getProductId() + "/" + bean.getCircle_id();
			Toast.makeText(mContext, url, Toast.LENGTH_SHORT).show();
			Utils.startWebViewShow(mContext, "", url);
			break;
		}
	}
}