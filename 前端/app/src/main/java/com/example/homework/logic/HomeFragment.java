package com.example.homework.logic;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.example.homework.R;
import com.example.homework.utils.HttpGetRequest;
import com.example.homework.utils.RequestData;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import me.jfenn.colorpickerdialog.dialogs.ColorPickerDialog;
import me.jfenn.colorpickerdialog.interfaces.OnColorPickedListener;
import me.jfenn.colorpickerdialog.views.picker.RGBPickerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
public class HomeFragment extends Fragment implements View.OnClickListener, OnColorPickedListener<ColorPickerDialog> {
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private TextView name,password,information,github;
    private Button quit;
    private com.example.homework.utils.CircleImageView image;
    private int color = Color.RED;
    private String mParam2;
    public HomeFragment() {
    }
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        Fragment retainedFragment = requireActivity().getSupportFragmentManager().findFragmentByTag("aaa");
        // 如果是 ColorPickerDialog 的实例，则设置监听器
        if (retainedFragment instanceof ColorPickerDialog)
            ((ColorPickerDialog) retainedFragment).withListener(this);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        name = rootView.findViewById(R.id.textView9);
        quit = rootView.findViewById(R.id.btn_profile_logout);
        image = rootView.findViewById(R.id.img_profile);
        information = rootView.findViewById(R.id.tv_information_label);
        github = rootView.findViewById(R.id.tv_information_label_4);
        rootView.findViewById(R.id.tv_information_label_2).setOnClickListener(this);
        information.setOnClickListener(this);
        github.setOnClickListener(this);
        password = rootView.findViewById(R.id.tv_information_label_1);
        password.setOnClickListener(this);
        quit.setOnClickListener(this);
        image.setOnClickListener(this);
        Map<String, String> map = new HashMap<>();
        RequestData data = new RequestData(map,"/user/getname");
        HttpGetRequest.sendOkHttpGetRequest(data,new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(getContext(), "请求失败", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                Looper.prepare();
                try {
                    System.out.println(response);
                    JSONObject data = new JSONObject(response.body().string());
                    name.setText(data.getString("data"));
                    // 反序列化JSON数据到对象列表
                    // 更新UI组件
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
        return rootView;
    }
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_profile_logout)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("系统退出"); // 设置对话框的标题文本
            builder.setMessage("您确定要退出此账号吗"); // 设置对话框的内容文本
// 设置对话框的肯定按钮文本及其点击监听器
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    MyApplication.token = "";
                    startActivity(intent);
                }
            });
            // 设置对话框的否定按钮文本及其点击监听器
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            AlertDialog alert = builder.create(); // 根据建造器构建提醒对话框对象
            alert.show(); // 显示提醒对话框
        } else if (v.getId() == R.id.tv_information_label_1)
        {
            Intent intent = new Intent(getContext(), resumepassord.class);
            startActivity(intent);
        } else if (v.getId() == R.id.tv_information_label)
        {
            Intent intent = new Intent(getContext(), Information.class);
            startActivity(intent);
        } else if (v.getId() == R.id.tv_information_label_4)
        {
            Intent intent = new Intent(getContext(), bind_github.class);
            startActivity(intent);
        } else if (v.getId() == R.id.tv_information_label_2)
        {
            new ColorPickerDialog()
                    .withColor(color)
                    .withRetainInstance(false)
                    .withTitle("颜色选择器")
                    .withCornerRadius(16)
//                    .withAlphaEnabled(v.getId() != R.id.normal)
                    .clearPickers()
                    .withPresets(new int[]{})
                    .withPicker(RGBPickerView.class)
                    .withTheme(R.style.ColorPickerDialog)
                    .withListener(this)
                    .show(requireActivity().getSupportFragmentManager(), "aaa");
        }
    }

    @Override
    public void onColorPicked(@Nullable ColorPickerDialog pickerView, int color)
    {
        Toast.makeText(getContext(), String.format("#%08X", color), Toast.LENGTH_SHORT).show();
        this.color = color;
    }
}