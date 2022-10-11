package com.example.finalwork;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.finalwork.bean.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;


//适配器ShareAdapter
public class ShareAdapter extends ArrayAdapter<com.example.finalwork.ShareItem> {

    private List<com.example.finalwork.ShareItem> mData;
    private Context mContext;
    private int resourceId;
    private ImageView bigimg;

    public ShareAdapter(Context context,
                        int resourceId, List<com.example.finalwork.ShareItem> data) {
        super(context, resourceId, data);
        this.mContext = context;
        this.mData = data;
        this.resourceId = resourceId;
    }

    @Override
    public View getView(int position,
                        View convertView, ViewGroup parent) {
        com.example.finalwork.ShareItem share = getItem(position);
        View view;

        final ViewHolder vh;

        if (convertView == null) {
            view = LayoutInflater.from(getContext())
                    .inflate(resourceId, parent, false);

            vh = new ViewHolder();
            vh.tvNickname = view.findViewById(R.id.tv_nickname);
            vh.ivHead = view.findViewById(R.id.iv_head);
            vh.ivImage = view.findViewById(R.id.iv_image);
            vh.tvContent = view.findViewById(R.id.tv_content);
            vh.tvLikes = view.findViewById(R.id.tv_likes);
            vh.tvDate = view.findViewById(R.id.tv_publish_date);
            vh.ivSave = view.findViewById(R.id.iv_save);
            vh.ivShare = view.findViewById(R.id.iv_share);
            vh.ivLikes = view.findViewById(R.id.iv_like);
            view.setTag(vh);
        } else {
            view = convertView;
            vh = (ViewHolder) view.getTag();
        }

        User user = share.getUser();
        Log.d("shareAdapter", user.getNickname() + "," + user.getHeadpicture());
        vh.tvNickname.setText(user.getNickname());
        vh.tvContent.setText(share.getPicContent());
        vh.tvLikes.setText(share.getLikes().toString());
        if (share.getLikeState()) {
            vh.ivLikes.setImageResource(R.drawable.liked);
        }
        else{
            vh.ivLikes.setImageResource(R.drawable.like);
        }
        vh.tvDate.setText(share.getCreatedAt());
        Glide.with(mContext).load(user.getHeadpicture().getUrl())
                .into(vh.ivHead);
        Glide.with(mContext).load(share.getSharePicture().getUrl())
                .into(vh.ivImage);

        vh.ivLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 点赞
                Log.d("shareAdapter", "点赞：" + share.toString());

                if (share.getLikeState()) {

                    BmobRelation relation = new BmobRelation();
                    relation.remove(BmobUser.getCurrentUser(User.class));
                    share.setLikesUser(relation);
                    share.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                vh.ivLikes.setImageResource(R.drawable.like);
                                share.setLikes(share.getLikes()-1);
                                vh.tvLikes.setText(share.getLikes().toString());
                                share.setLikeState(false);
//                                Toast.makeText(getContext(), "取消赞", Toast.LENGTH_SHORT).show();
                            }else{
//                                Toast.makeText(getContext(), "操作失败", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                } else {
                    BmobRelation relation = new BmobRelation();
                    relation.add(BmobUser.getCurrentUser(User.class));
                    share.setLikesUser(relation);
                    share.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                vh.ivLikes.setImageResource(R.drawable.liked);
                                share.setLikeState(true);
                                share.setLikes(share.getLikes()+1);
                                vh.tvLikes.setText(share.getLikes().toString());
//                                Toast.makeText(getContext(), "赞", Toast.LENGTH_SHORT).show();
                            }else{
//                                Toast.makeText(getContext(), "操作失败", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });

        vh.ivSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 下载图片资源
                Log.d("shareAdapter", "下载：" + share.getSharePicture().getUrl());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final Bitmap b = Glide.with(getContext())
                                    .asBitmap()
                                    .load( share.getSharePicture().getFileUrl())
                                    .submit()
                                    .get();

                            vh.ivImage.post(new Runnable() {
                                @Override
                                public void run() {
//                                    Toast.makeText(getContext(), "图片像素:" + b.getWidth() + "x" + b.getHeight() , Toast.LENGTH_SHORT).show();
                                    saveImageToGallery(b);
                                }
                            });
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        });

        vh.ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 分享图片     getUrl()和getFileUrl()结果一样
                Log.d("shareAdapter", "分享：" + share.getSharePicture().getFileUrl());

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final Bitmap b = Glide.with(getContext())
                                    .asBitmap()
                                    .load( share.getSharePicture().getFileUrl())
                                    .submit()
                                    .get();

                            vh.ivImage.post(new Runnable() {
                                @Override
                                public void run() {
//                                    Toast.makeText(getContext(), "图片像素:" + b.getWidth() + "x" + b.getHeight(), Toast.LENGTH_SHORT).show();
                                    Uri uri =  Uri.parse(MediaStore.Images.Media.insertImage(mContext.getContentResolver(), b, null,null));
                                    Intent intent = new Intent(Intent.ACTION_SEND);
                                    intent.setType("image/png");
                                    intent.putExtra(Intent.EXTRA_STREAM,uri);
                                    mContext.startActivity(Intent.createChooser(intent,"图片分享"));
                                }
                            });
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();




            }
        });

        vh.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 查看大图片
                Log.d("shareAdapter", "查看大图：" + share.getSharePicture().getFileUrl());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final Bitmap b = Glide.with(getContext())
                                    .asBitmap()
                                    .load( share.getSharePicture().getFileUrl())
                                    .submit()
                                    .get();

                            vh.ivImage.post(new Runnable() {
                                @Override
                                public void run() {
                                    DialogImage dialogImage = new DialogImage(getContext(),0,-300,b);
                                    dialogImage.show();
                                    dialogImage.picture.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialogImage.dismiss();
                                            dialogImage.cancel();
                                        }
                                    });
                                }
                            });
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();


            }
        });


        return view;
    }

    public void savePicture(com.example.finalwork.ShareItem share){


    }

    class ViewHolder {

        TextView tvNickname;
        TextView tvContent;
        ImageView ivHead;
        ImageView ivImage;
        TextView tvLikes;
        ImageView ivLikes;
        TextView tvDate;
        ImageView ivSave;
        ImageView ivShare;

    }

//        //Bitmap转换本地为文件
public Uri saveImageToGallery(Bitmap bmp) {
    if (bmp==null){
        Log.e("TAG","bitmap---为空");
        return null;
    }
    Uri uri = null;
    String galleryPath= Environment.getExternalStorageDirectory()
            + File.separator + Environment.DIRECTORY_DCIM
            +File.separator+File.separator;
    String fileName = +System.currentTimeMillis() + ".jpg";
    Log.d("path",galleryPath);
    File file = new File(galleryPath, fileName);
    try {
        FileOutputStream fos = new FileOutputStream(file);
        //通过io流的方式来压缩保存图片
        boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 60, fos);
        fos.flush();
        fos.close();
        //保存图片后发送广播通知更新数据库
        uri = Uri.fromFile(file);
        mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
        if (isSuccess) {
            Log.e("TAG","图片保存成功 保存在:" + file.getPath());
            Toast.makeText(getContext(), "图片保存成功 保存在:" + file.getPath(), Toast.LENGTH_SHORT).show();
        } else {
            Log.e("TAG","图片保存失败");
            Toast.makeText(getContext(), "图片保存失败", Toast.LENGTH_SHORT).show();
        }
    } catch (IOException e) {
        Log.e("TAG","保存图片找不到文件夹");
        Toast.makeText(getContext(), "保存图片找不到文件夹", Toast.LENGTH_SHORT).show();
        e.printStackTrace();
    }
    return uri;
}


}
