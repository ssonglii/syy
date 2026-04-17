package com.example.syy.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Dynamic {
   private Long did;
    private Long uid;
    private String dtitle;
    private String content;
    private String media;
    private String type;
    private String create_time;
    private int like_count;
    private int comment_count;
    private int collect_count;
    private String nickname;
    private String avatar;
    private int isLiked;
    private int isCollected;
    private String status;//审核状态 0 未审核 1 已审核 2已驳回


    public Dynamic() {
        like_count = 0;
        comment_count = 0;
        collect_count = 0;
        isLiked = 0;
        isCollected = 0;
    }


    public Dynamic(Long did, Long uid, String dtitle, String content, String media, String type, String create_time, int like_count, int comment_count, int collect_count, String nickname, String avatar, int isLiked, int isCollected) {
        this.did = did;
        this.uid = uid;
        this.dtitle = dtitle;
        this.content = content;
        this.media = media;
        this.type = type;
        this.create_time = create_time;
        this.like_count = like_count;
        this.comment_count = comment_count;
        this.collect_count = collect_count;
        this.nickname = nickname;
        this.avatar = avatar;
        this.isLiked = isLiked;
        this.isCollected = isCollected;
    }

    public Long getDid() {
        return did;
    }

    public void setDid(Long did) {
        this.did = did;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getDtitle() {
        return dtitle;
    }

    public void setDtitle(String dtitle) {
        this.dtitle = dtitle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(int isLiked) {
        this.isLiked = isLiked;
    }
    // 在 Dynamic 类中添加
//    public List<String> getMediaList() {
//        if (media == null) {
//            return new ArrayList<>();
//        }
//        // 假设 media 存的是逗号分隔的图片路径，如 "img1.jpg,img2.jpg"
//        return Arrays.asList(media.split(","));
//    }

    public int getIsCollected() {
        return isCollected;
    }

    public void setIsCollected(int isCollected) {
        this.isCollected = isCollected;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
