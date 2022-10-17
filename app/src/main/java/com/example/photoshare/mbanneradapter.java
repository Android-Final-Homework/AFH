package com.example.photoshare;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.photoshare.activity.PersonDetail;
import com.example.photoshare.bean.infoBean;
import com.youth.banner.adapter.BannerAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class mbanneradapter extends BannerAdapter<String,mbanneradapter.mbannerholder> {
    Context context;

    /**
     * 自定义构造方法
     *
     * @param context 上下文对象
     * @param datas   传入数据
     */
    public mbanneradapter(Context context, List<String> datas) {
        super(datas);
        this.context = context;
    }

    @Override
    public mbannerholder onCreateHolder(ViewGroup parent, int viewType) {
        return new mbannerholder(LayoutInflater.from(context).inflate(R.layout.mybannerrecycler, parent, false));
    }

    @Override
    public void onBindView(mbannerholder holder, String data, int position, int size) {
        if (data == null)
            Glide.with(context).load("https://guet-lab.oss-cn-hangzhou.aliyuncs.com/api/2022/10/12/ce5fc795-775e-4513-8a0f-a2841dcc2cd9.jpg").into(holder.imageView);
        else
            Glide.with(context).load(data).into(holder.imageView);

        //holder.imageView.setImageResource(data);
//设置监听
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "开始下载", Toast.LENGTH_SHORT).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final Bitmap b = Glide.with(context)
                                    .asBitmap()
                                    .load(data)
                                    .submit()
                                    .get();

                            holder.itemView.post(new Runnable() {
                                @Override
                                public void run() {
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

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final Bitmap b = Glide.with(context)
                                    .asBitmap()
                                    .load(data)
                                    .submit()
                                    .get();

                            holder.itemView.post(new Runnable() {
                                @Override
                                public void run() {
                                    DialogImage dialogImage = new DialogImage(context, 0, -300, b);
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



    }


    public class mbannerholder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public mbannerholder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_banner);
        }
    }


    public Uri saveImageToGallery(Bitmap bmp) {
        if (bmp == null) {
            Log.e("TAG", "bitmap---为空");
            return null;
        }
        Uri uri = null;
        String galleryPath = Environment.getExternalStorageDirectory()
                + File.separator + Environment.DIRECTORY_DCIM
                + File.separator + "Camera" + File.separator;
        String fileName = +System.currentTimeMillis() + ".jpg";
        Log.d("path", galleryPath);
        File file = new File(galleryPath, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 60, fos);
            fos.flush();
            fos.close();

            //保存图片后发送广播通知更新数据库
            uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            if (isSuccess) {

                Toast.makeText(context, "图片保存成功 ", Toast.LENGTH_SHORT).show();
            } else {

                Toast.makeText(context, "图片保存失败", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Toast.makeText(context, "保存图片找不到文件夹", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        return uri;
    }
}


