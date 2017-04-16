package com.learn.soft.product.main;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.learn.soft.product.bean.ProductTypeBean;
import com.learn.soft.product.bean.ProductTypeChildBean;
import com.learn.soft.product.main.activity.ShowWebViewInfoActivity;
import com.learn.soft.product.main.activity.fragment.ShowArrayTypeChoiceDlg;
import com.learn.soft.product.util.ApiWebCommon;
import com.learn.soft.product.util.BundleKey;
import com.learn.soft.product.util.MyLog;
import com.learn.soft.product.util.SendHttpUtils;
import com.learn.soft.product.util.StringUtil;
import com.learn.soft.product.util.ToastHelper;
import com.learn.soft.product1.R;
import com.wangyi.lelegou.maofengqi.view.NoCacheViewPager;

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
public class TabAllProductFragment extends BaseTabFragment implements
		View.OnClickListener, OnGoShowListener {

	private NoCacheViewPager mVpTabShow;
	// private PagerSlidingTabTitle mPstsIndicator;
	// private TextView mTvProductType;
	private int mSelectTypeId = 0;
	private final ArrayList<ProductTypeChildBean> mTypeList = new ArrayList<ProductTypeChildBean>();
	// private final ArrayList<ProductTypeChildBean> mListData =new
	// ArrayList<ProductTypeChildBean>();
	private int[] ids = { R.id.rb_0, R.id.rb_1, R.id.rb_2, R.id.rb_3, R.id.rb_4 };
	private RadioButton[] buttons = new RadioButton[ids.length];
	private View tabView;
	private int currIndex;// 当前页卡编号

	private int mPostion;
	private int mCateId;
	private String mCateName;
	
	private TextView tvArea;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		/**拿到商品id*/
		Bundle args = getArguments() != null ? getArguments()
				: savedInstanceState;
		if (args != null) {
			mPostion = args.getInt(BundleKey.Bundle_KEY_POSITION);
			mCateId = args.getInt(BundleKey.BUNDLE_KEY_CATE_ID);
			mCateName = args.getString("BUNDLE_KEY_CATE_NAME");
			Log.i("info1", "jp:" + mPostion + "---" + mCateId + "---"
					+ mCateName);
		}

		View view = inflater.inflate(R.layout.activity_main_tab_allproduct,
				container, false);
		//// TODO: 2017/3/16  
		mVpTabShow = (NoCacheViewPager) view.findViewById(R.id.vp_show_tab_allproduct);
		// mPstsIndicator = (PagerSlidingTabTitle)
		// view.findViewById(R.id.psts_tab_allproduct_indicator);
		// getProductType();
		// mTvProductType= (TextView)
		// view.findViewById(R.id.tv_show_select_type_product);
		// mTvProductType.setOnClickListener(this);

		for (int i = 0; i < ids.length; i++) {
			buttons[i] = (RadioButton) view.findViewById(ids[i]);
			buttons[i].setTag(i);
			buttons[i].setOnClickListener(this);
		}
		tabView = view.findViewById(R.id.tab_view);
		tvArea = (TextView) view.findViewById(R.id.tv_area);
		if (mCateId != 0) {
			buttons[0].setText(mCateName);
			buttons[0].setChecked(true);
			tvArea.setVisibility(View.VISIBLE);
		} else {
			tvArea.setVisibility(View.GONE);
		}
		tvArea.setOnClickListener(this);
		initTabView();

		showVpInfo();
		return view;
	}

	/*
	 * 初始化图片的位移像素
	 */
	private void initTabView() {
		Display display = getActivity().getWindow().getWindowManager()
				.getDefaultDisplay();
		// 得到显示屏宽度
		DisplayMetrics metrics = new DisplayMetrics();
		display.getMetrics(metrics);
		LayoutParams lp = (LayoutParams) tabView.getLayoutParams();
		int width = metrics.widthPixels / 5;
		lp.width = width;
		lp.leftMargin = mPostion * width;
		tabView.setLayoutParams(lp);
	}

	private void showVpInfo() {
		mVpTabShow.setAdapter(new AllProductFragmentDataAdapter(
				getChildFragmentManager()));
		// mPstsIndicator.setViewPager(mVpTabShow);
		mVpTabShow.setOnPageChangeListener(new NoCacheViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				Log.i("info1", "==================arg0:"+arg0);
				currIndex = arg0;
				buttons[currIndex].setChecked(true);
			}

			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				LinearLayout.LayoutParams ll = (android.widget.LinearLayout.LayoutParams) tabView
						.getLayoutParams();

				if (currIndex == position) {
					ll.leftMargin = (int) (currIndex * tabView.getWidth() + positionOffset
							* tabView.getWidth());
				} else if (currIndex > position) {
					ll.leftMargin = (int) (currIndex * tabView.getWidth() - (1 - positionOffset)
							* tabView.getWidth());
				}
				tabView.setLayoutParams(ll);
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
		mVpTabShow.setCurrentItem(mPostion);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.tv_area:
			MyLog.e("jipeng","===================tv_area");
			setCommonWebViewShow("", "http://91lelegou.com/?/carousel/carousel/three_two");
			break;
		case R.id.tv_show_select_type_product:
			MyLog.e("jipeng","TabAllProductFragment==============tv_show_select_type_product");
			mVpTabShow.setCurrentItem(0);
			clickBtnType();
			break;
		default:
			int index = (Integer) view.getTag();
			if (index == 0) {
				clickBtnType();
				mVpTabShow.setCurrentItem(index);
			} else {
				// 点击清空
				mCateId = 0;
				mSelectTypeId = 0;
				buttons[0].setText("全部");
				tvArea.setVisibility(View.GONE);
				mVpTabShow.setCurrentItem(index);
			}
			break;
		}
	}
	
	public void setCommonWebViewShow(String title, String url) {
		Intent intent = new Intent(getActivity(), ShowWebViewInfoActivity.class);
		Bundle args = new Bundle();
		args.putString(BundleKey.Bundle_KEY_Title, title);
		MyLog.i("xiaocai", "===================url=" + url);
		MyLog.e("jp", "====TabAllProductFragment----->url=" + url);
		args.putString(BundleKey.Bundle_KEY_Url, url);
		intent.putExtras(args);
		startActivity(intent);
	}

	public void getProductType() {
		showProgressInfo("");
		String url = String.format("%1s%2s",
				ApiWebCommon.API_COMMON.Api_Common_Url,
				ApiWebCommon.API_COMMON.Api_Index_Product_Category);
		SendHttpUtils.getInstance().postCommon(url, url, null,
				new SendHttpUtils.HttpCallback() {
					@Override
					public void onSuccess(String data) {
						dismissProgress();
						if (StringUtil.isNotEmpty(data)) {
							MyLog.i("xiaocai",
									"======================Api_Index_Product_Category string=" + data);
							MyLog.e("jp",
									"====TabAllProductFragment---->Api_Index_Product_Category string=" + data);
							ProductTypeBean mData = null;
							try {
								final TypeToken type = new TypeToken<ProductTypeBean>() {
								};
								mData = new Gson().fromJson(data,
										type.getType());
								if (mData != null && mData.status == 1) {
									mTypeList.clear();
									int number = mData.listType != null ? mData.listType
											.size() : 0;
									if (number > 0) {
										typeAddFirst();
										mTypeList.addAll(mData.listType);
									}
								}

								showDlgChoice();

							} catch (Exception e) {
								e.printStackTrace();
							}

						}

					}

					@Override
					public void onError(String msg) {
						dismissProgress();
						ToastHelper.toast(msg);
					}
				});
	}

	private void typeAddFirst() {
		ProductTypeChildBean b = new ProductTypeChildBean();
		b.cateid = "0";
		b.name = "全部";
		mTypeList.add(b);
	}

	public String getSelectCateId() {
		if (mCateId != 0) {
			return String.valueOf(mCateId);
		}
		int number = mTypeList.size();
		if (number == 0) {
			return "0";
		} else {
			try {
				return mTypeList.get(mSelectTypeId).cateid;
			} catch (Exception e) {
				e.printStackTrace();
				return "0";
			}
		}
	}

	private void clickBtnType() {
		// int position=mVpTabShow.getCurrentItem();
		int number = mTypeList.size();
		if (number <= 1) {
			getProductType();
		} else {
			showDlgChoice();
		}
	}

	private ShowArrayTypeChoiceDlg mFindDlg;
	public final String TAG_SHOW_FIND_DLG = "TAG_SHOW_FIND_DLG";

	private void showDlgChoice() {
		mFindDlg = new ShowArrayTypeChoiceDlg();
		mFindDlg.registerTVInfoListener(new ShowArrayTypeChoiceDlg.ResetTVShowInfo() {
			@Override
			public void refreshTVInfo(String s, int position) {
				mCateId = 0;
				mSelectTypeId = position;
				String cateId = mTypeList.get(mSelectTypeId).cateid;
				if (Integer.parseInt(cateId) == 52
						|| Integer.parseInt(cateId) == 53) {
					tvArea.setVisibility(View.VISIBLE);
				} else {
					tvArea.setVisibility(View.GONE);
				}
				// mTvProductType.setText(mTypeList.get(mSelectTypeId).name);
				buttons[0].setText(mTypeList.get(mSelectTypeId).name);
				for (OnGoShowListener listener : mListener) {
					listener.onGoShowInfo(mVpTabShow.getCurrentItem(), cateId);
				}
				// mPageIndex=1;
				// getProductList();
			}
		});
		getActivity().getSupportFragmentManager().beginTransaction()
				.add(mFindDlg, TAG_SHOW_FIND_DLG);
		if (!mFindDlg.isVisible()) {
			ArrayList<String> listArr = new ArrayList<String>();
			int selectIndex = mSelectTypeId;
			for (int i = 0; i < mTypeList.size(); i++) {
				if (mCateId != 0
						&& Integer.parseInt(mTypeList.get(i).cateid) == mCateId) {
					selectIndex = i;
				}
				listArr.add(mTypeList.get(i).name);
			}
			
			Bundle args = new Bundle();
			args.putString(ShowArrayTypeChoiceDlg.BUNDLE_KEY_TITLE, "请选择商品分类");
			args.putString(ShowArrayTypeChoiceDlg.BUNDLE_KEY_SELECT_ITEM,
					mTypeList.get(selectIndex).name);//mSelectTypeId
			args.putSerializable(ShowArrayTypeChoiceDlg.BUNDLE_KEY_ALL_ITEM,
					listArr);
			mFindDlg.setArguments(args);
			mFindDlg.show(getActivity().getSupportFragmentManager(),
					TAG_SHOW_FIND_DLG);
		} else {
			mFindDlg.dismiss();
		}
	}

	@Override
	public void onGoShowInfo(int position, String caiId) {

	}

	private final List<OnGoShowListener> mListener = new ArrayList<OnGoShowListener>();

	public void registerListener(OnGoShowListener listener) {
		mListener.add(listener);
		MyLog.i("xiaocai", "=========================getItem listener size " + mListener.size());
	}

	public void unRegisterListener(OnGoShowListener listener) {
		mListener.remove(listener);
		MyLog.i("xiaocai", "==========================getItem listener size " + mListener.size());
	}

	private class AllProductFragmentDataAdapter extends
			FragmentStatePagerAdapter {

		private final String[] mTitles = { "全部", "即将揭晓", "人气", "最新", "价格" };

		public AllProductFragmentDataAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			TabAllProductChildFragment frg = TabAllProductChildFragment
					.getInstance(i, null);
			registerListener(frg);
			return frg;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			TabAllProductChildFragment fragment = (TabAllProductChildFragment) object;
			unRegisterListener(fragment);
			super.destroyItem(container, position, object);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return mTitles[position];
		}

		@Override
		public int getCount() {
			return mTitles != null ? mTitles.length : 0;
		}

	}

}
