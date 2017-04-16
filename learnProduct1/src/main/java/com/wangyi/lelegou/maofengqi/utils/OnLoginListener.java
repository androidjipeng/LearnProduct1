package com.wangyi.lelegou.maofengqi.utils;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;

public interface OnLoginListener {
	
	public void onLogin(Platform platform, HashMap<String, Object> res);

}