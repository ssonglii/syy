package com.example.syy.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Activity {
    private String aid;
    private String nickname;
    private String atitle;
    private String type;
    private String status;
    private String start_time;
    private String end_time;
    private String max_people;
    private String sign_up_start;
    private String sign_up_end;
    private String uid;
    private String location;
    private String adscpt;
    private String create_time;
    private String aimg;

    private String current_people;
    private Integer cur_people;
    private String likes;
    private String collections;
    private Integer sign_status;  // 报名状态

    public Integer getCur_people() {
        return cur_people;
    }

    public void setCur_people(Integer cur_people) {
        this.cur_people = cur_people;
    }

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public Activity(){

    }


    public Activity(String aid, String nickname, String atitle, String type, String status, String start_time, String end_time, String max_people, String sign_up_start, String sign_up_end, String uid, String location, String adscpt, String create_time, String aimg, String current_people, String likes, String collections, Integer sign_status) {
        this.aid = aid;
        this.nickname = nickname;
        this.atitle = atitle;
        this.type = type;
        this.status = status;
        this.start_time = start_time;
        this.end_time = end_time;
        this.max_people = max_people;
        this.sign_up_start = sign_up_start;
        this.sign_up_end = sign_up_end;
        this.uid = uid;
        this.location = location;
        this.adscpt = adscpt;
        this.create_time = create_time;
        this.aimg = aimg;
        this.current_people = current_people;
        this.likes = likes;
        this.collections = collections;
        this.sign_status = sign_status;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getAtitle() {
        return atitle;
    }

    public void setAtitle(String atitle) {
        this.atitle = atitle;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }
    public String getMax_people() {
        return max_people;
    }

    public void setMax_people(String max_people) {
        this.max_people = max_people;
    }

    public String getSign_up_start() {
        return sign_up_start;
    }

    public void setSign_up_start(String sign_up_start) {
        this.sign_up_start = sign_up_start;
    }

    public String getSign_up_end() {
        return sign_up_end;
    }

    public void setSign_up_end(String sign_up_end) {
        this.sign_up_end = sign_up_end;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAdscpt() {
        return adscpt;
    }

    public void setAdscpt(String adscpt) {
        this.adscpt = adscpt;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getAimg() {
        return aimg;
    }

    public void setAimg(String aimg) {
        this.aimg = aimg;
    }

    public String getCurrent_people() {
        return current_people;
    }

    public void setCurrent_people(String current_people) {
        this.current_people = current_people;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getCollections() {
        return collections;
    }

    public void setCollections(String collections) {
        this.collections = collections;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getSign_status() {
        return sign_status;
    }

    public void setSign_status(Integer sign_status) {
        this.sign_status = sign_status;
    }
    // 判断活动状态（进行中/已结束）
//    public String getActivityStatus() {
//        LocalDateTime now = LocalDateTime.now();
//
//        // 解析开始时间（补充时间为 00:00:00）
//        LocalDate startDate = LocalDate.parse(start_time, DateTimeFormatter.ISO_LOCAL_DATE);
//        LocalDateTime start = startDate.atStartOfDay();
//
//        // 解析结束时间（补充时间为 23:59:59）
//        LocalDate endDate = LocalDate.parse(end_time, DateTimeFormatter.ISO_LOCAL_DATE);
//        LocalDateTime end = endDate.atTime(23, 59, 59); // 结束时间设为当天最晚时刻
//
//        if (now.isAfter(start) && now.isBefore(end)) {
//            return "进行中";
//        } else if (now.isAfter(end)) {
//            return "已结束";
//        } else {
//            return "未开始";
//        }
//    }
}
