package com.example.finalwork.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.finalwork.R;

import java.util.LinkedList;
import java.util.List;

public class PersonalCentelActivity extends Fragment implements View.OnClickListener{
    private TextView ivnic;//昵称
    private TextView userid;//账户
    private TextView message;//修改昵称
    private TextView myactivity;//我的动态
    private TextView help;//帮助
    private TextView comeback;//退出登录
    public static List<Activity> activityList = new LinkedList();       //将要销毁的Activity事先存在这个集合中

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(@Nullable LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View root=inflater.inflate(R.layout.activity_personalcentel,container,false);
        ivnic=root.findViewById(R.id.iv_nic);
        userid=root.findViewById(R.id.iv_userid);
        message=root.findViewById(R.id.iv_message);
        myactivity=root.findViewById(R.id.iv_my);
        help=root.findViewById(R.id.iv_help);
        comeback=root.findViewById(R.id.iv_back);
        return root;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);

        message.setOnClickListener(this);
        myactivity.setOnClickListener(this);
        help.setOnClickListener(this);
        comeback.setOnClickListener(this);

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //case R.id.iv_message:
            case R.id.iv_my:


            case R.id.iv_help:

            case R.id.iv_back:
                exit();
                break;

        }


    }
    //退出登录
    public void exit()              //  启动退出程序的按钮时，调用该方法，遍历一遍集合，销毁所有的Activity
    {
            for(Activity act:activityList)
            {
                Log.d("TAGS",act.toString());
                act.finish();
            }
            System.exit(0);
    }
}