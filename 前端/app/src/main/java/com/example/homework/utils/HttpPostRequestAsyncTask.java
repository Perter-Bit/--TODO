package com.example.homework.utils;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.homework.logic.MyApplication;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;
public class HttpPostRequestAsyncTask extends AsyncTask<RequestData, Void, Response>
{
    private static final String TAG = MyApplication.TAG;
    private OnResponseReceived listener; // 定义回调接口
    public HttpPostRequestAsyncTask(OnResponseReceived listener) {
        this.listener = listener;
}
    @Override
    protected Response doInBackground(RequestData... requestData) {
        Map<String, String> map = requestData[0].getParams();
        String extraParam = requestData[0].getExtraParam();
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

        // 异步执行请求
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "Error in sending POST request", e);
                if (listener != null) {
                    listener.onPostResponseError(e);
                }
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (listener != null) {
                    listener.onPostResponse(response);
                }
            }

        });
        return null;
    }
    @Override
    protected void onPostExecute(Response response) {
        super.onPostExecute(response);
        if (listener != null) {
            if (response != null) {
                listener.onPostResponse(response); // 成功回调
            } else {
                listener.onPostResponseError(new Exception("Error in response")); // 失败回调
            }
        }
    }
}
