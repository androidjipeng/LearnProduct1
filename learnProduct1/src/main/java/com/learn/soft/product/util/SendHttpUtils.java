package com.learn.soft.product.util;

import android.os.Handler;
import android.os.Looper;
import okhttp3.*;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * **********************************************************
 * <p/>
 * 说明：
 * <p/>
 * 作者：cailin
 * <p/>
 * 创建日期：2016/5/1
 * <p/>
 * 描述：
 * **********************************************************
 */
public class SendHttpUtils {
    private static SendHttpUtils mInstance;

    public static SendHttpUtils getInstance()
    {
        if (mInstance == null)
        {
            synchronized (SendHttpUtils.class)
            {
                if (mInstance == null)
                {
                    mInstance = new SendHttpUtils();
                }
            }
        }
        return mInstance;
    }


    private OkHttpClient client;

    public OkHttpClient getClient() {
        return client;
    }

    // 超时时间
    public static final int TIMEOUT = 1000 * 60;

    //json请求
    public static final MediaType sJsonType = MediaType.parse("application/json; charset=utf-8");

    private Handler handler = new Handler(Looper.getMainLooper());

    public SendHttpUtils() {
        this.init();
    }


    private void init() {
        client = new OkHttpClient();
        // 设置超时时间
        client.newBuilder().connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS).build();
    }
    public void postJson(String tag, String url, Map<String ,Object> param, final HttpCallback callback) {
        JSONObject jsonParam=new JSONObject(param);
        postJson(tag, url, jsonParam.toString(), callback);
    }

    public void postJson(String tag, String url, String json, final HttpCallback callback) {
        MyLog.i("xiaocai", "request="+json);
        RequestBody body = RequestBody.create(sJsonType, json);
        Request request = new Request.Builder().tag(tag).url(url).post(body).build();
        onStart(callback);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call arg0, Response response) throws IOException {
                if (response.isSuccessful()) {
                    onSuccess(callback, response.body().string());
                } else {
                    onError(callback, response.message());
                }
            }

            @Override
            public void onFailure(Call arg0, IOException arg1) {
                arg1.printStackTrace();
                onError(callback, arg1.getMessage());

            }
        });

    }

    /**
     * post请求 map为body
     *
     * @param url
     * @param map
     * @param callback
     */
    public void postCommon(String tag, String url, Map<String, Object> map,final HttpCallback callback) {
        /**
         * 创建请求的参数body
         */
        FormBody.Builder builder = new FormBody.Builder();

        /**
         * 遍历key
         */
        if (null != map) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                MyLog.i("xiaocai", "Key = " + entry.getKey() + ", Value = " + entry.getValue());
                builder.add(entry.getKey(), entry.getValue().toString());
            }
        }

        RequestBody body = builder.build();

        Request request = new Request.Builder().tag(tag).url(url).post(body).build();

//        .header("User-Agent", "OkHttp Headers.java")
//                .addHeader("Accept", "application/json; q=0.5")
//                .addHeader("Accept", "application/vnd.github.v3+json")

        onStart(callback);

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                onError(callback, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    onSuccess(callback, response.body().string());
                } else {
                    onError(callback, response.message());
                }

            }

        });

    }

    /**
     * get请求
     * @param url
     * @param callback
     */
    public void get(String url, final HttpCallback callback) {

        Request request = new Request.Builder().url(url).build();

        onStart(callback);

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException arg1) {
                onError(callback, arg1.getMessage());
                arg1.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    onSuccess(callback, response.body().string());
                } else {
                    onError(callback, response.message());
                }
            }

        });

    }

    private void onStart(HttpCallback callback) {
        if (null != callback) {
            callback.onStart();
        }
    }

    private void onSuccess(final HttpCallback callback, final String data) {
        if (null != callback) {
            handler.post(new Runnable() {
                public void run() {
                    // 需要在主线程的操作。
                    callback.onSuccess(data);
                }
            });
        }
    }

    private void onError(final HttpCallback callback,final String msg) {
        if (null != callback) {
            handler.post(new Runnable() {
                public void run() {
                    // 需要在主线程的操作。
                    callback.onError(msg);
                }
            });
        }
    }


    /**
     * http请求回调
     *
     * @author
     *
     */
    public static abstract class HttpCallback {
        // 开始
        public void onStart() {};

        // 成功回调
        public abstract void onSuccess(String data);

        // 失败回调
        public void onError(String msg) {};
    }


    public void cancelTagRequest(Request request){
        if (request!=null){
            client.newCall(request).cancel();
        }
    }


    public void cancel(Object tag) {
        Dispatcher dispatcher = client.dispatcher();
        for (Call call : dispatcher.queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : dispatcher.runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    public void cancelAll() {
        Dispatcher dispatcher = client.dispatcher();
        dispatcher.cancelAll();
    }
}
