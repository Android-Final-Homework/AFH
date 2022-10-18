package com.example.finalwork.bean;

import java.util.List;

public class PersonDetailBean {

    private int code;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String id;
        private String pUserId;
        private String imageCode;
        private String title;
        private String content;
        private String createTime;
        private List<?> imageUrlList;
        private Object likeId;
        private Object likeNum;
        private boolean hasLike;
        private Object collectId;
        private Object collectNum;
        private boolean hasCollect;
        private boolean hasFocus;
        private Object username;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPUserId() {
            return pUserId;
        }

        public void setPUserId(String pUserId) {
            this.pUserId = pUserId;
        }

        public String getImageCode() {
            return imageCode;
        }

        public void setImageCode(String imageCode) {
            this.imageCode = imageCode;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public List<?> getImageUrlList() {
            return imageUrlList;
        }

        public void setImageUrlList(List<?> imageUrlList) {
            this.imageUrlList = imageUrlList;
        }

        public Object getLikeId() {
            return likeId;
        }

        public void setLikeId(Object likeId) {
            this.likeId = likeId;
        }

        public Object getLikeNum() {
            return likeNum;
        }

        public void setLikeNum(Object likeNum) {
            this.likeNum = likeNum;
        }

        public boolean isHasLike() {
            return hasLike;
        }

        public void setHasLike(boolean hasLike) {
            this.hasLike = hasLike;
        }

        public Object getCollectId() {
            return collectId;
        }

        public void setCollectId(Object collectId) {
            this.collectId = collectId;
        }

        public Object getCollectNum() {
            return collectNum;
        }

        public void setCollectNum(Object collectNum) {
            this.collectNum = collectNum;
        }

        public boolean isHasCollect() {
            return hasCollect;
        }

        public void setHasCollect(boolean hasCollect) {
            this.hasCollect = hasCollect;
        }

        public boolean isHasFocus() {
            return hasFocus;
        }

        public void setHasFocus(boolean hasFocus) {
            this.hasFocus = hasFocus;
        }

        public Object getUsername() {
            return username;
        }

        public void setUsername(Object username) {
            this.username = username;
        }
    }
}
