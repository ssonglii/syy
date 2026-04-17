package com.example.syy.utils;

import javax.servlet.http.HttpSession;
import java.util.List;

public class PermissionUtils {
    public static boolean hasPermission(HttpSession session, String permissionCode) {
        List<String> permissionCodes = (List<String>) session.getAttribute("permissionCodes");
        if (permissionCodes == null) {
            return false;
        }
        return permissionCodes.contains(permissionCode);
    }
    public static boolean hasRole(HttpSession session, String roleCode) {
        List<String> roleCodes = (List<String>) session.getAttribute("roleCodes");
        if (roleCodes == null) {
            return false;
        }
        return roleCodes.contains(roleCode);
    }
}
