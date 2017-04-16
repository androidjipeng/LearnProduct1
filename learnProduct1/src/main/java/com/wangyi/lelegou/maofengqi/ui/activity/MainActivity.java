package com.wangyi.lelegou.maofengqi.ui.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import com.learn.soft.product.jni.JiaZhengApp;
import com.learn.soft.product.main.activity.fragment.ShowTibInfoDlg;
import com.learn.soft.product.util.*;
import com.learn.soft.product1.R;
import com.wangyi.lelegou.maofengqi.base.BaseActivity;
import com.wangyi.lelegou.maofengqi.bean.ResultBean;
import com.wangyi.lelegou.maofengqi.ui.fragment.CartFragment;
import com.wangyi.lelegou.maofengqi.ui.fragment.CircleFragment;
import com.wangyi.lelegou.maofengqi.ui.fragment.HomeFragment;
import com.wangyi.lelegou.maofengqi.ui.fragment.MineFragment;
import com.wangyi.lelegou.maofengqi.utils.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MainActivity extends BaseActivity {

	private RadioGroup radioGroup;
	private Fragment fragment;
    private TextView mTvInfo;
	// 选中Fragment对应的位置
	private int position;
	private int[] rbIds = { R.id.rb_home, R.id.rb_circle, R.id.rb_cart,
			R.id.rb_mine };

//    public int mAllDataInfo;
    public static int mHistoryNum;
    public static int mGettedGoodNum;
    public static boolean sIsNowClickBtn=false;
    public static boolean sIsFromShareOpen=false;
    public static String sShareId = null;

	@Override
	protected int getLayoutId() {
		return R.layout.activity_main2;
	}

	@Override
	protected void afterCreate(Bundle savedInstanceState) {
        mTvInfo = (TextView) findViewById(R.id.tv_unread_show_number);
		radioGroup = (RadioGroup) findViewById(R.id.radio_group);

		// 设置RadioGroup的监听
		radioGroup.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
		// 设置默认选中
		// radioGroup.check(rbIds[position]); // 不能用这个,会调用两次
		((RadioButton) findViewById(rbIds[position])).setChecked(true);


		if (isShowTranStatus()){
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
				try {
					setTranslucentStatus(true);
					SystemBarTintManager tintManager = new SystemBarTintManager(this);
					tintManager.setStatusBarTintEnabled(true);
					tintManager.setStatusBarTintResource(R.color.bg_common_red_color);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}



		// 检查更新APP
		UpdateAppUtils.updateApp(this);

		PrefrenceUtil util=new PrefrenceUtil();
		if (util.getFirstFlag()){
			util.setFirstFlag(false);
			radioGroup.postDelayed(new Runnable() {
				@Override
				public void run() {
					ShowTibInfoDlg.showDlg(getSupportFragmentManager());
				}
			}, 50);

		}

		JPushInterface.init(getApplicationContext());




		if (JiaZhengApp.getInstance().isLogin()&&sIsNowClickBtn) {
            sIsNowClickBtn=false;
			showNotifyInfoFirst();
		}

	}

	public void setTagLoginNotify(String alias){
		mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));
	}

	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case MSG_SET_ALIAS:
					MyLog.i("xiaocai", "Set alias in handler.");
					JPushInterface.setAliasAndTags(getApplicationContext(), (String) msg.obj, null, mAliasCallback);
					break;

				default:
			}
		}
	};
	private static final int MSG_SET_ALIAS = 1001;

	protected boolean isShowTranStatus(){
		return true;
	}

	@TargetApi(19)
	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}

	boolean isFirstEnter=false;

	@Override
	protected void onResume() {
		super.onResume();
        String alias=null;
        if (JiaZhengApp.getInstance().isLogin()) {
            alias=JiaZhengApp.getInstance().getUserId();
        }else{
            alias="000";
        }

		setTagLoginNotify(alias);

		if(sIsFromShareOpen){
			if (JiaZhengApp.getInstance().isLogin()){
				showOpenShareInfo();
			}else{
				if (!isFirstEnter){
					isFirstEnter=!isFirstEnter;
					LoginActivity.start(MainActivity.this);
				}
			}
		}

	}

	private final class MyOnCheckedChangeListener implements
			RadioGroup.OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			switch (checkedId) {
			case R.id.rb_home:
				position = 0;
				fragment = new HomeFragment();
				// fragment = new TabIndexFragment();
				break;
			case R.id.rb_circle:
				position = 1;
				fragment = new CircleFragment();
				break;
			case R.id.rb_cart:
				position = 2;
				fragment = new CartFragment();
				break;
			case R.id.rb_mine:
				position = 3;
				fragment = new MineFragment();
                mTvInfo.setVisibility(View.GONE);
				break;
			}

			FragmentTransaction transaction = getSupportFragmentManager()
					.beginTransaction();
			// mTransaction.setCustomAnimations(R.anim.slide_in_from_left, 0);
			transaction.replace(R.id.fl_content, fragment, "frg" + position);
			transaction.commit();
		}
	}

	private void showNotifyInfoFirst(){
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				position = 3;
				((RadioButton) findViewById(rbIds[position])).setChecked(true);
				reloadInfoNext();
			}
		}, 100);

	}

	private void showOpenShareInfo(){
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				position = 1;
				((RadioButton) findViewById(rbIds[position])).setChecked(true);
				shareInfoNext();
			}
		}, 100);

	}

	private void shareInfoNext() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (fragment instanceof CircleFragment){
					CircleFragment frg= (CircleFragment) fragment;
					frg.showInputInfoDlg();
				}

			}
		}, 100);
	}

	private void reloadInfoNext() {
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				goLoadingInfo();
			}
		}, 100);
	}

	private void goLoadingInfo() {
		if (mGettedGoodNum>0){
            Utils.startWebViewShow(MainActivity.this, "", Constant.MOBILE_IP + "/home/orderlist");
        }else if (mHistoryNum>0){
            Utils.startWebViewShow(MainActivity.this, "", Constant.MOBILE_IP+"/home/userbuylist");
        }
	}

	// 改变选项
	public void changeOption(int position) {
		((RadioButton) findViewById(rbIds[position])).setChecked(true);
	}



	// 新建圈子
	public void joinNewCircle(String circleid, final View.OnClickListener ok, final View.OnClickListener cancel) {
		showProgressInfo(null);
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("user_id", JiaZhengApp.getInstance().getUserId());
		paramsMap.put("circle_code", circleid);
		ResultCallback<String> callback = new ResultCallback<String>() {

			@Override
			public void onSuccess(ResultBean<String> resultBean, int id) {
				dismissProgress();
				if (resultBean!=null&&resultBean.getStatus() == 1) {
					if (position==1){
						if (fragment instanceof  CircleFragment&&fragment!=null){
							CircleFragment frg= (CircleFragment) fragment;
							frg.setData();
						}
					}
					if (ok!=null){
						ok.onClick(null);
					}
				}else{
					String info="";
					if (resultBean!=null){
						info=resultBean.getInfo();
						MyLog.i("xiaocai", "info="+resultBean.getInfo());
					}else{
						info="加入圈子失败";
					}
					ToastHelper.toast(getApplication(), info);
					if (cancel!=null){
						cancel.onClick(null);
					}
				}
			}

			public void onError(okhttp3.Call call, Exception e, int id) {
				super.onError(call, e, id);
				dismissProgress();
				if (cancel!=null){
					cancel.onClick(null);
				}
			}
		};
		ApiClass.joinNewCircle(paramsMap, callback);
	}




	private final TagAliasCallback mAliasCallback = new TagAliasCallback() {

		@Override
		public void gotResult(int code, String alias, Set<String> tags) {
			switch (code) {
				case 0:
					break;

				case 6002:
					if (ExampleUtil.isConnected(getApplicationContext())) {
						mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
					} else {
					}
					break;

				default:
			}

		}
	};


}