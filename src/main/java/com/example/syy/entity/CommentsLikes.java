package com.example.syy.entity;

public class CommentsLikes {
    private String fbid;
    private String uid;
    private String create_time;
    public CommentsLikes(String fbid, String uid, String create_time) {
        this.fbid = fbid;
        this.uid = uid;
        this.create_time = create_time;
    }

    public String getFbid() {
        return fbid;
    }

    public void setFbid(String fbid) {
        this.fbid = fbid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }
}
