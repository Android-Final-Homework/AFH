package com.example.finalwork;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.finalwork.bean.User;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


public class shareFragment extends Fragment {

    ShareAdapter shareAdapter;
    List<ShareItem> data;
    SwipeRefreshLayout swipeRefreshLayout;
    Boolean isRefresh = false;
    public shareFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkNeedPermissions();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_share, container, false);
        swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!isRefresh) {
                    isRefresh = true;
                    data = new ArrayList<ShareItem>();
                    BmobQuery<ShareItem> query = new BmobQuery<ShareItem>();
                    query.order("-createdAt");
                    query.include("user");
                    query.findObjects(new FindListener<ShareItem>() {
                        @Override
                        public void done(List<ShareItem> list, BmobException e) {
                            if (e == null) {
                                for (ShareItem ll : list) {

                                    Log.d("shareFragment", "" + ll);

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
                                                Toast.makeText(getContext(), "失败", Toast.LENGTH_SHORT).show();
                                            }


                                        }
                                    });
                                    data.add(ll);

                                }
                                if (shareAdapter != null) {
                                    shareAdapter.notifyDataSetChanged();
                                }

                            } else {
                                Toast.makeText(getContext(), "数据加载失败：" + e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });


                    shareAdapter = new ShareAdapter(getActivity(), R.layout.share_list_item, data);
                    ListView myShareList = rootView.findViewById(R.id.lv_fragment_list);
                    myShareList.setAdapter(shareAdapter);
                    swipeRefreshLayout.setRefreshing(false);
                    isRefresh = false;
                }
            }
        });
        data = new ArrayList<ShareItem>();
        BmobQuery<ShareItem> query = new BmobQuery<ShareItem>();
        query.order("-createdAt");
        query.include("user");
        query.findObjects(new FindListener<ShareItem>() {
            @Override
            public void done(List<ShareItem> list, BmobException e) {
                if (e == null) {
                    for (ShareItem ll : list) {

                        Log.d("shareFragment", "" + ll);

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
                                    Toast.makeText(getContext(), "失败", Toast.LENGTH_SHORT).show();
                                }


                            }
                        });
                        data.add(ll);

                    }
                    if (shareAdapter != null) {
                        shareAdapter.notifyDataSetChanged();
                    }

                } else {
                    Toast.makeText(getContext(), "数据加载失败：" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });


        shareAdapter = new ShareAdapter(getActivity(), R.layout.share_list_item, data);
        ListView myShareList = rootView.findViewById(R.id.lv_fragment_list);
        myShareList.setAdapter(shareAdapter);


        return rootView;


    }


    private void checkNeedPermissions() {
        //6.0以上需要动态申请权限
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //多个权限一起申请
            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, 1);
        }
    }


}