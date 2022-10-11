package com.example.finalwork;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.finalwork.bean.FindBean;

import java.util.ArrayList;
import java.util.List;

public class RecfindAdapter extends RecyclerView.Adapter<RecfindAdapter.ItemViewHolder> {

    private Context context;

    private List<FindBean.DataBean.RecordsBean> showList = new ArrayList<>();

    public RecfindAdapter(Context context, List<FindBean.DataBean.RecordsBean> showList) {
        this.context = context;
        this.showList = showList;
    }

    // 创建列表组件
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.share_list_item, null);
        return new ItemViewHolder(view, this);
    }

    // 绑定数据
    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        FindBean.DataBean.RecordsBean shows = showList.get(position);
        Glide.with(context).load(shows.getImageUrlList().get(0)).into(holder.ivPicture);
        holder.tvAuthor.setText(shows.getContent());
        holder.tvTitle.setText(shows.getTitle());
//        holder.ivLove.
    }

    // 返回列表总数
    @Override
    public int getItemCount() {
        return showList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView ivPicture;

        TextView tvTitle;

        TextView tvAuthor;

        ImageView ivLove;

        TextView tvLike;

        private RecyclerView.Adapter adapter;


        public ItemViewHolder(@NonNull View itemView, RecyclerView.Adapter adapter) {
            super(itemView);
            this.ivPicture = itemView.findViewById(R.id.iv_image);
            this.tvTitle = itemView.findViewById(R.id.tv_content);
            this.tvAuthor = itemView.findViewById(R.id.tv_nickname);
            this.ivLove = itemView.findViewById(R.id.iv_like);
            this.adapter = adapter;
        }
    }
}
