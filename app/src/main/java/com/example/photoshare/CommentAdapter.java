package com.example.photoshare;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.example.photoshare.Utils.App;
import com.example.photoshare.activity.PersonDetail;
import com.example.photoshare.bean.CommentBean;
import com.example.photoshare.bean.DefultBean;
import com.example.photoshare.bean.FindBean;
import com.example.photoshare.bean.LoginBean;
import com.example.photoshare.bean.MyDetailBean;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;


import java.io.IOException;
import java.io.Serializable;
import java.security.AccessControlContext;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ItemViewHolder> {

    private AccessControlContext context;

    private MediaType MEDIA_TYPE_JSON;

    App app = new App();
    private String appId = app.getAppId();
    private String appSecret = app.getAppSecret();

    private List<CommentBean.DataBean> showList = new ArrayList<>();
    String userId;

    public CommentAdapter(AccessControlContext context, List<CommentBean.DataBean> showList, String userId) {
        this.context = context;
        this.showList = showList;
        this.userId = userId;

    }

    // 创建列表组件
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, null);

        return new ItemViewHolder(view, this);
    }

    // 绑定数据
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        CommentBean.DataBean shows = showList.get(position);
        if(shows.getRecords().isEmpty())
        {
            holder.tvTitle.setText(shows.getRecords().get(0).getContent());
        }
        else
        {
            holder.tvTitle.setText("评论区空空如也~");
        }

        //holder.tvTitle=shows.get
    }

    // 返回列表总数
    @Override
    public int getItemCount() {
        return showList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

//        RoundedImageView ivHead;//头像

        TextView tvTitle;

        private RecyclerView.Adapter adapter;


        public ItemViewHolder(@NonNull View itemView, RecyclerView.Adapter adapter) {
            super(itemView);
            //this.ivHead = itemView.findViewById(R.id.iv_head);
            this.tvTitle = itemView.findViewById(R.id.item_content);

        }
    }
}
