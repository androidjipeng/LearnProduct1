package com.wangyi.lelegou.maofengqi.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map;

import com.learn.soft.product.util.MyLog;
import okhttp3.Response;
import android.util.Log;

import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.GetBuilder;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.Callback;

/**
 * **********************************************************
 * <p/>
 * 说明:OkHttpUtils封装
 * <p/>
 * 作者:@Doc.March
 * <p/>
 * 创建日期:2016-9-29
 * <p/>
 * 描述:
 * <p/>
 * **********************************************************
 */
public class OkHttpUtilsManager {

	// 获取OkHttpUtilsManager实例
	private static OkHttpUtilsManager mInstance;

	// 私有构造方法,初始化
	private OkHttpUtilsManager() {

	}

	// 获取OkHttpUtilsManager对象
	public static OkHttpUtilsManager getInstance() {
		if (mInstance == null) {
			synchronized (OkHttpUtilsManager.class) {
				if (mInstance == null) {
					mInstance = new OkHttpUtilsManager();
				}
			}
		}
		return mInstance;
	}

	// 同步的Get请求
	public Response getSyn(String url, Map<String, String> headerMap,
			Map<String, Object> paramsMap) throws IOException {
		GetBuilder builder = OkHttpUtils.get().url(url);
		getBuilder(builder, headerMap, paramsMap);
		return builder.build().execute();
	}

	// 异步的Get请求
	public <T> void getAsyn(String url, Map<String, String> headerMap,
			Map<String, Object> paramsMap, Callback<T> callback) {
		GetBuilder builder = OkHttpUtils.get().url(url);
		getBuilder(builder, headerMap, paramsMap);
		builder.build().execute(callback);
	}

	// 构造参数
	private GetBuilder getBuilder(GetBuilder builder,
			Map<String, String> headerMap, Map<String, Object> paramsMap) {
		if (headerMap != null && headerMap.size() > 0) {
			Iterator<String> iterator = headerMap.keySet().iterator();
			while (iterator.hasNext()) {
				String key = iterator.next();
				String value = String.valueOf(headerMap.get(key));
				builder.addHeader(key, value);
			}
		}

		if (paramsMap != null && paramsMap.size() > 0) {
			Iterator<String> iterator = paramsMap.keySet().iterator();
			while (iterator.hasNext()) {
				String key = iterator.next();
				String value = String.valueOf(paramsMap.get(key));
				builder.addParams(key, value);
			}
		}
		return builder;
	}

	// 同步POST请求,带参数,文件上传
	public Response postSyn(String url, Map<String, String> headerMap,
			Map<String, Object> paramsMap) throws IOException {
		PostFormBuilder builder = OkHttpUtils.post().url(url);
		getPostFormBuilder(builder, headerMap, paramsMap, null, null);
		return builder.build().execute();
	}

	// 异步POST请求,带参数,文件上传
	public <T> void postAsyn(String url, Map<String, String> headerMap,
			Map<String, Object> paramsMap, File[] files, String[] fileKeys,
			Callback<T> callback) {
		MyLog.i("xiaocai", url);
		PostFormBuilder builder = OkHttpUtils.post().url(url);
		getPostFormBuilder(builder, headerMap, paramsMap, files, fileKeys);
		builder.build().execute(callback);
	}

//	public <T> void postAsyn(String url,
//			Map<String, String> paramsMap ,
//			Callback<T> callback) {
//		MyLog.i("xiaocai", url);
//		OkHttpUtils.post().url(url).params(paramsMap).build().execute(callback);
//	}

	// 构造参数
	private PostFormBuilder getPostFormBuilder(PostFormBuilder builder,
			Map<String, String> headerMap, Map<String, Object> paramsMap,
			File[] files, String[] fileKeys) {
		if (headerMap != null && headerMap.size() > 0) {
			Iterator<String> iterator = headerMap.keySet().iterator();
			while (iterator.hasNext()) {
				String key = iterator.next();
				String value = String.valueOf(headerMap.get(key));
				builder.addHeader(key, value);
			}
		}

		if (paramsMap != null && paramsMap.size() > 0) {
			Iterator<String> iterator = paramsMap.keySet().iterator();
			while (iterator.hasNext()) {
				String key = iterator.next();
				String value = String.valueOf(paramsMap.get(key));
				builder.addParams(key, value);
				MyLog.i("xiaocai", key + "--" + value);
			}
		}

		if (files != null && files.length > 0) {
			for (int i = 0; i < files.length; i++) {
				File file = files[i];
				String fileName = file.getName();
				builder.addFile(fileKeys[i], fileName, file);
			}
		}

		return builder;
	}

	public abstract class AbsCallback<T> {

		// Post执行上传过程中的进度回调,get请求不回调,UI线程
		public void upProgress(long currentSize, long totalSize, float progress) {

		}

		// 执行下载过程中的进度回调,UI线程
		public void downloadProgress(long currentSize, long totalSize,
				float progress) {

		}

		public abstract T parseNetworkResponse(Response response)
				throws Exception;
	}

	// 支持传递一个泛型，将返回的Response对象解析成需要的类型并且返回
	public abstract class BeanCallBack<T> extends AbsCallback<T> {

		@SuppressWarnings("unchecked")
		@Override
		public T parseNetworkResponse(Response response) throws Exception {
			Type type = this.getClass().getGenericSuperclass();
			if (type instanceof ParameterizedType) {
				// 如果用户写了泛型，就会进入这里，否者不会执行
				ParameterizedType parameterizedType = (ParameterizedType) type;
				Type beanType = parameterizedType.getActualTypeArguments()[0];
				if (beanType == String.class) {
					// 如果是String类型，直接返回字符串
					return (T) response.body().string();
				} else {
					// 如果是 Bean List Map ，则解析完后返回
					return new Gson().fromJson(response.body().string(),
							beanType);
				}
			} else {
				// 如果没有写泛型，直接返回Response对象
				return (T) response;
			}
		}
	}
}