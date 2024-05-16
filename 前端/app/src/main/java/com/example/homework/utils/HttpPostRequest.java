package com.example.homework.utils;

import android.util.Log;

import com.example.homework.logic.MyApplication;

import java.io.IOException;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Post请求
 */
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Post请求
 */
public class HttpPostRequest {
    public static Response response = null;
    public static Request request = null;
    public static void okhttpPost(RequestData requestData ,okhttp3.Callback callback) {
        Map<String, String> map = requestData.getParams();
        String extraParam = requestData.getExtraParam();
        FormBody.Builder formBuilder = new FormBody.Builder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            formBuilder.add(entry.getKey(), entry.getValue());
        }
        RequestBody formBody = formBuilder.build();
        OkHttpClient client = MyApplication.client;
        Request.Builder requestBuilder = new Request.Builder().url(MyApplication.url + extraParam)
                .post(formBody);
        if (!MyApplication.token.isEmpty()) {
            requestBuilder.addHeader("Authorization", "Bearer " + MyApplication.token);
        }
        Request request = requestBuilder.build();
        client.newCall(request).enqueue(callback);
    }
}
