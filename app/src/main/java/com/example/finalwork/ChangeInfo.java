package com.example.finalwork;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.NetworkOnMainThreadException;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.example.finalwork.Utils.App;
import com.example.finalwork.Utils.CameraUtils;
import com.example.finalwork.Utils.User;
import com.example.finalwork.Utils.UserInfoSPTool;
import com.example.finalwork.bean.UploadBean;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
//import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChangeInfo extends AppCompatActivity {
    private String avatar_temp;
    private static final int DEFEAT = 4;
    private App app;
    private static final int POST_FILE = 0;

    private File file ;
    private SharedPreferences sharedPreferences;
    private UserInfoSPTool usp;
    private Handler handler;
    private ImageView img;
    UploadBean uploadBean;
    private int REQUEST_IMAGE_OPEN = 2;
    private int REQUEST_MANAGER_PERMISSION = 3;
    private int REQUEST_CODE_CAMERA = 4;
    //存储拍完照后的图片
    private File outputImagePath;
    //启动相机标识
    public static final int TAKE_PHOTO = 1;
    //启动相册标识
    public static final int SELECT_PHOTO = 2;
    //底部弹窗
    private BottomSheetDialog bottomSheetDialog;
    //弹窗视图
    private View bottomView;
    //是否拥有权限
    private boolean hasPermissions = false;
    //权限请求
    //private RxPermissions rxPermissions;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_info);
        //checkVersion();
        requestManagerPermission();
        app = new App();

        sharedPreferences = getSharedPreferences("userInfo",MODE_PRIVATE);
        usp = new UserInfoSPTool(sharedPreferences);

        //隐藏标题栏
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide(); //隐藏标题栏
        }

        img = findViewById(R.id.change_avatar);
        TextView text1 = findViewById(R.id.change_userId);
        EditText text2 = findViewById(R.id.change_sex);
        EditText text3 = findViewById(R.id.change_introduce);
        EditText text4 = findViewById(R.id.change_username);
        user = usp.getInfo();
        Picasso.get().load(user.getAvatar()).into(img);
        text1.setText(user.getUserId());
        text2.setText(user.getSex()+"");
        text3.setText(user.getIntroduce());
        text4.setText(user.getUsername());

        //跟新用户信息
        Button btn = findViewById(R.id.change_save);
        btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                user.setAvatar(avatar_temp);
                String introduce = text3.getText().toString();
                int sex = Integer.parseInt(text2.getText().toString());
                String userId = text1.getText().toString();

                String avatar = user.getAvatar();
                System.out.println("avater==================="+avatar);
                String username = text4.getText().toString();
                post(introduce,sex,userId,avatar,username);
                usp.saveInfo(introduce,sex,userId,avatar,username);
            }
        });
//1581091102155476992
        //添加更换头像的功能
        img.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){


                bottomSheetDialog = new BottomSheetDialog(view.getContext());
                bottomView = getLayoutInflater().inflate(R.layout.activity_change_info_avatar_button, null);
                bottomSheetDialog.setContentView(bottomView);
                TextView tvTakePictures = bottomView.findViewById(R.id.changeAvatar_take_pictures);
                TextView tvOpenAlbum = bottomView.findViewById(R.id.changeAvatar_open_album);
                TextView tvCancel = bottomView.findViewById(R.id.changeAvatar_cancel);

                //拍照
                tvTakePictures.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        System.out.println("拍照");
                        takePhoto();
                        bottomSheetDialog.cancel();
                    }
                });
                //打开相册
                tvOpenAlbum.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openAlbum();
                        showMsg("打开相册");
                        bottomSheetDialog.cancel();
//                        ImageView imgView = null;  //用于显示图片
//                        //打开相册
//                        Intent intent = new  Intent(Intent.ACTION_PICK);
//                        //指定获取的是图片
//                        intent.setType("image/*");
//                        //startActivityForResult()方法有两个参数，第一个参数为Intent，第二个参数为自定义的一个请求码，这个请求码会在onActivityResult()方法中被返回。
//                        startActivityForResult(intent, REQUEST_IMAGE_OPEN);
//                        bottomSheetDialog.cancel();

                    }
                });
                //取消
                tvCancel.setOnClickListener(v -> {
                    bottomSheetDialog.cancel();
                });

                bottomSheetDialog.show();
            }
        });
    }
    private void postfile() {
        Headers headers = new Headers.Builder()
                .add("appId", app.getAppId())
                .add("appSecret", app.getAppSecret())
                .add("Accept", "application/json, text/plain, */*")
                .build();
        OkHttpClient client = new OkHttpClient();

        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        //循环添加文件
        requestBodyBuilder.addFormDataPart("fileList", file.getName(),
                RequestBody.create(MediaType.parse("multipart/form-data"), file));

        RequestBody requestBody = requestBodyBuilder.build();


        Request request = new Request.Builder()//创建Request 对象。
                .url("http://47.107.52.7:88/member/photo/image/upload")
                .headers(headers)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("6666", "onFailure: ");
                handler.sendEmptyMessage(DEFEAT);
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                if (response.isSuccessful()) {//回调的方法执行在子线程。

                    Gson gson2 = new Gson();
                    uploadBean = gson2.fromJson(response.body().string(), UploadBean.class);
                    //Log.d("6666", "onResponse返回的图片上传信息: "+uploadBean.getData().getImageUrlList().get(0));
                    //子线程返回数据到主线程
//                    Message msg = new Message();
//                    msg.obj = uploadBean;
//                    handler.sendMessage(msg);

                    avatar_temp = uploadBean.getData().getImageUrlList().get(0);
                    System.out.println("avatar========="+avatar_temp);
                    user.setAvatar(avatar_temp);

//                    handler.sendEmptyMessage(POST_FILE);
                }
            }
        });
    }

    private void post(String introduce,int sex,String userId,String avatar,String username){
        new Thread(() -> {

            // url路径
            String url = "http://47.107.52.7:88/member/photo/user/update";

            // 请求头
            Headers headers = new Headers.Builder()
                    .add("Accept", "application/json, text/plain, */*")
                    .add("appId", app.getAppId())
                    .add("appSecret", app.getAppSecret())
                    .add("Content-Type", "application/json")
                    .build();

            // 请求体
            // PS.用户也可以选择自定义一个实体类，然后使用类似fastjson的工具获取json串
            Map<String, Object> bodyMap = new HashMap<>();
            bodyMap.put("introduce", introduce);
            bodyMap.put("sex", sex);
            bodyMap.put("avatar", avatar);
            bodyMap.put("id", userId);
            bodyMap.put("username", username);
            // 将Map转换为字符串类型加入请求体中
            String body = JSON.toJSONString(bodyMap);

            MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

            //请求组合创建
            Request request = new Request.Builder()
                    .url(url)
                    // 将请求头加至请求中
                    .headers(headers)
                    .post(RequestBody.create(MEDIA_TYPE_JSON, body))
                    .build();
            try {
                OkHttpClient client = new OkHttpClient();
                //发起请求，传入callback进行回调
                client.newCall(request).enqueue(callback);
            }catch (NetworkOnMainThreadException ex){
                ex.printStackTrace();
            }
        }).start();
    }

    /**
     * 回调
     */
    private final Callback callback = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, IOException e) {
            //TODO 请求失败处理
            e.printStackTrace();
        }
        @Override
        public void onResponse(@NonNull Call call, Response response) throws IOException {
            //TODO 请求成功处理
            String responseData = response.body().string();
            System.out.println(responseData);
            int code = JSONObject.parseObject(responseData).getInteger("code");
            if(code == 200){
                Looper.prepare();
                Toast.makeText(ChangeInfo.this,"修改成功",Toast.LENGTH_LONG).show();
                Looper.loop();
            }
            else{
                Looper.prepare();
                Toast.makeText(ChangeInfo.this,"修改失败",Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }
    };

    //申请文件管理权限
    private void requestManagerPermission() {
        //当系统在11及以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // 没文件管理权限时申请权限
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, REQUEST_MANAGER_PERMISSION);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            //拍照后返回
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    //显示图片
//                    displayImage(outputImagePath.getAbsolutePath());
                    System.out.println("Path:"+outputImagePath.getAbsolutePath());
                    file = new File(outputImagePath.getAbsolutePath());
                    if(cheak()){
                        postfile();
                    }else {
                        System.out.println("image == null ");
                    }
                }
                break;
            //打开相册后返回
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    String imagePath ;
                    //判断手机系统版本号
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                        //4.4及以上系统使用这个方法处理图片
                        imagePath = CameraUtils.getImageOnKitKatPath(data, this);
                    } else {
                        imagePath = CameraUtils.getImageBeforeKitKatPath(data, this);
                    }
                    System.out.println("aaPath:"+imagePath);
                    file = new File(imagePath);
//                    if(file==null)
//                    {
//                        System.out.println("file===============null");
//                    }else
//                    {
//                        System.out.println("file != null");
//                    }
                    if(cheak()){
                        postfile();
                    }else {
                        System.out.println("image == null ");
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 打开相册
     */
    private void openAlbum() {
        if (!hasPermissions) {
            showMsg("未获取到权限");
            //checkVersion();
            return;
        }
        startActivityForResult(CameraUtils.getSelectPhotoIntent(), SELECT_PHOTO);
    }
    /**
     * 拍照
     */
    private void takePhoto() {
        if (!hasPermissions) {
            showMsg("未获取到权限");
            //checkVersion();
            return;
        }
        SimpleDateFormat timeStampFormat = new SimpleDateFormat(
                "yyyy_MM_dd_HH_mm_ss");
        String filename = timeStampFormat.format(new Date());
        outputImagePath = new File(getExternalCacheDir(),
                filename + ".jpg");
        Intent takePhotoIntent = CameraUtils.getTakePhotoIntent(this, outputImagePath);
        // 开启一个带有返回值的Activity，请求码为TAKE_PHOTO
        startActivityForResult(takePhotoIntent, TAKE_PHOTO);
    }
    /**
     * 检查版本
     **/
    /**
    @SuppressLint("CheckResult")
    private void checkVersion() {
        //Android6.0及以上版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //如果你是在Fragment中，则把this换成getActivity()
            rxPermissions = new RxPermissions(this);
            //权限请求
            rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(granted -> {
                        if (granted) {//申请成功
                            showMsg("已获取权限");
                            hasPermissions = true;
                        } else {//申请失败
                            showMsg("权限未开启");
                            hasPermissions = false;
                        }
                    });
        } else {
            //Android6.0以下
            showMsg("无需请求动态权限");
        }
    }
     **/
    /**
     * Toast提示
     *
     * @param msg
     */
    private void showMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
    private boolean cheak() {
        if (file==null)
            return false;
        return true;
    }
}
