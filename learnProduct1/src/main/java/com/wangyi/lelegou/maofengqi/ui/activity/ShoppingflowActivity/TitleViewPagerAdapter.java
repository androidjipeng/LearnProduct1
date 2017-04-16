package com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by jipeng on 2017/3/16.
 */

public class TitleViewPagerAdapter extends PagerAdapter{
    private Context context;
    private ArrayList<String> titlelist;
    ArrayList<View> viewContainter;

    @Override
    public int getCount() {
        return viewContainter.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titlelist.get(position);
    }


    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView(viewContainter.get(position));

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ((ViewPager) container).addView(viewContainter.get(position));
        return viewContainter.get(position);

    }

    public TitleViewPagerAdapter(Context context, ArrayList<String> titlelist,ArrayList<View> viewContainter) {
        this.context=context;
        this.titlelist=titlelist;
        this.viewContainter=viewContainter;
    }
}
