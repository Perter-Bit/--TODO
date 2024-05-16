package com.example.homework.logic;
import android.app.Application;
import android.content.Context;
import android.util.Log;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
//import com.facebook.drawee.backends.pipeline.Fresco;
public class MyApplication extends Application
{
    public  static String url ="http://192.168.43.10:5000";
    public static OkHttpClient client = null;
    public static String TAG="wyj";
    public static int userId;
    private static MyApplication mApp;//声明一个当前应用的静态实例
    public HashMap<String, String> infoMap = new HashMap<String, String>();
    public static String token="";
    // 利用单例模式获取当前应用的唯一实例
    public static MyApplication getInstance() {
        return mApp;
    }
    public static Context getContext()
    {
        return mApp.getApplicationContext();
    }
    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate");
//        Fresco.initialize(this);
        mApp = this; // 在打开应用时对静态的应用实例赋值
        client = new OkHttpClient().newBuilder()
                .connectTimeout(180, TimeUnit.SECONDS)
                .readTimeout(180, TimeUnit.SECONDS)
                .writeTimeout(180, TimeUnit.SECONDS)
                .build();
    }
    private static ExecutorService executorService = Executors.newFixedThreadPool(5);
    // 获取线程池的实例
    public static ExecutorService getExecutorService() {
        return executorService;
    }
}
