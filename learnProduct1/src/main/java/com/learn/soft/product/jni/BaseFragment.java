package com.learn.soft.product.jni;

import android.support.v4.app.Fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * **********************************************************
 * <p/>
 * 说明：fragment的基类，需要转圈圈的要继承这个类
 * <p/>
 * 作者：cailin
 * <p/>
 * 创建日期：2014/11/23
 * <p/>
 * 描述：当多个线程同时要转圈圈时，会实现最后一个才消失的效果
 * **********************************************************
 */
public class BaseFragment extends Fragment {

    private final List<String> mTagRequestList=new ArrayList<String>();

    public final void showProgressInfo(final String s) {
        if (getActivity()!=null && getActivity() instanceof com.learn.soft.product.jni.BaseActivity){
            ((com.learn.soft.product.jni.BaseActivity)getActivity()).showProgressInfo(s);
        }
        if (getActivity()!=null && getActivity() instanceof com.wangyi.lelegou.maofengqi.base.BaseActivity){
            ((com.wangyi.lelegou.maofengqi.base.BaseActivity)getActivity()).showProgressInfo(s);
        }
    }

    public final void dismissProgress() {
        if (getActivity()!=null && getActivity() instanceof com.learn.soft.product.jni.BaseActivity){
            ((com.learn.soft.product.jni.BaseActivity)getActivity()).dismissProgress();
        }
        if (getActivity()!=null && getActivity() instanceof com.wangyi.lelegou.maofengqi.base.BaseActivity){
            ((com.wangyi.lelegou.maofengqi.base.BaseActivity)getActivity()).dismissProgress();
        }
    }

    public void clearRequestTag(){
        mTagRequestList.clear();
    }

    public void registerRequestTag(String tag){
        mTagRequestList.add(tag);
    }


}
