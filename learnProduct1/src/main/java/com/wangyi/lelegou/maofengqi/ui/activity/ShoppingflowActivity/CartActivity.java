package com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.learn.soft.product.jni.JiaZhengApp;
import com.learn.soft.product.util.MyLog;
import com.learn.soft.product.util.ToastHelper;
import com.learn.soft.product1.R;
import com.wangyi.lelegou.maofengqi.base.BaseActivity;
import com.wangyi.lelegou.maofengqi.ui.activity.CartBaseAdapter;
import com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity.bean.CartInformationModel;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class CartActivity extends BaseActivity implements View.OnClickListener {

    private ListView shopcart_listview;
    private Context context;

    private CheckBox all_check;
    private TextView allmoney;
    private TextView shop_nums;

    private Button button_buy;

    private List<CartInformationModel.ObjListBean> objList=new ArrayList<>();

    private int count = 0;
    private int numscount = 0;
    private CartBaseAdapter adapter;

    boolean check2 = false;

    //监听用户数据增加的数量，及时更新
    CartBaseAdapter.DataChange data = new CartBaseAdapter.DataChange() {
        @Override
        public void data(boolean bool, int nums) {
            if (bool) {
                count = nums + count;
            } else {
                count = count - nums;
            }
            int mun=0;
            List<CartInformationModel.ObjListBean> lists = adapter.getList();
            for (int i = 0; i <lists.size(); i++) {
                if (lists.get(i).getIscheck())
                {
                    mun=mun+lists.get(i).getNum();
                }
            }
            shop_nums.setText(mun+"个商品");
            allmoney.setText("￥"+mun);
        }

        @Override
        public void check(boolean is, int nums) {
            if (is) {
                numscount = numscount + nums;
//                ToastHelper.toast(""+numscount);

            } else {

                numscount = numscount - nums;
//                ToastHelper.toast(""+numscount);
            }

            if (numscount == objList.size()) {
                all_check.setChecked(true);
                check2 = false;
            } else {
                check2 = true;
                all_check.setChecked(false);

            }
            int mun=0;
            List<CartInformationModel.ObjListBean> lists = adapter.getList();
            for (int i = 0; i <lists.size(); i++) {
                if (lists.get(i).getIscheck())
                {
                    mun=mun+lists.get(i).getNum();
                }
            }
            shop_nums.setText(mun+"个商品");
            allmoney.setText("￥"+mun);
        }
    };


    @Override
    protected int getLayoutId() {
        return R.layout.activity_cart;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        context = this;
        shopcart_listview = (ListView) findViewById(R.id.shopcart_listview);

        button_buy = (Button) findViewById(R.id.button_buy);
        button_buy.setOnClickListener(this);
        all_check = (CheckBox) findViewById(R.id.all_check);
        allmoney = (TextView) findViewById(R.id.allmoney);
        shop_nums = (TextView) findViewById(R.id.shop_nums);
        ivBack = (ImageButton) findViewById(R.id.iv_back);
        tvContent = (TextView) findViewById(R.id.tv_content);
        ivBack.setVisibility(View.VISIBLE);
        tvContent.setText("购物车");

        all_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked||check2)
                {
                    if (!check2||isChecked)
                    {
                        for (int i = 0; i <objList.size() ; i++) {
                            objList.get(i).setIscheck(true);
                        }
                        adapter.setList(objList);
                        adapter.notifyDataSetChanged();
                        check2=false;

                        int mun=0;
                        List<CartInformationModel.ObjListBean> lists = adapter.getList();
                        for (int i = 0; i <lists.size(); i++) {
                            if (lists.get(i).getIscheck())
                            {
                                mun=mun+lists.get(i).getNum();
                            }
                        }
                        shop_nums.setText(mun+"个商品");
                        allmoney.setText("￥"+mun);


                    }else
                    {
//                        ToastHelper.toast(""+check2);
                        check2=false;

                        int mun=0;
                        List<CartInformationModel.ObjListBean> lists = adapter.getList();
                        for (int i = 0; i <lists.size(); i++) {
                            if (lists.get(i).getIscheck())
                            {
                                mun=mun+lists.get(i).getNum();
                            }
                        }
                        shop_nums.setText(mun+"个商品");
                        allmoney.setText("￥"+mun);
                    }

                }else {
                    for (int i = 0; i <objList.size() ; i++) {
                        objList.get(i).setIscheck(false);
                    }
                    adapter.setList(objList);
                    adapter.notifyDataSetChanged();
                    check2=false;

                    int mun=0;
                    List<CartInformationModel.ObjListBean> lists = adapter.getList();
                    for (int i = 0; i <lists.size(); i++) {
                        if (lists.get(i).getIscheck())
                        {
                            mun=mun+lists.get(i).getNum();
                        }
                    }
                    shop_nums.setText(mun+"个商品");
                    allmoney.setText("￥"+mun);
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        objList.clear();
        count = 0;
        allmoney.setText(count + "");
        shop_nums.setText(count + "");
        all_check.setChecked(false);
        data();
    }

    private void data() {
        showProgressInfo("");
        MyLog.e("jp", Config.testurl + Config.queryShopingCart + "====>uid:" + JiaZhengApp.getInstance().getUserId());
        OkHttpUtils.post()
                .url(Config.testurl + Config.queryShopingCart)
                .addParams("uid", JiaZhengApp.getInstance().getUserId())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        dismissProgress();
                        MyLog.e("jp", "===========>onError" + e.getMessage() + call.request());
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        MyLog.e("jp", "CartActivity===========>onResponse：" + s);
                        dismissProgress();
                        Gson gson = new Gson();
                        CartInformationModel cartInformationModel = gson.fromJson(s, CartInformationModel.class);
                        objList = cartInformationModel.getObjList();
                        adapter = new CartBaseAdapter(context, objList);
                        shopcart_listview.setAdapter(adapter);
                        adapter.setdataChangeLinstener(data);
                        for (int j = 0; j < objList.size(); j++) {
                            count = count + objList.get(j).getNum();
                        }
                        shop_nums.setText(count + "个商品");
                        allmoney.setText("￥" + count);
                    }
                });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_buy:
                paymoney();
                break;
            default:
                break;
        }
    }

    private void paymoney() {
        String shopids="";
        List<CartInformationModel.ObjListBean> lists = adapter.getList();
        String shopidss="";
        for (int i = 0; i <lists.size(); i++) {
            if (lists.get(i).getIscheck())
            {
                shopids=shopids+lists.get(i).getShopid()+","+lists.get(i).getNum()+";";
                shopidss=shopidss+lists.get(i).getShopid()+",";
            }
        }
        if (shopids.length()<=0)
        {
            ToastHelper.toast("请选择要购买的商品");
        }else
        {
            String shop=shopids.substring(0,shopids.length()-1);
            String shops=shopidss.substring(0,shopidss.length()-1);//只有商品id
            MyLog.e("jp","======>shop:"+shop);

            Intent shopid=new Intent(context, PayActivity.class);
            shopid.putExtra("tag","1");
            shopid.putExtra("shopids",shop);
            shopid.putExtra("shopidss",shops);
            startActivity(shopid);
        }


    }
}
