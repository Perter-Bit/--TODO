package com.example.homework.logic;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.homework.R;
import com.example.homework.entity.User;
import com.example.homework.utils.HttpGetRequest;
import com.example.homework.utils.RequestData;
import com.example.homework.utils.count;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener
{
    private EditText yanzheng, emailEditText, newpassword;
    private Button resetButton;
    private Button yanzhengma,modift;
    private ImageView back;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        // 初始化视图
        emailEditText = findViewById(R.id.forget_email);
        emailEditText.setOnClickListener(this);
        yanzhengma = findViewById(R.id.getcord);
        yanzhengma.setOnClickListener(this);
        back = findViewById(R.id.iv_forget_back);
        modift = findViewById(R.id.bt_modify);
        modift.setOnClickListener(this);
        yanzheng = findViewById(R.id.et_registeractivity_password11);
        newpassword = findViewById(R.id.et_registeractivity_password12);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onClick(View v)
    {
        if(v.getId() == R.id.getcord)
        {
            //发送qq邮箱请求
            String account = emailEditText.getText().toString();
            Map<String, String> map = new HashMap<>();
            map.put("account", account);
            RequestData data = new RequestData(map,"/loginByqq");
            HttpGetRequest.sendOkHttpGetRequest(data,new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Looper.prepare();
                    Toast.makeText(ForgotPasswordActivity.this, "post请求失败", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException
                {
                    Looper.prepare();
                    try {
                        System.out.println(response);
                        JSONObject data = new JSONObject(response.body().string());
                        Toast.makeText(ForgotPasswordActivity.this, data.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Looper.loop();
                }
            });
            count mCountDownTimerUtils = new count(yanzhengma, 60000, 1000);
            mCountDownTimerUtils.start();
        }
        else if(v.getId() == R.id.bt_modify)
        {
            String account = emailEditText.getText().toString();
            String verify = yanzheng.getText().toString();
            String password = newpassword.getText().toString();
            Map<String, String> map = new HashMap<>();
            map.put("account", account);
            map.put("verify",verify);
            map.put("password",password);
            RequestData data = new RequestData(map,"/loginverige");
            HttpGetRequest.sendOkHttpGetRequest(data,new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Looper.prepare();
                    Toast.makeText(ForgotPasswordActivity.this, "get请求失败", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException
                {
                    Looper.prepare();
                    try {
                        System.out.println(response);
                        JSONObject data = new JSONObject(response.body().string());
                        Toast.makeText(ForgotPasswordActivity.this, data.getString("message"), Toast.LENGTH_LONG).show();
                        if(response.code() == 200)
                        {
                            Intent intent = new Intent(ForgotPasswordActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Looper.loop();
                }
            });
        }
    }
}