package com.wangyi.lelegou.maofengqi.ui.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.learn.soft.product.jni.JiaZhengApp;
import com.learn.soft.product.util.ToastHelper;
import com.learn.soft.product1.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wangyi.lelegou.maofengqi.base.BaseActivity;
import com.wangyi.lelegou.maofengqi.bean.CircleOfFriendBean;
import com.wangyi.lelegou.maofengqi.bean.MyCircleOfFriendsBean;
import com.wangyi.lelegou.maofengqi.bean.ResultBean;
import com.wangyi.lelegou.maofengqi.bean.ShareBean;
import com.wangyi.lelegou.maofengqi.utils.ApiClass;
import com.wangyi.lelegou.maofengqi.utils.ResultCallback;
import com.wangyi.lelegou.maofengqi.utils.ShareUtils;
import com.wangyi.lelegou.maofengqi.view.adapter.CommonAdapter;
import com.wangyi.lelegou.maofengqi.view.adapter.ViewHolder;

public class MyCircleOfFriendsActivity extends BaseActivity implements
		OnClickListener {

	private TextView tvAccumulativeCut;
	private ListView listView;
	private List<CircleOfFriendBean> list = new ArrayList<CircleOfFriendBean>();
	private CommonAdapter<CircleOfFriendBean> adapter;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_my_circle_of_friends;
	}

	@Override
	protected void afterCreate(Bundle savedInstanceState) {
		tvContent.setText("我的好友");
		tvRight.setText("邀请好友");
		tvRight.setOnClickListener(this);

		tvAccumulativeCut = (TextView) findViewById(R.id.tv_accumulative_cut);
		listView = (ListView) findViewById(R.id.list_view);
		adapter = new CommonAdapter<CircleOfFriendBean>(this, list,
				R.layout.item_my_circle_of_friends) {

			@Override
			public void convert(ViewHolder holder, int position,
					CircleOfFriendBean item) {
				holder.setText(R.id.tv_nick_name, item.getNickname());
				holder.setText(R.id.tv_consumption,
						"本月消费:" + item.getConsumption());
				holder.setText(R.id.tv_monthCut, "本月分成:" + item.getMonthCut());

				ImageView ivHead = holder.getViewById(R.id.iv_head);
				ImageLoader.getInstance().displayImage(item.getHeadUrl(),
						ivHead, JiaZhengApp.getInstance().getUserPicOptions());
			}
		};
		listView.setAdapter(adapter);

		getData();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_right:
			showProgressInfo("");
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			paramsMap.put("id", JiaZhengApp.getInstance().getUserId());
			ApiClass.share(paramsMap, shareCallback);
			break;
		}
	}

	private void getData() {
		showProgressInfo("");
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("id", JiaZhengApp.getInstance().getUserId());
		ApiClass.myCircleOfFriends(paramsMap, myCircleOfFriendsCallback);
	}

	private ResultCallback<String> myCircleOfFriendsCallback = new ResultCallback<String>() {

		@Override
		public void onSuccess(ResultBean<String> resultBean, int id) {
			dismissProgress();
			if (resultBean.getStatus() != 1) {
				Toast.makeText(mActivity, resultBean.getInfo(),
						Toast.LENGTH_SHORT).show();
				return;
			}
			try {
				MyCircleOfFriendsBean bean = new Gson().fromJson(
						resultBean.getJsonMsg(),
						new TypeToken<MyCircleOfFriendsBean>() {
						}.getType());
				if (bean != null) {
					tvAccumulativeCut.setText("累计分成:"
							+ bean.getAccumulaticeCut());
					if (bean.getContacts() != null) {
						list.addAll(bean.getContacts());
					}
					adapter.notifyDataSetChanged();
				}
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

	// 启动MyCircleOfFriendsActivity
	public static void start(Context context) {
		Intent intent = new Intent(context, MyCircleOfFriendsActivity.class);
		context.startActivity(intent);
	}
}