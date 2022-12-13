package com.example.finalwork;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.finalwork.Utils.App;
import com.example.finalwork.activity.PersonDetail;
import com.example.finalwork.bean.DefultBean;
import com.example.finalwork.bean.FindBean;
import com.example.finalwork.bean.MyDetailBean;
import com.example.finalwork.R;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.IOException;
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

public class RecfindAdapter_mynews extends RecyclerView.Adapter<RecfindAdapter_mynews.ItemViewHolder> {

    private Context context;

    private static final int DEFEAT = 0;
    private static final int POST_LIKE = 1;
    private static final int POST_UNLIKE = 2;
    private static final int GET_LIST = 3;
    private static final int POST_COLLECT = 4;
    private static final int POST_UNCOLLECT = 5;

    private MediaType MEDIA_TYPE_JSON;

    App app = new App();
    private String appId = app.getAppId();
    private String appSecret = app.getAppSecret();

    private List<FindBean.DataBean.RecordsBean> showList = new ArrayList<>();
    String shareUserId1;
    String userId;
    String likeId;
    String collectId;
    String usename;

    int temp_like = -10, temp_collect = -10;

    public RecfindAdapter_mynews(Context context, List<FindBean.DataBean.RecordsBean> showList, String userId) {
        this.context = context;
        this.showList = showList;
        this.userId = userId;

    }

    // 创建列表组件
    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.share_list_item, null);
        browse(1);
        Log.d("6666", "onCreateViewHolder:更新 ");
        return new ItemViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        FindBean.DataBean.RecordsBean shows = showList.get(position);

        if (!shows.getImageUrlList().isEmpty())
            Glide.with(context).load(shows.getImageUrlList().get(0)).into(holder.ivPicture);
        else
            Glide.with(context).load("https://guet-lab.oss-cn-hangzhou.aliyuncs.com/api/2022/10/12/ce5fc795-775e-4513-8a0f-a2841dcc2cd9.jpg").into(holder.ivPicture);

        holder.tvTitle.setText(shows.getTitle());
        holder.tvContent.setText(shows.getContent());
        holder.tvTime.setText(timeStamp2Date(shows.getCreateTime(), "yyyy-MM-dd"));

        holder.tvLikeNum.setVisibility(View.INVISIBLE);

        holder.ivLike.setVisibility(View.INVISIBLE);//爱心

        holder.tvCollectNum.setVisibility(View.INVISIBLE);

        holder.ivCollect.setVisibility(View.INVISIBLE);//收藏

        holder.ivShare.setVisibility(View.INVISIBLE);
        Log.d("6666", "onBindViewHolde改变数据: " + Integer.toString(shows.getCollectNum()));
        shareUserId1 = showList.get(position).getId();
        likeId = (String) showList.get(position).getLikeId();
        usename = showList.get(position).getUsername();
        //初始化
        temp_like = -10;
        temp_collect = -10;
//        holder.ivLove.
        if (shows.isHasLike()) {
            holder.ivLike.setImageResource(R.drawable.like3);
        } else {
            holder.ivLike.setImageResource(R.drawable.dislike3);
        }

        if (shows.isHasCollect()) {
            holder.ivCollect.setImageResource(R.drawable.collect2);
        } else {
            holder.ivCollect.setImageResource(R.drawable.collect);
        }

        holder.ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = view.getVerticalScrollbarPosition();
                shareUserId1 = showList.get(pos).getId();
                if (showList.get(pos) != null)
                    likeId = (String) showList.get(pos).getLikeId();
                if (shows.isHasLike()) {
                    if (shareUserId1 != null) {
                        PostUnLike();
                        holder.ivLike.setImageResource(R.drawable.dislike3);
                        holder.tvLikeNum.setText(String.valueOf(Integer.valueOf(holder.tvLikeNum.getText().toString()) - 1));
                    }
                } else {
                    if (shareUserId1 != null) {
                        PostLike();
                        holder.ivLike.setImageResource(R.drawable.like3);
                        holder.tvLikeNum.setText(String.valueOf(Integer.valueOf(holder.tvLikeNum.getText().toString()) + 1));

                    }
                }
            }
        });

        holder.ivCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = view.getVerticalScrollbarPosition();
                shareUserId1 = showList.get(pos).getId();
                if (showList.get(pos) != null)
                    collectId = (String) showList.get(pos).getCollectId();
                if (shows.isHasCollect()) {
                    if (shareUserId1 != null) {
                        PostUnCollect();
                        holder.ivCollect.setImageResource(R.drawable.collect);
                        holder.tvCollectNum.setText(String.valueOf(Integer.valueOf(holder.tvCollectNum.getText().toString()) - 1));
                    }
                } else {
                    if (shareUserId1 != null) {
                        PostCollect();
                        holder.ivCollect.setImageResource(R.drawable.collect2);
                        holder.tvCollectNum.setText(String.valueOf(Integer.valueOf(holder.tvCollectNum.getText().toString()) + 1));

                    }
                }
            }
        });

        //内容界面，点击跳转详情
        holder.tvContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> list = null;
                list = shows.getImageUrlList();
                if (shows.getImageUrlList().size() != 0) {
                    list = shows.getImageUrlList();
                }

                Intent intent;//用户的数据
                MyDetailBean mydetail = new MyDetailBean(userId, shareUserId1, list, holder.tvContent.getText().toString(), holder.tvTitle.getText().toString(), userId, shows.isHasFocus());

                intent = new Intent(context, PersonDetail.class);

                intent.putExtra("detail", mydetail);
                context.startActivity(intent);
            }
        });

        holder.ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "敬请期待~", Toast.LENGTH_SHORT).show();
            }
        });

    }

    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GET_LIST:

                    break;
                case POST_LIKE:
                    //PostLike_2();
                    //browse(100);
                    break;
                case POST_UNLIKE:
                    //PostUnLike_2();
                    break;
                case DEFEAT:
                    break;
                case 10:
                    //browse(100);
                default:
                    break;
            }
        }
    };


    private void PostLike() {
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
        Headers headers = new Headers.Builder()
                .add("Accept", "application/json, text/plain, */*")
                .add("appId", appId)
                .add("appSecret", appSecret)
                .add("Content-Type", "application/json")
                .build();

        Request request = new Request.Builder()//创建Request 对象。
                .url("http://47.107.52.7:88/member/photo/like?shareId=" + shareUserId1 + "&userId=" + userId)
                .headers(headers)
                .post(RequestBody.create(MEDIA_TYPE_JSON, ""))//传递请求体
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("6666", "onFailure: ");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {//回调的方法执行在子线程。
                    Gson gson2 = new Gson();
                    //defultBean = gson2.fromJson(response.body().string(), DefultBean.class);
                    Log.d("6666", "onResponse: 点赞上传" + response.body().string());

                    handler.sendEmptyMessage(10);
                }
            }
        });
    }

    private void PostUnLike() {

        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
//        formBody.add("username","");//传递键值对参数
        Headers headers = new Headers.Builder()
                .add("Accept", "application/json, text/plain, */*")
                .add("appId", appId)
                .add("appSecret", appSecret)
                .add("Content-Type", "application/json")
                .build();

        Request request = new Request.Builder()//创建Request 对象。
                .url("http://47.107.52.7:88/member/photo/like/cancel?likeId=" + likeId)
                .headers(headers)
                .post(RequestBody.create(MEDIA_TYPE_JSON, ""))//传递请求体
                .build();
        Log.d("6666", "PostUnLike: " + likeId);
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("6666", "onFailure: ");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {//回调的方法执行在子线程。
                    Gson gson2 = new Gson();
                    //defultBean = gson2.fromJson(response.body().string(), DefultBean.class);
                    Log.d("6666", "onResponse: 取消点赞" + response.body().string());

                    handler.sendEmptyMessage(10);
                }
            }
        });
    }


    private void PostCollect() {
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
//        formBody.add("username","");//传递键值对参数
        Headers headers = new Headers.Builder()
                .add("Accept", "application/json, text/plain, */*")
                .add("appId", appId)
                .add("appSecret", appSecret)
                .add("Content-Type", "application/json")
                .build();

        Request request = new Request.Builder()//创建Request 对象。
                .url("http://47.107.52.7:88/member/photo/collect?shareId=" + shareUserId1 + "&userId=" + userId)
                .headers(headers)
                .post(RequestBody.create(MEDIA_TYPE_JSON, ""))//传递请求体
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("6666", "onFailure: ");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {//回调的方法执行在子线程。
                    //Gson gson2 = new Gson();
                    //defultBean = gson2.fromJson(response.body().string(), DefultBean.class);

                    Log.d("6666", "onResponse: 收藏上传" + response.body().string());

                    handler.sendEmptyMessage(POST_COLLECT);
                }
            }
        });

    }

    private void PostUnCollect() {
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
        MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
        FormBody.Builder formBody = new FormBody.Builder();//创建表单请求体
//        formBody.add("username","");//传递键值对参数
        Headers headers = new Headers.Builder()
                .add("Accept", "application/json, text/plain, */*")
                .add("appId", appId)
                .add("appSecret", appSecret)
                .add("Content-Type", "application/json")
                .build();

        Request request = new Request.Builder()//创建Request 对象。
                .url("http://47.107.52.7:88/member/photo/collect/cancel?collectId=" + collectId)
                .headers(headers)
                .post(RequestBody.create(MEDIA_TYPE_JSON, ""))//传递请求体
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("6666", "onFailure: ");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {//回调的方法执行在子线程。
                    //Gson gson2 = new Gson();
                    //defultBean = gson2.fromJson(response.body().string(), DefultBean.class);
                    Log.d("6666", "onResponse: " + response.body().string());
                    Log.d("6666", "onResponse: 取消收藏");

                    handler.sendEmptyMessage(POST_UNCOLLECT);
                }
            }
        });

    }


    private void browse(int i) {

        MEDIA_TYPE_JSON = MediaType.parse("application/json; charset=utf-8");
        Headers headers = new Headers.Builder()
                .add("appId", appId)
                .add("appSecret", appSecret)
                .add("Content-Type", "application/json")
                .build();

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
                handler.sendEmptyMessage(DEFEAT);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {//回调的方法执行在子线程。
                    Gson gson2 = new Gson();
                    //Log.d("6666", "onResponse: " + response.body().string());
                    FindBean findBean = gson2.fromJson(response.body().string(), FindBean.class);
                    List<FindBean.DataBean.RecordsBean> list = findBean.getData().getRecords();
                    showList.clear();
                    showList.addAll(list);
                    handler.sendEmptyMessage(i);
                }
            }
        });

    }

    // 返回列表总数
    @Override
    public int getItemCount() {
        return showList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        RoundedImageView ivHead;//头像

        ImageView ivPicture;

        TextView tvContent;

        TextView tvTime;//发布时间

        TextView tvTitle;

        TextView tvLikeNum;

        ImageView ivLike;//爱心

        TextView tvCollectNum;

        ImageView ivCollect;//收藏

        ImageView ivShare;


        private RecyclerView.Adapter adapter;


        public ItemViewHolder(@NonNull View itemView, RecyclerView.Adapter adapter) {
            super(itemView);
            this.ivHead = itemView.findViewById(R.id.iv_head);
            this.ivPicture = itemView.findViewById(R.id.iv_image);
            this.tvContent = itemView.findViewById(R.id.tv_content);
            this.tvTitle = itemView.findViewById(R.id.tv_nickname);
            this.tvTime = itemView.findViewById(R.id.tv_publish_date);
            this.tvLikeNum = itemView.findViewById(R.id.tv_likes);
            this.ivLike = itemView.findViewById(R.id.iv_like);
            this.tvCollectNum = itemView.findViewById(R.id.tv_collects);
            this.ivCollect = itemView.findViewById(R.id.iv_collect);
            this.ivShare = itemView.findViewById(R.id.iv_share);

            this.adapter = adapter;


        }
    }


    public static String timeStamp2Date(String seconds, String format) {
        if (seconds == null || seconds.isEmpty() || seconds.equals("null")) {
            return "";
        }
        if (format == null || format.isEmpty()) {
            format = "MM-dd";
        }
        //seconds=seconds.substring(0,seconds.length()-4);
        SimpleDateFormat sdf = new SimpleDateFormat(format);

        return sdf.format(new Date(Long.valueOf(seconds.substring(0, seconds.length() - 3) + "000")));
        //return sdf.format(new Date(Long.valueOf(seconds)));
    }

}
