package com.learn.soft.product.main;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.learn.soft.product.util.ApiWebCommon;
import com.learn.soft.product1.R;

/**
 * **********************************************************
 * <p/>
 * 说明：
 * <p/>
 * 作者：cailin
 * <p/>
 * 创建日期：2015/4/6
 * <p/>
 * 描述： **********************************************************
 */
public class TabIndexFragment extends BaseTabFragment implements
		View.OnClickListener{
	
	private ViewPager mVpTimeLimit;
	private View mAreaTime;
	private View mAreaHeadTitle;
	private int mCurrentIndex;
	private TextView[] mTvTitles;
	private ImageView[] mIvArrow;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_main_tab_index,
				container, false);
		mTvTitles = new TextView[3];
		mIvArrow = new ImageView[3];
		mTvTitles[0] = (TextView) view.findViewById(R.id.tv_tab_show_title1);
		mTvTitles[1] = (TextView) view.findViewById(R.id.tv_tab_show_title2);
		mTvTitles[2] = (TextView) view.findViewById(R.id.tv_tab_show_title3);

		mIvArrow[0] = (ImageView) view.findViewById(R.id.iv_tab_show_title1);
		mIvArrow[1] = (ImageView) view.findViewById(R.id.iv_tab_show_title2);
		mIvArrow[2] = (ImageView) view.findViewById(R.id.iv_tab_show_title3);

		for (int i = 0; i < mTvTitles.length; i++) {
			mTvTitles[i].setOnClickListener(this);
		}		
		return view;
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.tv_tab_show_title1:
			mVpTimeLimit.setCurrentItem(0);
			break;
		case R.id.tv_tab_show_title2:
			mVpTimeLimit.setCurrentItem(1);
			break;
		case R.id.tv_tab_show_title3:
			mVpTimeLimit.setCurrentItem(2);
			break;
		case R.id.btn_show_go_allproduct:
//			showGoTabIndex(1, 2, 0, "");
		case R.id.btn_two:
			//showGoTabIndex(1, 0, cateId2, cateName2);
			break;
		case R.id.btn_three:
			// showGoTabIndex(1, 0, cateId3, cateName3);
			break;
		case R.id.btn_show_go_newsinfo:
//			showGoTabIndex(2, 0, 0, "");
			break;
		case R.id.btn_show_go_todaylimit:{
			StringBuffer sb = new StringBuffer();
			sb.append(ApiWebCommon.API_COMMON.Api_Common_Url);
			sb.append(ApiWebCommon.API_COMMON.Web_Index_Time_Limit);
			String url = sb.toString();
		//	setCommonWebViewShow("", url);
			}
			break;
		
		}
	}

//	private void showGoTabIndex(int position, int secondPosition, int cateId,
//			String cateName) {
//		if (getActivity() instanceof MainActivity) {
//			MainActivity activity = (MainActivity) getActivity();
//			activity.initDataSelectPosition(position, secondPosition, cateId,
//					cateName);
//		}
//	}


	



}
