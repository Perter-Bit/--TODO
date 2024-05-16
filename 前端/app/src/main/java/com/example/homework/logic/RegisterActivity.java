package com.example.homework.logic;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.homework.R;
import com.example.homework.entity.User;
import com.example.homework.utils.Code;
import com.example.homework.utils.HttpGetRequest;
import com.example.homework.utils.HttpPostRequest;
import com.example.homework.utils.HttpPostRequestAsyncTask;
import com.example.homework.utils.OnResponseReceived;
import com.example.homework.utils.RequestData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {
    private EditText editTextUsername, editTextEmail, editTextPassword;
    private Button buttonRegister;
    private ImageView mIvloginactivityShowcode,back;
    private String realCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mIvloginactivityShowcode = findViewById(R.id.iv_registeractivity_showCode);
        mIvloginactivityShowcode.setImageBitmap(Code.getInstance().createBitmap());
        realCode = Code.getInstance().getCode().toLowerCase();
        editTextUsername = findViewById(R.id.et_registeractivity_username);
        editTextEmail = findViewById(R.id.et_registeractivity_password2);
        editTextPassword = findViewById(R.id.et_registeractivity_password1);
        buttonRegister = findViewById(R.id.bt_registeractivity_register);
        back = findViewById(R.id.iv_registeractivity_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        mIvloginactivityShowcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIvloginactivityShowcode.setImageBitmap(Code.getInstance().createBitmap());
                realCode = Code.getInstance().getCode().toLowerCase();
            }
        });
        // 初始化视图
        // 注册按钮点击事件
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editTextUsername.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                // 检查输入是否有效
                if (isValidInput(username, email, password))
                {
                    Map<String, String> map = new HashMap<>();
                    map.put("username", username);
                    map.put("email", email);
                    map.put("password", password);
                    RequestData data = new RequestData(map,"/register");
                    HttpPostRequestAsyncTask task = new HttpPostRequestAsyncTask(new OnResponseReceived() {
                        @Override
                        public void onPostResponse(Response response)
                        {
                            try {
                                JSONObject data = new JSONObject(response.body().string());
                                Toast.makeText(RegisterActivity.this, data.getString("message"), Toast.LENGTH_LONG).show();
                                if (response.code() == 200) {
                                    // 启动登录界面
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    // 结束当前界面
                                    finish();
                                }
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        @Override
                        public void onPostResponseError(Exception e)
                        {
                        }
                    });
                    task.execute(data);
                }
            }
        });
    }
    // 检查输入是否有效
    private boolean isValidInput(String username, String email, String password) {
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "请填写所有信息", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}