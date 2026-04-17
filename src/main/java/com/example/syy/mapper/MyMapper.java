package com.example.syy.mapper;

import com.example.syy.entity.Activity;
import com.example.syy.entity.Dynamic;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MyMapper {
    /**
     * 查询用户参加的活动数量（审核通过的报名记录）
     * @param uid 用户ID
     * @return 参加活动数量
     */
    @Select("SELECT COUNT(*) FROM sign_up WHERE uid = #{uid} AND status = 1")
    int countJoinedActivities(@Param("uid") Long uid);

    /**
     * 查询用户发布的动态数量（审核通过的动态）
     * @param uid 用户ID
     * @return 发布动态数量
     */
    @Select("SELECT COUNT(*) FROM dynamic WHERE uid = #{uid} AND audit_status = 1")
    int countPublishedDynamics(@Param("uid") Long uid);

    /**
     * 查询用户发布的活动数量（发布中的活动）
     * @param uid 用户ID
     * @return 发布活动数量
     */
    @Select("SELECT COUNT(*) FROM activity WHERE uid = #{uid} AND status = 1")
    int countHostedActivities(@Param("uid") Long uid);

    // 测试动态点赞部分（单独查询）
    @Select("SELECT COUNT(dl.like_id) AS like_count " +
            "FROM dynamic_like dl " +
            "JOIN dynamic d ON dl.did = d.did " +
            "WHERE d.uid =#{uid} AND d.audit_status = 1")
    int testDynamicLikes(@Param("uid") Long uid);

    // 测试活动点赞部分（单独查询）
    @Select("SELECT COUNT(al.lid) " +
            "FROM activity_like al " +
            "JOIN activity a ON al.aid = a.aid " +
            "WHERE a.uid = #{uid} AND a.status = 1")
    int testActivityLikes(@Param("uid") Long uid);

    // 测试活动评论点赞部分（单独查询）
    @Select("SELECT COUNT(fl.flid) " +
            "FROM feedback_like fl " +
            "JOIN activity_feedback af ON fl.fbid = af.fbid " +
            "WHERE af.uid = #{uid} AND af.audit_status = 1")
    int testFeedbackLikes(@Param("uid") Long uid);
    /**
     * 查询用户参加的活动（最多3条）
     */
    @Select("SELECT a.*, su.status AS sign_status " +
            "FROM activity a " +
            "JOIN sign_up su ON a.aid = su.aid " +
            "WHERE su.uid = #{uid} AND su.status = 1 " +  // 已审核通过的报名
            "ORDER BY a.start_time DESC " +
            "LIMIT 3")
    List<Activity> getJoinedActivities(@Param("uid") Long uid);

    /**
     * 查询用户发布的动态（最多3条）
     */
    @Select("SELECT d.*, " +
            "COALESCE(l.like_count, 0) AS like_count, " +
            "COALESCE(c.comment_count, 0) AS comment_count " +
            "FROM dynamic d " +
            "LEFT JOIN (" +
            "  SELECT did, COUNT(*) AS like_count " +
            "  FROM dynamic_like " +
            "  GROUP BY did" +
            ") l ON d.did = l.did " +
            "LEFT JOIN (" +
            "  SELECT did, COUNT(*) AS comment_count " +
            "  FROM dynamic_comment " +
            "  GROUP BY did" +
            ") c ON d.did = c.did " +
            "WHERE d.uid = #{uid} AND d.audit_status = 1 " +  // 审核通过的动态
            "ORDER BY d.create_time DESC " +
            "LIMIT 3")
    List<Dynamic> getPublishedDynamics(@Param("uid") Long uid);

    /**
     * 查询用户发布的活动（最多2条），并统计每个活动的报名人数
     * @param uid 用户ID，用于筛选指定用户发布的活动
     * @return 包含活动信息及对应报名人数的 Activity 列表
     */
    @Select("SELECT " +
            "    a.*, " +
            "    COUNT(s.aid) AS current_people " +
            "FROM " +
            "    activity a " +
            "LEFT JOIN " +
            "    sign_up s ON a.aid = s.aid " +
            "WHERE " +
            "    a.uid = #{uid} " +
            "    AND a.status = 1 " +
            "GROUP BY " +
            "    a.aid " +
            "ORDER BY " +
            "    a.start_time DESC " +
            "LIMIT 2")
    List<Activity> getPublishedActivities(@Param("uid") Long uid);

    /**
     * 查询即将到来的活动（最多3条，根据当前时间筛选）
     */
    @Select("SELECT * FROM activity " +
            "WHERE start_time > NOW() " +  // 活动开始时间在当前时间之后
            "AND status = 1 " +  // 发布中的活动
            "ORDER BY start_time ASC " +
            "LIMIT 3")
    List<Activity> getUpcomingActivities();
}
