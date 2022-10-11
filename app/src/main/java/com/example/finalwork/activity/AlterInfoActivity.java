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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.finalwork.R;
import com.example.finalwork.bean.User;

import java.io.File;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FetchUserInfoListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class AlterInfoActivity extends AppCompatActivity {


    BmobFile headPictureFile;
    private static int RESULT_LOAD_IMAGE = 1;
    private static String TAG = "AlterInfoActivity";
    String headPicturePath;
    ImageView ivHeadPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alter_info);
        setTitle("修改信息");
        Bmob.initialize(this, "e6736733aa1b7ebe0f8ffc79718d3773");

        TextView tvAccount = findViewById(R.id.account);
        TextView tvNickname = findViewById(R.id.nickname);
        Button btAlter = findViewById(R.id.bt_alter);
        ivHeadPicture = findViewById(R.id.headpicture);

        User currentUser = BmobUser.getCurrentUser(User.class);
        tvAccount.setText(currentUser.getUsername());
        tvNickname.setText(currentUser.getNickname());


        ivHeadPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(AlterInfoActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AlterInfoActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                } else {
                    selectHeadPicture();

                }
            }
        });


        btAlter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User();
                user.setObjectId(currentUser.getObjectId());
                if (tvNickname.getText().toString().isEmpty()) {
                    Toast.makeText(AlterInfoActivity.this, "昵称为空！", Toast.LENGTH_SHORT).show();
                } else {
                    user.setNickname(tvNickname.getText().toString());
                    if (headPictureFile != null) {
                        user.setHeadpicture(headPictureFile);
                    }
                    user.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            Toast.makeText(AlterInfoActivity.this, "修改成功！", Toast.LENGTH_SHORT).show();

                            BmobUser.fetchUserInfo(new FetchUserInfoListener<User>() {
                                @Override
                                public void done(User user, BmobException e) {
                                    if (e == null) {
                                        final User bUser = BmobUser.getCurrentUser(User.class);
                                        Log.d("AlterInfoActivity",bUser.getNickname());
                                    } else {
                                        Log.e("AlterInfoActivity",e.getMessage());
                                    }
                                }
                            });
                            finish();

                            Intent intent = new Intent(AlterInfoActivity.this, MainActivity.class);
                            startActivityForResult(intent,0);
                        }
                    });

                }

            }
        });


    }


    void selectHeadPicture() {
        Intent intent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent, RESULT_LOAD_IMAGE);
    }

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
            headPicturePath = cursor.getString(columnIndex);
            cursor.close();
            Log.d(TAG, "" + headPicturePath);
//
            ivHeadPicture.setImageBitmap(BitmapFactory.decodeFile(headPicturePath));

            headPictureFile = new BmobFile(new File(headPicturePath));
            headPictureFile.uploadblock(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        //bmobFile.getFileUFrl()--返回的上传文件的完整地址
                        Log.w("bbb", headPictureFile.getFileUrl());
                        Toast.makeText(AlterInfoActivity.this, "头像上传成功!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AlterInfoActivity.this, "头像上传失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            });


        }

    }
}