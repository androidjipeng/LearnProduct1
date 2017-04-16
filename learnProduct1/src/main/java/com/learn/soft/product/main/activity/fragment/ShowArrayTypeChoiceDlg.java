package com.learn.soft.product.main.activity.fragment;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.learn.soft.product.util.UtilConversionHelper;
import com.learn.soft.product1.R;
import com.wangyi.lelegou.maofengqi.utils.ListViewUtils;

/**
 * **********************************************************
 * <p/>
 * 说明：在单机TextView的位置显示下拉列表功能
 * <p/>
 * 作者：
 * <p/>
 * 创建日期：2014/12/9
 * <p/>
 * 描述： **********************************************************
 */
public class ShowArrayTypeChoiceDlg extends DialogFragment {
	public static final String BUNDLE_KEY_TITLE = "BUNDLE_KEY_TITLE";
	public static final String BUNDLE_KEY_SELECT_ITEM = "BUNDLE_KEY_SELECT_ITEM";
	public static final String BUNDLE_KEY_ALL_ITEM = "BUNDLE_KEY_ALL_ITEM";
	private ListView mLVChoice;
	// private TextView mTvTitle;

	private String mSelectItem;
	private DlgChoiceAdapter mAdapterChoice;
	private ArrayList<String> mListType;



	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NO_FRAME, 0);
	}

	@Override
	public void onStart() {
		super.onStart();
		       Window window = getDialog().getWindow();
		       WindowManager.LayoutParams windowParams = window.getAttributes();
		       windowParams.alpha=1f;
		       windowParams.dimAmount = 1f;
		       window.setAttributes(windowParams);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.dlg_show_array_type_choice, container,false);
		mLVChoice = (ListView) view.findViewById(R.id.lv_front_user_choice_dlg);
		// mTvTitle= (TextView) view.findViewById(R.id.dlg_title);
		// mLVChoice.setOnItemClickListener(this);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle arg0) {
		super.onActivityCreated(arg0);
		WindowManager.LayoutParams lp = getDialog().getWindow().getAttributes();
		WindowManager wm = (WindowManager) getActivity().getSystemService(
				Context.WINDOW_SERVICE);
		int mScreenWidth = wm.getDefaultDisplay().getWidth();
		int mScreenHeight = wm.getDefaultDisplay().getHeight();
		lp.gravity = Gravity.LEFT | Gravity.TOP;
		lp.x = 0;
		lp.y = UtilConversionHelper.dip2px(getActivity(), 42 + 46/* + 41*/);
		lp.width = (int) (0.5f * mScreenWidth);
		// lp.height =(int) ( mScreenHeight-
		// UtilConversionHelper.dip2px(getActivity(), 42+46+41+25));
		Bundle args = getArguments();
		mSelectItem = args.getString(BUNDLE_KEY_SELECT_ITEM);
		String title = args.getString(BUNDLE_KEY_TITLE);
		// mTvTitle.setText(title);
		mListType = (ArrayList<String>) args
				.getSerializable(BUNDLE_KEY_ALL_ITEM);

		if (mAdapterChoice == null) {
			mAdapterChoice = new DlgChoiceAdapter(getActivity());
		} else {
			mAdapterChoice.notifyDataSetChanged();
		}
		mLVChoice.setAdapter(mAdapterChoice);

		int actualHeight = ListViewUtils.getListViewHeight(mLVChoice);
		int height = (int) (mScreenHeight - UtilConversionHelper.dip2px(
				getActivity(), 42 + 46 + /* 41 + */25));
		if (actualHeight > height) {
			lp.height = height;
		} else {
			lp.height = actualHeight + 5;
		}

		getDialog().setCanceledOnTouchOutside(true);
	}

	// 下拉选中的文字
	public void setSelect(int index) {
		View view = mLVChoice.getChildAt(index);
		TextView mTV = (TextView) view
				.findViewById(R.id.tv_user_choice_dlg_item);
		mTV.setTextColor(getResources().getColor(R.color.bg_common_red_color));
	}

	class DlgChoiceAdapter extends BaseAdapter implements View.OnClickListener {
		private Context mContext;

		public DlgChoiceAdapter(Context c) {
			mContext = c;
		}

		@Override
		public int getCount() {
			return mListType != null ? mListType.size() : 0;
		}

		@Override
		public String getItem(int position) {
			return mListType.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(
						R.layout.dlg_show_array_type_choice_item, null);
				holder = new ViewHolder();
				holder.mTV = (TextView) convertView
						.findViewById(R.id.tv_user_choice_dlg_item);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (position == 1) {
				holder.mTV.setTextColor(getResources().getColor(
						R.color.bg_common_red_color));
			} else if (position == 2) {
				holder.mTV.setTextColor(getResources().getColor(
						R.color.bg_common_red_color));
			} else {
				holder.mTV.setTextColor(getResources().getColor(
						R.color.black_color));
			}
			holder.mTV.setText(mListType.get(position));
			/*
			 * if (mListType.get(position).equals(mSelectItem)) {
			 * holder.mTV.setTextColor(getResources().getColor(
			 * R.color.bg_common_red_color)); } else {
			 * holder.mTV.setTextColor(getResources().getColor(
			 * R.color.black_color)); }
			 */
			holder.mTV.setTag(position);
			holder.mTV.setOnClickListener(this);
			return convertView;
		}

		@Override
		public void onClick(View view) {
			switch (view.getId()) {
			case R.id.tv_user_choice_dlg_item:
				Integer position = (Integer) view.getTag();
				dismiss();
				if (mTVInfoListener != null) {
					mTVInfoListener.refreshTVInfo(mListType.get(position),
							position);
				}

				break;
			}
		}
	}

	private class ViewHolder {
		TextView mTV;
	}

	public interface ResetTVShowInfo {// 刷新TV的背景监听
		public void refreshTVInfo(String s, int position);
	}

	private ResetTVShowInfo mTVInfoListener;

	public void registerTVInfoListener(ResetTVShowInfo mTVInfoListener) {
		this.mTVInfoListener = mTVInfoListener;
	}
}
