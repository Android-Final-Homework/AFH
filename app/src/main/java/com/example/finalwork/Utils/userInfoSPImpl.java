package com.example.finalwork.Utils;

public interface userInfoSPImpl {

    //保存登录信息
    void saveInfo(String introduce, int sex, String userId, String avatar, String username);

    //获取登录信息
    User getInfo();

    //判断上次退出的时候是为否登录状态
    boolean isLogin();

    //退出退出登录
    void exitLogin();

    //设置登录状态为否
    void Login_in();

    //设置登陆状态为是
    void Login_out();

    //保存用户名密码
    void savePwd(String password);

    //获取用户名密码
    String getPwd();

    //获取用户名
    String getUsername();

    //获取id
    String getUserId();

}
