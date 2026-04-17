package com.example.syy.entity;

public class Dynamic_collect {
    private Long collect_id;
    private Long uid;
    private Long did;
    private String collect_time;


    public Dynamic_collect() {

    }

    public Dynamic_collect(Long collect_id, Long uid, Long did, String collect_time) {
        this.collect_id = collect_id;
        this.uid = uid;
        this.did = did;
        this.collect_time = collect_time;
    }

    public Long getCollect_id() {
        return collect_id;
    }

    public void setCollect_id(Long collect_id) {
        this.collect_id = collect_id;
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

    public String getCollect_time() {
        return collect_time;
    }

    public void setCollect_time(String collect_time) {
        this.collect_time = collect_time;
    }
}

