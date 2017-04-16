package com.wangyi.lelegou.maofengqi.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.learn.soft.product.main.TabShowHistoryFragment;
import com.learn.soft.product1.R;
import com.wangyi.lelegou.maofengqi.base.BaseActivity;

/**
 * 最新晒单列表
 * 
 * @author Doc.March
 * 
 */
public class ShareOrderActivity extends BaseActivity {

	@Override
	protected int getLayoutId() {
		return R.layout.activity_share_order;
	}

	@Override
	protected void afterCreate(Bundle savedInstanceState) {
		tvContent.setText("晒单");
		
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		Fragment fragment = new TabShowHistoryFragment();
		transaction.replace(R.id.fl_content, fragment);
		transaction.commit();
	}

	// 启动ShareOrderActivity
	public static void start(Context context) {
		Intent intent = new Intent(context, ShareOrderActivity.class);
		context.startActivity(intent);
	}
}