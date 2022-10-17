package com.example.photoshare.Utils;

import android.app.Application;

public class App extends Application {
    private static String appId = "1bfe220c6b2b4f66a92bc0b07788c770";
    private static String appSecret = "31963f285e49924744224aa30a79f58eee2ac";
    private static String dummy_userId = "0";
    private static int current = 1;
    private static int size = 10;
    private static String defaultAvatar =  "https://guet-lab.oss-cn-hangzhou.aliyuncs.com/api/2022/10/14/addbde85-e11d-4953-8621-d0d742bf50a4.JPG";
    private static String sex_0 = "https://guet-lab.oss-cn-hangzhou.aliyuncs.com/api/2022/10/15/92e8aafb-78fb-4fd5-893a-bfae3f05115b.JPG";
    private static String sex_1 = "https://guet-lab.oss-cn-hangzhou.aliyuncs.com/api/2022/10/15/7eb9d12d-e0d5-4978-953f-9d95a1e247a8.JPG";
    private static String sex_2 = "https://guet-lab.oss-cn-hangzhou.aliyuncs.com/api/2022/10/15/30427a8c-9fea-4ba7-a3bd-0e3407e8e003.JPG";

    private String defaultIntroduce = "This user has not yet had a personal introduction";
    private int defaultSex = 0;     //0：(默认)保密 1：男 2：女

    public String getAppId(){
        return appId;
    }

    public String getAppSecret() {
        return appSecret;
    }

    public int getCurrent() {
        return current;
    }

    public int getSize() {
        return size;
    }

    public String getDummy_userId() {
        return dummy_userId;
    }

    public String getDefaultAvatar() {
        return defaultAvatar;
    }

    public String getDefaultIntroduce() {
        return defaultIntroduce;
    }

    public int getDefaultSex() {
        return defaultSex;
    }

    public String getSex_0() {
        return sex_0;
    }

    public String getSex_1() {
        return sex_1;
    }

    public String getSex_2() {
        return sex_2;
    }

}
