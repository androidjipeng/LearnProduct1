package com.learn.soft.product.jni;

import android.os.Bundle;
import android.view.View;
import com.learn.soft.product.widget.swipeback.SwipeBackActivityBase;
import com.learn.soft.product.widget.swipeback.SwipeBackActivityHelper;
import com.learn.soft.product.widget.swipeback.SwipeBackLayout;
import com.learn.soft.product.widget.swipeback.Utils;

public abstract class BaseSwipeBackActivity extends BaseActivity implements SwipeBackActivityBase {

    protected SwipeBackActivityHelper mHelper;

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHelper = new SwipeBackActivityHelper(this);
        mHelper.onActivityCreate();
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mHelper.onPostCreate();
    }

    @Override
    public View findViewById(int id) {
        View v = super.findViewById(id);
        if (v == null && mHelper != null)
            return mHelper.findViewById(id);
        return v;
    }

    @Override
    public SwipeBackLayout getSwipeBackLayout() {
        return mHelper.getSwipeBackLayout();
    }

    @Override
    public void setSwipeBackEnable(boolean enable) {
        getSwipeBackLayout().setEnableGesture(enable);
    }

    @Override
    public void scrollToFinishActivity() {
        Utils.convertActivityToTranslucent(this);
        getSwipeBackLayout().scrollToFinishActivity();
    }

    @Override
    protected boolean toggleOverridePendingTransition() {
        return true;
    }

    @Override
    protected TransitionMode getOverridePendingTransitionMode() {
        return TransitionMode.RIGHT;
    }

}
