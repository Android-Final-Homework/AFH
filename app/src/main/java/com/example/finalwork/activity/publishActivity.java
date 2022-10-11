package com.example.finalwork.activity;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.finalwork.Path;
import com.example.finalwork.R;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class publishActivity extends Fragment implements View.OnClickListener {
    private static final int REQUEST_CODE_CHOOSE = 23;
    EditText text;
    TextView picture;
    ImageView imageView;
    Button b2, b3;
    String textsh;
    private List<Uri> mSelected;
    private static final int REQUEST_CODE = 1;
    private static final int REQUEST_FILE_CODE = 1000;
    String path="";
    List<String> result= new ArrayList<>();
    public static final int CHOOSE_PHOTO = 2;
    private List<File> file = new ArrayList<>();


    public View onCreateView(@Nullable LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_publish, container, false);
        text = root.findViewById(R.id.sharedtext);
        textsh = text.getText().toString().trim();
        picture = root.findViewById(R.id.sharedphoto);
        b2 = root.findViewById(R.id.deletephoto);
        b3 = root.findViewById(R.id.shardup);
        imageView=root.findViewById(R.id.shareimg);
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
        b2.setOnClickListener(this);//取消
        b3.setOnClickListener(this);//发布
    }
    //获取权限
    private void requestPermissino() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {

        }

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
                .theme(R.style.Matisse_Zhihu)
                //Glide加载方式
                .imageEngine(new GlideEngine())
                .forResult(REQUEST_CODE_CHOOSE);

//        Intent intent = new Intent("android.intent.action.GET_CONTENT");
//        intent.setType("image/*");
//        startActivityForResult(intent, CHOOSE_PHOTO); //打开相册
    }

    private void postfile() {
        Headers headers = new Headers.Builder()
                .add("appId", "d31493f0f333452da51ae927ce3c0ef8")
                .add("appSecret", "74729906f6996b3b943f28102db5822006f97")
                .add("Accept", "application/json, text/plain, */*")
                .build();

        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("fileList", file.get(0).getName(),
                        RequestBody.create(MediaType.parse("multipart/form-data"), file.get(0)))
                .build();
        Request request = new Request.Builder()//创建Request 对象。
                .url("http://47.107.52.7:88/member/photo/image/upload")
                .headers(headers)
                .post(requestBody)
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
                    Log.d("66667", "postfile: " + response.body().string());
                }
            }
        });
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

    @Override      //接收返回的地址
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
            mSelected = Matisse.obtainResult(data);
            Path findpath=new Path();
            path=findpath.getFileAbsolutePath(getActivity(),mSelected.get(0).normalizeScheme());
            File file1 = new File(path);
            file.add(file1);
            Log.d("666666", "mSelected: " + mSelected.get(0).normalizeScheme() + mSelected.size());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sharedphoto:
                openAlbum();
                break;
            case R.id.decelerate:
                text.setText("");
                break;
            case R.id.shardup:
                postfile();
                break;

        }

    }




    public  void OpenFile() {
        // 指定类型
        String[] mimeTypes = {"image/*"};
        // String[] mimeTypes = {"application/octet-stream"}; // 指定bin类型
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        StringBuilder mimeTypesStr = new StringBuilder();
        for (String mimeType : mimeTypes) {
            mimeTypesStr.append(mimeType).append("|");
        }
        intent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
        startActivityForResult(Intent.createChooser(intent, "ChooseFile"), REQUEST_FILE_CODE);
    }

    // 获取文件的真实路径
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_FILE_CODE && resultCode == RESULT_OK) {
//            uri = data.getData();
//            Toast.makeText(getActivity(), "uri:"+uri, Toast.LENGTH_SHORT).show();
////            Toast.makeText(this, getPath(this,uri), Toast.LENGTH_SHORT).show();
//            Path findpath=new Path();
////            Toast.makeText(this, pickUtils.getPath(this,uri), Toast.LENGTH_SHORT).show();
////            Toast.makeText(this, uri.getPath(), Toast.LENGTH_SHORT).show();
//            path=findpath.getFileAbsolutePath(getActivity(),uri);
//            /*textView.setText(path);*/
//            Glide.with(getActivity()).load(path).skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).into(imageView);
//
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }

    /**
     * 获取文件名
     * @param uri uri
     * @return
     */
    public String getFileNameByUri(Uri uri) {
        String filename = "";
        Cursor returnCursor = getContext().getContentResolver().query(uri, null,
                null, null, null);
        if (returnCursor != null) {
            int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            returnCursor.moveToFirst();
            filename = returnCursor.getString(nameIndex);
            returnCursor.close();
        }
        return filename;
    }
}