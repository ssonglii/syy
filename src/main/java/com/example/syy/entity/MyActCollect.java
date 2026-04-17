package com.example.syy.entity;

public class MyActCollect {
    private String myCollectTime;   // 当前用户的收藏时间（对应 dc.collect_time）
    private Long aid;
    private String atitle;
    private String aimg;
    private String adscpt;
    private String type;
    private String activityCreateTime; // 动态发布时间
    private String avatar;            // 发布者头像
    private String nickname;          // 发布者昵称
    private int like_count;
    private int comment_count;
    private int collect_count;

    public MyActCollect() {
    }

    public MyActCollect(String myCollectTime, Long aid, String atitle, String aimg, String adscpt, String type, String activityCreateTime, String avatar, String nickname, int like_count, int comment_count, int collect_count) {
        this.myCollectTime = myCollectTime;
        this.aid = aid;
        this.atitle = atitle;
        this.aimg = aimg;
        this.adscpt = adscpt;
        this.type = type;
        this.activityCreateTime = activityCreateTime;
        this.avatar = avatar;
        this.nickname = nickname;
        this.like_count = like_count;
        this.comment_count = comment_count;
        this.collect_count = collect_count;
    }

    public String getMyCollectTime() {
        return myCollectTime;
    }

    public void setMyCollectTime(String myCollectTime) {
        this.myCollectTime = myCollectTime;
    }

    public Long getAid() {
        return aid;
    }

    public void setAid(Long aid) {
        this.aid = aid;
    }

    public String getAtitle() {
        return atitle;
    }

    public void setAtitle(String atitle) {
        this.atitle = atitle;
    }

    public String getAimg() {
        return aimg;
    }

    public void setAimg(String aimg) {
        this.aimg = aimg;
    }

    public String getAdscpt() {
        return adscpt;
    }

    public void setAdscpt(String adscpt) {
        this.adscpt = adscpt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getActivityCreateTime() {
        return activityCreateTime;
    }

    public void setActivityCreateTime(String activityCreateTime) {
        this.activityCreateTime = activityCreateTime;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getLike_count() {
        return like_count;
    }

    public void setLike_count(int like_count) {
        this.like_count = like_count;
    }

    public int getComment_count() {
        return comment_count;
    }

    public void setComment_count(int comment_count) {
        this.comment_count = comment_count;
    }

    public int getCollect_count() {
        return collect_count;
    }

    public void setCollect_count(int collect_count) {
        this.collect_count = collect_count;
    }
}
