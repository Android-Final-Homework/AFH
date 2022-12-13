package com.example.finalwork.fragment;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalwork.Path;
import com.example.finalwork.R;
import com.example.finalwork.Utils.App;
import com.example.finalwork.bean.DefultBean;
import com.example.finalwork.bean.DiscoverBean;
import com.example.finalwork.bean.FindBean;
import com.example.finalwork.bean.UploadBean;
import com.google.gson.Gson;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

public class publishFragment extends Fragment implements View.OnClickListener {
    private static final int REQUEST_CODE_CHOOSE = 23;
    private static final int POST_FILE = 0;
    private static final int POST_SHARE = 1;
    private static final int POST_SAVE_SHARE = 2;
    private static final int GET_SAVE_SHARE = 3;
    private static final int DEFEAT = 4;
    private static final String TAG = "6666";
    private MediaType MEDIA_TYPE_JSON;


    EditText edTextContent;
    EditText edTextTitle;
    TextView picture;
    private Handler handler;
    UploadBean uploadBean;
    FindBean findBean;
    Button btSaveShare, btPostShare, btGetShare, btClear;
    String textsh;
    String texttitle;

    private List<Uri> mSelected;
    private static final int REQUEST_CODE = 1;
    private static final int REQUEST_FILE_CODE = 1000;
    String path = "";
    List<String> result = new ArrayList<>();
    public static final int CHOOSE_PHOTO = 2;
    private List<File> file = new ArrayList<>();
    String userId;

    App app = new App();
    private String appId = app.getAppId();
    private String appSecret = app.getAppSecret();

    DefultBean defultBean;
    private Headers headers;
    private List<FindBean.DataBean.RecordsBean> recordslocalList;
    List<FindBean.DataBean.RecordsBean> list;


    public View onCreateView(@Nullable LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_publish, container, false);
        //内容
        edTextContent = root.findViewById(R.id.sharedtext);
        textsh = edTextContent.getText().toString().trim();
        //标题
        edTextTitle = root.findViewById(R.id.sharedtext2);
        texttitle = edTextTitle.getText().toString().trim();

        picture = root.findViewById(R.id.sharedphoto);
        //b2 = root.findViewById(R.id.center);
        btPostShare = root.findViewById(R.id.shardup);
        btSaveShare = root.findViewById(R.id.saveShare);
        btGetShare = root.findViewById(R.id.getShare);
        btClear = root.findViewById(R.id.clear);

        //ivShareimg =root.findViewById(R.id.shareimg);
        return root;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //选择照片上传
        picture.setOnClickListener(this);//上传
        btSaveShare.setOnClickListener(this);//保存
        btPostShare.setOnClickListener(this);//发布
        btGetShare.setOnClickListener(this);//同步
        btClear.setOnClickListener(this);//同步
        editClear();
        GetUserId();

        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case POST_FILE:
                        if (uploadBean.getMsg() == null) {
                            Toast.makeText(getActivity(), "图片上传成功", Toast.LENGTH_SHORT).show();
                            uploadBean = null;
                        } else {
                            Toast.makeText(getActivity(), uploadBean.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case POST_SAVE_SHARE:
                        if (defultBean.getMsg() == null) {
                            Toast.makeText(getActivity(), "保存分享成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), defultBean.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case POST_SHARE:
                        if (defultBean.getMsg() == null) {
                            Toast.makeText(getActivity(), "分享上传成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), defultBean.getMsg(), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case GET_SAVE_SHARE:
                        if (findBean.getMsg() == null) {
                            Toast.makeText(getActivity(), "当前同步的分享", Toast.LENGTH_SHORT).show();
                        } else {
                            edTextContent.setText(list.get(0).getContent());
                            edTextTitle.setText(list.get(0).getTitle());
                            Gson gson2 = new Gson();
                            uploadBean = gson2.fromJson("{\"code\":200,\"msg\":\"成功\",\"data\":{\"imageCode\":\"" + list.get(0).getImageCode() + "\",\"imageUrlList\":[\"null\"]}}", UploadBean.class);
                            Log.d(TAG, "handleMessage图片码: " + uploadBean.getData().getImageCode());
                            Toast.makeText(getActivity(), "同步成功", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    default:
                        break;
                }
            }
        };
    }

    private void postfile() {
        Headers headers = new Headers.Builder()
                .add("appId", appId)
                .add("appSecret", appSecret)
                .add("Accept", "application/json, text/plain, */*")
                .build();
        OkHttpClient client = new OkHttpClient();

        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        //循环添加文件
        for (int i = 0; i < file.size(); i++) {
            requestBodyBuilder.addFormDataPart("fileList", file.get(i).getName(),
                    RequestBody.create(MediaType.parse("multipart/form-data"), file.get(i)));
        }

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
                    //Log.d(TAG, "onResponse: "+response.body().string());
                    uploadBean = gson2.fromJson(response.body().string(), UploadBean.class);
                    //Log.d("6666", "onResponse返回的图片上传信息: "+uploadBean.getData().getImageUrlList().get(0));
                    //子线程返回数据到主线程
//                    Message msg = new Message();
//                    msg.obj = uploadBean;
//                    handler.sendMessage(msg);

                    handler.sendEmptyMessage(POST_FILE);
                }
            }
        });
    }


    //发布分享
    private void postShare() {
        Gson gson = new Gson();
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("content", edTextContent.getText().toString().trim());
        bodyMap.put("imageCode", uploadBean.getData().getImageCode());
        bodyMap.put("pUserId", userId);
        bodyMap.put("title", edTextTitle.getText().toString().trim());

        // 将Map转换为字符串类型加入请求体中
        String json = gson.toJson(bodyMap);
        //MediaType  设置Content-Type 标头中包含的媒体类型值
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Log.d("6666", "postData3: " + json);

        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        Request request = new Request.Builder()//创建Request 对象。
                .url("http://47.107.52.7:88/member/photo/share/add")
                .addHeader("appId", appId)
                .addHeader("appSecret", appSecret)
                .addHeader("Content-Type", "application/json")
                .post(requestBody)//传递请求体
                .build();

        //enqueue自动开启一个子线程
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("6666", "onFailure: ");
                handler.sendEmptyMessage(DEFEAT);
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //respond.body().toString()这个只能用一次 序列化一次
                if (response.isSuccessful()) {//回调的方法执行在子线程。
                    Log.d("66667", "onResponse:成功上传分享 ");
                    Gson gson2 = new Gson();
                    defultBean = gson2.fromJson(response.body().string(), DefultBean.class);
                    //Log.d(TAG, "onResponse: defultBean保存成功:" + response.body().string());
                    //子线程返回数据到主线程
//                    Message msg = new Message();
//                    msg.obj = defultBean;
//                    handler.sendMessage(msg);
                    handler.sendEmptyMessage(POST_SHARE);
                }
            }
        });
    }

    //保存将要发布的分享
    private void postSaveShare() {
        Gson gson = new Gson();
        Map<String, Object> bodyMap = new HashMap<>();
        bodyMap.put("content", edTextContent.getText().toString().trim());
        bodyMap.put("imageCode", uploadBean.getData().getImageCode());
        bodyMap.put("pUserId", userId);
        bodyMap.put("title", edTextTitle.getText().toString().trim());

        // 将Map转换为字符串类型加入请求体中
        String json = gson.toJson(bodyMap);
        //MediaType  设置Content-Type 标头中包含的媒体类型值
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        Log.d("6666", "postData3: " + json);

        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        Request request = new Request.Builder()//创建Request 对象。
                .url("http://47.107.52.7:88/member/photo/share/save")
                .addHeader("appId", appId)
                .addHeader("appSecret", appSecret)
                .addHeader("Content-Type", "application/json")
                .post(requestBody)//传递请求体
                .build();

        //enqueue自动开启一个子线程
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("6666", "onFailure: ");
                handler.sendEmptyMessage(DEFEAT);
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //respond.body().toString()这个只能用一次 序列化一次
                if (response.isSuccessful()) {//回调的方法执行在子线程。
                    Log.d("66667", "onResponse:成功上传分享 ");
                    Gson gson2 = new Gson();
                    defultBean = gson2.fromJson(response.body().string(), DefultBean.class);
                    //子线程返回数据到主线程
//                    Message msg = new Message();
//                    msg.obj = defultBean;
//                    handler.sendMessage(msg);
                    handler.sendEmptyMessage(POST_SAVE_SHARE);
                }
            }
        });
    }

    @Override      //接收返回的地址
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainResult(data);
            Path findpath = new Path();
            for (int i = 0; i < mSelected.size(); i++) {
                path = findpath.getFileAbsolutePath(getActivity(), mSelected.get(i).normalizeScheme());
                File file1 = new File(path);
                file.add(file1);
                Log.d("666666", "mSelected: " + mSelected.get(i).normalizeScheme() + mSelected.size());
            }

            if (cheak()) {
                postfile();
            } else {
                Toast.makeText(getActivity(), "请填写完整的信息", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //从云端同步下将要分享的信息
    private void getShare() {
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
        headers = new Headers.Builder()
                .add("appId", appId)
                .add("appSecret", appSecret)
                .add("Content-Type", "application/json")
                .build();
        Request request = new Request.Builder()//创建Request 对象。
                .url("http://47.107.52.7:88/member/photo/share/save?size=100&userId=" + userId)
                .headers(headers)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("6666", "onFailure: ");
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {//回调的方法执行在子线程。
                    Gson gson2 = new Gson();
                    //Log.d("6666", "onResponse: " + response.body().string());
                    findBean = gson2.fromJson(response.body().string(), FindBean.class);
                    //Log.d(TAG, "onResponse: "+response.body().string());
                    list = findBean.getData().getRecords();
                    Log.d(TAG, "onResponse:findBean.getData().getRecords().size() " + findBean.getData().getRecords().size());
                    Log.d(TAG, "onResponse:list.size() " + list.size());
                    handler.sendEmptyMessage(GET_SAVE_SHARE);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sharedphoto:
                openAlbum();
                break;
            case R.id.clear:
                editClear();
                uploadBean = null;
                break;
            case R.id.saveShare:
                if (cheak()) {
                    postSaveShare();
                } else {
                    Toast.makeText(getActivity(), "请填写完整的信息", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.shardup:
                if (cheak() || uploadBean != null) {
                    postShare();
                    editClear();
                    uploadBean = null;
                } else {
                    Toast.makeText(getActivity(), "请填写完整的信息", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.getShare:
                getShare();
                break;
            default:
                break;
        }
    }

    //清空
    private void editClear() {
        edTextContent.setText("");
        edTextTitle.setText("");
        file.clear();
    }

    private boolean cheak() {
        if (edTextTitle.getText().toString().trim() == null || edTextContent.getText().toString().trim() == null || file.isEmpty())
            return false;
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(getActivity(), "你拒绝了该权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private void GetUserId() {
        String spFileName = getResources()
                .getString(R.string.shared_preferences_file_name);
        String userIdKey = getResources()
                .getString(R.string.user_id);
        SharedPreferences spFile = getActivity().getSharedPreferences(
                spFileName,
                MODE_PRIVATE);

        userId = spFile.getString(userIdKey, null);
        Log.d(TAG, "onActivityCreated_userId: " + userId);
    }

    //打开相册
    private void openAlbum() {
        Matisse.from(getActivity())
                .choose(MimeType.ofImage())
                .countable(true)
                .maxSelectable(9)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .thumbnailScale(0.85f)
                .showPreview(false)
                .theme(com.zhihu.matisse.R.style.Matisse_Zhihu)
                //Glide加载方式
                .imageEngine(new GlideEngine())
                .forResult(REQUEST_CODE_CHOOSE);
    }

}