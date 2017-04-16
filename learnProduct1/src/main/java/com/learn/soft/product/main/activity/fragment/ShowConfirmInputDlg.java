package com.learn.soft.product.main.activity.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.*;
import android.widget.EditText;
import com.learn.soft.product.util.StringUtil;
import com.learn.soft.product.util.ToastHelper;
import com.learn.soft.product1.R;
import com.wangyi.lelegou.maofengqi.ui.activity.MainActivity;

/**
 * **********************************************************
 * <p/>
 * 说明：显示是否确定对话框
 * <p/>
 * 作者：
 * <p/>
 * 创建日期：2016/1/5
 * <p/>
 * 描述：
 * **********************************************************
 */
public class ShowConfirmInputDlg extends DialogFragment implements View.OnClickListener, DialogInterface.OnKeyListener {

    private EditText mEtInput;
    private static View.OnClickListener sOnClickOkListener;
//    private static String sInfo;

    public static DialogFragment showDlg(FragmentManager manager, View.OnClickListener listener ) {
        sOnClickOkListener = listener;
        ShowConfirmInputDlg dlg = new ShowConfirmInputDlg();
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
        View view = inflater.inflate(R.layout.dlg_show_confirm_input, container);
//        mTvShowInfo1 = (TextView) view.findViewById(R.id.tv_show_info_dlg_confirm);
//        mTvShowInfo1.setText(sInfo);
        mEtInput= (EditText) view.findViewById(R.id.et_dlg_input);
        view.findViewById(R.id.btn_confirm_dlg_cancel).setOnClickListener(this);
        view.findViewById(R.id.btn_confirm_dlg_ok).setOnClickListener(this);

        if (MainActivity.sIsFromShareOpen&&StringUtil.isNotEmpty(MainActivity.sShareId)){
            MainActivity.sIsFromShareOpen=false;
            mEtInput.setText(MainActivity.sShareId);
        }

        return view;
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        WindowManager.LayoutParams layoutParams = getDialog().getWindow().getAttributes();
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        int mScreenWidth = wm.getDefaultDisplay().getWidth();
        layoutParams.width = (int) (0.7f * mScreenWidth);
        getDialog().setCanceledOnTouchOutside(false);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm_dlg_ok:
                String info=mEtInput.getText().toString();
                if (StringUtil.isEmptyOrNull(info)){
                    ToastHelper.toast("请输入圈子ID");
                    return ;
                }
                if (getActivity() instanceof MainActivity && getActivity()!=null){
                    MainActivity activity= (MainActivity) getActivity();
                    activity.joinNewCircle(info, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ToastHelper.toast("加入圈子成功");
                            dismiss();
                        }
                    }, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                            ToastHelper.toast("加入圈子失败");
                        }
                    });
                }

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
