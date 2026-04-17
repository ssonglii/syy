package com.example.syy.service;

import com.example.syy.entity.Activity;
import com.example.syy.entity.Dynamic;
import com.example.syy.entity.Statistics;
import com.example.syy.mapper.MyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MyService {
    @Autowired
    private MyMapper myMapper;

    /**
     * 获取用户数据统计信息
     * @param uid 用户ID
     * @return 统计数据对象
     */
    public Statistics getUserStatistics(Long uid) {
        int joinedActivities = myMapper.countJoinedActivities(uid);
        int publishedDynamics = myMapper.countPublishedDynamics(uid);
        int hostedActivities = myMapper.countHostedActivities(uid);

        int dynamicLikes = myMapper.testDynamicLikes(uid);
        int activityLikes = myMapper.testActivityLikes(uid);
        int feedbackLikes = myMapper.testFeedbackLikes(uid);
        int totalLikes = dynamicLikes + activityLikes + feedbackLikes;


        return new Statistics(joinedActivities, publishedDynamics, hostedActivities,totalLikes);
    }
    /**
     * 获取用户参加的活动
     */
    public List<Activity> getJoinedActivities(Long uid) {
        return myMapper.getJoinedActivities(uid);
    }

    /**
     * 获取用户发布的动态
     */
    public List<Dynamic> getPublishedDynamics(Long uid) {
        return myMapper.getPublishedDynamics(uid);
    }
    /**
     * 获取用户发布的活动
     */
    public List<Activity> getPublishedActivities(Long uid) {
        return myMapper.getPublishedActivities(uid);
    }

    /**
     * 获取即将到来的活动
     */
    public List<Activity> getUpcomingActivities() {
        return myMapper.getUpcomingActivities();
    }
}
