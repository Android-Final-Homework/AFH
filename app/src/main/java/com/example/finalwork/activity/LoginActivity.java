package com.example.finalwork.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.finalwork.R;
import com.example.finalwork.Utils.App;
import com.example.finalwork.Utils.UserInfoSPTool;
import com.example.finalwork.bean.LoginBean;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    EditText etAccount;
    EditText etPassword;
    private static int REQUEST_TO_REGIST = 0;
    private Button btLogin;
    private LoginBean loginBean;
    private Handler handler;

    App app=new App();
    private String appId = app.getAppId();
    private String appSecret = app.getAppSecret();

    String introduce,avatar;
    int sex;

    //回调方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_TO_REGIST && resultCode == RESULT_OK) {
            String account = data.getStringExtra("account");
            etAccount.setText(account);
            etPassword.setText("");
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("登录");


        Log.d("LoginActivity", "onCreate()调用");
        //事件绑定

        final ImageView ivPwdSwitch = findViewById(R.id.pwd_visibility);
        etPassword = findViewById(R.id.password);
        etAccount = findViewById(R.id.account);
        CheckBox cbRememberPwd = findViewById(R.id.checkBox);
        btLogin = findViewById(R.id.login);
        ImageView ivAccountClear = findViewById(R.id.account_clear);
        Button btRegister = findViewById(R.id.register);

        //自动登录

        String spFileName = getResources()
                .getString(R.string.shared_preferences_file_name);
        String accountKey = getResources()
                .getString(R.string.login_account_name);
        String passwordKey = getResources()
                .getString(R.string.login_password);
        String useridKey = getResources()
                .getString(R.string.user_id);
        String rememberPasswordKey = getResources()
                .getString(R.string.login_remember_password);

        SharedPreferences spFile = getSharedPreferences(
                spFileName,
                MODE_PRIVATE);
        String account = spFile.getString(accountKey, null);
        String password = spFile.getString(passwordKey, null);

        Boolean rememberPassword = spFile.getBoolean(
                rememberPasswordKey,
                false);

        if (account != null && !TextUtils.isEmpty(account)) {
            etAccount.setText(account);
        }

        if (password != null && !TextUtils.isEmpty(password)) {
            etPassword.setText(password);
        }

        cbRememberPwd.setChecked(rememberPassword);


        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etAccount == null || etAccount.getText().toString().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "请输入昵称！", Toast.LENGTH_SHORT).show();
                } else if (etPassword == null || etPassword.getText().toString().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "请输入密码！", Toast.LENGTH_SHORT).show();
                } else {
                    postData2(etAccount.getText().toString(), etPassword.getText().toString());
                }
            }
        });


        ivAccountClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etAccount.setText("");
            }
        });
        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivityForResult(intent, REQUEST_TO_REGIST);
            }
        });

        ivPwdSwitch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        ivPwdSwitch.setImageResource(
                                R.drawable.ic_baseline_visibility_24);
                        etPassword.setInputType(
                                InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        break;
                    case MotionEvent.ACTION_UP:
                        ivPwdSwitch.setImageResource(
                                R.drawable.ic_baseline_visibility_off_24);
                        etPassword.setInputType(
                                InputType.TYPE_TEXT_VARIATION_PASSWORD |
                                        InputType.TYPE_CLASS_TEXT);
                        etPassword.setTypeface(Typeface.DEFAULT);
                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.e("xxx","移动");
                        break;
                }
                return true;
            }
        });


        handler=new Handler(Looper.getMainLooper())
        {
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (loginBean.getCode()==200)
                {
                    //记住密码选项
                    SharedPreferences.Editor editor = spFile.edit();
                editor.putString(useridKey, loginBean.getData().getId());
                Log.d("6666", "handleMessage: userid:" + loginBean.getData().getId());
                Log.d("6666", "handleMessage: userid:" + loginBean.getMsg());
                //Log.d("6666", "handleMessage: userid:"+userid);
                if (cbRememberPwd.isChecked()) {
                    String password = etPassword.getText().toString();
                    String account = etAccount.getText().toString();

                    editor.putString(accountKey, account);
                    editor.putString(passwordKey, password);
                    editor.putBoolean(rememberPasswordKey, true);
                } else {
                    editor.remove(accountKey);
                    editor.remove(passwordKey);
                    editor.remove(rememberPasswordKey);
                }
                UserInfoSPTool usp = new UserInfoSPTool(spFile);
                App app = new App();
                //User user=usp.getInfo();
                if (loginBean.getData().getIntroduce() != null)
                    introduce = (String) loginBean.getData().getIntroduce();
                else
                    introduce = app.getDefaultIntroduce();

                if (loginBean.getData().getAvatar() != null)
                    avatar = (String) loginBean.getData().getAvatar();
                else
                    avatar = app.getDefaultAvatar();

                if (loginBean.getData().getSex() != null)
                    sex = 1;

                usp.saveInfo(introduce, sex, loginBean.getData().getId(), avatar, loginBean.getData().getUsername());

                editor.apply();
                Log.d("6666", "handleMessage: userid:" + loginBean.getData().getId());
                //Log.d("6666", "handleMessage: userid:"+userid);
                Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, userwindomActivity.class);
                startActivity(intent);
                finish();
            }
                else
                    Toast.makeText(LoginActivity.this, loginBean.getMsg(), Toast.LENGTH_SHORT).show();
            }
        };


    }

    private void postData2(String username, String password) {
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
//        formBody.add("username","");//传递键值对参数
        Headers headers = new Headers.Builder()
                .add("Accept", "application/json, text/plain, */*")
                .add("appId", appId)
                .add("appSecret", appSecret)
                .add("Content-Type", "application/json")
                .build();

        Request request = new Request.Builder()//创建Request 对象。
                .url("http://47.107.52.7:88/member/photo/user/login?password=" + password + "&username=" + username)
                .headers(headers)
                .post(RequestBody.create(MEDIA_TYPE_JSON, ""))//传递请求体
                .build();
        Log.d("6666", "postData2: " + username + password);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("6666", "onFailure: ");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {//回调的方法执行在子线程。
                    Gson gson2 = new Gson();
                    //Log.d("6666", "onResponse: "+ response.body().string());
                    loginBean = gson2.fromJson(response.body().string(), LoginBean.class);
                    //Log.d("6666", "onCreateID: "+loginBean.toString());
                    Message msg = new Message();
                    msg.obj = loginBean;
                    handler.sendMessage(msg);
                }
            }
        });
    }
}