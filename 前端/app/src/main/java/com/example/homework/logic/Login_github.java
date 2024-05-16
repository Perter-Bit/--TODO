package com.example.homework.logic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Login_github extends AppCompatActivity implements View.OnClickListener{
    private TextView Textname;
    private TextView Textpass;
    private Button button;
    private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_github);
        button = findViewById(R.id.github_login_btn);
        Textname = findViewById(R.id.github_account);
        Textpass = findViewById(R.id.github_password);
        button.setOnClickListener(this);
        back = findViewById(R.id.github_registeractivity_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login_github.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.github_login_btn)
        {
            String username = Textname.getText().toString().trim();
            String password = Textpass.getText().toString().trim();
            Map<String, String> map = new HashMap<>();
            map.put("username", username);
            map.put("password",password);
            RequestData data = new RequestData(map,"/loginBygithub");
            HttpGetRequest.sendOkHttpGetRequest(data,new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Looper.prepare();
                    Toast.makeText(Login_github.this, "请求失败", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException
                {
                    Looper.prepare();
                    try {
                        System.out.println(response);
                        JSONObject data = new JSONObject(response.body().string());
                        Toast.makeText(Login_github.this, data.getString("message"), Toast.LENGTH_LONG).show();
                        if(response.code() == 200)
                        {
                            MyApplication.token = data.getString("token");
                            Intent intent = new Intent(Login_github.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
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