package com.wangyi.lelegou.maofengqi.utils;

import android.widget.Toast;
import com.google.gson.Gson;
import com.learn.soft.product.jni.JiaZhengApp;
import com.learn.soft.product.util.MyLog;
import com.wangyi.lelegou.maofengqi.bean.ResultBean;
import com.zhy.http.okhttp.callback.StringCallback;
import okhttp3.Call;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * **********************************************************
 * <p/>
 * 说明:接口数据回调
 * <p/>
 * 作者:@Doc.March
 * <p/>
 * 创建日期:2016-9-29
 * <p/>
 * 描述:
 * <p/>
 * **********************************************************
 */
public abstract class ResultCallback<T> extends StringCallback {

	@Override
	public void onError(Call call, Exception e, int id) {
		Toast.makeText(JiaZhengApp.getInstance(), e.getMessage(),
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public final void onResponse(String response, int id) {
		MyLog.i("xiaocai", "response:" + response);
		Utils.save(JiaZhengApp.getInstance(),response);
		try {
			JSONObject jsonObject = new JSONObject(response);

			ResultBean<T> resultBean = new ResultBean<T>();
			resultBean.setStatus(jsonObject.getInt("status"));
			resultBean.setInfo(jsonObject.getString("info"));

			String jsonMsg = jsonObject.optString("jsonMsg");
			if (jsonMsg != null && !jsonMsg.equals("")) {

				Type type = this.getClass().getGenericSuperclass();
				if (type instanceof ParameterizedType) {
					// 如果用户写了泛型，就会进入这里，否者不会执行
					ParameterizedType parameterizedType = (ParameterizedType) type;
					Type beanType = parameterizedType.getActualTypeArguments()[0];
					if (beanType == String.class) {
						// 如果是String类型，直接返回字符串
						@SuppressWarnings("unchecked")
						T t = (T) jsonMsg;
						resultBean.setJsonMsg(t);
					} else {
						// 如果是 Bean List Map,则解析完后返回
						T t = new Gson().fromJson(jsonMsg, beanType);
						resultBean.setJsonMsg(t);
					}
				} else {
					// 如果没有写泛型，直接返回Response对象
				}
			}

			onSuccess(resultBean, id);
		} catch (JSONException e) {
			e.printStackTrace();
			Toast.makeText(JiaZhengApp.getInstance(), "ResultCallback:数据解析错误",
					Toast.LENGTH_SHORT).show();
		}
	}

	public abstract void onSuccess(ResultBean<T> resultBean, int id);
}