package com.example.finalwork.activity;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.finalwork.R;
import com.example.finalwork.ShareAdapter;
import com.example.finalwork.ShareItem;
import com.example.finalwork.bean.User;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MyShareActivity extends AppCompatActivity {

    private static String TAG = "MyShareActivity";
    ShareAdapter shareAdapter;
    List<ShareItem> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_share);
        setTitle("我发布的");
        data = new ArrayList<ShareItem>();
        BmobQuery<ShareItem> query = new BmobQuery<ShareItem>();
        query.order("-createdAt");
        query.addWhereEqualTo("user", BmobUser.getCurrentUser(User.class));
        query.include("user");
        query.findObjects(new FindListener<ShareItem>() {
            @Override
            public void done(List<ShareItem> list, BmobException e) {
                if (e == null) {
//                    for (ShareItem ad : list) {
//                        _User currentUser = BmobUser.getCurrentUser(_User.class);
//                        if (ad.getUsername() != null && currentUser.getUsername().equals(ad.getUsername())) {
//                            data.add(ad);
//                            Log.d(TAG, "" + ad.getSharePicture().getUrl());
//                        }
//
//                    }
                    for (ShareItem ll : list) {
                        BmobQuery<User> query = new BmobQuery<User>();
                        query.addWhereRelatedTo("likesUser", new BmobPointer(ll));
                        query.findObjects(new FindListener<User>() {
                            @Override
                            public void done(List<User> list, BmobException e) {

                                if (e == null) {
                                    ll.setLikes(list.size());
                                    ll.setLikeState(false);
                                    for (User u : list) {
                                        if (u.getObjectId().equals(BmobUser.getCurrentUser(User.class).getObjectId())) {
                                            ll.setLikeState(true);
                                            break;
                                        }
                                    }
                                    shareAdapter.notifyDataSetChanged();
                                } else {
                                    Toast.makeText(MyShareActivity.this, "失败", Toast.LENGTH_SHORT).show();
                                }


                            }
                        });
                        data.add(ll);
                    }
                    if (shareAdapter != null) {
                        shareAdapter.notifyDataSetChanged();
                    }

                } else {
                    Toast.makeText(MyShareActivity.this, "获取失败：" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        shareAdapter = new ShareAdapter(MyShareActivity.this, R.layout.share_list_item, data);
        ListView myShareList = findViewById(R.id.lv_myshare_list);
        myShareList.setAdapter(shareAdapter);

    }
}