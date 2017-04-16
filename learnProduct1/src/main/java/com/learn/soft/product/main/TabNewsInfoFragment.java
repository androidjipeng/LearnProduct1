package com.learn.soft.product.main;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.format.DateUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.learn.soft.product.bean.NewsInfoBean;
import com.learn.soft.product.bean.NewsInfoChildBean;
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
import com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity.NoShopActivity;
import com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity.ShopDetailActivity;
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
public class TabNewsInfoFragment extends BaseTabFragment implements View.OnClickListener, PullToRefreshBase.OnRefreshListener2 {

    private int mPageIndex=1;
    private PullToRefreshListView mPRLVshow;
    private ListView mLVShow;
    private NewsInfoAdapter mNewsInfoAdapter;
    private final ArrayList<NewsInfoChildBean> mNewsList=new ArrayList<NewsInfoChildBean>();
    private LoadDataType mLoadDataType = LoadDataType.FirstLoad;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main_tab_showhistory_childfrg, container, false);

        mPRLVshow = (PullToRefreshListView) view.findViewById(R.id.plv_common_pulltofresh);
        mPRLVshow.setOnRefreshListener(this);
        mPRLVshow.setEmptyView(LayoutInflater.from(getActivity()).inflate(R.layout.common_empty_show, null));
        mLVShow = mPRLVshow.getRefreshableView();
        mLoadDataType = LoadDataType.FirstLoad;
        mPageIndex=1;
        getNewsProductData();

        return view;
    }


    public void getNewsProductData() {
        showProgressInfo("");
        String url=String.format("%1s%2s%d", ApiWebCommon.API_COMMON.Api_Common_Url, ApiWebCommon.API_COMMON.Api_NewsInfo_Product_List, mPageIndex);
        SendHttpUtils.getInstance().postCommon(url, url, null, new SendHttpUtils.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                dismissProgress();
                if (StringUtil.isNotEmpty(data)) {
                    MyLog.e("xiaocai", "Api_NewsInfo_Product_List string=" + data);
                    NewsInfoBean mData = null;
                    try {
                        final TypeToken type = new TypeToken<NewsInfoBean>() {
                        };
                        mData = new Gson().fromJson(data, type.getType());
                        if (mLoadDataType == LoadDataType.FirstLoad||mLoadDataType == LoadDataType.RefreshLoad){
                            mNewsList.clear();
                        }
                        if (mData != null && mData.status == 1) {
                            int number = mData.list != null ? mData.list.size() : 0;
                            if (number > 0) {
                                mNewsList.addAll(mData.list);
                            }
                        }
                        showDataAdapter();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                if (mLoadDataType!=LoadDataType.FirstLoad){
                    stopLoadingRefreshState();
                }
            }

            @Override
            public void onError(String msg) {
                dismissProgress();
                ToastHelper.toast(msg);
                if (mLoadDataType!=LoadDataType.FirstLoad){
                    stopLoadingRefreshState();
                }
            }
        });
    }

    private void showDataAdapter() {
        if (mNewsInfoAdapter == null) {
            mNewsInfoAdapter = new NewsInfoAdapter();
            mLVShow.setAdapter(mNewsInfoAdapter);
        }else {
            mNewsInfoAdapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login_status:
                break;

        }

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

    public void setCommonWebViewShow(String title, String url){
        Intent intent=new Intent(getActivity(), ShowWebViewInfoActivity.class);
        Bundle args=new Bundle();
        args.putString(BundleKey.Bundle_KEY_Title, title);
        MyLog.i("xiaocai", "url=" + url);
        args.putString(BundleKey.Bundle_KEY_Url, url);
        intent.putExtras(args);
        startActivity(intent);
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
        getNewsProductData();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        if (!NetStateManager.OnNet()) {
            stopLoadingRefreshState();
            ToastHelper.toast(R.string.no_network_tip);
            return;
        }

        mLoadDataType = LoadDataType.MoreLoad;
        mPageIndex++;
        getNewsProductData();
    }


    public class NewsInfoAdapter extends BaseAdapter implements View.OnClickListener {

        private DisplayImageOptions options= JiaZhengApp.getInstance().getDefaultImgeOptions();

        @Override
        public int getCount() {
            return mNewsList!=null? mNewsList.size(): 0;
        }
        @Override
        public NewsInfoChildBean getItem(int position) {
            return mNewsList.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View view, ViewGroup parent) {
            final ViewHolder holder;
            if (view == null) {
                view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_main_tab_newsinfo_item, parent, false);
                holder = new ViewHolder();
                holder.mArea = view.findViewById(R.id.area_news_info_item);
                holder.mIvPic = (ImageView) view.findViewById(R.id.iv_news_info_item_pic);
                holder.mIvPic2 = (ImageView) view.findViewById(R.id.iv_news_info_item_pic2);
                holder.mTvUserName = (TextView) view.findViewById(R.id.tv_news_info_item_username);
                holder.mTvPeopleTimes = (TextView) view.findViewById(R.id.tv_news_info_item_peopletimes);
                holder.mTvHappyCode = (TextView) view.findViewById(R.id.tv_news_info_item_happycode);
                holder.mTvTime = (TextView) view.findViewById(R.id.tv_news_info_item_time);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            NewsInfoChildBean bean=getItem(position);
            if (bean!=null){
				ImageLoader.getInstance().displayImage(bean.thumb,
						holder.mIvPic, options);
				ImageLoader.getInstance().displayImage(bean.user_photo,
						holder.mIvPic2, options);
                int index1=0;
                int index2=0;

                StringBuffer sb3=new StringBuffer("幸运码：");
                index1=sb3.length();
                sb3.append(bean.q_user_code);
                index2=sb3.length();
                SpannableString ss3 = new SpannableString(sb3.toString());
                ss3.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.bg_common_red_color)), index1, index2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.mTvHappyCode.setText(ss3);

                StringBuffer sb2=new StringBuffer("本期购买：");
                index1=sb2.length();
                sb2.append(bean.gonumber);
                index2=sb2.length();
                sb2.append("人");
                SpannableString ss2 = new SpannableString(sb2.toString());
                ss2.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.bg_common_red_color)), index1, index2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.mTvPeopleTimes.setText(ss2);


                StringBuffer sb=new StringBuffer("获得者：");
                index1=sb.length();
                String title=bean.user_name;
                if (StringUtil.isNotEmpty(title)&& title.length()>4){
                    sb.append(title.substring(0, 2)).append("*").append(title.substring(title.length()-2, title.length()));
                }else{
                    sb.append(title);
                }
                index2=sb.length();
                SpannableString ss = new SpannableString(sb.toString());
                ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blue_main)), index1, index2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.mTvUserName.setText(ss);

                holder.mTvTime.setText(String.format("揭晓时间：%s", bean.q_end_time));

                holder.mArea.setTag(bean);
                holder.mArea.setOnClickListener(this);
                holder.mIvPic2.setTag(bean);
                holder.mIvPic2.setOnClickListener(this);
                holder.mTvUserName.setTag(bean);
                holder.mTvUserName.setOnClickListener(this);
            }
            return view;
        }


        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.area_news_info_item:{
                    MyLog.e("jp","TabNewsInfoFragment----------->area_news_info_item");
                    showProgressInfo(null);
                    NewsInfoChildBean bean= (NewsInfoChildBean) view.getTag();
                    MyLog.e("jp","TabNewsInfoFragment----------->url:"+"http://101.201.115.226:8082/yyg_app/queryShopDetail"+"/"+bean.id+"/"+bean.q_uid+"---->"+bean.circle_id);
                    if (bean.circle_id.equals("0"))
                    {
                        OkHttpUtils.post().url("http://101.201.115.226:8082/yyg_app/queryShopDetail")
                                .addParams("itemId",bean.id)
                                .addParams("uid",bean.q_uid)
                                .build()
                                .execute(new StringCallback() {
                                    @Override
                                    public void onError(Call call, Exception e, int id) {
                                        dismissProgress();
                                        Log.e("jp","订单详情==onError:"+e.getMessage());
                                    }

                                    @Override
                                    public void onResponse(String response, int id) {
                                        dismissProgress();
                                        Log.e("jp","TabNewsInfoFragment---->订单详情==onResponse:"+response);
                                        Gson gson=new Gson();
                                        HomeShopInformation homeShopInformation = gson.fromJson(response, HomeShopInformation.class);
                                        JiaZhengApp.getInstance().setBean(homeShopInformation);
                                        if (homeShopInformation.getResult()==1)
                                        {
                                            Intent intent1=new Intent(getActivity(), NoShopActivity.class);
                                            startActivity(intent1);
                                        }else {
                                            Intent intent=new Intent(getActivity(), ShopDetailActivity.class);
                                            startActivity(intent);
                                        }

                                    }
                                });
                    }else
                    {
                        OkHttpUtils.post().url("http://101.201.115.226:8082/yyg_app/queryShopDetail")
                                .addParams("itemId",bean.id)
                                .addParams("uid",bean.q_uid)
                                .addParams("circleId",bean.circle_id+"")
                                .build()
                                .execute(new StringCallback() {
                                    @Override
                                    public void onError(Call call, Exception e, int id) {
                                        dismissProgress();
                                        Log.e("jp","订单详情==onError:"+e.getMessage());
                                    }

                                    @Override
                                    public void onResponse(String response, int id) {
                                        dismissProgress();
                                        Log.e("jp","TabNewsInfoFragment---->订单详情==onResponse:"+response);
                                        Gson gson=new Gson();
                                        HomeShopInformation homeShopInformation = gson.fromJson(response, HomeShopInformation.class);
                                        JiaZhengApp.getInstance().setBean(homeShopInformation);
                                        if (homeShopInformation.getResult()==1)
                                        {
                                            Intent intent1=new Intent(getActivity(), NoShopActivity.class);
                                            startActivity(intent1);
                                        }else {
                                            Intent intent=new Intent(getActivity(), ShopDetailActivity.class);
                                            startActivity(intent);
                                        }

                                    }
                                });
                    }


//                    String title="揭晓结果";
//                    StringBuffer sb=new StringBuffer();
//                    sb.append(ApiWebCommon.API_COMMON.Api_Common_Url);
//                    sb.append(String.format(ApiWebCommon.API_COMMON.Web_Index_Product_NewsInfo, bean.id));
//                    String url=sb.toString();
//                    setCommonWebViewShow(title, url);
                }
                    break;
                case R.id.iv_news_info_item_pic2:
                case R.id.tv_news_info_item_username:{
                	NewsInfoChildBean bean=(NewsInfoChildBean) view.getTag();
                    StringBuffer sb=new StringBuffer();
                    sb.append(ApiWebCommon.API_COMMON.Api_Common_Url);
                    sb.append(String.format(ApiWebCommon.API_COMMON.Web_Show_UserInfo_Detail, bean.q_uid));
                    String url=sb.toString();
                    setCommonWebViewShow("", url);
                }
                	break;
            }
        }

        class ViewHolder {
            View mArea;
            ImageView mIvPic;
            ImageView mIvPic2;
            TextView mTvUserName;
            TextView mTvPeopleTimes;
            TextView mTvHappyCode;
            TextView mTvTime;
        }


    }

}
