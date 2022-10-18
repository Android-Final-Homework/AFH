package com.example.finalwork.Utils;

import android.app.Application;
import android.content.SharedPreferences;



public class UserInfoSPTool extends Application implements userInfoSPImpl{

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    User user = new User();

    App app = new App();

    public UserInfoSPTool(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
        this.editor = sharedPreferences.edit();
    }



    //保存用户的基础信息
    @Override
    public void saveInfo(String introduce,int sex,String userId,String avatar,String username) {
        editor.putInt("sex",sex);
        editor.putString("username",username);
        editor.putString("avatar",avatar);
        editor.putString("userId",userId);
        editor.putString("introduce",introduce);

        editor.commit();
    }

    //获取用户基础信息
    @Override
    public User getInfo() {
        String introduce = sharedPreferences.getString("introduce",app.getDefaultIntroduce());
        int sex = sharedPreferences.getInt("sex", app.getDefaultSex());
        String userId = sharedPreferences.getString("userId",app.getDummy_userId());
        String avatar = sharedPreferences.getString("avatar", app.getDefaultAvatar());
        String username = sharedPreferences.getString("username","空");
        user.setUserInfo(introduce,sex,userId,avatar,username);
        return user;
    }

    //判断登陆状态
    @Override
    public boolean isLogin() {
        System.out.println("123123123");
        boolean isl = sharedPreferences.getBoolean("isLogin",false);
        System.out.println("asdasdasdasd");
        return isl;
    }

    //退出登录
    @Override
    public void exitLogin() {
        editor.clear();
        editor.putBoolean("isLogin",false);
        editor.commit();
    }

    //设置登录状态为真
    @Override
    public void Login_in() {
        editor.putBoolean("isLogin",true);
        editor.commit();
    }

    //设置登录状态为假
    @Override
    public void Login_out() {
        editor.putBoolean("isLogin",false);
        editor.commit();
    }

    //保存用户的密码
    @Override
    public void savePwd(String pwd) {
        editor.putString("password",pwd);
        editor.commit();
    }

    //获取用户的登录密码
    @Override
    public String getPwd() {
        String pwd = sharedPreferences.getString("password","");
        return pwd;
    }

    //获取用户登录的账号
    @Override
    public String getUsername(){
        String username = sharedPreferences.getString("username","空");
        return username;
    }

    @Override
    //获取用户id
    public String getUserId(){
        String userId = sharedPreferences.getString("userId","空");
        return userId;
    }

}
