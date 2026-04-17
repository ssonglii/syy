package com.example.syy.entity;

public class CommentsReply {
    private String avatar;
    private String fbid;
    private String reply_nickname;
    private String reply_content;
    private String reply_time;
    private String reply_likes;
    private String uid;
    private String reply_id;

    public CommentsReply(String avatar, String fbid, String reply_nickname, String reply_content, String reply_time, String reply_likes, String uid, String reply_id) {
        this.avatar = avatar;
        this.fbid = fbid;
        this.reply_nickname = reply_nickname;
        this.reply_content = reply_content;
        this.reply_time = reply_time;
        this.reply_likes = reply_likes;
        this.uid = uid;
        this.reply_id = reply_id;
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

    public String getReply_nickname() {
        return reply_nickname;
    }

    public void setReply_nickname(String reply_nickname) {
        this.reply_nickname = reply_nickname;
    }

    public String getReply_content() {
        return reply_content;
    }

    public void setReply_content(String reply_content) {
        this.reply_content = reply_content;
    }

    public String getReply_time() {
        return reply_time;
    }

    public void setReply_time(String reply_time) {
        this.reply_time = reply_time;
    }

    public String getReply_likes() {
        return reply_likes;
    }

    public void setReply_likes(String reply_likes) {
        this.reply_likes = reply_likes;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getReply_id() {
        return reply_id;
    }

    public void setReply_id(String reply_id) {
        this.reply_id = reply_id;
    }
}
