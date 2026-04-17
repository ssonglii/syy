package com.example.syy.service;

import com.example.syy.entity.*;
import com.example.syy.mapper.ActivityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ActivityService {
    @Resource
    private ActivityMapper activityMapper;
    public List<ActivityComments> activityComments(String aid){
        return activityMapper.activityComments(aid);
    }
    public List<CommentsReply> commentsReply(String fbid){
        return activityMapper.commentsReply(fbid);
    }
    public int addActivityComments(ActivityComments activityComments){
        return activityMapper.addActivityComments(activityComments);
    }
    public int addCommentsReply(CommentsReply commentsReply){
        return activityMapper.addCommentsReply(commentsReply);
    }
    public int addActivityLikes(ActivityLikes activityLikes){
        return activityMapper.addActivityLikes(activityLikes);
    }
    public int addActivityCollects(ActivityCollects activityCollects){
        return activityMapper.addActivityCollects(activityCollects);
    }
    public List<ActivityLikes> findActivityLikes(String aid,String uid){
        return activityMapper.findActivityLikes(aid,uid);
    }
    public List<ActivityCollects> findActivityCollects(String aid,String uid){
        return activityMapper.findActivityCollects(aid,uid);
    }
    public int delActivityLikes(String aid,String uid){
        return activityMapper.delActivityLikes(aid,uid);
    }
    public int delActivityCollects(String aid,String uid){
        return activityMapper.delActivityCollects(aid,uid);
    }

    public List<CommentsLikes> findCommentsLikes(String fbid,String uid){
        return activityMapper.findCommentsLikes(fbid,uid);
    }
    public int addCommentsLikes(CommentsLikes commentsLikes){
        return activityMapper.addCommentsLikes(commentsLikes);
    }
    public int delCommentsLikes(String fbid,String uid){
        return activityMapper.delCommentsLikes(fbid,uid);
    }
    public List<Activity> findAllActivities(){
        return activityMapper.findAllActivities();
    }
    public List<Activity> findCounts(String aid){
        return activityMapper.findCounts(aid);
    }

    public List<Activity> findAllActivities0(String keyword){
        return activityMapper.findAllActivities0(keyword);
    }
    //查找未审核活动
    public List<Activity> findActivityUnreviewed(int keyword){
        return activityMapper.findActivityUnreviewed(keyword);
    }
    //更新审核表活动表
    public int updateActivity(String status,String aid){
        return activityMapper.updateActivity(status,aid);
    }
    public int addAudit(Audit audit){
        return activityMapper.addAudit(audit);
    }

    //查找动态
    public List<Dynamic> findDynamic(int keyword){return activityMapper.findDynamic(keyword);}

    public List<Dynamic> findAllDynamic(){return activityMapper.findAllDynamic();}

    public List<Dynamic> findDynamicDetails(String did){return activityMapper.findDynamicDetails(did);}

    //更新动态表状态
    public int updateDynamic(String status, String did) {
            return activityMapper.updateDynamic(status, did);
        }


    //查找报名
    public List<Signup> findSignup(String keyword){return activityMapper.findSignUp(keyword);}

    public List<Signup> findSignup0(){return activityMapper.findSignup0();}

    public int updateSignup(String status,String sid){
        return activityMapper.updateSignup(status,sid);
    }

    //查找所有用户
    public List<User> findAllUsers(){
        return activityMapper.findAllUsers();
    }

    //更新user表
    public int updateUser(String id_type,String uid){return activityMapper.updateUser(id_type,uid);}
    public int updateUser0(String id_type,String tel){
        return activityMapper.updateUser0(id_type,tel);
    }
    public List<Activity> findMyJoinedActivities(String uid){
        // 直接调用 Mapper 方法，需确保 uid 非空
        if (uid == null) {
            throw new IllegalArgumentException("用户 ID 不能为空");
        }
        return activityMapper.findMyJoinedActivities(uid);
    }

    public List<Activity> findMyActivities0(String keyword, String uid) {
        return activityMapper.findMyActivities0(keyword, uid);
    }
}
