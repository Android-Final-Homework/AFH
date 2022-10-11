package com.example.finalwork.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalwork.R;
import com.example.finalwork.bean.LoginBean;
import com.example.finalwork.userwindom;
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
    private LoginBean loginBean2;
    private Handler handler;
    private String appId = "27fc6823aab44ec59a915c49c00c4b4b";
    private String appSecret = "466046abac4bdecfb4ee8ab60144d9c1add29";

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode ==REQUEST_TO_REGIST &&resultCode ==RESULT_OK){
            String account=data.getStringExtra("account");
            etAccount.setText(account);
            etPassword.setText("");

        }

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("登录");


        Log.d("LoginActivity","onCreate()调用");
        //事件绑定

        final ImageView ivPwdSwitch = findViewById(R.id.pwd_visibility);
        etPassword = findViewById(R.id.password);
        etAccount = findViewById(R.id.account);
        CheckBox cbRememberPwd = findViewById(R.id.checkBox);
        btLogin = findViewById(R.id.login);
        ImageView ivAccountClear = findViewById(R.id.account_clear);
        Button btRegister= findViewById(R.id.register);

        //自动登录

        String spFileName = getResources()
                .getString(R.string.shared_preferences_file_name);
        String accountKey = getResources()
                .getString(R.string.login_account_name);
        String passwordKey =  getResources()
                .getString(R.string.login_password);
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
                if(etAccount==null||etAccount.getText().toString().isEmpty()){
                    Toast.makeText(LoginActivity.this, "请输入账号！", Toast.LENGTH_SHORT).show();
                }else if(etPassword==null||etPassword.getText().toString().isEmpty()){
                    Toast.makeText(LoginActivity.this, "请输入密码！", Toast.LENGTH_SHORT).show();
                }else{
                    postData2(etAccount.getText().toString(),etPassword.getText().toString());
                                Toast.makeText(LoginActivity.this, "登录成功！", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, userwindom.class);
                                startActivity(intent);
                                SharedPreferences.Editor editor = spFile.edit();

                                //记住密码选项
                                if (cbRememberPwd.isChecked()) {
                                    String password = etPassword.getText().toString();
                                    String account = etAccount.getText().toString();

                                    editor.putString(accountKey, account);
                                    editor.putString(passwordKey, password);
                                    editor.putBoolean(rememberPasswordKey, true);
                                    editor.apply();
                                } else {
                                    editor.remove(accountKey);
                                    editor.remove(passwordKey);
                                    editor.remove(rememberPasswordKey);
                                    editor.apply();
                                }
                                finish();
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
                Intent intent = new Intent(LoginActivity.this, com.example.finalwork.activity.RegisterActivity.class);
                startActivityForResult(intent,REQUEST_TO_REGIST);
            }
        });



    }

    private void postData2(String username,String password) {
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
                .url("http://47.107.52.7:88/member/photo/user/login?password="+ password +"&username="+username)
                .headers(headers)
                .post(RequestBody.create(MEDIA_TYPE_JSON, ""))//传递请求体
                .build();
        Log.d("6666", "postData2: "+ username +password);
        client.newCall(request).enqueue(new Callback(){
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("6666", "onFailure: ");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {//回调的方法执行在子线程。
                    Gson gson2 = new Gson();
                    Log.d("6666", "onResponse: "+ response.body().string());
//                    loginBean2 = gson2.fromJson(response.body().string(), LoginBean.class);
//                    Message msg = new Message();
//                    msg.obj = loginBean2;
//                    handler.sendMessage(msg);
                }
            }
        });
    }
}