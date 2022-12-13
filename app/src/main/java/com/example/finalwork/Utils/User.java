package com.example.finalwork.Utils;

import android.app.Application;

public class User extends Application {
    private String username;
    private String userId;
    private int sex;
    private String introduce;
    private String avatar;

    public void setUserInfo(String introduce, int sex, String userId, String avatar, String username) {
        setUsername(username);
        setUserId(userId);
        setAvatar(avatar);
        setIntroduce(introduce);
        setSex(sex);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getUsername() {
        return username;
    }


    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getSex() {
        return sex;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getIntroduce() {
        return introduce;
    }
}
