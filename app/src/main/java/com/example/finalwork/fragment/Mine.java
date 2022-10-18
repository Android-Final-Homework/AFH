package com.example.finalwork.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.finalwork.ChangeInfo;
import com.example.finalwork.R;
import com.example.finalwork.Utils.App;
import com.example.finalwork.Utils.User;
import com.example.finalwork.Utils.UserInfoSPTool;
import com.example.finalwork.activity.Like;
import com.example.finalwork.activity.LoginActivity;
import com.example.finalwork.activity.MyNews;
import com.example.finalwork.activity.focus;
import com.example.finalwork.activity.myCollect;

import com.squareup.picasso.Picasso;

public class Mine extends Fragment {

    //工具
    private SharedPreferences sharedPreferences;
    private UserInfoSPTool userInfoSPTool;

    //控件
    ImageButton bt_collect;
    ImageButton bt_like;
    ImageButton bt_focus;
    ImageButton bt_mynews;
    Button change_info;
    Button exit_login;
    ImageView avatar;
    ImageView sex;
    TextView username;
    TextView introduce;

    boolean sign_of_login;
    String userId;
    App app;

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if(getActivity().getApplication()==null)
//        {
//            Log.d("e","aaaanull");
//        }
        app = new App();

        sharedPreferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        userInfoSPTool = new UserInfoSPTool(sharedPreferences);

//        sign_of_login = userInfoSPTool.isLogin();
//        System.out.println(sign_of_login);

    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState){

        View root = inflater.inflate(R.layout.navigation_mine,container,false);

        //获取控件
        avatar = root.findViewById(R.id.mine_avatar);
        sex = root.findViewById(R.id.mine_sex);
        username = root.findViewById(R.id.mine_username);
        change_info = root.findViewById(R.id.mine_change_info);
        exit_login = root.findViewById(R.id.mine_exit_login);
        bt_collect = root.findViewById(R.id.mine_collect);
        bt_like = root.findViewById(R.id.mine_like);
        bt_focus = root.findViewById(R.id.mine_packup);
        introduce = root.findViewById(R.id.mine_introduce);
        bt_mynews = root.findViewById(R.id.mine_mynews);
        initData();

//        //点击头像，并通过判断是否已经为登录状态进行跳转
//        并通过判断是否已经为登录状态进行跳转avatar.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                if(!sign_of_login){
//                    Intent i = new Intent(getActivity(),Login.class);
//                    startActivity(i);
//                }
//            }
//        });

        exit_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                userInfoSPTool.exitLogin();
                initData();
                Intent i = new Intent(getActivity(), LoginActivity.class);
                startActivity(i);
            }
        });

        change_info.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent i = new Intent(getActivity(), ChangeInfo.class);
                startActivity(i);
            }
        });

        //like
        bt_like.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                    Intent i = new Intent(getActivity(), Like.class);
                    startActivity(i);

            }
        });
        //collect
        bt_collect.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                    Intent i = new Intent(getActivity(), myCollect.class);
                    startActivity(i);
            }
        });

        //packup
        bt_focus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                    Intent i = new Intent(getActivity(), focus.class);
                    startActivity(i);


            }
        });
        bt_mynews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), MyNews.class);
                startActivity(i);
            }
        });
        return root;
    }

    private void initData(){
        sign_of_login = userInfoSPTool.isLogin();
            userId = userInfoSPTool.getUserId();
            System.out.println(userId);
            User user = userInfoSPTool.getInfo();
            String sign_of_sex;
            switch (user.getSex()){
                case 1:
                    sign_of_sex = app.getSex_1();
                    break;
                case 2:
                    sign_of_sex = app.getSex_2();
                    break;
                default:
                    sign_of_sex = app.getSex_0();
            }
            Picasso.get().load(user.getAvatar()).into(avatar);
            Picasso.get().load(sign_of_sex).into(sex);
            username.setText(user.getUsername());
            introduce.setText(user.getIntroduce());
            exit_login.setVisibility(View.VISIBLE);
            change_info.setVisibility(View.VISIBLE);
    }

}
