package com.learn.soft.product.main;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.learn.soft.product.bean.IndexBeanToday;
import com.learn.soft.product.jni.BaseFragment;
import com.learn.soft.product.jni.JiaZhengApp;
import com.learn.soft.product.main.activity.ShowWebViewInfoActivity;
import com.learn.soft.product.util.ApiWebCommon;
import com.learn.soft.product.util.BundleKey;
import com.learn.soft.product.util.MyLog;
import com.learn.soft.product1.R;
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
public class TabIndexChildFragment extends BaseFragment implements View.OnClickListener {

    private View mArea;
    private ImageView mIvPic;
    private ImageView mIvPic2;
    private ProgressBar mPbShow;
    private TextView mTvTime;
    private TextView mTvInfo;
    private TextView mTvPrice;
    private TextView mTvJoin;
    private TextView mTvAll;
    private TextView mTvRelease;
    private IndexBeanToday mIndexBeanToday;

    public static TabIndexChildFragment getInstance(IndexBeanToday bean){
        TabIndexChildFragment frg=new TabIndexChildFragment();
        Bundle args=new Bundle();
        args.putSerializable(BundleKey.BUNDLE_KEY_SERIALIZABLE_DATA, bean);
        frg.setArguments(args);
        return frg;
    }

    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle args=getArguments()!=null? getArguments(): savedInstanceState;
        if (args!=null){
            mIndexBeanToday= (IndexBeanToday) args.getSerializable(BundleKey.BUNDLE_KEY_SERIALIZABLE_DATA);
        }

        View view = inflater.inflate(R.layout.activity_main_tab_index_second_item, container, false);

        mArea = view.findViewById(R.id.area_index_second_item);
        mIvPic = (ImageView) view.findViewById(R.id.iv_index_second_item_pic);
        mIvPic2 = (ImageView) view.findViewById(R.id.iv_index_second_item_pic2);
        mPbShow = (ProgressBar) view.findViewById(R.id.pb_index_second_item_bar);
        mTvTime = (TextView) view.findViewById(R.id.tv_index_second_item_status);
        mTvInfo = (TextView) view.findViewById(R.id.tv_index_second_item_info);
        mTvPrice = (TextView) view.findViewById(R.id.tv_index_second_item_price);
        mTvAll = (TextView) view.findViewById(R.id.tv_index_second_item_all);
        mTvJoin = (TextView) view.findViewById(R.id.tv_index_second_item_join);
        mTvRelease = (TextView) view.findViewById(R.id.tv_index_second_item_release);

		ImageLoader.getInstance().displayImage(mIndexBeanToday.thumb, mIvPic,
				JiaZhengApp.getInstance().getDefaultImgeOptions());
        mTvPrice.setText(String.format("价格:￥%s", mIndexBeanToday.money));
        mTvJoin.setText(String.valueOf(mIndexBeanToday.canyurenshu));
        mTvAll.setText(String.valueOf(mIndexBeanToday.zongrenshu));
        mTvRelease.setText(String.valueOf(mIndexBeanToday.zongrenshu - mIndexBeanToday.canyurenshu));
        int progress= 0;
        try {
            progress = mIndexBeanToday.canyurenshu*100/mIndexBeanToday.zongrenshu;
        } catch (Exception e) {
            e.printStackTrace();
        }

        refreshShowUi();
        mPbShow.setProgress(progress);
        mArea.setOnClickListener(this);
        return view;
    }

    private void refreshShowUi() {
        if (getActivity()!=null&&getActivity().getResources()!=null){
            long diff=getTimeInfo(mIndexBeanToday.xsjx_time*1000);
            if (diff>0){
                mIvPic2.setVisibility(View.GONE);
                mTvInfo.setText("第(1)期苹果6Plus");
                mTvTime.setBackgroundColor(getActivity().getResources().getColor(R.color.bg_common_red_color));
                mTvTime.setText(getTimeInfo2(diff));
            }else{
                mIvPic2.setVisibility(View.VISIBLE);
                mTvInfo.setText("恭喜*获得\n幸运好站长云购码：\n总共云购：20人次");
                mTvTime.setBackgroundColor(getActivity().getResources().getColor(R.color.transparent3));
                mTvTime.setText("已结束");
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.area_index_second_item:
                StringBuffer sb=new StringBuffer();
                sb.append(ApiWebCommon.API_COMMON.Api_Common_Url);
                sb.append(String.format(ApiWebCommon.API_COMMON.Web_Index_Product_Advice, mIndexBeanToday.id));
                String url=sb.toString();
                setCommonWebViewShow("", url);

                break;

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


    private long getTimeInfo(long time2){
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long diff =0;
        try
        {
            Date d1 = df.parse(df.format(new Date()));
            diff = time2 - d1.getTime();
        }
        catch (Exception e)
        {
        }
        return diff;
    }

    private String getTimeInfo2(long diff){
        long day=diff/(24*60*60*1000);
        long hour=(diff/(60*60*1000)-day*24);
        long min=((diff/(60*1000))-day*24*60-hour*60);
        long s=(diff/1000-day*24*60*60-hour*60*60-min*60);
        StringBuffer sb=new StringBuffer();
        if (day>0){
            sb.append(day).append("天");
        }
        if (hour>0){
            sb.append(String.format("%02d", hour)).append("时");
        }
        if (min>0){
            sb.append(String.format("%02d", min)).append("分");
        }
        if (s>0){
            sb.append(String.format("%02d", s)).append("秒");
        }
        return sb.toString();
    }

    private Timer mTimer;

    Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            refreshShowUi();
        }
    };

    private void beginTimer(){
        if (mTimer!=null){
            mTimer.cancel();
            mTimer=null;
        }
        if (mIndexBeanToday==null){
            return;
        }
        long diff=getTimeInfo(mIndexBeanToday.xsjx_time*1000);
        if (diff<0){
            return;
        }
        mTimer=new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                mHandler.sendEmptyMessage(0);
            }
        }, 1000, 1000);
    }

    private void cancelTimer(){
        if (mTimer!=null){
            mTimer.cancel();
            mTimer=null;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        beginTimer();
    }

    @Override
    public void onPause() {
        super.onPause();
        cancelTimer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelTimer();
    }
}
