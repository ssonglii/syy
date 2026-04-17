package com.example.syy.entity;

public class Dynamic_like {
    private Long like_id;//主键，数据库自增
    private Long uid;//点赞用户id
    private Long did;//动态id
    private String like_time;//点赞时间


    public Dynamic_like() {
    }

    public Dynamic_like(Long like_id, Long uid, Long did, String like_time) {
        this.like_id = like_id;
        this.uid = uid;
        this.did = did;
        this.like_time = like_time;

    }

    public Long getLike_id() {
        return like_id;
    }

    public void setLike_id(Long like_id) {
        this.like_id = like_id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Long getDid() {
        return did;
    }

    public void setDid(Long did) {
        this.did = did;
    }

    public String getLike_time() {
        return like_time;
    }

    public void setLike_time(String like_time) {
        this.like_time = like_time;
    }

}
