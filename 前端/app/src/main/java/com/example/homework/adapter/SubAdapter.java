package com.example.homework.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homework.R;
import com.example.homework.entity.Subscript;
import com.example.homework.logic.LoginActivity;
import com.example.homework.logic.MainActivity;
import com.example.homework.logic.MyApplication;
import com.example.homework.utils.GetResponseListener;
import com.example.homework.utils.HttpGetRequestAsyncTask;
import com.example.homework.utils.RequestData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;

import okhttp3.Response;

public class SubAdapter extends RecyclerView.Adapter<SubAdapter.ViewHolder>
{
    List<Subscript> subscriptList;
    private static Timer timer = new Timer();
    private Context context;
    private SubAdapter.OnItemClickListener itemClickListener;
    private SubAdapter.OnEditButtonClickListener editButtonClickListener;
    @NonNull
    @Override
    public SubAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_layout_subscript, parent, false);
        return new SubAdapter.ViewHolder(view, itemClickListener, editButtonClickListener);
    }
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    // Edit button click event listener interface
    public interface OnEditButtonClickListener {
        void onEditButtonClick(int position);
    }
    public SubAdapter(Context context, List<Subscript> subscriptList) {
        this.context = context;
        this.subscriptList = subscriptList;
    }
    public void setOnItemClickListener(SubAdapter.OnItemClickListener listener) {
        this.itemClickListener = listener;
    }
    // Setter for edit button click listener
    public void setOnEditButtonClickListener(SubAdapter.OnEditButtonClickListener listener) {
        this.editButtonClickListener = listener;
    }
    @Override
    public int getItemCount() {
        return subscriptList.size();
    }
    boolean isContentLayoutVisible = false;
    boolean shouldStopTimer = false;
    @Override
    public void onBindViewHolder(@NonNull SubAdapter.ViewHolder holder, int position)
    {
        System.out.println(position);
        Subscript sub = subscriptList.get(position);
        holder.textView_content.setText(sub.name);
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 切换contentLayout的显示状态
                if (isContentLayoutVisible) {
                    isContentLayoutVisible = false;
                    shouldStopTimer = true;
                }
                else
                {
                    isContentLayoutVisible = true;
                    timer.scheduleAtFixedRate(new TimerTask()
                    {
                        @Override
                        public void run() {
                            // 判断是否需要停止定时器
                            if (shouldStopTimer)
                            {
                                // 取消定时器任务
                                cancel();
                                // 重置标志
                                shouldStopTimer = false;
                                return;
                            }
                            Map<String, String> map = new HashMap<>();
                            map.put("username",sub.username);
                            //这里写请求
                            RequestData data = new RequestData(map,"/subscript/Roulette");
                            HttpGetRequestAsyncTask task = new HttpGetRequestAsyncTask(new GetResponseListener() {
                                @Override
                                public void onGetResponse(Response response)
                                {
                                    try {
                                        JSONObject data = new JSONObject(response.body().string());
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                                @Override
                                public void onGetResponseError(Exception e)
                                {
                                }
                            });
                            task.execute(data);
                        }
                }, 0, 5000); // 设置定时器每隔5秒执行一次
                }
            }
        });
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView_content;
        Button editButton;

        public ViewHolder(@NonNull View itemView, SubAdapter.OnItemClickListener itemClickListener, SubAdapter.OnEditButtonClickListener editButtonClickListener)
        {
            super(itemView);
            // 用于保存当前contentLayout的显示状态，默认为隐藏
            textView_content = itemView.findViewById(R.id.subcontent);
            System.out.println(textView_content.getText());
            editButton = itemView.findViewById(R.id.ck_status);
        }
    }
}
