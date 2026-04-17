package com.example.syy.mapper;

import com.example.syy.entity.UserCert;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CertMapper {


    //用户达人认证信息提交**
    @Insert("insert into user_certification(uid,real_name,id_card,phone,email,expertise,outdoor_experience,skill_desc,id_card_front,id_card_back,experience_proofs,supplement_info,audit_remark,create_time,update_time,cert_type) " +
            "values(#{uid},#{real_name},#{id_card},#{phone},#{email},#{expertise},#{outdoor_experience},#{skill_desc},#{id_card_front},#{id_card_back},#{experience_proofs},#{supplement_info},#{audit_remark},#{create_time},#{update_time},#{cert_type})")
    int addUserCert(UserCert userCert);
    // 查询认证申请列表（带分页和条件过滤）
    @Select("<script>" +
            "SELECT uc.*, u.avatar, u.sex, u.nickname " +
            "FROM user_certification uc " +
            "JOIN user u ON uc.uid = u.uid " +
            "WHERE 1=1 " +
            "<if test=\"audit_status != null\">AND uc.audit_status = #{audit_status}</if> " +
            "<if test=\"cert_type != null and cert_type != ''\">AND uc.cert_type = #{cert_type}</if> " +
            "<if test=\"time_range != null and time_range != ''\">" +
            "   <choose>" +
            "       <when test=\"time_range == 'time1'\">AND uc.create_time &gt;= DATE_SUB(NOW(), INTERVAL 7 DAY)</when>" +
            "       <when test=\"time_range == 'time2'\">AND uc.create_time &gt;= DATE_SUB(NOW(), INTERVAL 1 MONTH)</when>" +
            "   </choose>" +
            "</if> " +
            "<if test=\"keyword != null and keyword != ''\">" +
            "   AND (uc.real_name LIKE CONCAT('%', #{keyword}, '%') " +
            "   OR uc.id_card LIKE CONCAT('%', #{keyword}, '%') " +
            "   OR uc.phone LIKE CONCAT('%', #{keyword}, '%') " +
            "   OR u.nickname LIKE CONCAT('%', #{keyword}, '%'))" +
            "</if> " +
            "ORDER BY uc.create_time DESC " +
            "LIMIT #{offset}, #{limit}" +
            "</script>")
    List<UserCert> selectCertList(@Param("audit_status") Integer audit_status,
                                  @Param("keyword") String keyword,
                                  @Param("cert_type") String cert_type,
                                  @Param("time_range") String time_range,
                                  @Param("offset") Integer offset,
                                  @Param("limit") Integer limit);

    // 查询认证申请总数
    @Select("<script>" +
            "SELECT COUNT(*) FROM user_certification uc " +
            "WHERE 1=1 " +
            "<if test=\"audit_status != null\">AND audit_status = #{audit_status}</if> " +
            "<if test=\"cert_type != null and cert_type != ''\">AND cert_type = #{cert_type}</if> " +
            "<if test=\"time_range != null and time_range != ''\">" +
            "   <choose>" +
            "       <when test=\"time_range == 'time1'\">AND create_time &gt;= DATE_SUB(NOW(), INTERVAL 7 DAY)</when>" +
            "       <when test=\"time_range == 'time2'\">AND create_time &gt;= DATE_SUB(NOW(), INTERVAL 1 MONTH)</when>" +
            "   </choose>" +
            "</if> " +
            "<if test=\"keyword != null and keyword != ''\">" +
            "   AND (real_name LIKE CONCAT('%', #{keyword}, '%') " +
            "   OR id_card LIKE CONCAT('%', #{keyword}, '%') " +
            "   OR phone LIKE CONCAT('%', #{keyword}, '%'))" +
            "</if>" +
            "</script>")
    int countCertList(@Param("audit_status") Integer audit_status,
                      @Param("keyword") String keyword,
                      @Param("cert_type") String cert_type,
                      @Param("time_range") String time_range);
    // 根据ID查询认证申请详情，联合查询user表获取用户头像、性别等信息
    @Select("SELECT uc.*, u.avatar, u.sex, u.nickname " +
            "FROM user_certification uc " +
            "JOIN user u ON uc.uid = u.uid " +
            "WHERE uc.cert_id = #{cert_id}")
    UserCert selectCertDetail(@Param("cert_id") String cert_id);




    // 更新认证状态
    @Update("UPDATE user_certification " +
            "SET audit_status = #{audit_status}, " +
            "audit_remark = #{audit_remark}, " +
            "update_time = NOW() " +
            "WHERE cert_id = #{cert_id}")
    int updateAuditStatus(@Param("cert_id") String cert_id,
                          @Param("audit_status") Integer audit_status,
                          @Param("audit_remark") String audit_remark);
}
