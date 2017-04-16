package com.wangyi.lelegou.maofengqi.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

/**
 * Created by jipeng on 2016/8/8.
 */
public class DialogManager {
    private static DialogManager dialogManager;
    private static AlertDialog.Builder dialog;
    private static Context contexts=null;
    private DialogManager()
    {

    }
    /**
    * 获得唯一dialogmanager
    * @return  dialogManager
    * */
    public static synchronized DialogManager Instense(Context context)
    {
        if (contexts==context)
        {
            return dialogManager;
        }else
        {
            contexts=context;
            dialogManager=new DialogManager();
            dialog=new AlertDialog.Builder(contexts);
            return dialogManager;
        }
    }
    /**
    * @content 内容
    * 提示
    * */
    public void show_Toast(String content) {
        Toast.makeText(contexts,content, Toast.LENGTH_SHORT).show();
    }
    public void show_Toast(int content)
    {
        Toast.makeText(contexts,content, Toast.LENGTH_SHORT).show();
    }
    /**
      显示dialog信息
    * @title 标题
    * @content 内容
    * @clickListener 确定
     * @cancelListener 取消
     *用于提示，可供选择，取消，确定
    * */
    public void showDialog(String title, String content, DialogInterface.OnClickListener clickListener, DialogInterface.OnClickListener cancelListener)
    {
        AlertDialog.Builder dialog=new AlertDialog.Builder(contexts);
        dialog.setTitle(title);
        dialog.setMessage(content);
//        dialog.setIcon(R.drawable.dialog);
        dialog.setCancelable(false);
        dialog.setNegativeButton("取消",cancelListener);
        dialog.setPositiveButton("确定",clickListener);
        dialog.create().show();
    }

    /**
    * @title
    * @content
    * @clickListener
    * 用于警告提示，并只需做出确定的回应，做数据处理
    * */
    public void showDialog(String title, String content, DialogInterface.OnClickListener clickListener)
    {
//        AlertDialog.Builder dialog=new AlertDialog.Builder(contexts);
        dialog.setTitle(title);
        dialog.setMessage(content);
//        dialog.setIcon(R.drawable.dialog);
        dialog.setCancelable(false);
        dialog.setPositiveButton("确定",clickListener);
        dialog.create().show();
    }

    /**
    * @title
    * @content
    * 用于服务告知提示,点击取消，不做任何处理
    * */
    public void showDialog(String title, String content)
    {
//        AlertDialog.Builder dialog=new AlertDialog.Builder(contexts);
        dialog.setTitle(title);
        dialog.setMessage(content);
//        dialog.setIcon(R.drawable.dialog);
        dialog.setCancelable(false);
        dialog.setPositiveButton("取消",null);
        dialog.create().show();
    }
    /**
    * @title
    * @view
    * @clickListener
    * @cancelListener
    *
    * */
    public void showDialog(String title, View view, DialogInterface.OnClickListener clickListener, DialogInterface.OnClickListener cancelListener)
    {
//        AlertDialog.Builder dialog=new AlertDialog.Builder(contexts);
        dialog.setTitle(title);
        dialog.setView(view);
//        dialog.setIcon(R.drawable.dialog);
        dialog.setCancelable(false);
        dialog.setNegativeButton("取消",cancelListener);
        dialog.setPositiveButton("确定",clickListener);
        dialog.create().show();
    }

//    public void showProgress()
//    {
//        View view= LayoutInflater.from(contexts).inflate(R.layout.progress_layout,null);
//        dialog.setView(view);
//        dialog.setCancelable(false);
//        dialog.create().show();
//    }
    public void cancelProgress()
    {
        if (dialog!=null)
        {
            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    dialog.cancel();
                }
            });
        }

    }
}
