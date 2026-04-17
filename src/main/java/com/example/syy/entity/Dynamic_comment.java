package com.example.syy.entity;

public class Dynamic_comment {
    private Long dcid;
    private Long did;
    private Long uid;
    private Long parent_id;
    private String content;
    private String create_time;
    private String audit_status;
    private int like_count;
    private String nickname;

    private String avatar;

    public Dynamic_comment() {


        like_count = 0;
    }

    public Dynamic_comment(Long dcid, Long did, Long uid, Long parent_id, String content, String create_time, String audit_status, int like_count, String nickname, String avatar) {
        this.dcid = dcid;
        this.did = did;
        this.uid = uid;
        this.parent_id = parent_id;
        this.content = content;
        this.create_time = create_time;
        this.audit_status = audit_status;
        this.like_count = like_count;
        this.nickname = nickname;
        this.avatar = avatar;
    }

    public Long getDcid() {
        return dcid;
    }

    public void setDcid(Long dcid) {
        this.dcid = dcid;
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

    public Long getParent_id() {
        return parent_id;
    }

    public void setParent_id(Long parent_id) {
        this.parent_id = parent_id;
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

    public String getAudit_status() {
        return audit_status;
    }

    public void setAudit_status(String audit_status) {
        this.audit_status = audit_status;
    }

    public int getLike_count() {
        return like_count;
    }

    public void setLike_count(int like_count) {
        this.like_count = like_count;
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
}
