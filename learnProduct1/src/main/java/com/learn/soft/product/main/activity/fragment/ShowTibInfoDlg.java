package com.learn.soft.product.main.activity.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.DisplayMetrics;
import android.view.*;
import com.learn.soft.product.widget.fuckviewpager.FuckViewPager;
import com.learn.soft.product1.R;

/**
 * **********************************************************
 * <p/>
 * 说明：显示是否确定对话框
 * <p/>
 * 作者：
 * <p/>
 * 创建日期：2017/2/14
 * <p/>
 * 描述：
 * **********************************************************
 */
public class ShowTibInfoDlg extends DialogFragment implements View.OnClickListener {

    private FuckViewPager mVpShow;

    public static DialogFragment showDlg(FragmentManager manager ) {

        ShowTibInfoDlg dlg = new ShowTibInfoDlg();
        dlg.show(manager, null);
        return dlg;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.custom_dlg);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dlg_show_tib_info, container);
        mVpShow= (FuckViewPager) view.findViewById(R.id.vp_showinfo);
        mVpShow.getViewPager().setAdapter(new VPPagerAdapter(getChildFragmentManager()));
        view.findViewById(R.id.iv_show_close).setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        WindowManager.LayoutParams layoutParams = getDialog().getWindow().getAttributes();
        Display display = getActivity().getWindow().getWindowManager()
                .getDefaultDisplay();
        // 得到显示屏宽度
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        layoutParams.width = metrics.widthPixels;
        layoutParams.height = metrics.heightPixels;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_show_close:
                dismiss();
                break;
        }
    }

    private class VPPagerAdapter extends FragmentPagerAdapter {
        public VPPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 6;
        }

        @Override
        public Fragment getItem(int i) {
            return ShowLoadImageFragment.instance(i);
        }

    }

}
