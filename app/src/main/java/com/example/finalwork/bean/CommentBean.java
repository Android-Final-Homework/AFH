package com.example.finalwork.bean;

import java.util.List;

public class CommentBean {
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
        private List<RecordsBean> records;
        private int total;
        private int size;
        private int current;

        public List<RecordsBean> getRecords() {
            return records;
        }

        public void setRecords(List<RecordsBean> records) {
            this.records = records;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getCurrent() {
            return current;
        }

        public void setCurrent(int current) {
            this.current = current;
        }

        public static class RecordsBean {
            private String id;
            private String appKey;
            private String pUserId;
            private String userName;
            private String shareId;
            private Object parentCommentId;
            private Object parentCommentUserId;
            private Object replyCommentId;
            private Object replyCommentUserId;
            private int commentLevel;
            private String content;
            private int status;
            private int praiseNum;
            private int topStatus;
            private String createTime;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getAppKey() {
                return appKey;
            }

            public void setAppKey(String appKey) {
                this.appKey = appKey;
            }

            public String getPUserId() {
                return pUserId;
            }

            public void setPUserId(String pUserId) {
                this.pUserId = pUserId;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public String getShareId() {
                return shareId;
            }

            public void setShareId(String shareId) {
                this.shareId = shareId;
            }

            public Object getParentCommentId() {
                return parentCommentId;
            }

            public void setParentCommentId(Object parentCommentId) {
                this.parentCommentId = parentCommentId;
            }

            public Object getParentCommentUserId() {
                return parentCommentUserId;
            }

            public void setParentCommentUserId(Object parentCommentUserId) {
                this.parentCommentUserId = parentCommentUserId;
            }

            public Object getReplyCommentId() {
                return replyCommentId;
            }

            public void setReplyCommentId(Object replyCommentId) {
                this.replyCommentId = replyCommentId;
            }

            public Object getReplyCommentUserId() {
                return replyCommentUserId;
            }

            public void setReplyCommentUserId(Object replyCommentUserId) {
                this.replyCommentUserId = replyCommentUserId;
            }

            public int getCommentLevel() {
                return commentLevel;
            }

            public void setCommentLevel(int commentLevel) {
                this.commentLevel = commentLevel;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getPraiseNum() {
                return praiseNum;
            }

            public void setPraiseNum(int praiseNum) {
                this.praiseNum = praiseNum;
            }

            public int getTopStatus() {
                return topStatus;
            }

            public void setTopStatus(int topStatus) {
                this.topStatus = topStatus;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }
        }
    }
}
