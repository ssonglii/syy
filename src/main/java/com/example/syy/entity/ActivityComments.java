package com.example.syy.entity;

public class ActivityComments {
    private String avatar;
    private String fbid;
    private String nickname;
    private String content;
    private String create_time;
    private String like_count;

    private String uid;
    private String aid;

    public ActivityComments(String avatar, String fbid, String nickname, String content, String create_time, String like_count, String uid, String aid) {
        this.avatar = avatar;
        this.fbid = fbid;
        this.nickname = nickname;
        this.content = content;
        this.create_time = create_time;
        this.like_count = like_count;
        this.uid = uid;
        this.aid = aid;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getFbid() {
        return fbid;
    }

    public void setFbid(String fbid) {
        this.fbid = fbid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getLike_count() {
        return like_count;
    }

    public void setLike_count(String like_count) {
        this.like_count = like_count;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }
}
