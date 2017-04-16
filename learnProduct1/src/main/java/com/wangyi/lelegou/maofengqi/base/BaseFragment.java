package com.wangyi.lelegou.maofengqi.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * **********************************************************
 * <p/>
 * 说明:基类BaseFragment
 * <p/>
 * 作者:@Doc.March
 * <p/>
 * 创建日期:2016-9-29
 * <p/>
 * 描述:
 * <p/>
 * **********************************************************
 */
public abstract class BaseFragment extends Fragment {

	protected View mRootView;

	protected Activity mActivity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		mRootView = inflater.inflate(getLayoutId(), container, false);
		return mRootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mActivity = getActivity();
		afterCreate(savedInstanceState);
	}

	protected View findViewById(int id) {
		return getActivity().findViewById(id);
	}

	protected abstract int getLayoutId();

	protected abstract void afterCreate(Bundle savedInstanceState);

	public final void showProgressInfo(final String s) {
		if (getActivity() != null && getActivity() instanceof BaseActivity) {
			((BaseActivity) getActivity()).showProgressInfo(s);
		}
	}

	public final void dismissProgress() {
		if (getActivity() != null && getActivity() instanceof BaseActivity) {
			((BaseActivity) getActivity()).dismissProgress();
		}
	}
}