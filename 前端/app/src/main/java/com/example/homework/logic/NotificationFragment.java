package com.example.homework.logic;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.homework.R;
import com.example.homework.adapter.SubAdapter;
import com.example.homework.entity.Subscript;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationFragment extends Fragment implements SubAdapter.OnItemClickListener, SubAdapter.OnEditButtonClickListener{
    // TODO: Rename parameter arguments, choose names that match
    private RecyclerView recyclerView;
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private EditText name;
    private EditText describe;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private SubAdapter adapter;
    public static List<Subscript> subsList = new ArrayList<>();
    public static List<Subscript> updatedList = new ArrayList<>();
    public NotificationFragment() {
        // Required empty public constructor
    }
    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_notification, container, false);
        ImageButton addButton = rootView.findViewById(R.id.addsubs);
        if (addButton != null) {
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog();
                }
            });
        }
        recyclerView = rootView.findViewById(R.id.recyclerViewno);
        adapter = new SubAdapter(getContext(), subsList);
        adapter.setOnItemClickListener(this);
        adapter.setOnEditButtonClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        refreshRecyclerView();
        return rootView;
    }
    private void showDialog()
    {
        // 加载自定义对话框布局
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.add_subscript_dialog, null);
        // 获取对话框中的控件
        name = dialogView.findViewById(R.id.todo_name_input1);
        describe = dialogView.findViewById(R.id.countdown_input1);
        // 创建对话框构建器
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                String na = name.getText().toString();
                String de = describe.getText().toString();
                add(na,de);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

            }
        });
        // 创建对话框并显示
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void add(String name,String describe)
    {
        Map<String, String> map = new HashMap<>();
        map.put("name", name);
        map.put("message", describe);
        RequestData data = new RequestData(map,"/subscript/add");
        HttpGetRequestAsyncTask task = new HttpGetRequestAsyncTask(new GetResponseListener()
        {
            @Override
            public void onGetResponse(Response response)
            {
                ((MainActivity) getActivity()).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject data = null;
                            data = new JSONObject(response.body().string());
                            Toast.makeText(getContext(), data.getString("message"), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ((MainActivity)getActivity()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                refreshRecyclerView();
                            }
                        });
                    }
                }).start();
            }
            @Override
            public void onGetResponseError(Exception e) {
            }
        }
        );
        task.execute(data);
    }

    private void refreshRecyclerView()
    {
        Map<String, String> map = new HashMap<>();
        RequestData data = new RequestData(map, "/subscript/query");
        HttpGetRequest.sendOkHttpGetRequest(data, new Callback() {
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
                    Type listType = new TypeToken<List<Subscript>>() {}.getType();
                    updatedList = gson.fromJson(data.getString("data"), listType);
                    if (updatedList != null) {
                        // 使用Handler将更新操作发布到主线程
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run()
                            {
                                // 后台线程中执行数据获取和处理操作
                                subsList.clear(); // 清除旧数据
                                subsList.addAll(updatedList); // 添加新数据
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
