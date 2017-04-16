package com.wangyi.lelegou.maofengqi.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.learn.soft.product.jni.JiaZhengApp;
import com.learn.soft.product.util.MyLog;
import com.learn.soft.product.util.StringUtil;
import com.learn.soft.product.util.ToastHelper;
import com.learn.soft.product1.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.wangyi.lelegou.maofengqi.base.BaseFragment;
import com.wangyi.lelegou.maofengqi.bean.ResultBean;
import com.wangyi.lelegou.maofengqi.bean.UserShowBean;
import com.wangyi.lelegou.maofengqi.ui.activity.LoginActivity;
import com.wangyi.lelegou.maofengqi.ui.activity.MainActivity;
import com.wangyi.lelegou.maofengqi.ui.activity.PersonalDataActivity;
import com.wangyi.lelegou.maofengqi.ui.activity.SearchProductActivity;
import com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity.AccountRechargeActivity;
import com.wangyi.lelegou.maofengqi.ui.activity.ShoppingflowActivity.GetAwardShopInforActivity;
import com.wangyi.lelegou.maofengqi.utils.ApiClass;
import com.wangyi.lelegou.maofengqi.utils.Constant;
import com.wangyi.lelegou.maofengqi.utils.ResultCallback;
import com.wangyi.lelegou.maofengqi.utils.Utils;

import java.util.HashMap;
import java.util.Map;

public class MineFragment extends BaseFragment implements OnClickListener {

	protected TextView tvRight;
	protected TextView mTvPhone;
	protected TextView mTvJingyan;
	protected TextView mTvLedou;
//	protected TextView mTvUnReadMsg1;
//	protected TextView mTvUnReadMsg2;
	private ImageView mIvPic;

	private LinearLayout llNoLogin;
	private View mAreaLoginSuccess;
	private TextView tvLogin;
	private boolean isReload;


	private UserShowBean bean;

	public static MineFragment newInstance() {
		MineFragment fragment = new MineFragment();
		return fragment;
	}

	@Override
	protected int getLayoutId() {
		return R.layout.fragment_mine;
	}

	@Override
	protected void afterCreate(Bundle savedInstanceState) {
//		http://91lelegou.com/?/mobile/user/login
		tvRight = (TextView) findViewById(R.id.tv_right);

		llNoLogin = (LinearLayout) findViewById(R.id.ll_no_login);
		mAreaLoginSuccess = findViewById(R.id.ll_login_success);
		tvLogin = (TextView) findViewById(R.id.tv_login);

		mIvPic= (ImageView) findViewById(R.id.iv_tab_my_pic);
		mTvPhone= (TextView) findViewById(R.id.tv_tab_my_phone);
		mTvJingyan= (TextView) findViewById(R.id.tv_tab_my_jingyan);
		mTvLedou= (TextView) findViewById(R.id.tv_tab_my_ledou);
//        mTvUnReadMsg1= (TextView) findViewById(R.id.tv_unread_msg_number1);
//        mTvUnReadMsg2= (TextView) findViewById(R.id.tv_unread_msg_number2);

		mIvPic.setOnClickListener(this);
		/**我的乐乐购记录*/
		findViewById(R.id.tv_tab_item1).setOnClickListener(this);
		/**获得商品*/
		findViewById(R.id.tv_tab_item2).setOnClickListener(this);
		/**我的晒单*/
		findViewById(R.id.tv_tab_item3).setOnClickListener(this);
		/**账户明细*/
		findViewById(R.id.tv_tab_item4).setOnClickListener(this);
		/**常见问题*/
		findViewById(R.id.tv_tab_item5).setOnClickListener(this);
		/**联系客服*/
		findViewById(R.id.tv_tab_item6).setOnClickListener(this);
		/**去充值*/
		findViewById(R.id.btn_tab_my_gocharge).setOnClickListener(this);



		setData();
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
			setData();
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
	};

	private void setData() {
		tvRight.setOnClickListener(this);
		tvLogin.setOnClickListener(this);
		if (JiaZhengApp.getInstance().isLogin()) {
			tvRight.setText("退出");

			mAreaLoginSuccess.setVisibility(View.VISIBLE);
			llNoLogin.setVisibility(View.GONE);

            showTipInfo();

            getUserInfo();

//
//			WebSettings settings = webView.getSettings();
//			// 设置页面支持JavaScript
//			settings.setJavaScriptEnabled(true);
//
//			webView.setWebViewClient(new MyWebViewClient());
//			webView.setWebChromeClient(new WebChromeClient());
//
//			showProgressInfo("");
//			webView.loadUrl(Constant.MOBILE_IP + Constant.MOBILE_USER_LOGIN);
		} else {
			tvRight.setText("");
			mAreaLoginSuccess.setVisibility(View.GONE);
			llNoLogin.setVisibility(View.VISIBLE);

		}
	}

    private void showTipInfo() {
//        if (getActivity() instanceof MainActivity && getActivity()!=null){
//            MainActivity a= (MainActivity) getActivity();
//            if (a.mGettedGoodNum>0){
//                mTvUnReadMsg2.setVisibility(View.VISIBLE);
//				mTvUnReadMsg2.setText(String.valueOf(a.mGettedGoodNum));
//            }else{
//                mTvUnReadMsg2.setVisibility(View.GONE);
//            }
//            if (a.mHistoryNum>0){
//                mTvUnReadMsg1.setVisibility(View.VISIBLE);
//				mTvUnReadMsg1.setText(String.valueOf(a.mHistoryNum));
//            }else{
//                mTvUnReadMsg1.setVisibility(View.GONE);
//            }
//        }
    }

//
//	private class MyWebViewClient extends WebViewClient {
//
//		@Override
//		public void onPageFinished(WebView view, String url) {
//			dismissProgress();
//			super.onPageFinished(view, url);
//		}
//
//		@Override
//		public boolean shouldOverrideUrlLoading(WebView webView, String url) {
//			if (url.indexOf(Constant.MOBILE_IP + "/mobile/MyPersonHomePage/") != -1) {
//				webView.stopLoading();
//				PersonalDataActivity.start(webView.getContext());
//				return true;
//			}
//			// 我的圈友
//			if (url.indexOf("http://91lelegou.com/?/mobile/home/quanyou") != -1) {
//				webView.stopLoading();
//				MyCircleOfFriendsActivity.start(webView.getContext());
//				return true;
//			}
//			if (!(url.equals(Constant.MOBILE_IP + Constant.MOBILE_USER_LOGIN) || url
//					.equals(Constant.MOBILE_IP + Constant.MOBILE_HOME))) {
//				Utils.startWebViewShow(getActivity(), "", url);
//				return true;
//			}
//			return super.shouldOverrideUrlLoading(webView, url);
//		}
//	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_tab_my_pic:
			startActivity(new Intent(getActivity(), PersonalDataActivity.class));
			break;
		case R.id.iv_search:
			SearchProductActivity.start(getActivity());
			break;
		case R.id.tv_right:
			JiaZhengApp.getInstance().setLogin(false);
			JiaZhengApp.getInstance().setUserId("");
			removeCookie(getActivity().getApplication());
			setData();
		{
			if (getActivity() instanceof MainActivity && getActivity()!=null){
				MainActivity a= (MainActivity) getActivity();
				a.setTagLoginNotify("000");
			}
		}
			break;
		case R.id.tv_login:
			LoginActivity.start(getActivity());
			break;
		case R.id.btn_tab_my_gocharge:
			//// TODO: 2017/3/28
			/**
			 *
			 * 去充值
			 * */
            //==============================================原生
			Intent accountrecharge=new Intent(getActivity(), AccountRechargeActivity.class);
			accountrecharge.putExtra("leledou",bean.userMoney+"");
			startActivity(accountrecharge);

			//=======================================================================html5
//			Utils.startWebViewShow(getActivity(), "", Constant.MOBILE_IP + "/home/userrecharge");
			break;
		case R.id.tv_tab_item1:
        {
//            if (getActivity() instanceof MainActivity && getActivity()!=null){
//                MainActivity a= (MainActivity) getActivity();
//                a.makeRead(2);
//            }
        }
//            mTvUnReadMsg1.setVisibility(View.GONE);
            MainActivity.mHistoryNum=0;
            Utils.startWebViewShow(getActivity(), "", Constant.MOBILE_IP+"/home/userbuylist");
			break;
		case R.id.tv_tab_item2:
			/**获得商品*/
//        {
//            if (getActivity() instanceof MainActivity && getActivity()!=null){
//                MainActivity a= (MainActivity) getActivity();
//                a.makeRead(1);
//            }
//        }

			//// TODO: 2017/3/28
			//=========================================原生
			Intent getawardintent=new Intent(getActivity(), GetAwardShopInforActivity.class);
			JiaZhengApp.getInstance().setLeledouNum(bean.userMoney+"");
			startActivity(getawardintent);


        //===========================================================html5
//            MainActivity.mGettedGoodNum=0;
////            mTvUnReadMsg2.setVisibility(View.GONE);
//            Utils.startWebViewShow(getActivity(), "", Constant.MOBILE_IP+"/home/orderlist");
			break;
		case R.id.tv_tab_item3:
            Utils.startWebViewShow(getActivity(), "", Constant.MOBILE_IP+"/home/singlelist");
			break;
		case R.id.tv_tab_item4:
            Utils.startWebViewShow(getActivity(), "", Constant.MOBILE_IP+"/home/userbalance");
			break;
		case R.id.tv_tab_item5:
            Utils.startWebViewShow(getActivity(), "", "http://91lelegou.com/?/question/question/index");
			break;
		case R.id.tv_tab_item6:
			Utils.startWebViewShow(getActivity(), "", Constant.MOBILE_IP+"/home/server");
			break;
		}
	}

	private void removeCookie(Context context) {
		CookieSyncManager.createInstance(context);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();
		CookieSyncManager.getInstance().sync();
	}

	private void getUserInfo() {
		showProgressInfo("");
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("userId", JiaZhengApp.getInstance().getUserId());
		ApiClass.getUserInfo(paramsMap, callback);
	}


    private void showMyEmp(String emp) {
        StringBuffer sb=new StringBuffer("经验值");
        int index1=sb.length();
        sb.append(emp);
        int index2=sb.length();
        SpannableString ss = new SpannableString(sb.toString());
        ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.bg_common_red_dark)), index1, index2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvJingyan.setText(ss);
    }

    private void showLedou(String money) {
        StringBuffer sb=new StringBuffer("乐豆");
        int index1=sb.length();
        sb.append(money);
        sb.append("个");
        int index2=sb.length();
        SpannableString ss = new SpannableString(sb.toString());
        ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.bg_common_red_dark)), index1, index2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvLedou.setText(ss);
    }

	private ResultCallback<String> callback = new ResultCallback<String>() {

		@Override
		public void onSuccess(ResultBean<String> resultBean, int id) {
			dismissProgress();
			if (resultBean.getStatus() != 1) {
				Toast.makeText(getActivity(), resultBean.getInfo(),
						Toast.LENGTH_SHORT).show();
				return;
			}
			try {
				MyLog.i("xiaocai", "data=" + resultBean.getJsonMsg());
                bean = new Gson().fromJson(
						resultBean.getJsonMsg(),
						new TypeToken<UserShowBean>() {
						}.getType());
                if (bean!=null){
                    showMyEmp(bean.empiricalValue);
                    showLedou(bean.userMoney);
                    ImageLoader.getInstance().displayImage(bean.headImg, mIvPic, JiaZhengApp.getInstance().getDefaultImgeOptions());
                    if (StringUtil.isNotEmpty(bean.userMobile)&&bean.userMobile.length()>=10){
                        StringBuffer sb=new StringBuffer();
                        sb.append(bean.userMobile.substring(0, 3));
                        sb.append("****");
                        sb.append(bean.userMobile.substring(bean.userMobile.length()-4, bean.userMobile.length()));
                        mTvPhone.setText(sb.toString());
                    }else{
						mTvPhone.setText(bean.userName);
					}
                }
				if (getActivity() instanceof MainActivity && getActivity()!=null){
					MainActivity a= (MainActivity) getActivity();
					a.setTagLoginNotify(JiaZhengApp.getInstance().getUserId());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void onError(okhttp3.Call call, Exception e, int id) {
			super.onError(call, e, id);
			dismissProgress();
			ToastHelper.toast(e.getMessage());
		}
	};

}