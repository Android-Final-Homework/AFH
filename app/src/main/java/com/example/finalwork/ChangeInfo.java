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
import com.tbruyelle.rxpermissions2.RxPermissions;

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

    private File file;
    private SharedPreferences sharedPreferences;
    private UserInfoSPTool usp;
    private Handler handler;
    private ImageView img;
    UploadBean uploadBean;
    private int REQUEST_IMAGE_OPEN = 2;
    private int REQUEST_MANAGER_PERMISSION = 3;
    private int REQUEST_CODE_CAMERA = 4;
    //???????????????????????????
    private File outputImagePath;
    //??????????????????
    public static final int TAKE_PHOTO = 1;
    //??????????????????
    public static final int SELECT_PHOTO = 2;
    //????????????
    private BottomSheetDialog bottomSheetDialog;
    //????????????
    private View bottomView;
    //??????????????????
    private boolean hasPermissions = false;
    //????????????
    private RxPermissions rxPermissions;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_info);
        checkVersion();
        requestManagerPermission();
        app = new App();

        sharedPreferences = getSharedPreferences("userInfo", MODE_PRIVATE);
        usp = new UserInfoSPTool(sharedPreferences);

        //???????????????
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide(); //???????????????
        }

        img = findViewById(R.id.change_avatar);
        TextView text1 = findViewById(R.id.change_userId);
        EditText text2 = findViewById(R.id.change_sex);
        EditText text3 = findViewById(R.id.change_introduce);
        EditText text4 = findViewById(R.id.change_username);
        user = usp.getInfo();
        Picasso.get().load(user.getAvatar()).into(img);
        text1.setText(user.getUserId());
        text2.setText(user.getSex() + "");
        text3.setText(user.getIntroduce());
        text4.setText(user.getUsername());

        //??????????????????
        Button btn = findViewById(R.id.change_save);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.setAvatar(avatar_temp);
                String introduce = text3.getText().toString();
                int sex = Integer.parseInt(text2.getText().toString());
                String userId = text1.getText().toString();

                String avatar = user.getAvatar();
                System.out.println("avater===================" + avatar);
                String username = text4.getText().toString();
                post(introduce, sex, userId, avatar, username);
                usp.saveInfo(introduce, sex, userId, avatar, username);
            }
        });

        //???????????????????????????
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                bottomSheetDialog = new BottomSheetDialog(view.getContext());
                bottomView = getLayoutInflater().inflate(R.layout.activity_change_info_avatar_button, null);
                bottomSheetDialog.setContentView(bottomView);
                TextView tvTakePictures = bottomView.findViewById(R.id.changeAvatar_take_pictures);
                TextView tvOpenAlbum = bottomView.findViewById(R.id.changeAvatar_open_album);
                TextView tvCancel = bottomView.findViewById(R.id.changeAvatar_cancel);

                //??????
                tvTakePictures.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        System.out.println("??????");
                        takePhoto();
                        bottomSheetDialog.cancel();
                    }
                });
                //????????????
                tvOpenAlbum.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openAlbum();
                        showMsg("????????????");
                        bottomSheetDialog.cancel();
                    }
                });
                //??????
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
        //??????????????????
        requestBodyBuilder.addFormDataPart("fileList", file.getName(),
                RequestBody.create(MediaType.parse("multipart/form-data"), file));

        RequestBody requestBody = requestBodyBuilder.build();


        Request request = new Request.Builder()//??????Request ?????????
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

                if (response.isSuccessful()) {//????????????????????????????????????

                    Gson gson2 = new Gson();
                    uploadBean = gson2.fromJson(response.body().string(), UploadBean.class);
                    avatar_temp = uploadBean.getData().getImageUrlList().get(0);
                    System.out.println("avatar=========" + avatar_temp);
                    user.setAvatar(avatar_temp);
                }
            }
        });
    }

    private void post(String introduce, int sex, String userId, String avatar, String username) {
        new Thread(() -> {

            // url??????
            String url = "http://47.107.52.7:88/member/photo/user/update";

            // ?????????
            Headers headers = new Headers.Builder()
                    .add("Accept", "application/json, text/plain, */*")
                    .add("appId", app.getAppId())
                    .add("appSecret", app.getAppSecret())
                    .add("Content-Type", "application/json")
                    .build();

            // ?????????
            // PS.??????????????????????????????????????????????????????????????????fastjson???????????????json???
            Map<String, Object> bodyMap = new HashMap<>();
            bodyMap.put("introduce", introduce);
            bodyMap.put("sex", sex);
            bodyMap.put("avatar", avatar);
            bodyMap.put("id", userId);
            bodyMap.put("username", username);
            // ???Map??????????????????????????????????????????
            String body = JSON.toJSONString(bodyMap);

            MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");

            //??????????????????
            Request request = new Request.Builder()
                    .url(url)
                    // ???????????????????????????
                    .headers(headers)
                    .post(RequestBody.create(MEDIA_TYPE_JSON, body))
                    .build();
            try {
                OkHttpClient client = new OkHttpClient();
                //?????????????????????callback????????????
                client.newCall(request).enqueue(callback);
            } catch (NetworkOnMainThreadException ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    /**
     * ??????
     */
    private final Callback callback = new Callback() {
        @Override
        public void onFailure(@NonNull Call call, IOException e) {
            //TODO ??????????????????
            e.printStackTrace();
        }

        @Override
        public void onResponse(@NonNull Call call, Response response) throws IOException {
            //TODO ??????????????????
            String responseData = response.body().string();
            System.out.println(responseData);
            int code = JSONObject.parseObject(responseData).getInteger("code");
            if (code == 200) {
                Looper.prepare();
                Toast.makeText(ChangeInfo.this, "????????????", Toast.LENGTH_LONG).show();
                Looper.loop();
            } else {
                Looper.prepare();
                Toast.makeText(ChangeInfo.this, "????????????", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }
    };

    //????????????????????????
    private void requestManagerPermission() {
        //????????????11?????????
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // ????????????????????????????????????
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
            //???????????????
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    System.out.println("Path:" + outputImagePath.getAbsolutePath());
                    file = new File(outputImagePath.getAbsolutePath());
                    if (cheak()) {
                        postfile();
                    } else {
                        System.out.println("image == null ");
                    }
                }
                break;
            //?????????????????????
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    String imagePath;
                    //???????????????????????????
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                        //4.4?????????????????????????????????????????????
                        imagePath = CameraUtils.getImageOnKitKatPath(data, this);
                    } else {
                        imagePath = CameraUtils.getImageBeforeKitKatPath(data, this);
                    }
                    System.out.println("aaPath:" + imagePath);
                    file = new File(imagePath);
                    if (cheak()) {
                        postfile();
                    } else {
                        System.out.println("image == null ");
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * ????????????
     */
    private void openAlbum() {
        if (!hasPermissions) {
            showMsg("??????????????????");
            checkVersion();
            return;
        }
        startActivityForResult(CameraUtils.getSelectPhotoIntent(), SELECT_PHOTO);
    }

    /**
     * ??????
     */
    private void takePhoto() {
        if (!hasPermissions) {
            showMsg("??????????????????");
            //checkVersion();
            return;
        }
        SimpleDateFormat timeStampFormat = new SimpleDateFormat(
                "yyyy_MM_dd_HH_mm_ss");
        String filename = timeStampFormat.format(new Date());
        outputImagePath = new File(getExternalCacheDir(),
                filename + ".jpg");
        Intent takePhotoIntent = CameraUtils.getTakePhotoIntent(this, outputImagePath);
        // ??????????????????????????????Activity???????????????TAKE_PHOTO
        startActivityForResult(takePhotoIntent, TAKE_PHOTO);
    }
    /**
     * ????????????
     **/
    /**
     * ????????????
     */
    @SuppressLint("CheckResult")
    private void checkVersion() {
        //Android6.0???????????????
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //???????????????Fragment????????????this??????getActivity()
            rxPermissions = new RxPermissions(this);
            //????????????
            rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(granted -> {
                        if (granted) {//????????????
                            showMsg("???????????????");
                            hasPermissions = true;
                        } else {//????????????
                            showMsg("???????????????");
                            hasPermissions = false;
                        }
                    });
        } else {
            //Android6.0??????
            showMsg("????????????????????????");
        }
    }

    /**
     * Toast??????
     *
     * @param msg
     */
    private void showMsg(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private boolean cheak() {
        if (file == null)
            return false;
        return true;
    }
}
