package com.learn.soft.product.main.activity.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.*;
import android.widget.TextView;
import com.learn.soft.product1.R;

/**
 * **********************************************************
 * <p/>
 * 说明：显示是否确定对话框
 * <p/>
 * 作者：
 * <p/>
 * 创建日期：2014/9/5
 * <p/>
 * 描述：
 * **********************************************************
 */
public class ShowConfirmDlg extends DialogFragment implements View.OnClickListener, DialogInterface.OnKeyListener {

    private TextView mTvShowInfo1;
    private static View.OnClickListener sOnClickOkListener;
    private static String sInfo;
    private static boolean sIsOk;

    public static DialogFragment showDlg(FragmentManager manager, String s) {
        return showDlg(manager, null, s);
    }

    public static DialogFragment showDlg(FragmentManager manager, View.OnClickListener listener, String s) {
        return showDlg(manager, listener, s, false);
    }

    public static DialogFragment showDlg(FragmentManager manager, View.OnClickListener listener, String s, boolean isJustShowOk) {
        return showDlg(manager, listener, s, isJustShowOk, false);
    }

    public static DialogFragment showDlg(FragmentManager manager, View.OnClickListener listener, String s, boolean isJustShowOk, boolean isShowScroolView) {
        sOnClickOkListener = listener;
        sInfo = s;
        sIsOk=isJustShowOk;
        ShowConfirmDlg dlg = new ShowConfirmDlg();
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
        View view = inflater.inflate(R.layout.dlg_show_confirm, container);
        mTvShowInfo1 = (TextView) view.findViewById(R.id.tv_show_info_dlg_confirm);
        mTvShowInfo1.setText(sInfo);
        view.findViewById(R.id.btn_confirm_dlg_cancel).setOnClickListener(this);
        view.findViewById(R.id.btn_confirm_dlg_ok).setOnClickListener(this);
        if (sIsOk){
            view.findViewById(R.id.btn_confirm_dlg_cancel).setVisibility(View.GONE);
//            view.findViewById(R.id.btn_confirm_dlg_divider).setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        WindowManager.LayoutParams layoutParams = getDialog().getWindow().getAttributes();
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        int mScreenWidth = wm.getDefaultDisplay().getWidth();
        layoutParams.width = (int) (0.9f * mScreenWidth);
        getDialog().setCanceledOnTouchOutside(false);
        if (sIsOk){
            getDialog().setOnKeyListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm_dlg_ok:
                if (sOnClickOkListener != null) {
                    sOnClickOkListener.onClick(v);
                }
                dismiss();
                break;
			case R.id.btn_confirm_dlg_cancel:
				dismiss();
				break;
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        dismissSetInfoNull();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        dismissSetInfoNull();
    }

    private void dismissSetInfoNull() {
        sOnClickOkListener = null;
    }

    @Override
    public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            return true;
        }
        return false;
    }
}
