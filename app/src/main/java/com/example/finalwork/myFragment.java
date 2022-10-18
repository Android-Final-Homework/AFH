package com.example.finalwork;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.finalwork.activity.AlterInfoActivity;
import com.example.finalwork.activity.LoginActivity;
import com.example.finalwork.activity.MyShareActivity;
import com.example.finalwork.activity.PostActivity;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;


public class myFragment extends Fragment {
    String username;//账号
    TextView tvNickname;//昵称
    ImageView ivHeadPicture;//头像
    TextView tvUsername;//账号
    BmobFile headPicture;//头像文件
    String nickname;//昵称

    Button btShare;
    RelativeLayout rlMyshare;
    RelativeLayout rlExitLogin;
    RelativeLayout rlAlterInfo;

    public myFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("66667", "个人中心");
        //下载图片
//        User currentUser = BmobUser.getCurrentUser(User.class);
//        nickname = currentUser.getNickname();
//        username = currentUser.getUsername();
        //headPicture = currentUser.getHeadpicture();
//
//        Log.d("myFragment", nickname);
//        Log.d("myFragment", headPicture.getUrl());

    }
//    @Override
//    public void onResume() {
//        super.onResume();
//        User currentUser = BmobUser.getCurrentUser(User.class);
//        tvNickname.setText(currentUser.getNickname());
//        tvUsername.setText(currentUser.getUsername());
//        Glide.with(getContext()).load(currentUser.getHeadpicture().getUrl()).into(ivHeadPicture);
//    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_my, container, false);
        tvNickname = rootView.findViewById(R.id.info_nickname);
        tvUsername = rootView.findViewById(R.id.info_username);
        ivHeadPicture = rootView.findViewById(R.id.info_headpicture);
        rlMyshare = rootView.findViewById(R.id.my_share);
        rlExitLogin = rootView.findViewById(R.id.exit_login);
        rlAlterInfo = rootView.findViewById(R.id.update_info);

        btShare = rootView.findViewById(R.id.bt_share);
//        bt = rootView.findViewById(R.id.but);
//        tvNickname.setText(" ");
        tvNickname.setText(nickname);
        tvUsername.setText(username);
        //Glide.with(getContext()).load(headPicture.getUrl()).into(ivHeadPicture);
        //
        btShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PostActivity.class);
                startActivity(intent);
            }
        });

        rlMyshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyShareActivity.class);
                startActivity(intent);
            }
        });

        rlExitLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
                BmobUser.logOut();
                getActivity().finish();
            }
        });
        rlAlterInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AlterInfoActivity.class);
                startActivity(intent);
            }
        });
//        ivHeadpicture.setImageBitmap(BitmapFactory.decodeFile(url));
//        ivHeadpicture.setImageBitmap(BitmapFactory.decodeFile(headpicture);
//        bt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                BmobQuery<ShareItem> query = new BmobQuery<ShareItem>();
//                query.findObjects(new FindListener<ShareItem>() {
//                    @Override
//                    public void done(List<ShareItem> list, BmobException e) {
//                        if (e == null) {
//                            for (ShareItem ad : list) {
//                                Log.w("nnn", "ad.getName()===" + ad.getUsername() + "HomeFragment.current_user===" + username);
//                                if (ad.getUsername() != null && username.equals(ad.getUsername())) {
////                            Log.w("nnn","ad.getName()==="+ad.getUsername()+"HomeFragment.current_user==="+username);
//                                    icon = ad.getHeadPicture();
//                                    String picContent =ad.getPicContent();
//                                    Log.w("nnn", "hello" + picContent);
//                                    File saveFile = new File(Environment.getExternalStorageDirectory(), icon.getFilename());
//                                    icon.download(saveFile,new DownloadFileListener() {
//
//
//                                        @Override
//                                        public void onProgress(Integer integer, long l) {
//
//                                        }
//                                        @Override
//                                        public void onStart() {
//                        i                    Toast.makeText(getContext(), "开始下载！！！1", Toast.LENGTH_LONG).show();
//                                        }
//
//                                        @Override
//                                        public void done(String s, BmobException e) {
//                                            if (e == null) {
//                                                //设置圆形头像并显示
//                                                Log.w("nnn", "done" + s);
//                                                ivHeadpicture.setImageBitmap(BitmapFactory.decodeFile(s)); //根据地址解码并显示图片
////                                                 Glide.with(getContext())
////                                            .load(s)
////                                            .into(ivHeadpicture);
//                                            } else {
//                                            }
//                                        }
//                                    });
//                                    String url = icon.getUrl();
////                                    Glide.with(getContext())
////                                            .asBitmap()
////                                            .load("https://blog.csdn.net/yunnansunshitao");
//                                    Glide.with(getContext())
//                                            .load(url)
//                                            .into(ivHeadpicture);
//                                    Toast.makeText(getContext(), "hahahah", Toast.LENGTH_LONG).show();
//                                    break;
//                                }
//
//                            }
//                        } else {
//                            Toast.makeText(getContext(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
//                        }
//                    }
//                });
//
//
//
//            }
//        });

        // Inflate the layout for this fragment
        return rootView;
    }
}