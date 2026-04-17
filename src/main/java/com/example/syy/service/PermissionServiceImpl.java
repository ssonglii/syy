package com.example.syy.service;

import com.example.syy.mapper.RolePermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    private RolePermissionMapper rolePermissionMapper;

    @Override
    public List<String> getRoleCodesByUid(Integer uid) {
        return rolePermissionMapper.selectRoleCodesByUid(uid);
    }

    @Override
    public List<String> getPermissionCodesByUid(Integer uid) {
        return rolePermissionMapper.selectPermissionCodesByUid(uid);
    }
}
