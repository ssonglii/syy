package com.example.syy.service;

import java.util.List;

public interface PermissionService {
    List<String> getRoleCodesByUid(Integer uid);
    List<String> getPermissionCodesByUid(Integer uid);
}
