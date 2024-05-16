package com.example.homework.logic;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homework.R;
import com.example.homework.adapter.Newadapter;
import com.example.homework.entity.News;
import com.example.homework.utils.HttpGetRequest;
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

public class newFragment  extends Fragment implements Newadapter.OnItemClickListener, Newadapter.OnEditButtonClickListener
{
    public newFragment() {
        // Required empty public constructor
    }
    private Newadapter adapter;
    private RecyclerView recyclerView;
    private String mParam1;
    private String mParam2;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static List<News> NewsList = new ArrayList<>();
    public static List<News> updatedList = new ArrayList<>();
    public static newFragment newInstance(String param1, String param2) {
        newFragment fragment = new newFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragement_new, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerViewnew);
        adapter = new Newadapter(getContext(), NewsList);
        adapter.setOnItemClickListener(this);
        adapter.setOnEditButtonClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        refreshRecyclerView();
        return rootView;
    }
    private void refreshRecyclerView()
    {
        Map<String, String> map = new HashMap<>();
        RequestData data = new RequestData(map, "/news/query");
        HttpGetRequest.sendOkHttpGetRequest(data, new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e)
            {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                try {
                    JSONObject data = new JSONObject(response.body().string());
                    // 反序列化JSON数据到对象列表
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<News>>() {}.getType();
                    updatedList = gson.fromJson(data.getString("data"), listType);
                    System.out.println(updatedList);
                    if (updatedList != null) {
                        // 使用Handler将更新操作发布到主线程
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run()
                            {
                                // 后台线程中执行数据获取和处理操作
                                NewsList.clear(); // 清除旧数据
                                NewsList.addAll(updatedList); // 添加新数据
                                // 更新UI组件
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                } catch (JSONException e) {
                    // 处理JSON解析异常
                    e.printStackTrace();
                } catch (IOException e) {
                    // 处理IO异常
                    e.printStackTrace();
                }
            }
        });
    }
    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onEditButtonClick(int position) {

    }
}
