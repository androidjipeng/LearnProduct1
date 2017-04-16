package com.learn.soft.product.widget;

import android.util.SparseArray;
import android.view.View;

/**
 * **********************************************************
 * <p/>
 * 说明：
 * <p/>
 * 作者：cailin
 * <p/>
 * 创建日期：2016/1/19
 * <p/>
 * 描述：
 * **********************************************************
 */
public class ListViewHoder {

    SparseArray<View> holderViews = new SparseArray<View>();

    public Boolean holderView(View view) {
        if (null != view) {
            holderViews.put(view.getId(), view);
            return true;
        } else {
            return false;
        }
    }

    public View findViewById(int viewId) {
        return holderViews.get(viewId);
    }

}