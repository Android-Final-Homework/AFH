package com.example.finalwork.bean;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

//持久化类User记录用户的信息
public class User extends BmobUser {

    //username、password等成员变量继承了父类BombUser
    private String nickname;
    private BmobFile headpicture;


    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public BmobFile getHeadpicture() {
        return headpicture;
    }
    public void setHeadpicture(BmobFile headpicture) {
        this.headpicture = headpicture;
    }
}
