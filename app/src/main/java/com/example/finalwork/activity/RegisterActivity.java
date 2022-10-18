package com.example.finalwork.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalwork.R;
import com.example.finalwork.Utils.App;
import com.example.finalwork.bean.RegisterBean;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    private static int RESULT_LOAD_IMAGE = 1;
    private static String TAG = "RegisterActivity";
    ImageView ivHeadPicture;
    TextView tvNickname;
    TextView tvAccount;
    TextView tvPassword1;
    TextView tvPassword2;
    private Handler handler;

    App app=new App();
    private String appId = app.getAppId();
    private String appSecret = app.getAppSecret();

    private RegisterBean registerBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("注册");
        tvAccount = findViewById(R.id.account);
        ImageView ivAccountClear = findViewById(R.id.account_clear);
        tvNickname = findViewById(R.id.nickname);
        ImageView ivNicknameClear = findViewById(R.id.nickname_clear);
        tvPassword1 = findViewById(R.id.password_1);
        ImageView ivPasswordClear1 = findViewById(R.id.password_clear_1);
        tvPassword2 = findViewById(R.id.password_2);
        ImageView ivPasswordClear2 = findViewById(R.id.password_clear_2);
        TextView tvPswTip = findViewById(R.id.psw_tip);
        ivHeadPicture = findViewById(R.id.headpicture);
        Button btRegister = findViewById(R.id.regist);

        //注册的子线程返回到主线程
        handler = new Handler(Looper.getMainLooper()){
            @SuppressLint("HandlerLeak")
            @Override
            public void handleMessage(@NonNull Message msg) {
                RegisterBean registerBean = (RegisterBean ) msg.obj;
                if(registerBean.getCode()!=200)
                {
                    Toast.makeText(RegisterActivity.this, "注册失败！"+registerBean.getMsg(), Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                    finish();
                }
                Intent intent1 = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent1);
                finish();
            }
        };

        tvAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    ivAccountClear.setVisibility(View.VISIBLE);
                } else {
                    ivAccountClear.setVisibility(View.INVISIBLE);
                }

            }
        });

        tvNickname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    ivNicknameClear.setVisibility(View.VISIBLE);
                } else {
                    ivNicknameClear.setVisibility(View.INVISIBLE);
                }

            }
        });

        tvPassword1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    ivPasswordClear1.setVisibility(View.VISIBLE);
                } else {
                    ivPasswordClear1.setVisibility(View.INVISIBLE);
                }

                if (tvPassword2.getText().toString().equals(s.toString())) {
                    tvPswTip.setVisibility(View.INVISIBLE);
                } else {
                    tvPswTip.setVisibility(View.VISIBLE);
                }

            }
        });

        tvPassword2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    ivPasswordClear2.setVisibility(View.VISIBLE);
                } else {
                    ivPasswordClear2.setVisibility(View.INVISIBLE);
                }
                if (tvPassword1.getText().toString().equals(s.toString())) {
                    tvPswTip.setVisibility(View.INVISIBLE);
                } else {
                    tvPswTip.setVisibility(View.VISIBLE);
                }

            }
        });


        ivAccountClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvAccount.setText("");
            }
        });

        ivNicknameClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvNickname.setText("");
            }
        });

        ivAccountClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvAccount.setText("");
            }
        });

        ivNicknameClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvNickname.setText("");
            }
        });


        ivPasswordClear1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvPassword1.setText("");
            }
        });
        ivPasswordClear2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvPassword2.setText("");
            }
        });

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (tvAccount == null || tvAccount.getText().toString().isEmpty()) {
//                    Toast.makeText(RegisterActivity.this, "请输入账号！", Toast.LENGTH_SHORT).show();
//                } else
                    if (tvNickname == null || tvNickname.getText().toString().isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "请输入昵称！", Toast.LENGTH_SHORT).show();
                } else if (tvPassword1 == null || tvPassword1.getText().toString().isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "请输入密码！", Toast.LENGTH_SHORT).show();
                } else if (tvPassword2 == null || tvPassword2.getText().toString().isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "请重复输入密码！", Toast.LENGTH_SHORT).show();
                } else if (!tvPassword1.getText().toString().equals(tvPassword2.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "两次输入的密码不一致！", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, "onClick: 注册事件");
                    postData3(tvNickname.getText().toString(), tvPassword1.getText().toString());
                }
            }

        });



    }


    private void postData3(String username,String password) {
        Gson gson = new Gson();
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("password", password);
        bodyMap.put("username", username);
        // 将Map转换为字符串类型加入请求体中
        String json = gson.toJson(bodyMap);
        //MediaType  设置Content-Type 标头中包含的媒体类型值
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Log.d("6666", "postData3: " + json);

        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        Request request = new Request.Builder()//创建Request 对象。
                .url("http://47.107.52.7:88/member/photo/user/register")
                .addHeader("appId", appId)
                .addHeader("appSecret", appSecret)
                .addHeader("Content-Type","application/json")
                .post(requestBody)//传递请求体
                .build();

        //enqueue自动开启一个子线程
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("6666", "onFailure: ");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //respond.body().toString()这个只能用一次 序列化一次
                Log.d("66667", "onResponse: " + response.code());
                if (response.isSuccessful()) {//回调的方法执行在子线程。
                    Gson gson2 = new Gson();
                    registerBean = gson2.fromJson(response.body().string(),RegisterBean.class);
                    //子线程返回数据到主线程
                    Message msg = new Message();
                    msg.obj = registerBean;
                    handler.sendMessage(msg);
                }
            }
        });
    }
}




