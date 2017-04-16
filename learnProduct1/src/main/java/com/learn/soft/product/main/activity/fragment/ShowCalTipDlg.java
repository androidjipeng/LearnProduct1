package com.learn.soft.product.main.activity.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.learn.soft.product.util.BundleKey;
import com.learn.soft.product1.R;

public class ShowCalTipDlg extends DialogFragment implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dlg_type_choice_more, null);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        WindowManager.LayoutParams lp = getDialog().getWindow().getAttributes();
        Bundle args = getArguments();
        lp.gravity = Gravity.RIGHT|Gravity.TOP;
        int x=args.getInt(BundleKey.BUNDLE_KEY_X);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        lp.width= screenWidth*9 / 10;

        lp.x =x;
        lp.y = args.getInt(BundleKey.BUNDLE_KEY_Y);
        getDialog().setCanceledOnTouchOutside(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
        }
    }
}
