package com.learn.soft.product.main.activity.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.*;
import com.learn.soft.product.util.BundleKey;
import com.learn.soft.product.util.ToastHelper;
import com.learn.soft.product.util.UtilConversionHelper;
import com.learn.soft.product1.R;
import com.wangyi.lelegou.maofengqi.ui.activity.CreateNewCircleActivity;

public class ShowSelectTypeDlg extends DialogFragment implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.custom_dlg);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dlg_type_choice_two_go, null);
        view.findViewById(R.id.tv_dlg_show_choice1).setOnClickListener(this);
        view.findViewById(R.id.tv_dlg_show_choice2).setOnClickListener(this);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        WindowManager.LayoutParams lp = getDialog().getWindow().getAttributes();
        Bundle args = getArguments();
        lp.gravity = Gravity.RIGHT| Gravity.TOP;
        int x=args.getInt(BundleKey.BUNDLE_KEY_X);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
//        lp.width= screenWidth*3 / 7;
        lp.width= UtilConversionHelper.dip2px(getActivity(), 120);

        lp.x =screenWidth-x;
        lp.y = args.getInt(BundleKey.BUNDLE_KEY_Y);
        getDialog().setCanceledOnTouchOutside(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_dlg_show_choice1:
//                startActivity(new Intent(getActivity(), ShowBillListActivity.class));
                if (getActivity()!=null){
                    CreateNewCircleActivity.start(getActivity());
                }
                dismiss();
                break;
            case R.id.tv_dlg_show_choice2:
                dismiss();
                if (getActivity()!=null){
                    ShowConfirmInputDlg.showDlg(getActivity().getSupportFragmentManager(), null);
                }
//                if (getActivity()!=null){
//                    getActivity().finish();
//                }
                break;
        }
    }
}
