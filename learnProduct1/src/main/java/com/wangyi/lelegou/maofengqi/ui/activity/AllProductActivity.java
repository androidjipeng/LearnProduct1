package com.wangyi.lelegou.maofengqi.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.learn.soft.product.main.TabAllProductFragment;
import com.learn.soft.product.util.BundleKey;
import com.learn.soft.product1.R;
import com.wangyi.lelegou.maofengqi.base.BaseActivity;

/**
 * 所有商品
 * 
 * @author Doc.March
 * 
 */
public class AllProductActivity extends BaseActivity {

	private int position;
	private int cateId;
	private String cateName;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_all_product;
	}

	@Override
	protected void afterCreate(Bundle savedInstanceState) {
		tvContent.setText("所有商品");

		Bundle bundle = getIntent().getExtras();
		position = bundle.getInt("position", 0);
		cateId = bundle.getInt("cateId", 0);
		cateName = bundle.getString("cateName", "");

		showTabAllProductFragment();
	}

	private void showTabAllProductFragment() {
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		Fragment fragment = new TabAllProductFragment();
		Bundle bundle = new Bundle();
		bundle.putInt(BundleKey.Bundle_KEY_POSITION, position);
		bundle.putInt(BundleKey.BUNDLE_KEY_CATE_ID, cateId);
		bundle.putString(BundleKey.BUNDLE_KEY_CATE_NAME, cateName);
		fragment.setArguments(bundle);
		transaction.replace(R.id.fl_content, fragment);
		transaction.commit();
	}

	// 启动AllProductActivity
	public static void start(Context context, int position, int cateId,
			String cateName) {
		Intent intent = new Intent(context, AllProductActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("position", position);
		bundle.putInt("cateId", cateId);
		bundle.putString("cateName", cateName);
		intent.putExtras(bundle);
		context.startActivity(intent);
	}
}