package com.example.homework.logic;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.homework.R;
import com.example.homework.entity.Affair;
import com.example.homework.entity.Detail;
import com.example.homework.utils.GetResponseListener;
import com.example.homework.utils.HttpGetRequest;
import com.example.homework.utils.HttpGetRequestAsyncTask;
import com.example.homework.utils.RequestData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
public class CustomDialog {
//    public  static  DetailDao detailDao = MyApplication.getInstance().getDB().detailDao();
    public static   Affair affair = null;
//    public static AffairDao affairDao = MyApplication.getInstance().getDB().affairDao();
    public  static List<Detail> detailList = new ArrayList<>();
    public static void showDialog(Context context) {
        // 使用AlertDialog.Builder构建对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // 获取布局填充器
        LayoutInflater inflater = LayoutInflater.from(context);
        // 加载自定义布局
        View dialogView = inflater.inflate(R.layout.custom_dialog_layout, null);

        // 初始化布局中的控件
        RelativeLayout backgroundImageView  = dialogView.findViewById(R.id.backgroundImageView);
        TextView titleTextView = dialogView.findViewById(R.id.titleTextView);
        TextView timeTextView = dialogView.findViewById(R.id.time);
        TextView timesTextView = dialogView.findViewById(R.id.times);

        Button deleteButton = dialogView.findViewById(R.id.deleteButton);
        Button returnButton = dialogView.findViewById(R.id.returnButton);
        // 设置全局图片和数据
        switch (affair.imageId) {
            case 1:
                backgroundImageView.setBackgroundResource(R.drawable.bp1);
                break;
            case 2:
                backgroundImageView.setBackgroundResource(R.drawable.bp2);
                break;
            case 3:
                backgroundImageView.setBackgroundResource(R.drawable.bp3);
                break;
            case 4:
                backgroundImageView.setBackgroundResource(R.drawable.bp4);
                break;
            case 5:
                backgroundImageView.setBackgroundResource(R.drawable.bp5);
                break;
            case 6:
                backgroundImageView.setBackgroundResource(R.drawable.bp6);
                break;
            case 7:
                backgroundImageView.setBackgroundResource(R.drawable.bp7);
                break;
            case 8:
                backgroundImageView.setBackgroundResource(R.drawable.bp8);
                break;
            case 9:
                backgroundImageView.setBackgroundResource(R.drawable.bp9);
                break;
            case 10:
                backgroundImageView.setBackgroundResource(R.drawable.bp10);
                break;
        }
        Map<String, String> map = new HashMap<>();
        map.put("message", affair.message);
        RequestData data = new RequestData(map,"/detail/query");
        HttpGetRequest.sendOkHttpGetRequest(data,new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(context, "请求失败", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                Looper.prepare();
                try {
                    System.out.println(response);
                    JSONObject data = new JSONObject(response.body().string());
                    // 反序列化JSON数据到对象列表
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<Detail>>() {}.getType();
                    detailList = gson.fromJson(data.getString("data"), listType);
                    long time = 0;
                    int times = 0;
                    for (Detail detail : detailList) {
                        time+=detail.time;
                        times++;
                    }
                    titleTextView.setText(affair.message);
                    timesTextView.setText("累计次数:" + times);
                    timeTextView.setText("累计时间:" + time+"分钟");
                } catch (JSONException e) {
                    // 处理JSON解析异常
                    e.printStackTrace();
                } catch (IOException e) {
                    // 处理IO异常
                    e.printStackTrace();
                }
                Looper.loop();
            }
        });
        // 设置对话框的视图
        builder.setView(dialogView);
        // 创建并显示对话框
        AlertDialog dialog = builder.create();
        dialog.show();
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> map = new HashMap<>();
                map.put("message", String.valueOf(affair.message));
                RequestData data = new RequestData(map,"/affair/delete");
                HttpGetRequestAsyncTask task = new HttpGetRequestAsyncTask(new GetResponseListener() {
                    @Override
                    public void onGetResponse(Response response)
                    {
                        try {
//                            System.out.println(response);
                            JSONObject data = new JSONObject(response.body().string());
                            Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            // 处理JSON解析异常
                            e.printStackTrace();
                        } catch (IOException e) {
                            // 处理IO异常
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onGetResponseError(Exception e)
                    {
                    }
                });
                task.execute(data);
                Log.i("215144", "删除 ");
                dialog.dismiss();
            }
        });
    }
    public void refresh(List<Detail> detailList)
    {
    }
}
