package com.example.homework.logic;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.homework.R;
import com.example.homework.utils.Code;
import com.example.homework.utils.GetResponseListener;
import com.example.homework.utils.HttpGetRequest;
import com.example.homework.utils.HttpGetRequestAsyncTask;
import com.example.homework.utils.HttpPostRequestAsyncTask;
import com.example.homework.utils.OnResponseReceived;
import com.example.homework.utils.RequestData;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText editTextUsername;
    private EditText editTextPassword;
    private String realCode;
    private Button buttonLogin;
    private TextView buttonRegister;
    private TextView buttonForgotPassword;
    private EditText mEtloginactivityPhonecodes;
    private ImageView mIvloginactivityShowcode;
    private ImageButton button_github;
    private ImageButton button_email;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editTextUsername = findViewById(R.id.et_loginactivity_username);
        editTextPassword = findViewById(R.id.et_loginactivity_password);
        buttonLogin = findViewById(R.id.bt_loginactivity_login);
        buttonRegister = findViewById(R.id.btn_register);
        buttonForgotPassword = findViewById(R.id.btn_find_password);
        mEtloginactivityPhonecodes = findViewById(R.id.et_loginactivity_phoneCodes);
        mIvloginactivityShowcode = findViewById(R.id.iv_loginactivity_showCode);
        button_github = findViewById(R.id.github);
        button_email = findViewById(R.id.email);
        // 设置登录按钮的点击事件监听器
        buttonRegister.setOnClickListener(this);
        buttonForgotPassword.setOnClickListener(this);
        buttonLogin.setOnClickListener(this);
        button_email.setOnClickListener(this);
        button_github.setOnClickListener(this);
        //将验证码用图片的形式显示出来
        mIvloginactivityShowcode.setImageBitmap(Code.getInstance().createBitmap());
        realCode = Code.getInstance().getCode().toLowerCase();
    }
    @Override
    public void onClick(View v)
    {
        System.out.println(v.getId());
        if(v.getId() == R.id.bt_loginactivity_login)
        {
            String username = editTextUsername.getText().toString().trim();
            String password = editTextPassword.getText().toString().trim();
            Map<String, String> map = new HashMap<>();
            map.put("username", username);
            map.put("password",password);
            RequestData data = new RequestData(map,"/login");
            HttpGetRequestAsyncTask task = new HttpGetRequestAsyncTask(new GetResponseListener() {
                @Override
                public void onGetResponse(Response response)
                {
                    try {
                        JSONObject data = new JSONObject(response.body().string());
                        LoginActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Toast.makeText(LoginActivity.this, data.getString("message"), Toast.LENGTH_LONG).show();
                                } catch (JSONException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        });
                        if (response.code() == 200)
                        {
                            MyApplication.token = data.getString("token");
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
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
        else if(v.getId() == R.id.btn_find_password)
        {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        }
        else if(v.getId() == R.id.btn_register)
        {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        }
        else if(v.getId() == R.id.iv_loginactivity_showCode)
        {
            mIvloginactivityShowcode.setImageBitmap(Code.getInstance().createBitmap());
            realCode = Code.getInstance().getCode().toLowerCase();
        }
        else if(v.getId() == R.id.github)
        {
            Intent intent = new Intent(LoginActivity.this, Login_github.class);
            startActivity(intent);
        }
        else if(v.getId() == R.id.email)
        {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        }
    }
}