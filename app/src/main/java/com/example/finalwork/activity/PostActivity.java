package com.example.finalwork.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.finalwork.R;
import com.example.finalwork.ShareItem;
import com.example.finalwork.bean.User;

import java.io.File;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

public class PostActivity extends AppCompatActivity {

    EditText etPicContent;//分享内容
    ImageView ivSharePicture;//分享图片
    Button btPost;//发布按钮

    private static int RESULT_LOAD_IMAGE = 1;
    private static String TAG = "PostActivity";
    String picturePath;//要分享图片的路径
    BmobFile pictureFile;//要分享的图片
    BmobFile headpictureFile;//头像

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        setTitle("发布分享");
        Bmob.initialize(this, "e6736733aa1b7ebe0f8ffc79718d3773");

        etPicContent = findViewById(R.id.picContent);
        ivSharePicture = findViewById(R.id.sharePicture);
        btPost = findViewById(R.id.bt_post);


        ivSharePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(PostActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(PostActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                } else {
                    selectHeadPicture();

                }


            }
        });

        //点击分享按钮
        btPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String picContent = etPicContent.getText().toString();
                if(picContent.isEmpty()){
                    Toast.makeText(PostActivity.this, "请输入分享内容！", Toast.LENGTH_SHORT).show();
                }else if(pictureFile ==null){
                    Toast.makeText(PostActivity.this, "请选择图片！", Toast.LENGTH_SHORT).show();
                }else{            //TODO
                    //获取当前登录用户的信息和要分享的图片及内容
                    User currentUser = BmobUser.getCurrentUser(User.class);
//                    String nickname = currentUser.getNickname();
//                    String username = currentUser.getUsername();
                    headpictureFile = currentUser.getHeadpicture();


//                BmobFile headpicture = currentUser.getHeadpicture();
                    ShareItem shareItem = new ShareItem();
                    shareItem.setUser(currentUser);
//                    shareItem.setNickname(nickname);
//                    shareItem.setUsername(username);
                    shareItem.setPicContent(picContent);
//                    shareItem.setHeadPicture(headpictureFile);
                    shareItem.setSharePicture(pictureFile);
                    shareItem.setLikes(0);
//                    Log.d(TAG,nickname);

                    shareItem.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if(e==null){
                                Toast.makeText(PostActivity.this, "分享成功!", Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                Toast.makeText(PostActivity.this, "分享失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });}

                Intent intent = new Intent(PostActivity.this, MainActivity.class);
                startActivityForResult(intent,0);

            }
        });


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    selectHeadPicture();
                } else {
                    Toast.makeText(this, "你的权限不足！", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            //查询我们需要的数据
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);
            cursor.close();
            Log.d(TAG, "" + picturePath);
//
            ivSharePicture.setImageBitmap(BitmapFactory.decodeFile(picturePath));

            pictureFile = new BmobFile(new File(picturePath));
            pictureFile.uploadblock(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        //bmobFile.getFileUrl()--返回的上传文件的完整地址
                        Log.w("bbb", pictureFile.getFileUrl());
                        Toast.makeText(PostActivity.this, "图片上传成功!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(PostActivity.this, "图片上传失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            });


        }

    }


    void selectHeadPicture() {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent, RESULT_LOAD_IMAGE);
    }


}