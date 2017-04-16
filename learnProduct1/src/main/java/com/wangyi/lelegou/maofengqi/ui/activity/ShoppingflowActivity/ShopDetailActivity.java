package com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity;

import android.content.Context;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.learn.soft.product.bean.IndexBean;
import com.learn.soft.product.bean.IndexBeanAd;
import com.learn.soft.product.jni.JiaZhengApp;
import com.learn.soft.product.main.TabAllProductChildFragment;
import com.learn.soft.product.main.activity.ShowWebViewInfoActivity;
import com.learn.soft.product.util.ApiWebCommon;
import com.learn.soft.product.util.BundleKey;
import com.learn.soft.product.util.MyLog;
import com.learn.soft.product.util.StringUtil;
import com.learn.soft.product.util.ToastHelper;
import com.learn.soft.product.widget.adapter.AdImagePagerAdapter;
import com.learn.soft.product.widget.adview.AdViewFlow;
import com.learn.soft.product.widget.adview.CircleFlowIndicator;
import com.wangyi.lelegou.maofengqi.base.BaseActivity;
import com.learn.soft.product1.R;
import com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity.bean.AddCartInformation;
import com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity.bean.HomeShopInformation;
import com.wangyi.lelegou.maofengqi.utils.ApiClass;
import com.wangyi.lelegou.maofengqi.utils.OldResultCallback;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class ShopDetailActivity extends BaseActivity implements View.OnClickListener {

    //    private LinearLayout scrollview_title;//滑动的标题
    private AdViewFlow ad_shop_flow;//轮播
    private CircleFlowIndicator shop_circle_flow_indicator;//轮播的原点
    private Context context;
    private AdviseImageAdapter mAdPagerAdapter;//商品广播的适配器

    private TextView tv_shop_item_id;//商品id
    private TextView tv_shop_item_price;//商品价格
    private ProgressBar pb_shop_item_bar;//进度条
    private TextView tv_shop_item_join;//已加入人数
    private TextView tv_shop_item_all;//全部人数
    private TextView tv_shop_item_release;//剩余人数
    private Button btn_now_lelegou;//立即乐乐购
    private Button btn_add_cart;//添加购物车
    private RelativeLayout rl_lelegou_remark;//乐乐购记录
    private RelativeLayout rl_picture_information;//图文信息
    private RelativeLayout rl_review_and_comment;//晒单和评论父布局
    private TextView tv_review_and_comment;//晒单和评论

    private RelativeLayout rl_award_information;//整个中奖者信息的布局id

    private LinearLayout ll_old_award_information;//往期中奖人信息
    private LinearLayout ll_activity_over_information;//

    private ImageView img_award_user;//中奖者头像
    private TextView award_name;//中奖姓名
    private TextView buy_shop_times;//购买商品次数
    private TextView luck_lelegou_code;//幸运乐乐购码
    private TextView open_award_time;//开奖时间
    private TextView buy_lelegou_time;//购买时间

    private TextView shop_summary;//商品描述
    private TextView tv_price_zone;//价格专区

//    private ViewPager viewpager;
//    private PagerTabStrip tabstrip;
//    private ArrayList<String> viewpagerTitleList=new ArrayList<>();
//    ArrayList<View> viewContainter = new ArrayList<View>();

    private TabLayout tabs;//顶部导航栏

    TextView tv_content;//标题内容

    private Button btn_quanzhongbao_now_lelegou;

    private ImageView old_usr_img;//老的中奖
    private TextView old_times;
    private TextView open_time;
    private TextView buy_time;
    private TextView dubao_code;
    private Button btn_check_result;


    private LinearLayout ll_double_button;


    private TextView tv_not_award;
    private Button btn_daze_buy;
    private TextView tv_times_ing;
    private Button btn_look_information;

    private RelativeLayout ll_round_informa;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_now_lelegou:
                MyLog.e("jp","立即乐乐购===========btn_now_lelegou");
                /**立即乐乐购*/
                NowGoLelegou();
                break;
            case R.id.btn_add_cart:
                /**添加购物车*/
                MyLog.e("jp","添加购物车===========btn_add_cart");
                AddCart();
                break;
            case R.id.rl_lelegou_remark:
                /**乐乐购记录*/
                MyLog.e("jp","乐乐购记录===========rl_lelegou_remark");
                rllelegouremark();
                break;
            case R.id.rl_picture_information:
                /**图文信息*/
                MyLog.e("jp","图文信息===========rl_picture_information");
                rlpictureinformation();
                break;
            case R.id.rl_review_and_comment:
                /**晒单和评论父布局*/
                MyLog.e("jp","晒单和评论父布局===========rl_review_and_comment");
                reviewandcomment();
                break;
            case R.id.rl_award_information:
                /**整个中奖者信息的布局id*/
                MyLog.e("jp","整个中奖者信息的布局id===========rl_award_information");
                rlawardinformation();
                break;
            case R.id.btn_quanzhongbao_now_lelegou:
                /**只有一个乐乐购*/
                MyLog.e("jp","只有一个乐乐购===========btn_quanzhongbao_now_lelegou");
                quanzhongbaonowlelegou();
                break;
            case R.id.btn_check_result:
                /**计算结果*/
                MyLog.e("jp","计算结果===========btn_check_result");
                checkComputeResult();
                break;
            case R.id.btn_look_information:
                /**查看期数详情*/
                MyLog.e("jp","查看期数详情===========btn_look_information");
                rlawardinformationing();
                break;
            case R.id.btn_daze_buy:
                MyLog.e("jp","打折购买===========btn_daze_buy");
                /**打折购买*/
                dazebuy();
                break;
            case R.id.img_award_user:
                /**个人主页*/
                MyLog.e("jp","个人主页===========img_award_user");
                personZeon();
                break;
            default:
                break;
        }
    }

    private void rlawardinformationing() {

        showProgressInfo(null);
        OkHttpUtils.post().url("http://101.201.115.226:8082/yyg_app/queryShopDetail")
                .addParams("itemId",bean.getObj().getShoplistNums().get(0).getItemId()+"")
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
                        Log.e("jp","CircleAdapter2---->订单详情==onResponse:"+response);
                        Gson gson=new Gson();
                        HomeShopInformation homeShopInformation = gson.fromJson(response, HomeShopInformation.class);
                        JiaZhengApp.getInstance().setBean(homeShopInformation);
//                        bean=null;
//                        setdata();
                        Intent intent=new Intent(context,ShopDetailActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
    }


    /**圈中宝过来，立即乐乐购*/
    private void quanzhongbaonowlelegou() {

        //=========================html5
//        showProgressInfo("");
//        StringBuffer sb=new StringBuffer();
//        sb.append(ApiWebCommon.API_COMMON.Api_Common_Url);
//        sb.append(String.format(ApiWebCommon.API_COMMON.Api_circle_pay+circleid));
//        String url=sb.toString();
//        MyLog.e("jp","url:"+url);
//        setCommonWebViewShow("获取宝贝",url);
//        dismissProgress();
//        //// TODO: 2017/3/27
//       /**==============20170327===立即乐乐购的时候结束这个页面*/
//        finish();

        //============================================原生
        Intent intent=new Intent(context,PayActivity.class);
        intent.putExtra("tag","2");//表示是从商品详情里跳转的
//        intent.putExtra("shopid",bean.getObj().getItemId()+"");
        intent.putExtra("circleid",circleid);
        intent.putExtra("type","1");
        startActivity(intent);

    }

    /**打折立即购买*/
    private void dazebuy() {

       //=========================html5
//        String url="http://91lelegou.com/?/pay/discount_shoplist/go_cart/"+bean.getObj().getItemId()+"/"+bean.getObj().getDiscountPrice();
//        setCommonWebViewShow("计算结果",url);

        //=========================================原生
        Intent intent=new Intent(context,PayActivity.class);
        intent.putExtra("tag","3");
        intent.putExtra("shopid",bean.getObj().getItemId()+"");
//        intent.putExtra("circleid","");
        intent.putExtra("type","2");
        startActivity(intent);
    }

    private void checkComputeResult() {
        String url="http://91lelegou.com/?/mobile/mobile/calResult/"+bean.getObj().getItemId();
        setCommonWebViewShow("计算结果",url);
    }

    private void personZeon() {
        String url="http://91lelegou.com/?/mobile/mobile/userindex/"+bean.getObj().getItemId();
        setCommonWebViewShow("个人中心",url);
    }

    private void rlawardinformation() {
        MyLog.e("jp","====>rlawardinformation:"+bean.getObj().getShoplistNums());
        showProgressInfo(null);
        OkHttpUtils.post().url("http://101.201.115.226:8082/yyg_app/queryShopDetail")
                .addParams("itemId",bean.getObj().getShoplistNums().get(1).getItemId()+"")
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
                        Log.e("jp","CircleAdapter2---->订单详情==onResponse:"+response);
                        Gson gson=new Gson();
                        HomeShopInformation homeShopInformation = gson.fromJson(response, HomeShopInformation.class);
                        JiaZhengApp.getInstance().setBean(homeShopInformation);
//                        bean=null;
//                        setdata();
                        Intent intent=new Intent(context,ShopDetailActivity.class);
                        startActivity(intent);
                        finish();

                    }
                });


    }

    private void reviewandcomment() {
        String url="http://91lelegou.com/?/mobile/mobile/goodspost/"+bean.getObj().getSid();
        setCommonWebViewShow("晒单和评论",url);
    }

    private void rlpictureinformation() {
        String url="http://91lelegou.com/?/mobile/mobile/goodsdesc/"+bean.getObj().getItemId();
        setCommonWebViewShow("商品详情",url);
    }

    private void rllelegouremark() {
        String url="http://91lelegou.com/?/mobile/mobile/buyrecords/"+bean.getObj().getItemId();
        setCommonWebViewShow("所有乐乐购记录",url);
    }

    public void setCommonWebViewShow(String title, String url) {
        Intent intent = new Intent(this, ShowWebViewInfoActivity.class);
        Bundle args = new Bundle();
        args.putString(BundleKey.Bundle_KEY_Title, title);
        args.putString(BundleKey.Bundle_KEY_Url, url);
//        args.putString("mark","mark");//用于标识从商品详情跳转的
        intent.putExtras(args);
        startActivity(intent);
    }

    private void AddCart() {

//==========================================================================原生
//        showProgressInfo(null);
        OkHttpUtils.post()
                .url(Config.testurl+Config.addShopingCart)
                .addParams("uid",JiaZhengApp.getInstance().getUserId())
                .addParams("shopid",bean.getObj().getItemId()+"")
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
//=====================================================================================================


//        StringBuffer sb=new StringBuffer();
//        sb.append(ApiWebCommon.API_COMMON.Api_Common_Url);
//        sb.append(String.format(ApiWebCommon.API_COMMON.Api_Add_Cart_Shop, bean.getObj().getItemId()));
//        String url=sb.toString();
//        loadcart(url);


    }
      /**添加购物车*/
   private void loadcart(String url)
    {
        if (mWvShow==null){
            mWvShow=new WebView(this);
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

            mWvShow.setWebViewClient(new SelfWebViewClient1());
            mWvShow.setWebChromeClient(new SelfChromeClient1());
        }
        mWvShow.loadUrl(url);
    }


    class SelfWebViewClient1 extends WebViewClient {

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

    class SelfChromeClient1 extends WebChromeClient {
        public SelfChromeClient1() {
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (mHandler1 !=null){
                mHandler1.sendEmptyMessage(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }

    }

    Handler mHandler1 = new Handler() {
        public void handleMessage(Message msg) {
            if(msg.what==100) {
                dismissProgress();
                //这里是添加成功的消息
                ToastHelper.toast("添加成功");
            }
        }
    };

  //=======================================================================

    private void NowGoLelegou() {
        //=====html5
//        showProgressInfo("");
//        StringBuffer sb=new StringBuffer();
//        sb.append(ApiWebCommon.API_COMMON.Api_Common_Url);
//        sb.append(String.format(ApiWebCommon.API_COMMON.Api_Add_Cart_Shop, bean.getObj().getItemId()));
//        String url=sb.toString();
//        beginLoadData(url);


        //===========================================原生
        /**添加购物车*/

        OkHttpUtils.post()
                .url(Config.testurl+Config.addShopingCart)
                .addParams("uid",JiaZhengApp.getInstance().getUserId())
                .addParams("shopid",bean.getObj().getItemId()+"")
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
//                ToastHelper.toast(addCartInformation.getInfo());
                Intent cartinformation=new Intent(context,CartActivity.class);
                startActivity(cartinformation);
            }
        });
        //===========================================

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shop_detail;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        context = this;
        initview();
    }
    String tag;
    String circleid;
    @Override
    protected void onResume() {
        super.onResume();
        Intent intent=getIntent();
        /**证明是从自己圈子去掉*/
        if (intent!=null)
        {
            tag = getIntent().getStringExtra("tag");
            circleid=getIntent().getStringExtra("circleid");
        }

        setdata();
    }

    private HomeShopInformation bean;

    private void setdata() {
        bean = JiaZhengApp.getInstance().getBean();

        MyLog.e("jp", "ShopDetailActivity--------------->HomeShopInformation:" + bean.getResult() + bean.getInfo() + bean.getObj());
        int type = bean.getObj().getType();
        switch (type) {
            case 0:
                /**0 普通商品*/
//                ll_old_award_information.setVisibility(View.GONE);
//                ll_activity_over_information.setVisibility(View.GONE);
//                btn_quanzhongbao_now_lelegou.setVisibility(View.GONE);
                tv_price_zone.setVisibility(View.GONE);
                ll_old_award_information.setVisibility(View.GONE);
                ll_double_button.setVisibility(View.VISIBLE);
                ll_activity_over_information.setVisibility(View.GONE);
                showgeneralgoods();
                break;
            case 1:
                /**1 圈子商品*/
                tv_price_zone.setVisibility(View.GONE);
                tabs.setVisibility(View.GONE);
                showRoundGoods();
                break;
            case 2:
                /**2 揭晓结果-普通商品*/
                rl_award_information.setVisibility(View.GONE);
                tv_price_zone.setVisibility(View.GONE);
                ll_old_award_information.setVisibility(View.VISIBLE);
                ll_activity_over_information.setVisibility(View.VISIBLE);
                ll_double_button.setVisibility(View.GONE);
                btn_quanzhongbao_now_lelegou.setVisibility(View.GONE);
                tv_not_award.setVisibility(View.GONE);
                btn_daze_buy.setVisibility(View.GONE);
                AnnounceResultsGeneralGoods();
                break;
            case 3:
                /**3 揭晓结果-圈子*/
                tv_price_zone.setVisibility(View.GONE);
                tabs.setVisibility(View.GONE);
                ll_old_award_information.setVisibility(View.VISIBLE);
                ll_activity_over_information.setVisibility(View.VISIBLE);
                ll_double_button.setVisibility(View.GONE);
                btn_quanzhongbao_now_lelegou.setVisibility(View.GONE);
                tv_not_award.setVisibility(View.GONE);
                btn_daze_buy.setVisibility(View.GONE);
                ll_round_informa.setVisibility(View.GONE);

                RoundResultGenalGoods();

                break;
            case 4:
                /**揭晓结果-二人专区*/
                tv_price_zone.setVisibility(View.GONE);
                ll_old_award_information.setVisibility(View.VISIBLE);
                ll_activity_over_information.setVisibility(View.VISIBLE);
                ll_double_button.setVisibility(View.GONE);
                TowPersonResult();
                break;
            default:

                break;
        }

    }

    private void TowPersonResult() {
        //期数
        List<HomeShopInformation.ObjBean.ShoplistNumsBean> titles = bean.getObj().getShoplistNums();
        /**商品期数信息列表*/
        if (!isclick)
        {
            GoodsPeriodsInformation(titles);
            isclick=true;
        }
        /**往期中奖信息*/
        oldAwardInformation();
        tv_not_award.setText("很遗憾，您未中奖，您可以以8折购买此商品");
        btn_daze_buy.setText("¥"+bean.getObj().getDiscountPrice()+",立即购买");
        /**公共信息*/
        commonPart();
        /**轮播图*/
        GoodsCarousel();
    }

    private void RoundResultGenalGoods() {
        /**往期中奖信息*/
        oldAwardInformation();
        /**公共信息*/
        commonPart();
        /**轮播图*/
        GoodsCarousel();
    }

    private void AnnounceResultsGeneralGoods() {
        //期数
        List<HomeShopInformation.ObjBean.ShoplistNumsBean> titles = bean.getObj().getShoplistNums();
        /**商品期数信息列表*/
        if (!isclick)
        {
            GoodsPeriodsInformation(titles);
            isclick=true;
        }
        /**往期中奖信息*/
        oldAwardInformation();

        /**公共信息*/
        commonPart();
        /**轮播图*/
        GoodsCarousel();
    }

    private void oldAwardInformation() {
        //中奖人信息
        HomeShopInformation.ObjBean.PrizeRecordBean prizeRecord = bean.getObj().getPrizeRecord();
        MyLog.e("jp","oldAwardInformation----->prizeRecord.getUphoto():"+prizeRecord.getUphoto());

            Glide.with(this)
                    .load(prizeRecord.getUphoto())
                    .error(R.drawable.ic_user_head)
                    .into(old_usr_img);

        old_times.setText(prizeRecord.getGonumber() + "");
        open_time.setText(prizeRecord.getEndTime());
        buy_time.setText(prizeRecord.getStartTime());
        dubao_code.setText(prizeRecord.getUserCode());
        List<HomeShopInformation.ObjBean.ShoplistNumsBean> titles = bean.getObj().getShoplistNums();
        if (titles.size()>0)
        {
            HomeShopInformation.ObjBean.ShoplistNumsBean shoplistNumsBean = titles.get(0);
            tv_times_ing.setText("第"+shoplistNumsBean.getQishu()+"正在进行...");
        }

    }

    private void showRoundGoods() {
        if (tag.equals("1"))
        {
            ll_double_button.setVisibility(View.GONE);
            btn_quanzhongbao_now_lelegou.setVisibility(View.VISIBLE);
        }
        /**公共信息*/
        commonPart();
        /**轮播图*/
        GoodsCarousel();
    }

    private void showgeneralgoods() {

        //期数
        List<HomeShopInformation.ObjBean.ShoplistNumsBean> titles = bean.getObj().getShoplistNums();

        /**商品期数信息列表*/
        if (!isclick)
        {
            GoodsPeriodsInformation(titles);
            isclick=true;
        }


        /**公共信息*/
        commonPart();

        /**中奖人信息*/
        AwardPersonInformation(titles);

        /**轮播图*/
        GoodsCarousel();


    }
    boolean isclick=false;
    private void GoodsPeriodsInformation(List<HomeShopInformation.ObjBean.ShoplistNumsBean> titles) {

        for (int i = 0; i < titles.size(); i++) {
            if (titles.get(i).isIsCurrent()) {
                tabs.addTab(tabs.newTab().setText("第" + titles.get(i).getQishu() + "期"),i,true);
            } else {
                tabs.addTab(tabs.newTab().setText("第" + titles.get(i).getQishu() + "期"));
            }

        }


        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.e("jp", "==onTabSelected:" + tab.getPosition());
                List<HomeShopInformation.ObjBean.ShoplistNumsBean> titles = bean.getObj().getShoplistNums();
                int itemId=titles.get(tab.getPosition()).getItemId();
                    showProgressInfo(null);
                    OkHttpUtils.post().url("http://101.201.115.226:8082/yyg_app/queryShopDetail")
                            .addParams("itemId",itemId+"")
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
                                    Log.e("jp","onTabSelected---->订单详情==onResponse:"+response);
                                    Gson gson=new Gson();
                                    HomeShopInformation homeShopInformation = gson.fromJson(response, HomeShopInformation.class);
                                    JiaZhengApp.getInstance().setBean(homeShopInformation);

//                                    Intent intent=new Intent(context,ShopDetailActivity.class);
//                                    startActivity(intent);
//                                    finish();
                                    bean=null;
//                                    tabs.removeAllTabs();
                                    setdata();

                                }
                            });
//                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Log.e("jp", "==onTabUnselected:" + tab.getPosition());

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                Log.e("jp", "==onTabReselected:" + tab.getPosition());
                isclick=true;
            }
        });
    }

    private void AwardPersonInformation(List<HomeShopInformation.ObjBean.ShoplistNumsBean> titles) {
        if (titles.size() <= 1) {
            rl_award_information.setVisibility(View.GONE);
        } else {
            rl_award_information.setVisibility(View.VISIBLE);
            //中奖人信息
            HomeShopInformation.ObjBean.PrizeRecordBean prizeRecord = bean.getObj().getPrizeRecord();
            MyLog.e("jp","AwardPersonInformation====>prizeRecord.getUphoto():"+prizeRecord.getUphoto());
//            if (prizeRecord.getUphoto().equals(""))
//            {
//                img_award_user.setImageResource(R.drawable.ic_user_head);
//            }else {
                Glide.with(this)
                        .load(prizeRecord.getUphoto())
                        .error(R.drawable.ic_user_head)
                        .into(img_award_user);
//            }
            award_name.setText(prizeRecord.getUsername());
            buy_shop_times.setText(prizeRecord.getGonumber() + "次");
            luck_lelegou_code.setText(prizeRecord.getUserCode());
            open_award_time.setText(prizeRecord.getEndTime());
            buy_lelegou_time.setText(prizeRecord.getStartTime());
        }
    }


    private void commonPart() {
        //// TODO: 2017/3/17 公共部分
        HomeShopInformation.ObjBean data = bean.getObj();
        shop_summary.setText(data.getTitle()+data.getTitle2());
        tv_shop_item_price.setText("¥:" + data.getMoney());
//        int progress = data.getCanyurenshu() / data.getZongrenshu();
        pb_shop_item_bar.setMax(data.getZongrenshu());
        pb_shop_item_bar.setProgress(data.getCanyurenshu());//// TODO: 2017/3/17
        tv_shop_item_join.setText(data.getCanyurenshu() + "");
        tv_shop_item_all.setText(data.getZongrenshu() + "");
        tv_shop_item_release.setText(data.getShenyurenshu() + "");
//        tv_review_and_comment.setText("已有" + data.getShaidanNum() + "个幸运者晒单" + data.getShaidanCommentNum() + "条评论");
        sinple_jiage.setText("单价："+data.getUnitMoney()+"");
    }

    private void GoodsCarousel() {
        String picarr = bean.getObj().getPicarr();
        String p = picarr.replaceAll("[\\[\\]]", "");
        String[] jpgs = p.split(", ");
        List<IndexBeanAd> mListAd = new ArrayList<>();
        IndexBeanAd ad = null;
        for (int i = 0; i < jpgs.length; i++) {
            if (ad == null) {
                ad = new IndexBeanAd();
                ad.img = "http://goodimage.91lelegou.com/" + jpgs[i];
                ad.title = "";
                mListAd.add(ad);
            }
            ad = null;
        }
        showAdDataAdapter(mListAd);

    }

    // 数据
    private OldResultCallback indexCallback = new OldResultCallback() {

        @Override
        public void onResponse(String response, int id) {
            dismissProgress();
            if (StringUtil.isNotEmpty(response)) {
                showAdapter(response);
            }

        }

        @Override
        public void onError(Call call, Exception e, int id) {
            dismissProgress();
            ToastHelper.toast(e.getMessage());

        }
    };

    private void showAdapter(String response) {
        try {
            MyLog.i("xiaocai", "data=" + response);
            IndexBean mData = new Gson().fromJson(response,
                    new TypeToken<IndexBean>() {
                    }.getType());
            if (mData != null && mData.status == 1) {
                // 首页的广告
                int number = mData.listAd != null ? mData.listAd.size() : 0;
                if (number > 0) {
                    showAdDataAdapter(mData.listAd);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 广告 轮播图
     */
    private void showAdDataAdapter(List<IndexBeanAd> mListAd) {
        int number = mListAd != null ? mListAd.size() : 0;
        if (number > 0) {
            mAdPagerAdapter = new AdviseImageAdapter(this, mListAd)
                    .setInfiniteLoop(true);
            ad_shop_flow.setAdapter(mAdPagerAdapter);
            ad_shop_flow.setmSideBuffer(number);
            if (number > 1) {
                shop_circle_flow_indicator.setVisibility(View.VISIBLE);
                ad_shop_flow.setFlowIndicator(shop_circle_flow_indicator);
            }
            ad_shop_flow.setTimeSpan(4500);
            ad_shop_flow.setSelection(number * 1000); // 设置初始位置
            ad_shop_flow.startAutoFlowTimer(); // 启动自动播放
            shop_circle_flow_indicator.postInvalidate();
        }
    }

    private void initview() {
        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_content.setText("商品详情");
        tabs = (TabLayout) findViewById(R.id.tabs);

        ll_old_award_information = (LinearLayout) findViewById(R.id.ll_old_award_information);

        ad_shop_flow = (AdViewFlow) findViewById(R.id.ad_shop_flow);
        shop_circle_flow_indicator = (CircleFlowIndicator) findViewById(R.id.shop_circle_flow_indicator);


        tv_shop_item_id = (TextView) findViewById(R.id.tv_shop_item_id);
        tv_shop_item_price = (TextView) findViewById(R.id.tv_shop_item_price);
        pb_shop_item_bar = (ProgressBar) findViewById(R.id.pb_shop_item_bar);
        tv_shop_item_join = (TextView) findViewById(R.id.tv_shop_item_join);
        tv_shop_item_all = (TextView) findViewById(R.id.tv_shop_item_all);
        tv_shop_item_release = (TextView) findViewById(R.id.tv_shop_item_release);

        btn_now_lelegou = (Button) findViewById(R.id.btn_now_lelegou);
        btn_now_lelegou.setOnClickListener(this);
        btn_add_cart = (Button) findViewById(R.id.btn_add_cart);
        btn_add_cart.setOnClickListener(this);

        rl_lelegou_remark = (RelativeLayout) findViewById(R.id.rl_lelegou_remark);
        rl_lelegou_remark.setOnClickListener(this);
        rl_picture_information = (RelativeLayout) findViewById(R.id.rl_picture_information);
        rl_picture_information.setOnClickListener(this);
        rl_review_and_comment = (RelativeLayout) findViewById(R.id.rl_review_and_comment);
        rl_review_and_comment.setOnClickListener(this);
        tv_review_and_comment = (TextView) findViewById(R.id.tv_review_and_comment);

        rl_award_information = (RelativeLayout) findViewById(R.id.rl_award_information);
        rl_award_information.setOnClickListener(this);
        img_award_user = (ImageView) findViewById(R.id.img_award_user);
        img_award_user.setOnClickListener(this);

        award_name = (TextView) findViewById(R.id.award_name);
        buy_shop_times = (TextView) findViewById(R.id.buy_shop_times);
        luck_lelegou_code = (TextView) findViewById(R.id.luck_lelegou_code);
        open_award_time = (TextView) findViewById(R.id.open_award_time);
        buy_lelegou_time = (TextView) findViewById(R.id.buy_lelegou_time);

        shop_summary = (TextView) findViewById(R.id.shop_summary);
        tv_price_zone = (TextView) findViewById(R.id.tv_price_zone);

        btn_quanzhongbao_now_lelegou = (Button) findViewById(R.id.btn_quanzhongbao_now_lelegou);
        btn_quanzhongbao_now_lelegou.setOnClickListener(this);


        old_usr_img = (ImageView) findViewById(R.id.old_usr_img);
        old_times = (TextView) findViewById(R.id.old_times);
        open_time = (TextView) findViewById(R.id.open_time);
        buy_time = (TextView) findViewById(R.id.buy_time);
        dubao_code = (TextView) findViewById(R.id.dubao_code);
        btn_check_result = (Button) findViewById(R.id.btn_check_result);
        btn_check_result.setOnClickListener(this);

        ll_double_button= (LinearLayout) findViewById(R.id.ll_double_button);

        tv_not_award= (TextView) findViewById(R.id.tv_not_award);
        btn_daze_buy= (Button) findViewById(R.id.btn_daze_buy);
        btn_daze_buy.setOnClickListener(this);
        tv_times_ing= (TextView) findViewById(R.id.tv_times_ing);
        btn_look_information=(Button) findViewById(R.id.btn_look_information);
        btn_look_information.setOnClickListener(this);

        ll_activity_over_information= (LinearLayout) findViewById(R.id.ll_activity_over_information);

        ll_round_informa= (RelativeLayout) findViewById(R.id.ll_round_informa);
        sinple_jiage= (TextView) findViewById(R.id.sinple_jiage);
    }
     TextView sinple_jiage;
    private WebView mWvShow=null;
    private void beginLoadData(String url) {
        showProgressInfo("");
        if (mWvShow==null){
            mWvShow=new WebView(this);
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
       
        String url1="http://91lelegou.com/?/mobile/cart/cartlist";
        setCommonWebViewShow("购物车",url1);
        dismissProgress();
        //// TODO: 2017/3/27  
        /**==============20170327*/
        finish();
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
                //这里是立即乐乐购按钮添加成功的消息
//                ToastHelper.toast("添加成功");
            }
        }
    };

}
