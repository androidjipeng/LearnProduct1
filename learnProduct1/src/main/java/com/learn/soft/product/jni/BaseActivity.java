package com.learn.soft.product.jni;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.WindowManager;

import com.learn.soft.product.util.ActivityFrgManager;
import com.learn.soft.product.util.SystemBarTintManager;
import com.learn.soft.product1.R;
import com.umeng.analytics.MobclickAgent;

public abstract class BaseActivity extends FragmentActivity implements ILoading {

    private Loading mLoading;
    private final List<String> mTagRequestList=new ArrayList<String>();

    public enum TransitionMode {
        LEFT, RIGHT, TOP, BOTTOM, SCALE, FADE
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (toggleOverridePendingTransition()) {
            switch (getOverridePendingTransitionMode()) {
                case LEFT:
                    overridePendingTransition(R.anim.left_in, R.anim.left_out);
                    break;
                case RIGHT:
                    overridePendingTransition(R.anim.right_in, R.anim.right_out);
                    break;
                case TOP:
                    overridePendingTransition(R.anim.top_in, R.anim.top_out);
                    break;
                case BOTTOM:
                    overridePendingTransition(R.anim.bottom_in, R.anim.bottom_out);
                    break;
                case SCALE:
                    overridePendingTransition(R.anim.scale_in, R.anim.scale_out);
                    break;
                case FADE:
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    break;
            }
        }
        super.onCreate(savedInstanceState);

        if (isShowTranStatus()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                try {
                    setTranslucentStatus(true);
                    SystemBarTintManager tintManager = new SystemBarTintManager(this);
                    tintManager.setStatusBarTintEnabled(true);
                    tintManager.setStatusBarTintResource(R.color.bg_common_red_color);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


        if (isAddToAllExit()){
            ActivityFrgManager.getInstance().registerActivity(this);
        }
        mLoading = initLoading();
        onBaseCreate(savedInstanceState);
        initViews();
    }

    protected boolean isAddToAllExit(){
        return true;
    }

    protected boolean toggleOverridePendingTransition(){
        return false;
    }

    protected TransitionMode getOverridePendingTransitionMode(){
        return TransitionMode.RIGHT;
    }


    protected abstract void onBaseCreate(Bundle savedInstanceState);

    protected abstract void initViews();

    protected boolean isShowTranStatus(){
        return true;
    }

    @Override
    public final Context getLoadingContext() {
        return this;
    }

    @Override
    public final Loading getLoading() {
        return mLoading;
    }

    @Override
    public final Loading initLoading() {
        return new Loading(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    public final void showProgressInfo(final String s) {
        if (mLoading != null) {
            mLoading.show(s);
        }
    }

    public final void dismissProgress() {
        if (mLoading != null) {
            mLoading.dismiss();
        }
    }


    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    public void clearRequestTag(){
        mTagRequestList.clear();
    }

    public void registerRequestTag(String tag){
        mTagRequestList.add(tag);
    }

    @Override
    public void finish() {
        super.finish();
        if (toggleOverridePendingTransition()) {
            switch (getOverridePendingTransitionMode()) {
                case LEFT:
                    overridePendingTransition(R.anim.left_in,R.anim.left_out);
                    break;
                case RIGHT:
                    overridePendingTransition(R.anim.right_in,R.anim.right_out);
                    break;
                case TOP:
                    overridePendingTransition(R.anim.top_in,R.anim.top_out);
                    break;
                case BOTTOM:
                    overridePendingTransition(R.anim.bottom_in,R.anim.bottom_out);
                    break;
                case SCALE:
                    overridePendingTransition(R.anim.scale_in,R.anim.scale_out);
                    break;
                case FADE:
                    overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                    break;
            }
        }
    }


}
