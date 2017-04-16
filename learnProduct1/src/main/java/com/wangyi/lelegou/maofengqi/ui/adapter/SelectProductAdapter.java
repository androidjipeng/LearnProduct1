package com.wangyi.lelegou.maofengqi.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.learn.soft.product.jni.JiaZhengApp;
import com.learn.soft.product.util.ApiWebCommon;
import com.learn.soft.product1.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wangyi.lelegou.maofengqi.bean.SelectProductBean;
import com.wangyi.lelegou.maofengqi.ui.activity.CreateNewCircleActivity;
import com.wangyi.lelegou.maofengqi.utils.Constant;
import com.wangyi.lelegou.maofengqi.utils.Utils;
import com.wangyi.lelegou.maofengqi.view.adapter.CommonAdapter;
import com.wangyi.lelegou.maofengqi.view.adapter.ViewHolder;

import java.util.List;

public class SelectProductAdapter extends CommonAdapter<SelectProductBean>
		implements OnClickListener {

	private Activity mActivity;
	private DisplayImageOptions options = JiaZhengApp.getInstance()
			.getDefaultImgeOptions();

	public SelectProductAdapter(Activity activity,
			List<SelectProductBean> mDatas, int itemLayoutId) {
		super(activity, mDatas, itemLayoutId);
		mActivity = activity;
	}

	@Override
	public void convert(ViewHolder holder, int position, SelectProductBean item) {
		LinearLayout llItem = (LinearLayout) holder.getViewById(R.id.ll_item);
		ImageView ivProductPic = (ImageView) holder
				.getViewById(R.id.iv_product_pic);
		TextView tvProductName = (TextView) holder
				.getViewById(R.id.tv_product_name);
		TextView tvProductPrice = (TextView) holder
				.getViewById(R.id.tv_product_price);

		if (item != null) {
			ImageLoader.getInstance().displayImage(item.getPictureUrl(),
					ivProductPic, options);
			tvProductName.setText(Html.fromHtml(item.getProductName()));
			tvProductPrice.setText("商品价格：¥ " + item.getProductPrice());

			llItem.setTag(item);
			llItem.setOnClickListener(this);
			// ivProductPic.setTag(item);
			// ivProductPic.setOnClickListener(this);
		}
	}

	@Override
	public void onClick(View v) {
		SelectProductBean bean;
		switch (v.getId()) {
		case R.id.ll_item:
			bean = (SelectProductBean) v.getTag();
			Intent intent = new Intent(mContext, CreateNewCircleActivity.class);
			Bundle bundle = new Bundle();
			bundle.putInt("productId", Integer.parseInt(bean.getProductId()));
			bundle.putString("productName", bean.getProductName());
			bundle.putString("productPrice", bean.getProductPrice());
			intent.putExtras(bundle);
			mActivity.setResult(Activity.RESULT_OK, intent);
			mActivity.finish();
			break;
		case R.id.iv_product_pic:
			bean = (SelectProductBean) v.getTag();
			StringBuffer sb = new StringBuffer();
			sb.append(Constant.MOBILE_IP);
			sb.append(String.format(
					ApiWebCommon.API_COMMON.Web_Index_Product_Advice,
					bean.getProductId()));
			Utils.startWebViewShow(mContext, "", sb.toString());
			break;
		}
	}
}