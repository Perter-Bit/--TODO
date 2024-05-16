package com.example.homework.logic;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.homework.R;
import com.example.homework.adapter.AffairAdapter;
import com.example.homework.entity.Affair;
import com.example.homework.utils.GetResponseListener;
import com.example.homework.utils.HttpGetRequest;
import com.example.homework.utils.HttpGetRequestAsyncTask;
import com.example.homework.utils.HttpPostRequest;
import com.example.homework.utils.HttpPostRequestAsyncTask;
import com.example.homework.utils.OnResponseReceived;
import com.example.homework.utils.RequestData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
public class SmsFragment extends Fragment implements AffairAdapter.OnItemClickListener, AffairAdapter.OnEditButtonClickListener {
    private RecyclerView recyclerView;
    private AffairAdapter adapter;
    public static List<Affair> affairList = new ArrayList<>();
    public static List<Affair> updatedList = new ArrayList<>();
    public String oldName = "";
    public String oldTime ="";
    public boolean ifCheck = true;
    public SmsFragment()
    {
    }
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static SmsFragment newInstance(String param1, String param2) {
        SmsFragment fragment = new SmsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sms, container, false);
        ImageButton addButton = rootView.findViewById(R.id.addAffair);
        if (addButton != null) {
            addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(MyApplication.TAG, "添加按钮");
                    showConfirmationDialog();
                }
            });
        }
        recyclerView = rootView.findViewById(R.id.recyclerView);
        adapter = new AffairAdapter(getContext(), affairList);
        adapter.setOnItemClickListener(this);
        adapter.setOnEditButtonClickListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        refreshRecyclerView();
        return rootView;
    }
    private void showConfirmationDialog()
    {
        // 加载自定义对话框布局
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.add_affair_dialog, null);
        // 获取对话框中的控件
        TextView dialogTitle = dialogView.findViewById(R.id.dialog_title);
        EditText todoNameInput = dialogView.findViewById(R.id.todo_name_input);
        EditText countdownInput = dialogView.findViewById(R.id.countdown_input);
        RadioGroup radioGroup = dialogView.findViewById(R.id.radio_group);
        RadioButton radioTimer = dialogView.findViewById(R.id.radio_timer);
        RadioButton radioCountdown = dialogView.findViewById(R.id.radio_countdown);
        //使得控件不可见
        countdownInput.setVisibility(View.GONE);
        //默认显示的代办内容和时间
        todoNameInput.setText(oldName);
        countdownInput.setText(oldTime);
        radioTimer.setChecked(ifCheck);
        radioCountdown.setChecked(!ifCheck);
        if (ifCheck){
            countdownInput.setVisibility(View.GONE);
        }else{
            countdownInput.setVisibility(View.VISIBLE);
        }
        // 根据单选按钮选择状态显示或隐藏倒计时输入框
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio_timer) {
                    countdownInput.setVisibility(View.GONE); // 隐藏倒计时输入框
                } else if (checkedId == R.id.radio_countdown) {
                    countdownInput.setVisibility(View.VISIBLE); // 显示倒计时输入框
                }
            }
        });
        // 创建对话框构建器
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(dialogView);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //先判断 是否输入了 代办的名称
                oldName = todoNameInput.getText().toString();
                oldTime = countdownInput.getText().toString();
                ifCheck = radioTimer.isChecked();
                //未输入代办名称
                if(oldName.equals("")){
                    Toast.makeText(getContext(), "请输入代办名称", Toast.LENGTH_SHORT).show();
                    showConfirmationDialog();
                }
                //勾选了代办
                if(ifCheck)
                {//如果勾选了计时
                    Random random = new Random();
                    // 生成一个在 1 到 20 之间的随机整数（包括 1 和 20）
                    int id = random.nextInt(10) + 1;
                    String imageId = String.valueOf(id);
                    add("1",oldName,imageId,"0");
                }
                else
                {
                    //勾选了倒计时
                    if(oldTime.equals(""))
                    {
                        Toast.makeText(getContext(), "请输入倒计时的时间", Toast.LENGTH_SHORT).show();
                        showConfirmationDialog();
                    }
                    Random random = new Random();
                    // 生成一个在 1 到 20 之间的随机整数（包括 1 和 20）
                    int id = random.nextInt(10) + 1;
                    String imageId = String.valueOf(id);
                    add("0",oldName,imageId,oldTime);
                }
                refreshRecyclerView();
                oldName = "";
                oldTime = "";
                ifCheck = true;
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        // 创建对话框并显示
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void add(String type,String oldName,String imageId,String time)
    {
        Map<String, String> map = new HashMap<>();
        map.put("type", type);
        map.put("message", oldName);
        map.put("imageId", imageId);
        map.put("time",time);
        Log.d(MyApplication.TAG,"加一个被调用了");
        RequestData data = new RequestData(map,"/affair/add");
        HttpPostRequestAsyncTask task = new HttpPostRequestAsyncTask(new OnResponseReceived() {
            @Override
            public void onPostResponse(Response response)
            {
                try {
                    JSONObject data = new JSONObject(response.body().string());
                    ((MainActivity) getActivity()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String message = null;
                            try {
                                message = data.getString("message");
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
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

                    // 处理其他数据
                } catch (JSONException e) {
                    // 处理JSON解析异常
                    e.printStackTrace();
                } catch (IOException e) {
                    // 处理IO异常
                    e.printStackTrace();
                }
            }
            @Override
            public void onPostResponseError(Exception e)
            {
            }
        });
        task.execute(data);
    }
    //重新渲染页面
    private void refreshRecyclerView()
    {
        Map<String, String> map = new HashMap<>();
        RequestData data = new RequestData(map, "/affair/query");
        HttpGetRequest.sendOkHttpGetRequest(data, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                try {
                    JSONObject data = new JSONObject(response.body().string());
                    // 反序列化JSON数据到对象列表
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<Affair>>() {}.getType();
                    updatedList = gson.fromJson(data.getString("data"), listType);
                    System.out.println(updatedList.get(0).affairId);
                    if (updatedList != null) {
                        // 使用Handler将更新操作发布到主线程
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                // 后台线程中执行数据获取和处理操作
                                affairList.clear(); // 清除旧数据
                                affairList.addAll(updatedList); // 添加新数据
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
    public void onItemClick(int position)
    {
        // Item click logic here
        Log.i("215144", "onItemClick: Item clicked at position " + position);
    }
    @Override
    public void onEditButtonClick(int position)
    {
        // Button click logic here
        Log.i("215144", "onEditButtonClick: Edit button clicked at position " + position);
    }
}