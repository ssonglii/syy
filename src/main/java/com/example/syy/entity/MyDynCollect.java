package com.example.syy.entity;

public class MyDynCollect {
    private String myCollectTime;   // 当前用户的收藏时间（对应 dc.collect_time）
    private Long did;     // 动态id（对应 dc.did）
    private String dtitle;      // 动态标题（对应 dc.dtitle）
    private String media;     // 动态图片（对应 dc.media）可能是多张图片路径用“,”隔开,
    // 如“/static/image/upload/图片1.png,/static/image/upload/图片2.png”

    private String content;  // 动态内容（对应 dc.content）
    private String type; // 动态类型（对应 dc.type） 如旅行游记、户外知识、摄影分享等
    // 旅行游记：travel，美食分享：food，摄影作品：photo
    private String dynamicCreateTime; // 动态发布时间
    private String avatar;            // 发布者头像
    private String nickname;          // 发布者昵称
    private int like_count;           // 点赞数
    private int comment_count;        // 评论数
    private int collect_count;        // 收藏数



    public MyDynCollect() {
    }

    public MyDynCollect(String myCollectTime, Long did, String dtitle, String media,String content, String type, String dynamicCreateTime, String avatar, String nickname, int like_count, int comment_count, int collect_count) {
        this.myCollectTime = myCollectTime;
        this.did = did;
        this.dtitle = dtitle;
        this.media = media;
        this.content = content;
        this.type = type;
        this.dynamicCreateTime = dynamicCreateTime;
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

    public Long getDid() {
        return did;
    }

    public void setDid(Long did) {
        this.did = did;
    }

    public String getDtitle() {
        return dtitle;
    }

    public void setDtitle(String dtitle) {
        this.dtitle = dtitle;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDynamicCreateTime() {
        return dynamicCreateTime;
    }

    public void setDynamicCreateTime(String dynamicCreateTime) {
        this.dynamicCreateTime = dynamicCreateTime;
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
