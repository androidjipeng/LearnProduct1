package com.wangyi.lelegou.maofengqi.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * 描述：ListView帮助类
 *
 * @author Doc.March
 * @TIME 2015-01-09
 */
public class ListViewUtils {

    private ListViewUtils() {
        // 不能被实例化
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 滚动ListView到指定位置
     *
     * @param pos
     */
    public static void setListViewPos(ListView listView, int pos) {
        if (android.os.Build.VERSION.SDK_INT >= 8) {
            listView.smoothScrollToPosition(pos);
        } else {
            listView.setSelection(pos);
        }
    }

    /**
     * 设置ListView高度，注意ListView子项必须为LinearLayout才能调用该方法
     *
     * @param listView
     */
    public static void setListViewHeight(ListView listView) {
        int totalHeight = 0;
        ListAdapter adapter = listView.getAdapter();
        if (null != adapter) {
            for (int i = 0; i < adapter.getCount(); i++) {
                View listItem = adapter.getView(i, null, listView);
                if (null != listItem) {
                    // 注意ListView子项必须为LinearLayout才能调用该方法
                    listItem.measure(0, 0);
                    totalHeight += listItem.getMeasuredHeight();
                }
            }
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight + (listView.getDividerHeight() * (listView.getCount() - 1));
            listView.setLayoutParams(params);
        }
    }

    /**
     * 获取ListView的高度
     *
     * @param listView
     * @return
     */
    public static int getListViewHeight(ListView listView) {
        int totalHeight = 0;
        ListAdapter adapter = listView.getAdapter();
        if (null != adapter) {
            for (int i = 0; i < adapter.getCount(); i++) {
                View listItem = adapter.getView(i, null, listView);
                if (null != listItem) {
                    // 注意ListView子项必须为LinearLayout才能调用该方法
                    listItem.measure(0, 0);
                    totalHeight += listItem.getMeasuredHeight();
                }
            }
            return totalHeight + (listView.getDividerHeight() * (listView.getCount() - 1));
        }
        return 0;
    }
}