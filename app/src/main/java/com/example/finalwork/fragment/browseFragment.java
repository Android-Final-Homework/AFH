package com.example.finalwork.fragment;

import static android.content.Context.MODE_PRIVATE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.finalwork.R;
import com.example.finalwork.RecfindAdapter;
import com.example.finalwork.Utils.App;
import com.example.finalwork.Utils.User;
import com.example.finalwork.Utils.UserInfoSPTool;
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

public class browseFragment extends Fragment {
    FloatingActionButton floatingActionButton;
    RecyclerView mRecyclerView;
    RecfindAdapter recfindAdapter;
    private MediaType MEDIA_TYPE_JSON;

    App app = new App();
    private String appId = app.getAppId();
    private String appSecret = app.getAppSecret();

    private SharedPreferences sharedPreferences;
    private UserInfoSPTool usp;


    String userId;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Headers headers;
    private ArrayList<FindBean.DataBean.RecordsBean> recordslocalList = new ArrayList<>();

    public browseFragment() {

    }

    public static browseFragment newInstance(String param1, String param2) {
        browseFragment fragment = new browseFragment();
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
        sharedPreferences = getActivity().getSharedPreferences("userInfo", MODE_PRIVATE);
        usp = new UserInfoSPTool(sharedPreferences);
        User user = usp.getInfo();
        Log.d("6666", "onCreate111: " + user.getUserId().toString());


        MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
        headers = new Headers.Builder()
                .add("appId", appId)
                .add("appSecret", appSecret)
                .add("Content-Type", "application/json")
                .build();

        GetUserId();
    }

    public View onCreateView(@Nullable LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_browse, container, false);
        GetUserId();
        browse();
        floatingActionButton = root.findViewById(R.id.floatingActionButton);

        recfindAdapter = new RecfindAdapter(getContext(), recordslocalList, userId);
        mRecyclerView = root.findViewById(R.id.recylerview);
        mRecyclerView.setAdapter(recfindAdapter);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

        //刷新按钮
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "刷新列表...", Toast.LENGTH_LONG).show();
                browse();
            }

        });
        return root;
    }

    private void browse() {
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        Request request = new Request.Builder()//创建Request 对象。
                .url("http://47.107.52.7:88/member/photo/share?size=100&userId=" + userId)
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
                    FindBean findBean = gson2.fromJson(response.body().string(), FindBean.class);
                    List<FindBean.DataBean.RecordsBean> list = findBean.getData().getRecords();
                    recordslocalList.clear();
                    recordslocalList.addAll(list);
                    Message msg = new Message();
                    msg.obj = list;
                    handler.sendMessage(msg);
                }
            }
        });
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
        Log.d("6666", "onActivityCreated: " + userId);
    }
}