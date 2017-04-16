package com.wangyi.lelegou.maofengqi.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.learn.soft.product.jni.JiaZhengApp;
import com.learn.soft.product.util.MyLog;
import com.learn.soft.product.util.ToastHelper;
import com.learn.soft.product1.R;
import com.wangyi.lelegou.maofengqi.base.BaseFragment;
import com.wangyi.lelegou.maofengqi.ui.activity.CartBaseAdapter;
import com.wangyi.lelegou.maofengqi.ui.activity.LoginActivity;
import com.wangyi.lelegou.maofengqi.ui.activity.SearchProductActivity;
import com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity.CartActivity;
import com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity.Config;
import com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity.PayActivity;
import com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity.bean.CartInformationModel;
import com.wangyi.lelegou.maofengqi.utils.Constant;
import com.wangyi.lelegou.maofengqi.utils.Utils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class CartFragment extends BaseFragment implements OnClickListener {

    // protected ImageButton ivSearch;
    protected ImageButton ivBack;
    protected TextView tvContent;

    private WebView webView;
    private LinearLayout llNoLogin;
    private TextView tvLogin;
    private boolean isReload = true;


    private ListView frag_shopcart_listview;
    private List<String> list = new ArrayList<>();
    private Context context;
    private CheckBox all_check;
    private TextView allmoney;
    private TextView shop_nums;

    private Button button_buy;


    private LinearLayout all_layout;//后加布局的id
    int count = 0;
    int numscount = 0;
    private CartBaseAdapter adapter;

    boolean check2 = false;
    private List<CartInformationModel.ObjListBean> objList = new ArrayList<>();

    //监听用户数据增加的数量，及时更新
    CartBaseAdapter.DataChange data = new CartBaseAdapter.DataChange() {
        @Override
        public void data(boolean bool, int nums) {
            if (bool) {
                count = nums + count;
            } else {
                count = count - nums;
            }

            int mun = 0;
            List<CartInformationModel.ObjListBean> lists = adapter.getList();
            for (int i = 0; i < lists.size(); i++) {
                if (lists.get(i).getIscheck()) {
                    mun = mun + lists.get(i).getNum();
                }
            }
            shop_nums.setText(mun + "个商品");
            allmoney.setText("￥" + mun);
        }

        @Override
        public void check(boolean is, int nums) {
            if (is) {
                numscount = numscount + nums;
//				ToastHelper.toast(""+numscount);


            } else {

                numscount = numscount - nums;
//				ToastHelper.toast(""+numscount);

            }

            if (numscount == objList.size()) {
                all_check.setChecked(true);
                check2 = false;

            } else {
                check2 = true;
                all_check.setChecked(false);

            }

            int mun = 0;
            List<CartInformationModel.ObjListBean> lists = adapter.getList();
            for (int i = 0; i < lists.size(); i++) {
                if (lists.get(i).getIscheck()) {
                    mun = mun + lists.get(i).getNum();
                }
            }
            shop_nums.setText(mun + "个商品");
            allmoney.setText("￥" + mun);
        }
    };

    /**
     * Use this factory method to create a new instance of this fragment using
     * the provided parameters.
     *
     * @return A new instance of fragment CartFragment.
     */
    public static CartFragment newInstance() {
        CartFragment fragment = new CartFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_cart;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        context = getActivity();

        button_buy = (Button) findViewById(R.id.button_buy);
        button_buy.setOnClickListener(this);

        all_layout = (LinearLayout) findViewById(R.id.all_layout);

        all_check = (CheckBox) findViewById(R.id.all_check);
        all_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked || check2) {
                    if (!check2 || isChecked) {
                        for (int i = 0; i < objList.size(); i++) {
                            objList.get(i).setIscheck(true);
                        }
                        adapter.setList(objList);
                        adapter.notifyDataSetChanged();
                        check2 = false;

                        int mun = 0;
                        List<CartInformationModel.ObjListBean> lists = adapter.getList();
                        for (int i = 0; i < lists.size(); i++) {
                            if (lists.get(i).getIscheck()) {
                                mun = mun + lists.get(i).getNum();
                            }
                        }
                        shop_nums.setText(mun + "个商品");
                        allmoney.setText("￥" + mun);

                    } else {
                        //不做任何操作,可能会做list数据改变
//						check2=true;
//						ToastHelper.toast(""+check2);
                        check2 = false;

                        int mun = 0;
                        List<CartInformationModel.ObjListBean> lists = adapter.getList();
                        for (int i = 0; i < lists.size(); i++) {
                            if (lists.get(i).getIscheck()) {
                                mun = mun + lists.get(i).getNum();
                            }
                        }
                        shop_nums.setText(mun + "个商品");
                        allmoney.setText("￥" + mun);
                    }

                } else {
                    for (int i = 0; i < objList.size(); i++) {
                        objList.get(i).setIscheck(false);
                    }
                    adapter.setList(objList);
                    adapter.notifyDataSetChanged();
                    check2 = false;

                    int mun = 0;
                    List<CartInformationModel.ObjListBean> lists = adapter.getList();
                    for (int i = 0; i < lists.size(); i++) {
                        if (lists.get(i).getIscheck()) {
                            mun = mun + lists.get(i).getNum();
                        }
                    }
                    shop_nums.setText(mun + "个商品");
                    allmoney.setText("￥" + mun);
                }

            }
        });
        allmoney = (TextView) findViewById(R.id.allmoney);
        shop_nums = (TextView) findViewById(R.id.shop_nums);

        // ivSearch = (ImageButton) findViewById(R.id.iv_search);
        ivBack = (ImageButton) findViewById(R.id.iv_back);
        tvContent = (TextView) findViewById(R.id.tv_content);

        webView = (WebView) findViewById(R.id.web_view);
        llNoLogin = (LinearLayout) findViewById(R.id.ll_no_login);
        tvLogin = (TextView) findViewById(R.id.tv_login);

        // ivSearch.setVisibility(View.VISIBLE);
        // ivSearch.setOnClickListener(this);
        ivBack.setVisibility(View.INVISIBLE);
        tvContent.setText("购物车");

        frag_shopcart_listview = (ListView) findViewById(R.id.frag_shopcart_listview);

//		setData();


    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(Constant.ACTION_LOGN);
        getActivity().registerReceiver(receiver, filter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isReload) {
            all_check.setChecked(false);
            objList.clear();
            count = 0;
            allmoney.setText(count + "");
            shop_nums.setText(count + "");
            setData();
            isReload = false;
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Constant.ACTION_LOGN)) {
                isReload = true;
            }
        }
    };

    public void onStop() {
        super.onStop();

        if (receiver != null) {
            getActivity().unregisterReceiver(receiver);
        }
    }


    private void setData() {


        if (JiaZhengApp.getInstance().isLogin()) {


//			webView.setVisibility(View.VISIBLE);
//			llNoLogin.setVisibility(View.GONE);
//
//			WebSettings settings = webView.getSettings();
//			// 设置页面支持JavaScript
//			settings.setJavaScriptEnabled(true);
//
//			webView.setWebViewClient(new MyWebViewClient());
//			webView.setWebChromeClient(new WebChromeClient());
//
//			showProgressInfo("");
//			webView.loadUrl(Constant.MOBILE_IP + Constant.MOBILE_CART_LIST);

//=======================================================================================原生代码

            all_layout.setVisibility(View.VISIBLE);
            llNoLogin.setVisibility(View.GONE);
            showProgressInfo("");
            MyLog.e("jp", Config.testurl + Config.queryShopingCart + "====" + JiaZhengApp.getInstance().getUserId());
            OkHttpUtils.post()
                    .url(Config.testurl + Config.queryShopingCart)
                    .addParams("uid", JiaZhengApp.getInstance().getUserId())
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int i) {
                            dismissProgress();
                            MyLog.e("jp", "CartFragment===========>onError" + e.getMessage() + call.request());
                        }

                        @Override
                        public void onResponse(String s, int i) {
                            dismissProgress();
                            MyLog.e("jp", "CartFragment==============resposon:" + s);

                            Gson gson = new Gson();
                            CartInformationModel cartInformationModel = gson.fromJson(s, CartInformationModel.class);
                            objList = cartInformationModel.getObjList();
                            adapter = new CartBaseAdapter(context, objList);
                            frag_shopcart_listview.setAdapter(adapter);
                            adapter.setdataChangeLinstener(data);
//							for (int j = 0; j <objList.size(); j++) {
//								count=count+objList.get(j).getNum();
//							}
//							shop_nums.setText(count+"个商品");
//							allmoney.setText("￥"+count);
                        }
                    });

//==========================================================================================


        } else {
            all_layout.setVisibility(View.GONE);
//			webView.setVisibility(View.GONE);
            llNoLogin.setVisibility(View.VISIBLE);
            tvLogin.setOnClickListener(this);

        }
    }


    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            dismissProgress();
            super.onPageFinished(view, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (!url.equals(Constant.MOBILE_IP + Constant.MOBILE_CART_LIST)) {
                Utils.startWebViewShow(getActivity(), "", url);
                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_search:
                SearchProductActivity.start(getActivity());
                break;
            case R.id.tv_login:
                LoginActivity.start(getActivity());
                break;
            case R.id.button_buy:
                paymoney();
                break;
            default:
                break;
        }
    }

    private void paymoney() {
        String shopids = "";
        List<CartInformationModel.ObjListBean> lists = adapter.getList();
        String shopidss = "";
        for (int i = 0; i < lists.size(); i++) {
            if (lists.get(i).getIscheck()) {
                shopids = shopids + lists.get(i).getShopid() + "," + lists.get(i).getNum() + ";";
                shopidss = shopidss + lists.get(i).getShopid() + ",";
            }
        }
        if (shopids.length() <= 0) {
            ToastHelper.toast("请选择要购买的商品");
        } else {
            String shop = shopids.substring(0, shopids.length() - 1);//商品id+数量
            String shops = shopidss.substring(0, shopidss.length() - 1);//只有商品id
            MyLog.e("jp", "======>shop:" + shop);

            Intent shopid = new Intent(context, PayActivity.class);
            shopid.putExtra("tag", "1");//表示是购物车
            shopid.putExtra("shopids", shop);
            shopid.putExtra("shopidss", shops);
            startActivity(shopid);
        }


    }
}