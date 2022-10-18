package com.example.finalwork;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.finalwork.Utils.App;
import com.example.finalwork.bean.CommentBean;


import java.security.AccessControlContext;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;

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
