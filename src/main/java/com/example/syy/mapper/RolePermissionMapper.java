package com.example.syy.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RolePermissionMapper {

    /**
     * 根据用户ID查询角色编码
     */
    @Select("""
        SELECT r.rcode
        FROM user_role ur
        JOIN role r ON ur.role_id = r.rid
        WHERE ur.user_id = #{uid}
    """)
    List<String> selectRoleCodesByUid(@Param("uid") Integer uid);

    /**
     * 根据用户ID查询权限编码
     */
    @Select("""
        SELECT DISTINCT p.permission_code
        FROM user_role ur
        JOIN role_permission rp ON ur.role_id = rp.role_id
        JOIN permission p ON rp.permission_id = p.id
        WHERE ur.user_id = #{uid}
    """)
    List<String> selectPermissionCodesByUid(@Param("uid") Integer uid);
}