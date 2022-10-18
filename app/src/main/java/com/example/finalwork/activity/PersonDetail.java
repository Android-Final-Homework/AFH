package com.example.finalwork.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.finalwork.R;
import com.example.finalwork.Utils.App;
import com.example.finalwork.bean.CommentBean;
import com.example.finalwork.bean.MyDetailBean;
import com.example.finalwork.bean.PersonDetailBean;
import com.example.finalwork.bean.infoBean;
import com.example.finalwork.mbanneradapter;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.youth.banner.Banner;
import com.youth.banner.indicator.CircleIndicator;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class PersonDetail extends AppCompatActivity  {


    private Banner mbanner;

    App app=new App();
    private String appId = app.getAppId();
    private String appSecret = app.getAppSecret();

    PersonDetailBean personDetailBean = null;
    private  List<String> imgList;
    private String userId;
    private String puserId;
    private String focusUserId;
    private String username;
    private String shareId;
    private String TEXT;
    private TextView  detail_title, detail_content,detail_useid,detail_time;
    private ImageView user_name_detail;
    private MaterialButton btFocous;
    private ArrayList<String> list_title;


    Handler handler = new Handler(Looper.getMainLooper()) {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 1) {

                detail_time.setText(timeStamp2Date(personDetailBean.getData().getCreateTime(), "yyyy-MM-dd"));
                focusUserId = personDetailBean.getData().getPUserId();

                if(personDetailBean.getData().isHasFocus())
                    btFocous.setText("取消关注");
                else
                    btFocous.setText("+关注");
            }
            else if(msg.what==2){
                if(btFocous.getText().toString()== "取消关注")
                    btFocous.setText("+关注");
                else
                    btFocous.setText("取消关注");

                personDetailBean=null;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homedetail);

        MyDetailBean mydetail = (MyDetailBean) getIntent().getSerializableExtra("detail");
        userId = mydetail.getUserid();
        focusUserId = mydetail.getPuserid();
        imgList=mydetail.getImageUrlList();

        detail_content=findViewById(R.id.contentdetail);
        user_name_detail=findViewById(R.id.iv_head1);
        detail_useid=findViewById(R.id.username);
        detail_title=findViewById(R.id.detail_title);
        detail_time=findViewById(R.id.sharetime);
        btFocous=findViewById(R.id.focus_button);

        if(mydetail.isHanfocus())
            btFocous.setText("取消关注");
        else
            btFocous.setText("+关注");


        detail_useid.setText("id:"+mydetail.getPuserid());
        detail_content.setText("    内容：\n\n"+"              "+mydetail.getContent());
        detail_title.setText(mydetail.getTitle());


        Log.d("6666", "onCreate: "+userId+" "+shareId);
        initView();
    }


    private void initView() {
        mbanner = findViewById(R.id.banner);
        //设置mbanner设配器
        mbanner.setAdapter(new mbanneradapter(PersonDetail.this,imgList));
//是否允许自动轮播
        mbanner.isAutoLoop(true);
//设置指示器， CircleIndicator为已经定义好的类，直接用就好
        mbanner.setIndicator(new CircleIndicator(this));
//开始轮播
        mbanner.start();

        btFocous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("6666", "onClick: "+btFocous.getText().toString());
                if(btFocous.getText().toString().equals("+关注"))
                    PostFocus();
                else
                    PostUnFocus();
            }
        });
    }


    public void GetCommandDetails() {
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        Headers headers = new Headers.Builder()
                .add("Accept", "application/json, text/plain, */*")
                .add("appId", appId)
                .add("appSecret", appSecret)
                .add("Content-Type", "application/json")
                .build();
        Request request = new Request.Builder()//创建Request 对象。
                .url("http://47.107.52.7:88/member/photo/comment/first?shareId=" + shareId )
                .headers(headers)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("6666", "onFailure: ");
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {//回调的方法执行在子线程。
                    Gson gson2 = new Gson();
                    //commentBean = gson2.fromJson(response.body().string(), CommentBean.class);
                    Log.d("6666", "onResponse: "+response.body().string());
                    handler.sendEmptyMessage(3);
                }
            }
        });
    }


    private void PostFocus() {
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体

        Headers headers = new Headers.Builder()
                .add("Accept", "application/json, text/plain, */*")
                .add("appId", appId)
                .add("appSecret", appSecret)
                .add("Content-Type", "application/json")
                .build();

        Request request = new Request.Builder()//创建Request 对象。
                .url("http://47.107.52.7:88/member/photo/focus?focusUserId=" + focusUserId + "&userId=" + userId)
                .headers(headers)
                .post(RequestBody.create(MEDIA_TYPE_JSON, ""))//传递请求体
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("6666", "onFailure: ");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {//回调的方法执行在子线程。
                    Gson gson2 = new Gson();

                    Log.d("6666", "onResponse: 关注上传" + response.body().string());

                    handler.sendEmptyMessage(2);
                }
            }
        });
    }

    private void PostUnFocus() {

        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体

        Headers headers = new Headers.Builder()
                .add("Accept", "application/json, text/plain, */*")
                .add("appId", appId)
                .add("appSecret", appSecret)
                .add("Content-Type", "application/json")
                .build();

        Request request = new Request.Builder()//创建Request 对象。
                .url("http://47.107.52.7:88/member/photo/focus/cancel?focusUserId=" + focusUserId + "&userId=" + userId)
                .headers(headers)
                .post(RequestBody.create(MEDIA_TYPE_JSON, ""))//传递请求体
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("6666", "onFailure: ");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {//回调的方法执行在子线程。
                    Gson gson2 = new Gson();
                    //defultBean = gson2.fromJson(response.body().string(), DefultBean.class);
                    Log.d("6666", "onResponse: 取消关注" + response.body().string());

                    handler.sendEmptyMessage(2);
                }
            }
        });
    }

    /**
     * 重写onStop、onDestroy、onStart方法控制banner生命周期，体验更好
     */
    @Override
    protected void onStop() {
        super.onStop();
        mbanner.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mbanner.destroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mbanner.start();
    }
    public static String timeStamp2Date(String seconds, String format) {
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        if (format == null || format.isEmpty()) {
            format = "MM-dd";
        }

        SimpleDateFormat sdf = new SimpleDateFormat(format);

        return sdf.format(new Date(Long.valueOf(seconds.substring(0, seconds.length() - 3) + "000")));
    }

}