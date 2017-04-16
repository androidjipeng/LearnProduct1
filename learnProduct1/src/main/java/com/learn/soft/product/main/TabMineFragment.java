package com.learn.soft.product.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
public class TabMineFragment extends BaseTabFragment implements View.OnClickListener {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main_tab_mine, container, false);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login_status:
                break;

        }

    }


}
