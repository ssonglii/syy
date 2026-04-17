package com.example.syy.controls;

import com.example.syy.entity.Activity;
import com.example.syy.entity.Dynamic;
import com.example.syy.entity.Statistics;
import com.example.syy.service.CertService;
import com.example.syy.service.MyService;
import com.example.syy.service.SecurityService;
import com.example.syy.service.SyyService;
import com.example.syy.utils.MyTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.List;

@RestController//请求和响应都是json格式
@CrossOrigin//跨域注释
public class MyControls {
    @Autowired
    public MyControls(MyService myService) {
        this.myService = myService;
    }


    private final MyService myService;
    /**
     * 获取用户数据统计
     * @param uid 用户ID
     * @return 统计结果
     */
    @GetMapping("/user/{uid}")
    public Statistics getUserStatistics(@PathVariable Long uid) {
        return myService.getUserStatistics(uid);
    }
    /**
     * 获取用户参加的活动
     */
    @GetMapping("/activities/joined/{uid}")
    public List<Activity> getJoinedActivities(@PathVariable Long uid) {
        return myService.getJoinedActivities(uid);
    }

    /**
     * 获取用户发布的动态
     */
    @GetMapping("/dynamics/published/{uid}")
    public List<Dynamic> getPublishedDynamics(@PathVariable Long uid) {
        return myService.getPublishedDynamics(uid);
    }
    /**
     * 获取用户发布的活动
     */
    @GetMapping("/activities/published/{uid}")
    public List<Activity> getPublishedActivities(@PathVariable Long uid) {
        return myService.getPublishedActivities(uid);
    }

    /**
     * 获取即将到来的活动
     */
    @GetMapping("/activities/upcoming")
    public List<Activity> getUpcomingActivities() {
        return myService.getUpcomingActivities();
    }

//     private SyyService syyService;
//    @GetMapping("/MySpace/activities")
//    public List<Activity> getMySpaceActivities() {
//
//        List<Activity> activities = syyService.list();
//        // 标记为“我的空间”请求，让 Activity 返回状态
//        activities.forEach(activity -> activity.setShowStatus(true));
//        return activities;
//    }
}
