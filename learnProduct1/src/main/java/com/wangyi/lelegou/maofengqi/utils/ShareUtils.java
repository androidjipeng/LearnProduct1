package com.wangyi.lelegou.maofengqi.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;
import com.learn.soft.product1.R;
import com.wangyi.lelegou.maofengqi.bean.QQBean;
import com.wangyi.lelegou.maofengqi.bean.ShareBean;
import com.wangyi.lelegou.maofengqi.bean.WeiXinBean;

import java.util.HashMap;

public class ShareUtils implements Callback {

	private static final int MSG_AUTH_CANCEL = 1;
	private static final int MSG_AUTH_ERROR = 2;
	private static final int MSG_AUTH_COMPLETE = 3;

	private static final int MSG_SHARE_CANCEL = 4;
	private static final int MSG_SHARE_ERROR = 5;
	private static final int MSG_SHARE_COMPLETE = 6;

	private Handler handler;
	private String platformName;
	private OnLoginListener onLoginListener;
	private Context context;

	public ShareUtils() {
		handler = new Handler(Looper.getMainLooper(), this);
	}

	/**
	 * 设置平台名称
	 * 
	 * @param platformName
	 */
	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}

	/**
	 * 设置登录回调
	 * 
	 * @param listener
	 */
	public void setOnLoginListener(OnLoginListener onLoginListener) {
		this.onLoginListener = onLoginListener;
	}

	public void login(Context context) {
		this.context = context.getApplicationContext();
		Platform platform = ShareSDK.getPlatform(platformName);
		if (platform == null) {
			return;
		}

		if (platform.isAuthValid()) {
			platform.removeAccount(true);
		}

		// 使用SSO授权,通过客户单授权
		// 使用了SSO授权后,有客户端的都会优先启用客户端授权,没客户端的则任然使用网页版进行授权。
		platform.SSOSetting(false);
		platform.setPlatformActionListener(new PlatformActionListener() {

			public void onComplete(Platform plat, int action,
					HashMap<String, Object> res) {
				if (action == Platform.ACTION_USER_INFOR) {
					Message msg = new Message();
					msg.what = MSG_AUTH_COMPLETE;
					msg.arg2 = action;
					msg.obj = new Object[] { plat.getName(), res };
					handler.sendMessage(msg);
				}
			}

			public void onError(Platform plat, int action, Throwable t) {
				if (action == Platform.ACTION_USER_INFOR) {
					Message msg = new Message();
					msg.what = MSG_AUTH_ERROR;
					msg.arg2 = action;
					msg.obj = t;
					handler.sendMessage(msg);
				}
				t.printStackTrace();
			}

			public void onCancel(Platform plat, int action) {
				if (action == Platform.ACTION_USER_INFOR) {
					Message msg = new Message();
					msg.what = MSG_AUTH_CANCEL;
					msg.arg2 = action;
					msg.obj = plat;
					handler.sendMessage(msg);
				}
			}
		});
		// 授权并获取用户信息
		platform.showUser(null);
	}

	/**
	 * 处理操作结果
	 */
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case MSG_AUTH_CANCEL:
			// 取消
			Toast.makeText(context, "canceled", Toast.LENGTH_SHORT).show();
			break;
		case MSG_AUTH_ERROR:
			// 失败
			Throwable t = (Throwable) msg.obj;
			String text = "caught error: " + t.getMessage();
			Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
			t.printStackTrace();
			break;
		case MSG_AUTH_COMPLETE:
			// 成功
			Object[] objs = (Object[]) msg.obj;
			String name = (String) objs[0];
			@SuppressWarnings("unchecked")
			HashMap<String, Object> res = (HashMap<String, Object>) objs[1];

			Platform platform = ShareSDK.getPlatform(name);

			if (onLoginListener != null) {
				onLoginListener.onLogin(platform, res);
			}
			break;
		}
		return false;
	}

	private static Dialog dialog;
	private static Activity mActivity;
	private static ShareBean mShareBean;

	// 邀请圈友
	public static void show(Activity activity, ShareBean shareBean) {
		mActivity = activity;
		mShareBean = shareBean;
		// 加载布局文件
		View view = LayoutInflater.from(activity).inflate(
				R.layout.layout_share, null);

		TextView tvWeixin = (TextView) view.findViewById(R.id.tv_weixin);
		TextView tvQq = (TextView) view.findViewById(R.id.tv_qq);
		TextView tvContacts = (TextView) view.findViewById(R.id.tv_contacts);
		TextView tvCancel = (TextView) view.findViewById(R.id.tv_cancel);

		tvWeixin.setOnClickListener(new MyClickListener());
		tvQq.setOnClickListener(new MyClickListener());
		tvContacts.setOnClickListener(new MyClickListener());
		tvCancel.setOnClickListener(new MyClickListener());

		dialog = new Dialog(activity, R.style.custom_sheet_dialog);
		dialog.setContentView(view);
		dialog.setCancelable(false);

		Window dialogWindow = dialog.getWindow();
		dialogWindow.setGravity(Gravity.BOTTOM | Gravity.LEFT | Gravity.RIGHT);

		dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (event.getAction() == KeyEvent.ACTION_DOWN
						&& KeyEvent.KEYCODE_BACK == keyCode) {
					dialog.dismiss();
					return true;
				}
				return false;
			}
		});

		dialog.show();
	}

	private static final class MyClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.tv_weixin:
				dialog.dismiss();
				shareToWinXinx();
				break;
			case R.id.tv_qq:
				dialog.dismiss();
				shareToQQ();
				break;
			case R.id.tv_contacts:
				dialog.dismiss();
//				ContactsActivity.start(mActivity, mShareBean.getMessageBean()
//						.getBody());
				shareToWinXinFriend();
				break;
			case R.id.tv_cancel:
				dialog.dismiss();
				break;
			}
		}
	}

	private static void shareToWinXinx() {
		Toast.makeText(mActivity, "正在启动微信...", Toast.LENGTH_SHORT).show();
		ShareSDK.initSDK(mActivity);
		Platform weiChat = ShareSDK.getPlatform(Wechat.NAME);

		WeiXinBean bean = mShareBean.getWeiXinBean();
		ShareParams sp = new ShareParams();
		sp.setTitle(bean.getTitle());
		sp.setText(bean.getText());
		sp.setUrl(bean.getUrl());
		sp.setImageData(BitmapFactory.decodeResource(mActivity.getResources(),
				R.drawable.icon_yungou));
		sp.setImageUrl(bean.getImageUrl());
		sp.setShareType(Platform.SHARE_WEBPAGE);

		// 设置分享事件回调
		weiChat.setPlatformActionListener(new PlatformActionListener() {

			@Override
			public void onError(Platform plat, int action, Throwable t) {
				Message msg = new Message();
				msg.what = MSG_SHARE_ERROR;
				msg.arg2 = action;
				msg.obj = t;
				mHandler.sendMessage(msg);
			}

			@Override
			public void onComplete(Platform plat, int action,
					HashMap<String, Object> res) {
				Message msg = new Message();
				msg.what = MSG_SHARE_COMPLETE;
				msg.arg2 = action;
				msg.obj = new Object[] { plat.getName(), res };
				mHandler.sendMessage(msg);
			}

			@Override
			public void onCancel(Platform plat, int action) {
				Message msg = new Message();
				msg.what = MSG_SHARE_CANCEL;
				msg.arg2 = action;
				msg.obj = plat;
				mHandler.sendMessage(msg);
			}
		});

		// 执行图文分享
		weiChat.share(sp);
	}

	private static void shareToWinXinFriend() {
//		Toast.makeText(mActivity, "正在启动微信...", Toast.LENGTH_SHORT).show();
		ShareSDK.initSDK(mActivity);
		Platform weiChat = ShareSDK.getPlatform(WechatMoments.NAME);

		WeiXinBean bean = mShareBean.getWeiXinBean();
		ShareParams sp = new ShareParams();
		sp.setTitle(bean.getTitle());
		sp.setText(bean.getText());
		sp.setUrl(bean.getUrl());
		sp.setImageData(BitmapFactory.decodeResource(mActivity.getResources(),
				R.drawable.icon_yungou));
		sp.setImageUrl(bean.getImageUrl());
		sp.setShareType(Platform.SHARE_WEBPAGE);

		// 设置分享事件回调
		weiChat.setPlatformActionListener(new PlatformActionListener() {

			@Override
			public void onError(Platform plat, int action, Throwable t) {
				Message msg = new Message();
				msg.what = MSG_SHARE_ERROR;
				msg.arg2 = action;
				msg.obj = t;
				mHandler.sendMessage(msg);
			}

			@Override
			public void onComplete(Platform plat, int action,
					HashMap<String, Object> res) {
				Message msg = new Message();
				msg.what = MSG_SHARE_COMPLETE;
				msg.arg2 = action;
				msg.obj = new Object[] { plat.getName(), res };
				mHandler.sendMessage(msg);
			}

			@Override
			public void onCancel(Platform plat, int action) {
				Message msg = new Message();
				msg.what = MSG_SHARE_CANCEL;
				msg.arg2 = action;
				msg.obj = plat;
				mHandler.sendMessage(msg);
			}
		});

		// 执行图文分享
		weiChat.share(sp);
	}

	private static void shareToQQ() {
		Toast.makeText(mActivity, "正在启动QQ...", Toast.LENGTH_SHORT).show();
		ShareSDK.initSDK(mActivity);
		Platform weiChat = ShareSDK.getPlatform(QQ.NAME);

		QQBean bean = mShareBean.getQqBean();
		ShareParams sp = new ShareParams();
		sp.setTitle(bean.getTitle());
		sp.setTitleUrl(bean.getUrl());
		sp.setText(bean.getText());
		sp.setImageData(BitmapFactory.decodeResource(mActivity.getResources(),
				R.drawable.icon_yungou));
		sp.setImageUrl(bean.getImageUrl());
		sp.setShareType(Platform.SHARE_WEBPAGE);

		// 设置分享事件回调
		weiChat.setPlatformActionListener(new PlatformActionListener() {

			@Override
			public void onError(Platform plat, int action, Throwable t) {
				Message msg = new Message();
				msg.what = MSG_SHARE_ERROR;
				msg.arg2 = action;
				msg.obj = t;
				mHandler.sendMessage(msg);
			}

			@Override
			public void onComplete(Platform plat, int action,
					HashMap<String, Object> res) {
				Message msg = new Message();
				msg.what = MSG_SHARE_COMPLETE;
				msg.arg2 = action;
				msg.obj = new Object[] { plat.getName(), res };
				mHandler.sendMessage(msg);
			}

			@Override
			public void onCancel(Platform plat, int action) {
				Message msg = new Message();
				msg.what = MSG_SHARE_CANCEL;
				msg.arg2 = action;
				msg.obj = plat;
				mHandler.sendMessage(msg);
			}
		});

		// 执行图文分享
		weiChat.share(sp);
	}

	private static Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_SHARE_CANCEL:
				// 用户取消,分享失败
				Toast.makeText(mActivity, "用户取消,分享失败", Toast.LENGTH_SHORT)
						.show();
				break;
			case MSG_SHARE_ERROR:
				// 分享失败
				Toast.makeText(mActivity, "分享失败", Toast.LENGTH_SHORT).show();
				break;
			case MSG_SHARE_COMPLETE:
				// 分享成功
				Toast.makeText(mActivity, "分享成功", Toast.LENGTH_SHORT).show();
				break;
			}
		};
	};
}