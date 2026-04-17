package com.example.syy.entity;

public class ActivityCollects {
    private String aid;
    private String uid;
    private String create_time;

    public ActivityCollects(String aid, String uid, String create_time) {
        this.aid = aid;
        this.uid = uid;
        this.create_time = create_time;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
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
