package com.example.photoshare.bean;

import java.io.Serializable;
import java.util.List;

public class MyDetailBean implements Serializable {
    private String userid;
    private String username;
    private Integer usersex;
    private String Userhead;
    private String content;
    private String useid;
    private String puserid;



    private String usename;


    private String title;
    private String User1;
    private String shareid;
    private boolean hanfocus;



    private List<String> imageUrlList;

    public MyDetailBean(String userid,String shareid,List<String> imageUrlList,String content,String title, String puserid,Boolean hanfocus) {
        this.userid = userid;
        //this.username = username;
        //this.usersex = usersex;
        //this.Userhead = Userhead;
        //this.User1 = User1;
        this.shareid =shareid;
        this.imageUrlList=imageUrlList;
        this.content=content;
        this.title=title;
        this.puserid =puserid;
        this.hanfocus=isHanfocus();
    }
    public boolean isHanfocus() {
        return hanfocus;
    }

    public void setHanfocus(boolean hanfocus) {
        this.hanfocus = hanfocus;
    }
    public String getPuserid() {
        return puserid;
    }

    public void setPuserid(String puserid) {
        this.puserid = puserid;
    }
    public String getUsename() {
        return usename;
    }

    public void setUsename(String usename) {
        this.usename = usename;
    }

    public String getUseid() {
        return useid;
    }

    public void setUseid(String useid) {
        this.useid = useid;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public MyDetailBean() {

    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getUsersex() {
        return usersex;
    }

    public void setUsersex(Integer usersex) {
        this.usersex = usersex;
    }

    public String getUserhead() {
        return Userhead;
    }

    public void setUserhead(String userhead) {
        Userhead = userhead;
    }

    public String getUser1() {
        return User1;
    }

    public void setUser1(String user1) {
        User1 = user1;
    }


    public String getShareid() {
        return shareid;
    }

    public void setShareid(String shareid) {
        this.shareid = shareid;
    }

    public List<String> getImageUrlList() {
        return imageUrlList;
    }

    public void setImageUrlList(List<String> imageUrlList) {
        this.imageUrlList = imageUrlList;
    }
}
