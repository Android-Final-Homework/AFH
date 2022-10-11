package com.example.finalwork.bean;

import java.util.List;

public class ShareBean {

    private String msg;
    private int code;
    private DataBean data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private int current;
        private List<RecordsBean> records;
        private int size;
        private int total;

        public int getCurrent() {
            return current;
        }

        public void setCurrent(int current) {
            this.current = current;
        }

        public List<RecordsBean> getRecords() {
            return records;
        }

        public void setRecords(List<RecordsBean> records) {
            this.records = records;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public static class RecordsBean {
            private int collectId;
            private int collectNum;
            private String content;
            private int createTime;
            private boolean hasCollect;
            private boolean hasFocus;
            private boolean hasLike;
            private int id;
            private int imageCode;
            private List<ImageUrlListBean> imageUrlList;
            private int likeId;
            private int likeNum;
            private int pUserId;
            private String title;
            private String username;

            public int getCollectId() {
                return collectId;
            }

            public void setCollectId(int collectId) {
                this.collectId = collectId;
            }

            public int getCollectNum() {
                return collectNum;
            }

            public void setCollectNum(int collectNum) {
                this.collectNum = collectNum;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getCreateTime() {
                return createTime;
            }

            public void setCreateTime(int createTime) {
                this.createTime = createTime;
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

            public boolean isHasLike() {
                return hasLike;
            }

            public void setHasLike(boolean hasLike) {
                this.hasLike = hasLike;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getImageCode() {
                return imageCode;
            }

            public void setImageCode(int imageCode) {
                this.imageCode = imageCode;
            }

            public List<ImageUrlListBean> getImageUrlList() {
                return imageUrlList;
            }

            public void setImageUrlList(List<ImageUrlListBean> imageUrlList) {
                this.imageUrlList = imageUrlList;
            }

            public int getLikeId() {
                return likeId;
            }

            public void setLikeId(int likeId) {
                this.likeId = likeId;
            }

            public int getLikeNum() {
                return likeNum;
            }

            public void setLikeNum(int likeNum) {
                this.likeNum = likeNum;
            }

            public int getPUserId() {
                return pUserId;
            }

            public void setPUserId(int pUserId) {
                this.pUserId = pUserId;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public static class ImageUrlListBean {
            }
        }
    }
}
