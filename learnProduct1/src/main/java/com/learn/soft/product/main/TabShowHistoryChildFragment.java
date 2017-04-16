package com.learn.soft.product.main;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.format.DateUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.learn.soft.product.bean.HistoryBean;
import com.learn.soft.product.bean.HistoryChildBean;
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
public class TabShowHistoryChildFragment extends BaseTabFragment implements View.OnClickListener, PullToRefreshBase.OnRefreshListener2 {

    private PullToRefreshListView mPRLVshow;
    private ListView mLVShow;
    private HistoryInfoAdapter mHistoryInfoAdapter;
    private LoadDataType mLoadDataType = LoadDataType.FirstLoad;
    private int mPageIndex=1;
    private int mPostion=0;
    private final ArrayList<HistoryChildBean> mProductList=new ArrayList<HistoryChildBean>();

    public static TabShowHistoryChildFragment getInstance(int position){
        TabShowHistoryChildFragment frg=new TabShowHistoryChildFragment();
        Bundle args=new Bundle();
        args.putInt(BundleKey.Bundle_KEY_POSITION, position);
        frg.setArguments(args);
        return frg;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args=getArguments()!=null? getArguments(): savedInstanceState;
        if (args!=null){
            mPostion=args.getInt(BundleKey.Bundle_KEY_POSITION);
        }

        View view = inflater.inflate(R.layout.activity_main_tab_showhistory_childfrg, container, false);
        mPRLVshow = (PullToRefreshListView) view.findViewById(R.id.plv_common_pulltofresh);
        mPRLVshow.setOnRefreshListener(this);
        mPRLVshow.setEmptyView(LayoutInflater.from(getActivity()).inflate(R.layout.common_empty_show, null));
        mLVShow = mPRLVshow.getRefreshableView();
        getHistoryList();

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

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
        getHistoryList();
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
        getHistoryList();
    }


    public void getHistoryList() {
        StringBuffer sb=new StringBuffer();
        sb.append(ApiWebCommon.API_COMMON.Api_Common_Url);
        if (mPostion==0){
            sb.append(ApiWebCommon.API_COMMON.Api_ShaiDan_List_New);
        }else if (mPostion==1){
            sb.append(ApiWebCommon.API_COMMON.Api_ShaiDan_List_People);
        }else if (mPostion==2){
            sb.append(ApiWebCommon.API_COMMON.Api_ShaiDan_List_Comment);
        }else{
            sb.append(ApiWebCommon.API_COMMON.Api_ShaiDan_List_New);
        }
        sb.append(mPageIndex);
        String url=sb.toString();
        SendHttpUtils.getInstance().postCommon(url, url, null, new SendHttpUtils.HttpCallback() {
            @Override
            public void onSuccess(String data) {
                if (StringUtil.isNotEmpty(data)) {
                    MyLog.i("xiaocai", "Api_Index_Product_List string=" + data);
                    HistoryBean mData = null;
                    try {
                        final TypeToken type = new TypeToken<HistoryBean>() {
                        };
                        mData = new Gson().fromJson(data, type.getType());
                        if (mLoadDataType == LoadDataType.FirstLoad || mLoadDataType == LoadDataType.RefreshLoad) {
                            mProductList.clear();
                        }
                        if (mData != null && mData.status == 1) {
                            int number = mData.list != null ? mData.list.size() : 0;
                            if (number > 0) {
                                mProductList.addAll(mData.list);
                            }
                        }
                        showLvShowDataAdapter();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (mLoadDataType != LoadDataType.FirstLoad) {
                        stopLoadingRefreshState();
                    }

                }

            }

            @Override
            public void onError(String msg) {
                ToastHelper.toast(msg);
                if (mLoadDataType != LoadDataType.FirstLoad) {
                    stopLoadingRefreshState();
                }

            }
        });
    }

    private void showLvShowDataAdapter() {
        if (mHistoryInfoAdapter ==null){
            mHistoryInfoAdapter =new HistoryInfoAdapter();
            mLVShow.setAdapter(mHistoryInfoAdapter);
        }else{
            mHistoryInfoAdapter.notifyDataSetChanged();
        }
    }


    public class HistoryInfoAdapter extends BaseAdapter implements View.OnClickListener {

        private DisplayImageOptions options= JiaZhengApp.getInstance().getDefaultImgeOptions();

        @Override
        public int getCount() {
            return mProductList!=null? mProductList.size(): 0;
        }
        @Override
        public HistoryChildBean getItem(int position) {
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
                view = LayoutInflater.from(getActivity()).inflate(R.layout.activity_main_tab_showhistory_childfrg_item, parent, false);
                holder = new ViewHolder();
                holder.mArea = view.findViewById(R.id.area_history_info_item);
                holder.mIvUserPic = (ImageView) view.findViewById(R.id.iv_history_info_item_userpic);
                holder.mIvPic = (ImageView) view.findViewById(R.id.iv_history_info_item_pic);
                holder.mTvTitle = (TextView) view.findViewById(R.id.tv_history_info_item_title);
                holder.mTvInfo = (TextView) view.findViewById(R.id.tv_history_info_item_info);
                holder.mTvTitle = (TextView) view.findViewById(R.id.tv_history_info_item_title);
                holder.mTvLove = (TextView) view.findViewById(R.id.tv_history_info_item_love);
                holder.mTvComment = (TextView) view.findViewById(R.id.tv_history_info_item_comment);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            HistoryChildBean bean=getItem(position);
            if (bean!=null){
				ImageLoader.getInstance().displayImage(bean.sd_thumbs,
						holder.mIvPic, options);
				ImageLoader.getInstance().displayImage(bean.pic,
						holder.mIvUserPic,
						JiaZhengApp.getInstance().getUserPicOptions());
                holder.mTvInfo.setText(Html.fromHtml(bean.sd_content));
                StringBuffer sb=new StringBuffer();
                int index1=0;
                sb.append(bean.user);
                int index2=sb.toString().length();
                sb.append(": ");
                sb.append(bean.sd_title);
                int index3=sb.toString().length();
                sb.append(" ");
                sb.append(bean.time);
                int index4=sb.toString().length();
                SpannableString ss = new SpannableString(sb.toString());
                ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blue_main)), index1, index2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black_light_color)), index3, index4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                AbsoluteSizeSpan span = new AbsoluteSizeSpan(20);
                ss.setSpan(span, index3, index4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.mTvTitle.setText(ss);
                holder.mTvLove.setText(String.format("%s人羡慕嫉妒", bean.sd_zhan));
                holder.mTvComment.setText(String.format("%s条评论", bean.sd_ping));

                holder.mArea.setTag(bean);
                holder.mArea.setOnClickListener(this);
                holder.mIvUserPic.setTag(bean);
                holder.mIvUserPic.setOnClickListener(this);
                holder.mTvTitle.setTag(bean);
                holder.mTvTitle.setOnClickListener(this);
            }
            return view;
        }


        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.area_history_info_item:{
                    HistoryChildBean bean= (HistoryChildBean) view.getTag();
                    StringBuffer sb=new StringBuffer();
                    sb.append(ApiWebCommon.API_COMMON.Api_Common_Url);
                    sb.append(String.format(ApiWebCommon.API_COMMON.Web_Show_History_Detail, bean.sd_id));
                    String url=sb.toString();
                    setCommonWebViewShow("", url);
                }
                break;
                case R.id.tv_history_info_item_title:
                case R.id.iv_history_info_item_userpic:{
                    HistoryChildBean bean= (HistoryChildBean) view.getTag();
                    StringBuffer sb=new StringBuffer();
                    sb.append(ApiWebCommon.API_COMMON.Api_Common_Url);
                    sb.append(String.format(ApiWebCommon.API_COMMON.Web_Show_UserInfo_Detail, bean.sd_userid));
                    String url=sb.toString();
                    setCommonWebViewShow("", url);
                }
                break;
            }
        }

        class ViewHolder {
            View mArea;
            ImageView mIvUserPic;
            ImageView mIvPic;
            TextView mTvTitle;
            TextView mTvInfo;
            TextView mTvLove;
            TextView mTvComment;
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

}
