package com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.learn.soft.product.bean.IndexBeanAd;
import com.learn.soft.product.jni.JiaZhengApp;
import com.learn.soft.product.main.activity.ShowWebViewInfoActivity;
import com.learn.soft.product.main.activity.fragment.ShowTibInfoDlg;
import com.learn.soft.product.util.ApiWebCommon;
import com.learn.soft.product.util.BundleKey;
import com.learn.soft.product.util.MyLog;
import com.learn.soft.product.widget.adapter.AdImagePagerAdapter;
import com.learn.soft.product1.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by jipeng on 2017/3/24.
 */

public class AdviseImageAdapter extends BaseAdapter {
    private FragmentActivity mActivity;
    private List<IndexBeanAd> mAdData;
    private boolean isInfiniteLoop=true;
    private DisplayImageOptions options= JiaZhengApp.getInstance().getDefaultImgeOptions();

    public AdviseImageAdapter(FragmentActivity activity, List<IndexBeanAd> mAdData) {
        this.mAdData = mAdData;
        mActivity=activity;
        isInfiniteLoop = true;

    }

    @Override
    public int getCount() {
        return isInfiniteLoop ? Integer.MAX_VALUE : (mAdData!=null? mAdData.size(): 0);
    }

    private int getPosition(int position) {
        int number=mAdData!=null? mAdData.size(): 0;
        if (number>0){
            return isInfiniteLoop ? position % number : position;
        }else{
            return 0;
        }

    }

    @Override
    public View getView(final int position, View view, ViewGroup container) {
        final AdviseImageAdapter.ViewHolderAd holder;
        if (view == null) {
            holder = new AdviseImageAdapter.ViewHolderAd();
            view = LayoutInflater.from(mActivity).inflate(R.layout.activity_main_tab_index_common_frg_ad_item, null);
            holder.imageView = (ImageView) view.findViewById(R.id.iv_ad_pic);
            holder.mTvInfo= (TextView) view.findViewById(R.id.tv_ad_info);
            view.setTag(holder);
        } else {
            holder = (AdviseImageAdapter.ViewHolderAd) view.getTag();
        }
        try {
            IndexBeanAd item=mAdData.get(getPosition(position));
            if (item!=null){
                ImageLoader.getInstance().displayImage(item.img, holder.imageView, options);
                holder.mTvInfo.setText(item.title);
                holder.imageView.setTag(item);
//                holder.imageView.setOnClickListener(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

//    @Override
//    public void onClick(View view) {
//        switch (view.getId()){
//            case R.id.iv_ad_pic:
//                IndexBeanAd item= (IndexBeanAd) view.getTag();
//                StringBuffer sb=new StringBuffer();
//                sb.append(ApiWebCommon.API_COMMON.Api_Common_Url);
//                sb.append(String.format(ApiWebCommon.API_COMMON.Web_Index_Product_Advice, item.link));
//                String url=sb.toString();
////                setCommonWebViewShow("", url);
//                if ("圈中宝".equals(item.title)){
//                    if (mActivity!=null){
//                        ShowTibInfoDlg.showDlg(mActivity.getSupportFragmentManager());
//                    }
//                }else{
//                    /**点击图片的效果*/
//                    setCommonWebViewShow("", item.url);
//                }
//
//
////                Intent intent=new Intent(mActivity, ShowWebViewInfoActivity.class);
////                Bundle args=new Bundle();
////                args.putString(BundleKey.Bundle_KEY_Title, item.title);
////                StringBuffer sb=new StringBuffer();
////                sb.append(ApiWebCommon.API_COMMON.Api_Info).append(ApiWebCommon.API_COMMON.Api_InfoView_ViewPageAD).append(item.iD);
////                MyLog.i("xiaocai", "url=" + sb.toString());
////                args.putString(BundleKey.Bundle_KEY_Url, sb.toString());
////                intent.putExtras(args);
////                mActivity.startActivity(intent);
//                break;
//        }
//    }

//
//    public void setCommonWebViewShow(String title, String url){
//        Intent intent=new Intent( mActivity, ShowWebViewInfoActivity.class);
//        Bundle args=new Bundle();
//        args.putString(BundleKey.Bundle_KEY_Title, title);
//        MyLog.i("xiaocai", "url=" + url);
//        args.putString(BundleKey.Bundle_KEY_Url, url);
//        intent.putExtras(args);
//        mActivity.startActivity(intent);
//    }


    private class ViewHolderAd {
        ImageView imageView;
        TextView mTvInfo;
    }

    public AdviseImageAdapter setInfiniteLoop(boolean isInfiniteLoop) {
        this.isInfiniteLoop = isInfiniteLoop;
        return this;
    }

    @Override
    public Object getItem(int arg0) {
        return arg0;
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }
}
