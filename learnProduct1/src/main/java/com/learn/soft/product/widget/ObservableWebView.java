package com.learn.soft.product.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * Created by Administrator on 2016/7/5.
 */
public class ObservableWebView extends WebView {

    public ObservableWebView(Context context) {
        super(context);

    }

    public ObservableWebView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public ObservableWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mListener!=null){
            mListener.onScroll(l,t, oldl, oldt);
        }
    }

    private OnScrollChangedCallback mListener;

    public void registerListener(OnScrollChangedCallback mListener) {
        this.mListener = mListener;
    }

    public interface OnScrollChangedCallback{
        public void  onScroll(int l, int t, int oldl, int oldt);
    }


}
