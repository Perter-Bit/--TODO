package com.example.homework.utils;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.example.homework.logic.MyApplication;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
public class HttpGetRequestAsyncTask extends AsyncTask<RequestData, Void, Response>
{
    private static final String TAG = "HttpGetRequestAsyncTask";
    private GetResponseListener listener; // 定义回调接口
    private Response response;
    public HttpGetRequestAsyncTask(GetResponseListener listener) {
        this.listener = listener;
    }
    @Override
    protected Response doInBackground(RequestData... data)
    {
        String url = MyApplication.url + data[0].getExtraParam() + "?";

        Iterator<Map.Entry<String, String>> it = data[0].getParams().entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            url += entry.getKey() + "=" + entry.getValue();
            if (it.hasNext()) url += "&";
        }
        OkHttpClient client = MyApplication.client;
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + MyApplication.token)
                .get()
                .build();
        try {
            Response response = client.newCall(request).execute();
            System.out.println(response);
            return response;
        } catch (IOException e) {
            Log.e(TAG, "Error in sending POST request", e);
            throw new RuntimeException(e); // 抛出运行时异常，以便在异步任务之外处理
        }
    }
    @Override
    protected void onPostExecute(Response response) {
        super.onPostExecute(response);
        if (listener != null) {
            if (response != null && response.isSuccessful()) {
                listener.onGetResponse(response); // 成功回调
            } else {
                listener.onGetResponseError(new Exception("Error in response")); // 失败回调
            }
        }
    }

}