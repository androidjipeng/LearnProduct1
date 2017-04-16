package com.wangyi.lelegou.maofengqi.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.learn.soft.product.util.ActivityFrgManager;
import com.learn.soft.product1.R;
import com.wangyi.lelegou.maofengqi.view.ILoading;
import com.wangyi.lelegou.maofengqi.view.Loading;

/**
 * **********************************************************
 * <p/>
 * 说明:基类FragmentActivity
 * <p/>
 * 作者:@Doc.March
 * <p/>
 * 创建日期:2016-9-29
 * <p/>
 * 描述:
 * <p/>
 * **********************************************************
 */
public abstract class BaseActivity extends FragmentActivity implements ILoading {

	protected Activity mActivity;
	private Loading mLoading;

	protected ImageButton ivSearch;
	protected ImageButton ivBack;
	protected EditText etSearch;
	protected TextView tvContent;
	protected ImageButton ivBtn;
	protected RelativeLayout layoutRight;
	protected TextView tvRight;
	protected ImageButton ivRight;
	protected TextView tvShowBuyNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = this;
		mLoading = initLoading();
		if (beforeCreate()) {
			return;
		}
		setContentView(getLayoutId());
		initTitleBar();
		afterCreate(savedInstanceState);
        ActivityFrgManager.getInstance().registerActivity(this);
	}

	private void initTitleBar() {
		ivSearch = (ImageButton) findViewById(R.id.iv_search);
		ivBack = (ImageButton) findViewById(R.id.iv_back);
		etSearch = (EditText) findViewById(R.id.et_search);
		tvContent = (TextView) findViewById(R.id.tv_content);
		ivBtn = (ImageButton) findViewById(R.id.iv_btn);
		layoutRight = (RelativeLayout) findViewById(R.id.layout_right);
		tvRight = (TextView) findViewById(R.id.tv_right);
		ivRight = (ImageButton) findViewById(R.id.iv_right);
		tvShowBuyNumber = (TextView) findViewById(R.id.tv_show_buy_number);

		if (ivBack != null) {
			ivBack.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					finish();
				}
			});
		}
		if (tvContent != null) {
			tvContent.setText("");
		}
	}

	protected boolean beforeCreate() {
		return false;
	}

	protected abstract int getLayoutId();

	protected abstract void afterCreate(Bundle savedInstanceState);

	@Override
	public final Loading getLoading() {
		return mLoading;
	}

	@Override
	public final Loading initLoading() {
		return new Loading(this);
	}

	@Override
	public final Context getLoadingContext() {
		return this;
	}

	public final void showProgressInfo(final String s) {
		if (mLoading != null) {
			mLoading.show(s);
		}
	}

	public final void dismissProgress() {
		if (mLoading != null) {
			mLoading.dismiss();
		}
	}

}