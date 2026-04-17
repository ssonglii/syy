package com.example.syy.mapper;

import com.example.syy.entity.*;
import org.apache.ibatis.annotations.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Mapper
public interface SyyMapper {

    // 更新用户登录失败次数和时间
    @Update("UPDATE User SET fail_count = fail_count + 1, last_attempt_time = NOW() WHERE tel = #{tel}")
    int updateLoginFail(String tel);

    // 重置用户登录失败记录（登录成功时）
    @Update("UPDATE User SET fail_count = 0, last_attempt_time = NULL WHERE tel = #{tel}")
    int resetLoginFail(String tel);

    // 更新用户最后登录时间##
    @Update("UPDATE User SET last_login_time = #{last_login_time} WHERE tel = #{tel}")
    int updateLastLogin( String last_login_time,String tel);

    // 查询用户登录尝试记录
    @Select("SELECT fail_count  AS failCount, last_attempt_time AS lastAttemptTime FROM User WHERE tel = #{tel}")
    User getLoginAttemptByTel(String tel);
    //用户信息全查询？(登录)
    @Select("SELECT  uid, tel, nickname,CONVERT(id_type,CHAR(5)) AS id_type, pwd, perm, birth FROM User where tel=#{tel}")
    List<Map<String, Object>> findUserByid3(String tel);



    // 添加获取UID的方法
    @Select("SELECT uid FROM User WHERE tel=#{tel}")
    Long getUidByTel(String tel);
    // 从Session获取用户信息



    // 根据uid查询用户
    @Select("SELECT * FROM User WHERE uid = #{uid}")
    List<User> findByUid(Long uid);


    //根据手机号查找用户信息
    @Select("select * from User where tel=#{tel}")
    List<User> findUserBytel(String tel);
    // 添加根据手机号查询用户的方法(用于校验）
    @Select("SELECT * FROM User WHERE tel = #{tel}")
    User findUserByTel(String tel);
    //用户注册
    @Insert("insert into User(tel,pwd,nickname,sex,email,birth,avatar,fav,addr,intr,create_time)" +
            "values(#{tel},#{pwd},#{nickname},#{sex},#{email},#{birth},#{avatar},#{fav},#{addr},#{intr},#{create_time})")
    int addUser(User user);


    //查询并修改用户信息
    @Update("<script>update User set tel=#{tel},nickname=#{nickname},pwd=#{pwd},birth=#{birth},email=#{email}\"\"" +
            "<if test='avatar!=null'>,avatar=#{avatar}</if> "+
            ",sex=#{sex}, fav=#{fav}, addr=#{addr},intr=#{intr}  " +
            "where tel=#{tel}"+"</script>")
    int modifyUser(User user);
    @Select("select * from ${tableName} where ${fieldName}=#{keyValue}")
    List<User> findBytel(@Param("tableName") String tableName,@Param("fieldName") String fieldName,@Param("keyValue") String keyValue);


    //活动发布
    @Insert  ("insert into activity(uid,atitle,adscpt,type,create_time,aimg,start_time,end_time,sign_up_start,sign_up_end,max_people,location)values(#{uid},#{atitle},#{adscpt},#{type},#{create_time},#{aimg},#{start_time},#{end_time},#{sign_up_start},#{sign_up_end},#{max_people},#{location})")
    int addAct(Activity activity);

    //活动查询 通过aid
    @Select("<script>select * from Activity where 1=1  " +
            "<if test='aid!=null'> and aid=#{aid}</if>" +
            "</script>")
    List<Activity> findActByaid(String aid);

    //修改活动信息 通过aid 修改uid,atitle,adscpt,type,create_time,aimg,start_time,end_time,sign_up_start,sign_up_end,max_people,location
    @Update("<script>update Activity set  uid=#{uid},atitle=#{atitle},adscpt=#{adscpt},type=#{type} ,create_time=#{create_time},start_time=#{start_time},end_time=#{end_time},sign_up_start=#{sign_up_start},sign_up_end=#{sign_up_end},max_people=#{max_people},location=#{location}" +
            "<if test='aimg!=null'>,aimg=#{aimg}</if> "+
            "where aid=#{aid}"+"</script>")
    int modifyAct(Activity activity);

//    @Select("select * from ${tableName} where ${fieldName}=#{keyValue}")
//    List<Activity> findByaid(String tableName,String fieldName, String keyValue);

    // 改为参数化查询，只允许固定字段名
    @Select("SELECT * FROM ${tableName} WHERE ${fieldName} = #{keyValue}")
    List<Activity> findByaid(@Param("tableName") String tableName,@Param("fieldName") String fieldName,@Param("keyValue") String keyValue);




    //活动删除
    @Delete("delete from activity where aid=#{aid}")
    int actDeleteByaid(String aid);


    //活动查询，前四个用于首页轮播（L）

    @Select("SELECT a.aid,a.atitle,a.type,a.max_people,a.location,a.aimg,a.start_time,a.end_time," +
            "COUNT(s.aid) AS current_people" +
            " FROM" +
            " activity a" +
            " LEFT JOIN " +
            "    sign_up s ON a.aid = s.aid " +
            " GROUP BY " +
            "    a.aid " +
            " ORDER BY " +
            "    start_time" +
            " LIMIT 4;")
    List<Activity> findActivity();

    @Select("SELECT aid,atitle,type,max_people,location,aimg,start_time,end_time FROM activity ORDER BY RAND() LIMIT 4")
    List<Activity> findActivity0();
    //活动详情
    @Select("SELECT a.aid,a.atitle,a.type,a.status,a.start_time,a.end_time,a.max_people,a.sign_up_start,a.sign_up_end,a.uid,a.location,a.adscpt,a.create_time,a.aimg,u.nickname\n" +
            " FROM activity a LEFT JOIN user u ON a.uid=u.uid where a.aid=#{aid}")
    List<Activity> findActivityDetails(String aid);
    //活动报名
    @Insert("insert into sign_up(uid,aid,sign_time,sname,remark,experience)values ((select uid from user where tel=#{tel}),#{aid},#{sign_time},#{sname},#{remark},#{experience})")
    int addSignup(Signup signup);


    //动态发布
    @Insert  ("insert into dynamic(did,uid,dtitle,content,media,type,create_time)values(#{did},#{uid},#{dtitle},#{content},#{media},#{type},#{create_time})")
    int addDyn(Dynamic dynamic);

    /**
     * 查询所有动态并包含点赞数、收藏数和评论数
     * @return 动态列表
     */
    //动态全查询(社区展示)
// SyyMapper.java
    @Select({
            "SELECT d.*, ",
            "u.avatar, ",
            "u.nickname, ",
            "COALESCE(l.like_count, 0) AS like_count, ",
            "COALESCE(c.collect_count, 0) AS collect_count, ",
            "COALESCE(cm.comment_count, 0) AS comment_count, ",
            "CASE WHEN dl.uid IS NOT NULL THEN 1 ELSE 0 END AS isLiked, ",
            "CASE WHEN dc.uid IS NOT NULL THEN 1 ELSE 0 END AS isCollected ",
            "FROM dynamic d ",
            "JOIN user u ON d.uid = u.uid ",
            "LEFT JOIN (SELECT did, COUNT(*) AS like_count FROM dynamic_like GROUP BY did) l ON d.did = l.did ",
            "LEFT JOIN (SELECT did, COUNT(*) AS collect_count FROM dynamic_collect GROUP BY did) c ON d.did = c.did ",
            "LEFT JOIN (SELECT did, COUNT(*) AS comment_count FROM dynamic_comment GROUP BY did) cm ON d.did = cm.did ",
            "LEFT JOIN dynamic_like dl ON d.did = dl.did AND dl.uid = #{uid} ",
            "LEFT JOIN dynamic_collect dc ON d.did = dc.did AND dc.uid = #{uid} ",
            "${dynamicWhere}",  // 动态拼接 WHERE 条件
            "ORDER BY d.create_time DESC ",
            "LIMIT #{offset}, #{pageSize}"
    })
    List<Dynamic> findDynAll1(
            @Param("uid") Long uid,
            @Param("category") String category,
            @Param("keyword") String keyword,
            @Param("offset") Integer offset,
            @Param("pageSize") Integer pageSize,
            @Param("dynamicWhere") String dynamicWhere  // 动态条件字符串
    );


    // 查看我发布的动态
    @Select({
            "SELECT d.*, ",
            "u.avatar, ",
            "u.nickname, ",
            "COALESCE(l.like_count, 0) AS like_count, ",
            "COALESCE(c.collect_count, 0) AS collect_count, ",
            "COALESCE(cm.comment_count, 0) AS comment_count, ",
            "CASE WHEN dl.uid IS NOT NULL THEN 1 ELSE 0 END AS isLiked, ",
            "CASE WHEN dc.uid IS NOT NULL THEN 1 ELSE 0 END AS isCollected ",
            "FROM dynamic d ",
            "JOIN user u ON d.uid = u.uid ",
            "LEFT JOIN (SELECT did, COUNT(*) AS like_count FROM dynamic_like GROUP BY did) l ON d.did = l.did ",
            "LEFT JOIN (SELECT did, COUNT(*) AS collect_count FROM dynamic_collect GROUP BY did) c ON d.did = c.did ",
            "LEFT JOIN (SELECT did, COUNT(*) AS comment_count FROM dynamic_comment GROUP BY did) cm ON d.did = cm.did ",
            "LEFT JOIN dynamic_like dl ON d.did = dl.did AND dl.uid = #{uid} ",
            "LEFT JOIN dynamic_collect dc ON d.did = dc.did AND dc.uid = #{uid} ",
            "${dynamicWhere}",
            "ORDER BY d.create_time DESC ",
            "LIMIT #{offset}, #{pageSize}"
    })
    List<Dynamic> findDynAll2(
            @Param("uid") Long uid,
            @Param("category") String category,
            @Param("keyword") String keyword,
            @Param("offset") Integer offset,
            @Param("pageSize") Integer pageSize,
            @Param("dynamicWhere") String dynamicWhere
    );

    @Select("<script>" +
            "SELECT " +
            "d.did, d.dtitle, d.content, d.type, " +
            "d.create_time , d.media, " +
            "u.avatar, u.nickname, " +
            "COALESCE(l.like_count, 0) AS like_count, " + // 来自分组统计结果
            "COALESCE(cm.comment_count, 0) AS comment_count, " +
            "COALESCE(c.collect_count, 0) AS collect_count " +
            "FROM dynamic d " +
            "JOIN user u ON d.uid = u.uid " +
            "LEFT JOIN (" + // 点赞统计子查询
            "    SELECT did, COUNT(*) AS like_count " +
            "    FROM dynamic_like " +
            "    GROUP BY did " +
            ") l ON d.did = l.did " +
            "LEFT JOIN (" + // 评论统计子查询
            "    SELECT did, COUNT(*) AS comment_count " +
            "    FROM dynamic_comment " +
            "    GROUP BY did " +
            ") cm ON d.did = cm.did " +
            "LEFT JOIN (" + // 收藏统计子查询
            "    SELECT did, COUNT(*) AS collect_count " +
            "    FROM dynamic_collect " +
            "    GROUP BY did " +
            ") c ON d.did = c.did " +
            "<where>" +
            "  <if test='category != \"all\" and category != null'>" +
            "    AND d.type = #{category}" +
            "  </if>" +
            "  <if test='keyword != null and keyword != \"\"'>" +
            "    AND (d.dtitle LIKE CONCAT('%', #{keyword}, '%') OR d.content LIKE CONCAT('%', #{keyword}, '%'))" +
            "  </if>" +
            "</where>" +
            "ORDER BY d.create_time DESC " +
            "LIMIT #{limit} OFFSET #{offset}" +
            "</script>")
    List<Dynamic> findDynAll(
            @Param("category") String category,
            @Param("keyword") String keyword,
            @Param("offset") int offset,
            @Param("limit") int limit
    );
    //动态详情
    // 动态详情（含统计数据和用户信息）
    @Select("SELECT " +
            "d.*, " +
            "u.avatar, " +
            "u.nickname, " +
            "COALESCE(l.like_count, 0) AS like_count, " +
            "COALESCE(c.collect_count, 0) AS collect_count, " +
            "COALESCE(cm.comment_count, 0) AS comment_count " +
            "FROM dynamic d " +
            "JOIN user u ON d.uid = u.uid " +  // 关联用户表
            "LEFT JOIN (" +
            "SELECT did, COUNT(*) AS like_count " +
            "FROM dynamic_like " +
            "GROUP BY did " +
            ") l ON d.did = l.did " +
            "LEFT JOIN (" +
            "SELECT did, COUNT(*) AS collect_count " +
            "FROM dynamic_collect " +
            "GROUP BY did " +
            ") c ON d.did = c.did " +
            "LEFT JOIN (" +
            "SELECT did, COUNT(*) AS comment_count " +
            "FROM dynamic_comment " +
            "GROUP BY did " +
            ") cm ON d.did = cm.did " +
            "WHERE d.did = #{did}")  // 使用 #{did} 接收参数
    List<Dynamic> findByDid(Long did);
    //动态评论发布
    @Insert  ("insert into dynamic_comment(did,uid,parent_id,content,create_time)values(#{did},#{uid},#{parent_id},#{content},#{create_time})")
    int addDcom(Dynamic_comment dcom);
    //动态评论查询显示
    @Select("<script>" +
            "SELECT dc.*, u.nickname, u.avatar " +
            "FROM dynamic_comment dc " +
            "LEFT JOIN user u ON dc.uid = u.uid " +
            "WHERE 1=1 " +
            "<if test='did != null'> AND dc.did = #{did}</if>" +
            "</script>")
    List<Dynamic_comment> findDcomBydid(String did);


    //动态点赞+1
    @Insert  ("insert into dynamic_like(uid,did,like_time)values(#{uid},#{did},#{like_time})")
    int addDlk(Dynamic_like dlk);
    // 检查用户是否已点赞
    @Select("SELECT EXISTS(SELECT 1 FROM dynamic_like WHERE uid = #{uid} AND did = #{did})")
    boolean checkDlk(@Param("uid") Long uid,@Param("did") Long did);

    //动态点赞-1
    @Delete("delete from dynamic_like where uid=#{uid} and did=#{did}")
    int dlkDel(@Param("uid") Long uid,@Param("did") Long did);


    //动态收藏+1
    @Insert  ("insert into dynamic_collect(uid,did,collect_time)values(#{uid},#{did},#{collect_time})")
    int addDcl(Dynamic_collect dcl);
    // 检查用户是否已后厨
    @Select("SELECT EXISTS(SELECT 1 FROM dynamic_collect WHERE uid = #{uid} AND did = #{did})")
    boolean checkDcl(@Param("uid") Long uid,@Param("did") Long did);

    //动态收藏-1
    @Delete("delete from dynamic_collect where uid=#{uid} and did=#{did}")
    int dclDel(@Param("uid") Long uid,@Param("did") Long did);






    /**
     * 查询我的收藏动态记录列表 - 支持分页、分类筛选和关键词搜索
     * @param uid 用户ID
     * @param offset 偏移量
     * @param limit 每页数量
     * @param category 分类（all表示全部）
     * @param keyword 关键词
     * @return 收藏列表
     */
    @Select("<script>" +
            "SELECT " +
            "dc.collect_time AS my_collect_time, " +
            "d.did, " +
            "d.dtitle, " +
            "d.content, " +
            "d.type, " +
            "d.create_time AS dynamicCreateTime, " +
            "d.media, " + // 添加 media 字段！
            "u.avatar, " +
            "u.nickname, " +
            "COALESCE(l.like_count, 0) AS like_count, " +
            "COALESCE(cm.comment_count, 0) AS comment_count, " +
            "COALESCE(c.collect_count, 0) AS collect_count " +
            "FROM dynamic_collect dc " +
            "JOIN dynamic d ON dc.did = d.did " +
            "JOIN user u ON d.uid = u.uid " +
            "LEFT JOIN (" +
            "SELECT did, COUNT(*) AS like_count " +
            "FROM dynamic_like " +
            "GROUP BY did " +
            ") l ON d.did = l.did " +
            "LEFT JOIN (" +
            "SELECT did, COUNT(*) AS comment_count " +
            "FROM dynamic_comment " +
            "GROUP BY did " +
            ") cm ON d.did = cm.did " +
            "LEFT JOIN (" +
            "SELECT did, COUNT(*) AS collect_count " +
            "FROM dynamic_collect " +
            "GROUP BY did " +
            ") c ON d.did = c.did " +
            "WHERE dc.uid = #{uid} " +
            "<if test='category != \"all\" and category != null'>" +
            "AND d.type = #{category} " +
            "</if>" +
            "<if test='keyword != null and keyword != \"\"'>" +
            "AND (d.dtitle LIKE CONCAT('%', #{keyword}, '%') " +
            "OR d.content LIKE CONCAT('%', #{keyword}, '%')) " +
            "</if>" +
            "ORDER BY dc.collect_time DESC " +
            "LIMIT #{limit} OFFSET #{offset}" +
            "</script>")
    List<MyDynCollect> findMyDCl(@Param("uid") Long uid,
                                 @Param("offset") int offset,
                                 @Param("limit") int limit,
                                 @Param("category") String category,
                                 @Param("keyword") String keyword);

    // 查询我的收藏活动记录列表
    /**
     * 查询我的收藏活动记录列表（支持分页、分类筛选和关键词搜索）
     * @param uid 用户ID
     * @param offset 偏移量
     * @param limit 每页数量
     * @param category 分类（日常登山，中长途旅游，其他，all=全部）
     * @param keyword 关键词（搜索标题或详情）
     * @return 收藏的活动列表
     */
    @Select("<script>" +
            "SELECT " +
            "ac.create_time AS myCollectTime, " +
            "a.aid, " +
            "a.atitle, " +
            "a.aimg, " +
            "a.adscpt, " +
            "a.type, " +
            "a.create_time AS activityCreateTime, " +
            "u.avatar, " +
            "u.nickname, " +
            "COALESCE(al.like_count, 0) AS like_count, " +
            "COALESCE(ar.comment_count, 0) AS comment_count, " +
            "COALESCE(ac_total.collect_count, 0) AS collect_count " +
            "FROM activity_collect ac " +
            "JOIN activity a ON ac.aid = a.aid " +
            "JOIN user u ON a.uid = u.uid " +
            "LEFT JOIN (" +
            "SELECT aid, COUNT(*) AS like_count " +
            "FROM activity_like " +
            "GROUP BY aid " +
            ") al ON a.aid = al.aid " +
            "LEFT JOIN (" +
            "SELECT fbid, COUNT(DISTINCT rid) AS comment_count " +
            "FROM activity_reply " +
            "GROUP BY fbid " +
            ") ar ON a.aid = ar.fbid " +
            "LEFT JOIN (" +
            "SELECT aid, COUNT(*) AS collect_count " +
            "FROM activity_collect " +
            "WHERE is_cancel = 0 " +
            "GROUP BY aid " +
            ") ac_total ON a.aid = ac_total.aid " +
            "WHERE ac.uid = #{uid} " +
            "<if test='category != \"all\" and category != null'>" +
            "AND a.type = #{category} " +
            "</if>" +
            "<if test='keyword != null and keyword != \"\"'>" +
            "AND (a.atitle LIKE CONCAT('%', #{keyword}, '%') " +
            "OR a.adscpt LIKE CONCAT('%', #{keyword}, '%')) " +
            "</if>" +
            "ORDER BY ac.create_time DESC " +
            "LIMIT #{limit} OFFSET #{offset}" +
            "</script>")
    List<MyActCollect> findMyACl(
            @Param("uid") Long uid,
            @Param("offset") Integer offset,
            @Param("limit") Integer limit,
            @Param("category") String category,
            @Param("keyword") String keyword
    );

// 260326新增
    // ====================== 评论频率统计 ======================
    @Select("SELECT COUNT(*) FROM dynamic_comment " +
            "WHERE uid = #{uid} " +
            "AND did = #{did} " +
            "AND create_time >= #{time}")
    int countUserCommentInLast10Min(
            @Param("uid") Long uid,
            @Param("did") Long did,
            @Param("time") String time
    );

    // 10分钟内相同评论计数
    @Select("SELECT COUNT(*) " +
            "FROM dynamic_comment " +
            "WHERE uid = #{uid} " +
            "AND did = #{did} " +
            "AND content = #{content} " +
            "AND create_time >= #{time}")
    int countSameCommentIn10Min(
            @Param("uid") Long uid,
            @Param("did") Long did,
            @Param("content") String content,
            @Param("time") String time
    );

    // 1小时内用户点赞次数（只统计创建时间）
    @Select("SELECT COUNT(*) FROM dynamic_like " +
            "WHERE uid = #{uid} " +
            "AND like_time >= #{time}")
    int countUserLikeIn1Hour(
            @Param("uid") Long uid,
            @Param("time") String time
    );

    // 1小时内用户收藏次数
    @Select("SELECT COUNT(*) FROM dynamic_collect " +
            "WHERE uid = #{uid} " +
            "AND collect_time >= #{time}")
    int countUserCollectIn1Hour(
            @Param("uid") Long uid,
            @Param("time") String time
    );

    // ====================== 发布频次限制 ======================
    // 统计用户1小时内发布的动态数量
    @Select("SELECT COUNT(*) FROM dynamic WHERE uid = #{uid} AND create_time >= #{oneHourAgo}")
    int countUserDynIn1Hour(@Param("uid") Long uid, @Param("oneHourAgo") String oneHourAgo);

    // 查询用户权限等级（完全匹配你的user表perm字段：1=普通，2=管理员，3=达人）
    @Select("SELECT perm FROM user WHERE uid = #{uid} LIMIT 1")
    int getUserPermStatus(@Param("uid") Long uid);


    // 查询点赞数最多的前4条动态（含用户信息、点赞/收藏/评论数）
    @Select("SELECT d.*, u.avatar, u.nickname, " +
            "COALESCE(l.like_count, 0) AS like_count, " +
            "COALESCE(c.collect_count, 0) AS collect_count, " +
            "COALESCE(cm.comment_count, 0) AS comment_count " +
            "FROM dynamic d " +
            "JOIN user u ON d.uid = u.uid " +
            "LEFT JOIN (SELECT did, COUNT(*) AS like_count FROM dynamic_like GROUP BY did) l ON d.did = l.did " +
            "LEFT JOIN (SELECT did, COUNT(*) AS collect_count FROM dynamic_collect GROUP BY did) c ON d.did = c.did " +
            "LEFT JOIN (SELECT did, COUNT(*) AS comment_count FROM dynamic_comment GROUP BY did) cm ON d.did = cm.did " +
            "ORDER BY like_count DESC " +
            "LIMIT 4")
    List<Dynamic> findHotDynamics();
}
