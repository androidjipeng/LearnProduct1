package com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.learn.soft.product.jni.JiaZhengApp;
import com.learn.soft.product.util.MyLog;
import com.learn.soft.product.util.ToastHelper;
import com.learn.soft.product1.R;
import com.wangyi.lelegou.maofengqi.base.BaseActivity;
import com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity.bean.MyGetShopInformation;
import com.wangyi.lelegou.maofengqi.ui.adapter.GetAwardShopInforAdapter;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class GetAwardShopInforActivity extends BaseActivity {
    private static final String TAG = "GetAwardShopInforActivi";
    private ListView getaward_listview;
    private List<String> list=new ArrayList<>();
    private GetAwardShopInforAdapter adapter;
    private Context context;
    boolean isfrist=true;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_get_award_shop_infor;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        context=this;
        tvContent = (TextView) findViewById(R.id.tv_content);
        tvContent.setText("获得的商品");
        getaward_listview= (ListView) findViewById(R.id.getaward_listview);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (isfrist)
//        {
            isfrist=false;
            list.clear();
            datas();
//        }
    }

    private void datas() {
        showProgressInfo("");
        OkHttpUtils
                .post()
                .url(Config.testurl+Config.queryMyShoplist)
                .addParams("uid", JiaZhengApp.getInstance().getUserId())
                .addParams("currentPage","1")
                .addParams("pageSize","10")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        dismissProgress();
                        MyLog.e("jp",TAG+"=====onError:"+e.getMessage());
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        dismissProgress();
                        MyLog.e("jp",TAG+"=====onResponse:"+s);
                        Gson gson=new Gson();
                        MyGetShopInformation myGetShopInformation = gson.fromJson(s, MyGetShopInformation.class);
                        if (myGetShopInformation.getResult()==0)
                        {
                            List<MyGetShopInformation.ObjListBean> objList = myGetShopInformation.getObjList();
                            adapter=new GetAwardShopInforAdapter(context,objList);
                            getaward_listview.setAdapter(adapter);
                        }else{
                            ToastHelper.toast(myGetShopInformation.getInfo());
                        }
                    }
                });




    }
}
