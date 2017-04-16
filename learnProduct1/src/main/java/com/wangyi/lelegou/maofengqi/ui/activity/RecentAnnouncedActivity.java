package com.wangyi.lelegou.maofengqi.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.learn.soft.product.main.TabNewsInfoFragment;
import com.learn.soft.product1.R;
import com.wangyi.lelegou.maofengqi.base.BaseActivity;

/**
 * 最新揭晓
 * 
 * @author Doc.March
 * 
 */
public class RecentAnnouncedActivity extends BaseActivity {

	@Override
	protected int getLayoutId() {
		return R.layout.activity_recent_announced;
	}

	@Override
	protected void afterCreate(Bundle savedInstanceState) {
		tvContent.setText("最新揭晓");
		
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		Fragment fragment = new TabNewsInfoFragment();
		transaction.replace(R.id.fl_content, fragment);
		transaction.commit();
	}

	// 启动RecentAnnouncedActivity
	public static void start(Context context) {
		Intent intent = new Intent(context, RecentAnnouncedActivity.class);
		context.startActivity(intent);
	}
}