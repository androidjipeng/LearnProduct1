package com.learn.soft.product.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.learn.soft.product.widget.PagerSlidingTabTitle;
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
 * 描述：
 * **********************************************************
 */
public class TabShowHistoryFragment extends BaseTabFragment implements View.OnClickListener {

    private ViewPager mVpTabShow;
    private PagerSlidingTabTitle mPstsIndicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main_tab_showhistory, container, false);

        mVpTabShow= (ViewPager) view.findViewById(R.id.vp_show_tab_showhistory);
        mPstsIndicator = (PagerSlidingTabTitle) view.findViewById(R.id.psts_tab_showhistory_indicator);
        mVpTabShow.setAdapter(new ShowHistoryFragmentDataAdapter(getChildFragmentManager()));
        mPstsIndicator.setViewPager(mVpTabShow);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

        }

    }



    private class ShowHistoryFragmentDataAdapter extends FragmentStatePagerAdapter {

        private final String[] mTitles={"最新晒单", "人气晒单", "评论最多" };

        public ShowHistoryFragmentDataAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {

            return TabShowHistoryChildFragment.getInstance(i);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public int getCount() {
            return mTitles!=null? mTitles.length: 0;
        }

    }

}
