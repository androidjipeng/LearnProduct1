package com.learn.soft.product.main;

import java.util.ArrayList;

import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.learn.soft.product.bean.ProductInfoBean;
import com.learn.soft.product.bean.ProductInfoChildBean;
import com.learn.soft.product.bean.ProductTypeChildBean;
import com.learn.soft.product.jni.BaseFragment;
import com.learn.soft.product.jni.JiaZhengApp;
import com.learn.soft.product.main.activity.ShowWebViewInfoActivity;
import com.learn.soft.product.util.ApiWebCommon;
import com.learn.soft.product.util.BundleKey;
import com.learn.soft.product.util.LoadDataType;
import com.learn.soft.product.util.MyLog;
import com.learn.soft.product.util.NetStateManager;
import com.learn.soft.product.util.SendHttpUtils;
import com.learn.soft.product.util.StringUtil;
import com.learn.soft.product.util.ToastHelper;
import com.learn.soft.product.widget.pulltorefresh.PullToRefreshBase;
import com.learn.soft.product.widget.pulltorefresh.PullToRefreshListView;
import com.learn.soft.product1.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity.Config;
import com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity.ShopDetailActivity;
import com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity.bean.AddCartInformation;
import com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity.bean.HomeShopInformation;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

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
public class TabAllProductChildFragment extends BaseFragment implements View.OnClickListener, PullToRefreshBase.OnRefreshListener2, OnGoShowListener {

    public static TabAllProductChildFragment getInstance(int position,ArrayList<ProductTypeChildBean> list){
        TabAllProductChildFragment frg=new TabAllProductChildFragment();
        Bundle args=new Bundle();
        args.putInt(BundleKey.Bundle_KEY_POSITION, position);
        //args.putInt(BundleKey.BUNDLE_KEY_TYPE_ID, typeId);
        //args.putSerializable(BundleKey.BUNDLE_KEY_SERIALIZABLE_DATA, list);
        frg.setArguments(args);
        return frg;
    }

    private ImageView mIvShow;
    private PullToRefreshListView mPRLVshow;
    private ListView mLVShow;
    private ProductInfoAdapter mProductInfoAdapter;
    private LoadDataType mLoadDataType = LoadDataType.FirstLoad;
//    private int mPageSize=10;
    private int mPageIndex=1;
    private int mPostion=0;
//    private int mSelectTypeId=0;

    private String mCateId="0";

    private final ArrayList<ProductInfoChildBean> mProductList=new ArrayList<ProductInfoChildBean>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args=getArguments()!=null? getArguments(): savedInstanceState;
        if (args!=null){
            mPostion=args.getInt(BundleKey.Bundle_KEY_POSITION);
//            mTypeList= (ArrayList<ProductTypeChildBean>) args.getSerializable(BundleKey.BUNDLE_KEY_SERIALIZABLE_DATA);
        }
        View view = inflater.inflate(R.layout.activity_main_tab_allproduct_childfrg, container, false);

        mIvShow= (ImageView) view.findViewById(R.id.iv_show_target);
        mIvShow.setOnClickListener(this);
        mPRLVshow = (PullToRefreshListView) view.findViewById(R.id.plv_common_pulltofresh);
        mPRLVshow.setOnRefreshListener(this);
        mPRLVshow.setEmptyView(LayoutInflater.from(getActivity()).inflate(R.layout.common_empty_show, null));
        mLVShow = mPRLVshow.getRefreshableView();
//        mLVShow.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView absListView, int i) {
//
//            }
//
//            @Override
//            public void onScroll(AbsListView paramAbsListView, int firstVisibleItem,
//                                 int visibleItemCount, int totalItemCount) {
//                if (firstVisibleItem<=3){
//                    mIvShow.setVisibility(View.VISIBLE);
//                }else{
//                    mIvShow.setVisibility(View.GONE);
//                }
//                MyLog.i("xiaocai", "firstVisibleItem = "+firstVisibleItem);
//            }
//        });
        mLoadDataType = LoadDataType.FirstLoad;
        mPageIndex=1;
//        mSelectTypeId=0;
//        getProductList();

        return view;
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	getProductList();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_show_target:
//                clickBtnTarget();
                break;
        }

    }



    private void showLvShowDataAdapter() {
        if (mProductInfoAdapter==null){
            mProductInfoAdapter=new ProductInfoAdapter();
            mLVShow.setAdapter(mProductInfoAdapter);
        }else{
            mProductInfoAdapter.notifyDataSetChanged();
        }
    }

    private void getCateId(){
		if (mPostion != 0) {
			mCateId="0";
			return;
		}
        Fragment frg=getActivity().getSupportFragmentManager().findFragmentByTag("frg1");
        if (frg!=null&&frg instanceof TabAllProductFragment){
            TabAllProductFragment frg2= (TabAllProductFragment) frg;
            mCateId=frg2.getSelectCateId();
        }
    }


    public void getProductList() {
//        final String cateId="0";
//        String type=String.valueOf(mPostion+1);
    	String type=String.valueOf(mPostion);
        String p=String.valueOf(mPageIndex);

        StringBuffer sb=new StringBuffer();
        sb.append(ApiWebCommon.API_COMMON.Api_Common_Url);
        sb.append(ApiWebCommon.API_COMMON.Api_Index_Product_List);
        getCateId();
        String url=String.format(sb.toString(), mCateId, type, p);
        Log.i("info1",url);
        SendHttpUtils.getInstance().postCommon(url, url, null, new SendHttpUtils.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                if (StringUtil.isNotEmpty(data)) {
                    MyLog.i("xiaocai", "Api_Index_Product_List string=" + data);
                    ProductInfoBean mData = null;
                    try {
                        final TypeToken type = new TypeToken<ProductInfoBean>() {
                        };
                        mData = new Gson().fromJson(data, type.getType());
                        if (mLoadDataType == LoadDataType.FirstLoad||mLoadDataType == LoadDataType.RefreshLoad){
                            mProductList.clear();
                        }
                        if (mData != null && mData.status == 1) {
                            int number=mData.list!=null? mData.list.size(): 0;
                            if (number>0){
                                mProductList.addAll(mData.list);
                            }
                        }
                        showLvShowDataAdapter();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (mLoadDataType!=LoadDataType.FirstLoad){
                        stopLoadingRefreshState();
                    }

                }

            }

            @Override
            public void onError(String msg) {
                ToastHelper.toast(msg);
                if (mLoadDataType!=LoadDataType.FirstLoad){
                    stopLoadingRefreshState();
                }

            }
        });
    }


    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        if (!NetStateManager.OnNet()) {
            stopLoadingRefreshState();
            ToastHelper.toast(R.string.no_network_tip);
            return;
        }

        String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
        refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("最后更新：" + label);

        mLoadDataType = LoadDataType.RefreshLoad;
        mPageIndex = 1;
        getProductList();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        if (!NetStateManager.OnNet()) {
            stopLoadingRefreshState();
            ToastHelper.toast(R.string.no_network_tip);
            return;
        }

        mLoadDataType = LoadDataType.MoreLoad;
//        mPageIndex = mProductList.size()%mPageSize==0? mProductList.size()/mPageSize+1: mProductList.size()/mPageSize+2;
        mPageIndex++;
        getProductList();
    }


    private void stopLoadingRefreshState() {
        if (mPRLVshow != null) {
            mPRLVshow.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mPRLVshow.onRefreshComplete();
                }
            }, 100);
        }
    }

    @Override
    public void onGoShowInfo(int position, String caiId) {
//        if (position==mPostion){
//            mPageIndex=1;
//            mCateId=caiId;
//            getProductList();
//        }
		mPageIndex = 1;
		mCateId = caiId;
		mProductList.clear();
		if (mProductInfoAdapter != null) {
			mProductInfoAdapter.notifyDataSetChanged();
		}
		Log.i("info1", "清空数据");
		getProductList();
    }


    public class ProductInfoAdapter extends BaseAdapter implements View.OnClickListener {

        private DisplayImageOptions options= JiaZhengApp.getInstance().getDefaultImgeOptions();

        @Override
        public int getCount() {
            return mProductList!=null? mProductList.size(): 0;
        }
        @Override
        public ProductInfoChildBean getItem(int position) {
            return mProductList.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View view, ViewGroup parent) {
            final ViewHolder holder;
            if (view == null) {
                view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_main_tab_allproduct_childfrg_item, parent, false);
                holder = new ViewHolder();
                holder.mArea = view.findViewById(R.id.area_product_info_item);
                holder.mIvPic = (ImageView) view.findViewById(R.id.iv_product_info_item_pic);
                holder.mIvAdd = (ImageView) view.findViewById(R.id.iv_product_info_item_add);
                holder.mPbShow = (ProgressBar) view.findViewById(R.id.pb_product_info_item_bar);
                holder.mTvTitle = (TextView) view.findViewById(R.id.tv_product_info_item_title);
                holder.mTvAll = (TextView) view.findViewById(R.id.tv_product_info_item_all);
                holder.mTvJoin = (TextView) view.findViewById(R.id.tv_product_info_item_join);
                holder.mTvRelease = (TextView) view.findViewById(R.id.tv_product_info_item_release);
                holder.mTvYunJiaGe=(TextView) view.findViewById(R.id.tv_yun_jia_ge);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            ProductInfoChildBean bean=getItem(position);
            if (bean!=null){
            	if (bean.yunjiage == 10) {
					holder.mTvYunJiaGe.setVisibility(View.VISIBLE);
					holder.mTvYunJiaGe.setText("十元商品");
				} else if (bean.yunjiage == 100) {
					holder.mTvYunJiaGe.setVisibility(View.VISIBLE);
					holder.mTvYunJiaGe.setText("百元商品");
				} else {
					holder.mTvYunJiaGe.setVisibility(View.GONE);
				}
				ImageLoader.getInstance().displayImage(bean.thumb,
						holder.mIvPic, options);
                holder.mTvTitle.setText(Html.fromHtml(bean.title));
                holder.mTvJoin.setText(String.valueOf(bean.canyurenshu));
                holder.mTvAll.setText(String.valueOf(bean.zongrenshu));
                holder.mTvRelease.setText(String.valueOf(bean.zongrenshu - bean.canyurenshu));
                int progress= 0;
                try {
                    progress = bean.canyurenshu*100/bean.zongrenshu;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                holder.mPbShow.setProgress(progress);
                holder.mArea.setTag(bean);
                holder.mArea.setOnClickListener(this);
                holder.mIvAdd.setTag(bean);
                holder.mIvAdd.setOnClickListener(this);
            }
            return view;
        }


        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.area_product_info_item:{
                    //// TODO: 2017/3/16 商品详情
                    Log.e("jp","jipeng=========================area_product_info_item");

                    ProductInfoChildBean bean= (ProductInfoChildBean) view.getTag();

                    OkHttpUtils.post().url(Config.testurl+Config.queryShopDetail)
                            .addParams("itemId",bean.id)
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    Log.e("jp","订单详情==onError:"+e.getMessage());
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    Log.e("jp","订单详情==onResponse:"+response);
                                    Gson gson=new Gson();
                                    HomeShopInformation homeShopInformation = gson.fromJson(response, HomeShopInformation.class);
                                    JiaZhengApp.getInstance().setBean(homeShopInformation);
                                    Intent intent=new Intent(getActivity(), ShopDetailActivity.class);
                                    startActivity(intent);
                                }
                            });

//                    String title="商品信息";
//                    StringBuffer sb=new StringBuffer();
//                    sb.append(ApiWebCommon.API_COMMON.Api_Common_Url);
//                    sb.append(String.format(ApiWebCommon.API_COMMON.Web_Index_Product_Advice, bean.id));
//                    String url=sb.toString();
//                    setCommonWebViewShow(title, url);
                }
                    break;
                case R.id.iv_product_info_item_add:{
                   /**=============================================原生*/
                    ProductInfoChildBean bean= (ProductInfoChildBean) view.getTag();
                    OkHttpUtils.post()
                            .url(Config.testurl+Config.addShopingCart)
                            .addParams("uid",JiaZhengApp.getInstance().getUserId())
                            .addParams("shopid",bean.id)
                            .addParams("num","1")
                            .addParams("type","1")
                            .build().execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int i) {
//                dismissProgress();
                            MyLog.e("jp","AddCart====>onError:"+e.getMessage());
                        }

                        @Override
                        public void onResponse(String s, int i) {
//              dismissProgress();
                            MyLog.e("jp","AddCart====>onResponseon:"+s);
                            Gson gson=new Gson();
                            AddCartInformation addCartInformation = gson.fromJson(s, AddCartInformation.class);
                            ToastHelper.toast(addCartInformation.getInfo());
                        }
                    });

                    /**==================================================h5*/
//                    ProductInfoChildBean bean= (ProductInfoChildBean) view.getTag();
//                    StringBuffer sb=new StringBuffer();
//                    sb.append(ApiWebCommon.API_COMMON.Api_Common_Url);
//                    sb.append(String.format(ApiWebCommon.API_COMMON.Api_Add_Cart_Shop, bean.id));
//                    String url=sb.toString();
//                    beginLoadData(url);
                }
                    break;
            }
        }

        class ViewHolder {
            View mArea;
            ImageView mIvPic;
            ImageView mIvAdd;
            ProgressBar mPbShow;
            TextView mTvTitle;
            TextView mTvJoin;
            TextView mTvAll;
            TextView mTvRelease;
			TextView mTvYunJiaGe;
        }


    }

    public void setCommonWebViewShow(String title, String url){
        Intent intent=new Intent(getActivity(), ShowWebViewInfoActivity.class);
        Bundle args=new Bundle();
        args.putString(BundleKey.Bundle_KEY_Title, title);
        MyLog.i("xiaocai", "url=" +url);
        args.putString(BundleKey.Bundle_KEY_Url, url);
        intent.putExtras(args);
        startActivity(intent);
    }

    private WebView mWvShow=null;
    private void beginLoadData(String url) {
        showProgressInfo("");
        if (mWvShow==null){
            mWvShow=new WebView(getActivity());
            mWvShow.getSettings().setJavaScriptEnabled(true);// 设置页面支持Javascript
            mWvShow.getSettings().setLoadWithOverviewMode(true);
            mWvShow.getSettings().setSupportZoom(true);// 支持缩放
//        mWvShow.getSettings().setBuiltInZoomControls(true); // 显示放大缩小
            mWvShow.getSettings().setDefaultTextEncodingName("utf-8");
            mWvShow.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);// 解决缓存问题

            mWvShow.getSettings().setUseWideViewPort(true);
            mWvShow.getSettings().setLoadWithOverviewMode(true);
            mWvShow.getSettings().setSavePassword(true);
            mWvShow.getSettings().setSaveFormData(true);
            mWvShow.getSettings().setJavaScriptEnabled(true);
            // enable Web Storage: localStorage, sessionStorage
            mWvShow.getSettings().setDomStorageEnabled(true);

            mWvShow.setWebViewClient(new SelfWebViewClient());
            mWvShow.setWebChromeClient(new SelfChromeClient());
        }

        mWvShow.loadUrl(url);
    }


    class SelfWebViewClient extends WebViewClient {

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            // 如果加载网页失败的话就接受证书
            handler.proceed();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.indexOf("tel:") < 0) {
                view.loadUrl(url);
            }
            return true;
        }

    }

    class SelfChromeClient extends WebChromeClient {
        public SelfChromeClient() {
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (mHandler !=null){
                mHandler.sendEmptyMessage(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }

    }

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if(msg.what==100) {
                dismissProgress();
                refreshShow();
                ToastHelper.toast("添加成功");
            }
        }
    };

    private void refreshShow(){
        if (getActivity() instanceof MainActivity){
            MainActivity activity= (MainActivity) getActivity();
            activity.refreshShowUI();
        }
    }

}
