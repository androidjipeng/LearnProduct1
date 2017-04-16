package com.learn.soft.product.main.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.learn.soft.product.jni.BaseFragment;
import com.learn.soft.product.util.BundleKey;
import com.learn.soft.product1.R;
import com.wangyi.lelegou.maofengqi.ui.activity.MainActivity;

/**
 * **********************************************************
 * <p/>
 * 说明：
 * <p/>
 * 作者：cailin
 * <p/>
 * 创建日期：2016/1/29
 * <p/>
 * 描述：
 * **********************************************************
 */
public class ShowLoadImageFragment extends BaseFragment implements View.OnClickListener {

    public static ShowLoadImageFragment instance(int position){
        ShowLoadImageFragment frg=new ShowLoadImageFragment();
        Bundle args=new Bundle();
        args.putInt(BundleKey.Bundle_KEY_POSITION, position);
        frg.setArguments(args);
        return frg;
    }

    private int mPosition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args=getArguments()!=null? getArguments(): savedInstanceState;
        if (args!=null){
            mPosition=args.getInt(BundleKey.Bundle_KEY_POSITION);
        }
        View v = inflater.inflate(R.layout.activity_splash_first_frg, null);
        ImageView iv= (ImageView) v.findViewById(R.id.iv_show_splash_pic);
        if (mPosition==0){
            iv.setImageResource(R.drawable.ic_show_load1);
        }else if (mPosition==1){
            iv.setImageResource(R.drawable.ic_show_load2);
        }else if (mPosition==2){
            iv.setImageResource(R.drawable.ic_show_load3);
        }else if (mPosition==3){
            iv.setImageResource(R.drawable.ic_show_load4);
        }else if (mPosition==4){
            iv.setImageResource(R.drawable.ic_show_load5);
        }else if (mPosition==5){
            iv.setImageResource(R.drawable.ic_show_load6);
        }else {
            iv.setImageResource(R.drawable.ic_show_load1);
        }

        return v;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_show_splash_pic:
                break;
        }
    }
}
