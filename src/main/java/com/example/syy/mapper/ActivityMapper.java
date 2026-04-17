package com.example.syy.mapper;

import com.example.syy.entity.*;
import org.apache.ibatis.annotations.*;
import org.springframework.security.core.parameters.P;

import java.util.List;

@Mapper
public interface ActivityMapper {

    //查询评论 渲染页面
    @Select("SELECT \n" +
            "    u.avatar,\n"+
            "    af.fbid,\n"+
            "    u.nickname AS nickname,\n" +
            "    af.content,\n" +
            "    af.create_time,\n" +
            "    af.like_count,\n" +
            "    u.uid,\n"+
            "    af.aid\n"+
            "FROM \n" +
            "    activity_feedback af\n" +
            "JOIN \n" +
            "    user u ON af.uid = u.uid  -- 通过 uid 关联两个表\n" +
            "WHERE \n" +
            "    af.aid = #{aid};")
    List<ActivityComments> activityComments(String aid);

    //查询回复 渲染页面
    @Select("SELECT \n" +
            "    u.avatar,\n"+
            "    ar.fbid,\n"+
            "    u.nickname AS nickname,\n" +
            "    ar.content,\n" +
            "    ar.create_time,\n" +
            "\t\tar.like_count,\n" +
            "    u.uid,\n"+
            "    ar.reply_uid\n"+
            "FROM \n" +
            "    activity_reply ar\n" +
            "JOIN \n" +
            "    user u ON ar.uid = u.uid\n" +
            "WHERE \n" +
            "    ar.fbid = #{fbid};")
    List<CommentsReply> commentsReply(String fbid);

    //插入评论
    @Insert("INSERT INTO activity_feedback(uid,aid,content,create_time) values(#{uid},#{aid},#{content},#{create_time})")
    int addActivityComments(ActivityComments activityComments);

    //插入回复
    @Insert("insert into activity_reply( fbid,uid,content,create_time) values(#{fbid},#{uid},#{reply_content},#{reply_time}) ")
    int addCommentsReply(CommentsReply commentsReply);

    //提交活动点赞 更新点赞
    @Insert("insert into activity_like(aid,uid,create_time) values(#{aid},#{uid},#{create_time})")
    int addActivityLikes(ActivityLikes activityLikes);

    //提交活动收藏 更新收藏
    @Insert("insert into activity_collect(aid,uid,create_time) values(#{aid},#{uid},#{create_time})")
    int addActivityCollects(ActivityCollects activityCollects);

    //查询是否已点赞 activity_like
    @Select("select * from activity_like where aid=#{aid} and uid=#{uid}")
    List<ActivityLikes> findActivityLikes(@Param("aid") String aid,@Param("uid") String uid);

    @Select("select * from feedback_like where fbid=#{fbid} and uid=#{uid}")
    List<CommentsLikes> findCommentsLikes(@Param("fbid") String fbid,@Param("uid") String uid);

    //查询是否已收藏
    @Select("select * from activity_collect where aid=#{aid} and uid=#{uid}")
    List<ActivityCollects> findActivityCollects(@Param("aid") String aid,@Param("uid") String uid);

    //取消活动点赞
    @Delete("delete from activity_like where aid=#{aid} and uid=#{uid}")
    int delActivityLikes(@Param("aid") String aid,@Param("uid") String uid);

    //取消活动收藏
    @Delete("delete from activity_collect where aid=#{aid} and uid=#{uid}")
    int delActivityCollects(@Param("aid") String aid,@Param("uid") String uid);

    //提交评论点赞 更新点赞
    @Insert("insert into feedback_like(fbid,uid,create_time) values(#{fbid},#{uid},#{create_time})")
    int addCommentsLikes(CommentsLikes commentsLikes);

    //取消评论点赞
    @Delete("delete from feedback_like where fbid=#{fbid} and uid=#{uid}")
    int delCommentsLikes(@Param("fbid") String fbid,@Param("uid") String uid);

    //查询所有活动
    @Select("SELECT \n" +
            "    a.aid,\n"+
            "    a.atitle,\n" +
            "    a.type,\n" +
            "    a.location,\n" +
            "    a.aimg,\n" +
            "    a.start_time,\n" +
            "    a.status,\n"+
            "    COALESCE(al.likes, 0) AS likes,\n" +
            "    COALESCE(ac.collects, 0) AS collections\n" +
            "FROM \n" +
            "    activity a\n" +
            "LEFT JOIN (\n" +
            "    SELECT \n" +
            "        aid, \n" +
            "        COUNT(*) AS likes \n" +
            "    FROM \n" +
            "        activity_like \n" +
            "    GROUP BY \n" +
            "        aid\n" +
            ") al ON a.aid = al.aid\n" +
            "LEFT JOIN (\n" +
            "    SELECT \n" +
            "        aid, \n" +
            "        COUNT(*) AS collects \n" +
            "    FROM \n" +
            "        activity_collect\n" +
            "    GROUP BY \n" +
            "        aid\n" +
            ") ac ON a.aid = ac.aid;")
    List<Activity> findAllActivities();

    //查询点赞数和收藏数
    @Select("SELECT \n" +
            "    a.aid,\n" +
            "    COALESCE(COUNT(DISTINCT al.lid), 0) AS likes,\n" +
            "    COALESCE(COUNT(DISTINCT ac.cid), 0) AS collections\n" +
            "FROM \n" +
            "    activity a\n" +
            "LEFT JOIN \n" +
            "    activity_like al ON a.aid = al.aid\n" +
            "LEFT JOIN \n" +
            "    activity_collect ac ON a.aid = ac.aid\n" +
            "WHERE \n" +
            "    a.aid = #{aid}\n" +
            "GROUP BY \n" +
            "    a.aid;")
    List<Activity> findCounts(String aid);

    //查找活动 根据keyword
    @Select("SELECT " +
            "    a.aid, " +
            "    a.atitle, " +
            "    a.type, " +
            "    a.location, " +
            "    a.aimg, " +
            "    a.start_time, " +
            "    COALESCE(al.likes, 0) AS likes, " +
            "    COALESCE(ac.collects, 0) AS collections " +
            "FROM " +
            "    activity a " +
            "LEFT JOIN ( " +
            "    SELECT " +
            "        aid, " +
            "        COUNT(*) AS likes " +
            "    FROM " +
            "        activity_like " +
            "    GROUP BY " +
            "        aid " +
            ") al ON a.aid = al.aid " +
            "LEFT JOIN ( " +
            "    SELECT " +
            "        aid, " +
            "        COUNT(*) AS collects " +
            "    FROM " +
            "        activity_collect " +
            "    GROUP BY " +
            "        aid " +
            ") ac ON a.aid = ac.aid " +
            "WHERE " +
            "    a.adscpt LIKE CONCAT('%', #{keyword}, '%') " +
            "    OR a.atitle LIKE CONCAT('%', #{keyword}, '%')")
    List<Activity> findAllActivities0(String keyword);

    //查找未审核活动
    @Select("select aid,atitle,aimg,status,location,start_time from activity where status=#{keyword}")
    List<Activity> findActivityUnreviewed(int keyword);

    //更新审核状态 活动表和审核表
    @Update("update activity set status=#{status} where aid=#{aid}")
    int updateActivity(@Param("status") String status,@Param("aid") String aid);

    //插入审核表
    @Insert("insert into content_audit(content_type,content_id,auditor_id,audit_time,audit_result,reject_reason)values(#{content_type},#{content_id},#{auditor_id},#{audit_time},#{audit_result},#{reject_reason})")
    int addAudit(Audit audit);

    //查找未审核动态
    @Select("select did,dtitle,media,status,type,create_time from dynamic where status=#{keyword}")
    List<Dynamic> findDynamic(int keyword);

    //查找所有动态
    @Select("select did,dtitle,media,status,type,create_time from dynamic")
    List<Dynamic> findAllDynamic();

    //查找动态 根据did
    @Select("SELECT d.did,d.dtitle,d.content,d.media,d.type,d.create_time,d.status,u.nickname \n" +
            "from dynamic d \n" +
            "LEFT JOIN user u \n" +
            "ON d.uid=u.uid\n" +
            "where d.did=#{did}")
    List<Dynamic> findDynamicDetails(String did);

    //更新审核状态 动态表和审核表
    @Update("update dynamic set status=#{status} where did=#{did}")
    int updateDynamic(@Param("status") String status, @Param("did") String did);


    //查询报名
    @Select("select s.sid,s.status,s.sign_time,s.remark,s.experience,a.aid,a.atitle,a.aimg,u.uid,u.nickname,u.tel from sign_up s \n" +
            "LEFT JOIN activity a ON s.aid=a.aid\n" +
            "LEFT JOIN user u ON s.uid=u.uid\n" +
            "where s.status=#{status}")
    List<Signup> findSignUp(String status);

    @Select("select s.sid,s.status,s.sign_time,s.remark,s.experience,a.aid,a.atitle,a.aimg,u.uid,u.nickname,u.tel from sign_up s \n" +
            "LEFT JOIN activity a ON s.aid=a.aid\n" +
            "LEFT JOIN user u ON s.uid=u.uid")
    List<Signup> findSignup0();

    //更新报名表 status
    @Update("update sign_up set status=#{status} where sid=#{sid}")
    int updateSignup(@Param("status") String status,@Param("sid") String sid);

    //查找用户信息
    @Select("select uid,avatar,nickname,tel,create_time,last_login_time,id_type from user")
    List<User> findAllUsers();

    //更新user表
    @Update("update user set id_type=#{id_type} where uid=#{uid}")
    int updateUser(@Param("id_type") String id_type,@Param("uid") String uid);

    @Update("update user set id_type=#{id_type} where tel=#{tel}")
    int updateUser0(@Param("id_type") String id_type,@Param("tel") String tel);
    // 查询我参加的所有活动，关联 activity_sign 表，根据 uid 筛选
    @Select("SELECT \n" +
            "    a.* ,\n" +
            "    COALESCE(al.likes, 0) AS likes,\n" +
            "    COALESCE(ac.collects, 0) AS collections\n" +
            "FROM \n" +
            "    activity a\n" +
            " JOIN sign_up asig ON a.aid = asig.aid AND asig.uid = #{uid}\n" +
            "LEFT JOIN (\n" +
            "    SELECT \n" +
            "        aid, \n" +
            "        COUNT(*) AS likes \n" +
            "    FROM \n" +
            "        activity_like \n" +
            "    GROUP BY \n" +
            "        aid\n" +
            ") al ON a.aid = al.aid\n" +
            "LEFT JOIN (\n" +
            "    SELECT \n" +
            "        aid, \n" +
            "        COUNT(*) AS collects \n" +
            "    FROM \n" +
            "        activity_collect\n" +
            "    GROUP BY \n" +
            "        aid\n" +
            ") ac ON a.aid = ac.aid;")
    List<Activity> findMyJoinedActivities(String uid);

    // 原来根据 keyword 查找活动的方法，可保留或按需调整
    @Select("SELECT " +
            "    a.*, " +
            "    COALESCE(al.likes, 0) AS likes, " +
            "    COALESCE(ac.collects, 0) AS collections " +
            "FROM " +
            "    activity a " +
            "LEFT JOIN ( " +
            "    SELECT " +
            "        aid, " +
            "        COUNT(*) AS likes " +
            "    FROM " +
            "        activity_like " +
            "    GROUP BY " +
            "        aid " +
            ") al ON a.aid = al.aid " +
            "LEFT JOIN ( " +
            "    SELECT " +
            "        aid, " +
            "        COUNT(*) AS collects " +
            "    FROM " +
            "        activity_collect " +
            "    GROUP BY " +
            "        aid " +
            ") ac ON a.aid = ac.aid " +
            "WHERE " +
            "    (a.adscpt LIKE CONCAT('%', #{keyword}, '%') " +
            "    OR a.atitle LIKE CONCAT('%', #{keyword}, '%')) " +
            "    AND a.uid = #{uid}")
    List<Activity> findMyActivities0(@Param("keyword") String keyword, @Param("uid") String uid);

}
