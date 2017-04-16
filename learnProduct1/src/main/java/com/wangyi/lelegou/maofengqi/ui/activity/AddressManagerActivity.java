package com.wangyi.lelegou.maofengqi.ui.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.learn.soft.product.jni.JiaZhengApp;
import com.learn.soft.product.util.MyLog;
import com.learn.soft.product1.R;
import com.wangyi.lelegou.maofengqi.base.BaseActivity;
import com.wangyi.lelegou.maofengqi.bean.AddressBean;
import com.wangyi.lelegou.maofengqi.bean.ResultBean;
import com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity.BalanceActivity;
import com.wangyi.lelegou.maofengqi.utils.ApiClass;
import com.wangyi.lelegou.maofengqi.utils.Constant;
import com.wangyi.lelegou.maofengqi.utils.ResultCallback;
import com.wangyi.lelegou.maofengqi.view.adapter.RecyclerViewAdapter;
import com.wangyi.lelegou.maofengqi.view.adapter.RecyclerViewAdapter.OnItemClickListener;
import com.wangyi.lelegou.maofengqi.view.adapter.ViewHolder;
import com.wangyi.lelegou.maofengqi.view.recyclerview.DividerItemDecoration;

/**
 * **********************************************************
 * <p/>
 * 说明:地址管理
 * <p/>
 * 作者:@Doc.March
 * <p/>
 * 创建日期:2016-9-29
 * <p/>
 * 描述:
 * <p/>
 * **********************************************************
 */
public class AddressManagerActivity extends BaseActivity implements
		OnClickListener {

	private int shopId;
	private int circleId;

	private TextView tvAddAddress;

	private RecyclerView recyclerView;
	private List<AddressBean> addressBeans = new ArrayList<AddressBean>();
	private RecyclerViewAdapter<AddressBean> adapter;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_address_manager;
	}

	@Override
	protected void afterCreate(Bundle savedInstanceState) {
		Bundle bundle = getIntent().getExtras();
		shopId = bundle.getInt("shopId", -1);
		circleId = bundle.getInt("circleId", 0);
		MyLog.i("xiaocai", "2222 shopid=" + shopId + ", circleId=" + circleId);
		tvContent.setText(R.string.address_manager);
		tvRight.setText(R.string.add);
		tvRight.setOnClickListener(this);

		tvAddAddress = (TextView) findViewById(R.id.tv_add_address);
		tvAddAddress.setOnClickListener(this);

		recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
		// 设置布局管理器
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		// 如果可以确定每个item的高度是固定的,设置这个选项可以提高性能
		// recyclerView.setHasFixedSize(true);
		adapter = new RecyclerViewAdapter<AddressBean>(this, addressBeans,
				R.layout.item_address_manager) {

			@Override
			public void convert(ViewHolder holder, int position,
					AddressBean item) {
				View view = holder.getConvertView();
				// 是否是默认地址(0：不是 1：是)
				if (item.getIsDefault() == 1) {
					view.setBackgroundResource(R.drawable.bg_linear_pressed);
				} else {
					view.setBackgroundResource(R.drawable.bg_linear_normal);
				}
				holder.setText(R.id.tv_consignee, item.getConsignee());
				holder.setText(R.id.tv_phone_number, item.getPhoneNumber());
				String str = item.getProvinceBean().getProvinceName()
						+ item.getCityBean().getCityName()
						+ item.getDistrictBean().getDistrictName();
				holder.setText(R.id.tv_addess, str + " " + item.getAddress());
			}
		};
		adapter.setOnItemClickListener(new OnItemClickListener<AddressBean>() {

			@Override
			public void onItemClick(View view, int position, AddressBean t) {
				if (shopId == -1) {
					EditAddressActivity.start(mActivity, 2, t,
							Constant.REQUESTCODE_ADDRESS_MANAGER);
				} else {
					qrdizhi(t.getAddressId());
//					Intent intent = new Intent(mActivity,
//							BalanceActivity.class);
//					intent.putExtra("add",t);
//                    setResult(RESULT_OK,intent);
				}
			}
		});
		// 设置adapter
		recyclerView.setAdapter(adapter);
		// 设置Item增加、移除动画
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		// 添加分割线
		recyclerView.addItemDecoration(new DividerItemDecoration(this,
				DividerItemDecoration.VERTICAL_LIST));

		getData();
	}

	protected void qrdizhi(int addressId) {
		showProgressInfo(null);
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("userid", JiaZhengApp.getInstance().getUserId());
		paramsMap.put("shopid", shopId);
		paramsMap.put("addressid", addressId);
		paramsMap.put("circleid", circleId);
		ApiClass.confirmAddress(paramsMap, confirmAddress);
	}

	private ResultCallback<String> confirmAddress = new ResultCallback<String>() {

		@Override
		public void onSuccess(ResultBean<String> resultBean, int id) {
			dismissProgress();
			Toast.makeText(mActivity, resultBean.getInfo(), Toast.LENGTH_SHORT)
					.show();
			if (resultBean.getStatus() == 1) {
				sendBroadcast(new Intent(Constant.ACTION_RELOAD));
				finish();
			}
		}

		public void onError(okhttp3.Call call, Exception e, int id) {
			super.onError(call, e, id);
			dismissProgress();
		}
	};

	private void getData() {
		showProgressInfo(null);
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("id", JiaZhengApp.getInstance().getUserId());
		ApiClass.getAllAddress(paramsMap, callback);
	}

	private void updateData() {
		if (addressBeans == null || addressBeans.size() == 0) {
//			tvAddAddress.setVisibility(View.VISIBLE);
			tvAddAddress.setVisibility(View.GONE);
		} else {
			tvAddAddress.setVisibility(View.GONE);
			adapter.notifyItemRangeChanged(0, addressBeans.size());
		}
	}

	@Override
	public void onClick(View v) {
		EditAddressActivity.start(this, 1, null,
				Constant.REQUESTCODE_ADDRESS_MANAGER);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// super.onActivityResult(requestCode, resultCode, data);
		if (data != null && data.getBooleanExtra("updateAddressData", false)) {
			getData();
		}
	}

	private ResultCallback<List<AddressBean>> callback = new ResultCallback<List<AddressBean>>() {

		@Override
		public void onSuccess(ResultBean<List<AddressBean>> resultBean, int id) {
			dismissProgress();
			addressBeans.clear();
			addressBeans.addAll(resultBean.getJsonMsg());
			updateData();
		}

		public void onError(okhttp3.Call call, Exception e, int id) {
			super.onError(call, e, id);
			dismissProgress();
		}
	};

	public static void start(Context context, int shopId, int circleId) {
		Intent intent = new Intent(context, AddressManagerActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("shopId", shopId);
		bundle.putInt("circleId", circleId);
		intent.putExtras(bundle);
		context.startActivity(intent);
	}
}