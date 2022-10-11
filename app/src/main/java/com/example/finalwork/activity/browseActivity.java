package com.example.finalwork.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.finalwork.R;
import com.example.finalwork.RecfindAdapter;
import com.example.finalwork.bean.FindBean;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class browseActivity extends Fragment {
    FloatingActionButton button;
    RecyclerView mRecyclerView;
    RecfindAdapter recfindAdapter ;
    private MediaType MEDIA_TYPE_JSON;
    List<FindBean.DataBean.RecordsBean> mNewsList = new ArrayList<>();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Headers headers;
    private ArrayList<FindBean.DataBean.RecordsBean> recordslocalList = new ArrayList<>();
    public browseActivity(){

    }

    public static browseActivity newInstance(String param1, String param2) {
        browseActivity fragment = new browseActivity();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    Handler handler = new Handler(Looper.getMainLooper()) {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(@NonNull Message msg) {
            recfindAdapter.notifyDataSetChanged();
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
        headers = new Headers.Builder()
                .add("appId", "d31493f0f333452da51ae927ce3c0ef8")
                .add("appSecret", "74729906f6996b3b943f28102db5822006f97")
                .add("Content-Type","application/json")
                .build();

    }

    public View onCreateView(@Nullable LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        View root=inflater.inflate(R.layout.activity_browse,container,false);
        find();
        recfindAdapter = new RecfindAdapter(getContext(),recordslocalList);
        mRecyclerView = root.findViewById(R.id.recylerview);
        mRecyclerView.setAdapter(recfindAdapter);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        return root;
    }

    private void find() {
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        Request request = new Request.Builder()//创建Request 对象。
                .url("http://47.107.52.7:88/member/photo/share?size=100&userId=1578270906067849216")
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
                    FindBean findBean = gson2.fromJson(response.body().string(),FindBean.class);
                    List<FindBean.DataBean.RecordsBean> list = findBean.getData().getRecords();
                    Log.d("6666", "onResponse: " + list.get(0).getContent());
                    recordslocalList.addAll(list);
                    Message msg = new Message();
                    msg.obj = list;
                    handler.sendMessage(msg);
                }
            }
        });

    }

}