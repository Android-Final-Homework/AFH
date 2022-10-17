package com.example.photoshare.activity;

import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.example.photoshare.CommentAdapter;
import com.example.photoshare.R;
import com.example.photoshare.RecfindAdapter;
import com.example.photoshare.Utils.App;
import com.example.photoshare.bean.CommentBean;
import com.example.photoshare.bean.FindBean;
import com.example.photoshare.bean.LoginBean;
import com.example.photoshare.bean.MyDetailBean;
import com.example.photoshare.bean.PersonDetailBean;
import com.example.photoshare.bean.infoBean;
import com.example.photoshare.mbanneradapter;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.youth.banner.Banner;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.listener.OnBannerListener;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.xml.transform.Transformer;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class PersonDetail extends AppCompatActivity  {


    private Banner mbanner;
    List<infoBean> data = new ArrayList<>();

    App app=new App();
    private String appId = app.getAppId();
    private String appSecret = app.getAppSecret();
    CommentAdapter commentAdapter;
    RecyclerView mRecyclerView;
    PersonDetailBean personDetailBean = null;
    private  List<String> imgList;
    private List<CommentBean.DataBean> commentBean;
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
//    private Recyleruserdetail recyclermy1;
//    private String[] imageUrlList;
//    private SinglePicture singlePicture;
//    private RecyclerView deteil_person_image;
//    ArrayList<PictureDetails> pictureDetails = new ArrayList<>();

    Handler handler = new Handler(Looper.getMainLooper()) {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == 1) {
//                Log.d("6666", "handleMessage: "+personDetailBean.getData().getTitle());
                //detail_content.setText("内容：\n" + "    " + personDetailBean.getData().getContent());
                //user_name_detail.setText(personDetailBean.getData().getContent());
                //detail_useid.setText("Id: " + personDetailBean.getData().getId());
                //detail_title.setText(personDetailBean.getData().getTitle());
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

        //setContentView(R.layout.user_detail);
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

//        commentAdapter = new CommentAdapter(getContext(),commentBean,userId);
//        mRecyclerView = findViewById(R.id.recylerview);
//        mRecyclerView.setAdapter(commentAdapter);
//        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

        Log.d("6666", "onCreate: "+userId+" "+shareId);
        //GetPictureDetails();
        //GetCommandDetails();
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