package com.example.homework.utils;
import com.example.homework.logic.MyApplication;
import com.example.homework.utils.RequestData;

import java.util.Iterator;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Get请求
 */
public class HttpGetRequest {
    public static void sendOkHttpGetRequest(RequestData data,okhttp3.Callback callback)
    {
        String url = MyApplication.url + data.getExtraParam() + "?";

        Iterator<Map.Entry<String, String>> it = data.getParams().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            url += entry.getKey() + "=" + entry.getValue();
            if (it.hasNext()) url += "&";
        }
        System.out.println(url);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + MyApplication.token)
                .get()
                .build();
        MyApplication.client.newCall(request).enqueue(callback);
    }
}

