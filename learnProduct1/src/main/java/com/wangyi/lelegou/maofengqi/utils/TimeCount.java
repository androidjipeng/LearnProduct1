package com.wangyi.lelegou.maofengqi.utils;

import android.os.CountDownTimer;
import android.widget.TextView;

import com.learn.soft.product1.R;

/**
 * **********************************************************
 * <p/>
 * 说明:计时器
 * <p/>
 * 作者:@Doc.March
 * <p/>
 * 创建日期:2016-9-29
 * <p/>
 * 描述:
 * <p/>
 * **********************************************************
 */
public final class TimeCount extends CountDownTimer {

	private TextView tvSendCode;

	// 参数依次为总时长,和计时的时间间隔
	public TimeCount(long millisInFuture, long countDownInterval, TextView tvSendCode) {
		super(millisInFuture, countDownInterval);
		this.tvSendCode = tvSendCode;
	}

	// 计时完毕时触发
	@Override
	public void onFinish() {
		tvSendCode.setClickable(true);
		tvSendCode.setBackgroundResource(R.drawable.bg_send_code_1);
		tvSendCode.setText("重新验证");
	}

	// 计时过程显示
	@Override
	public void onTick(long millisUntilFinished) {
		tvSendCode.setClickable(false);
		tvSendCode.setBackgroundResource(R.drawable.bg_send_code_2);
		tvSendCode.setText(millisUntilFinished / 1000 + "秒后重新发送");
	}
}