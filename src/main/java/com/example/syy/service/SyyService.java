package com.example.syy.service;

import com.example.syy.entity.*;
import com.example.syy.mapper.SyyMapper;
import com.example.syy.utils.CookieUtils;
import com.example.syy.utils.EncryptionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class SyyService {
    @Autowired
    private SyyMapper syyMapper;
    private PermissionService permissionService;

    public SyyService(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    //根据手机号和密码查询用户信息(登录）
    // 登录方法（修改后）
// 根据手机号和密码查询用户信息(登录），修改后
    // 根据手机号和密码查询用户信息(登录）
    public List<Map<String, Object>> findUserByid3(String tel, String pwd,
                                                   HttpServletRequest request,
                                                   HttpServletResponse response) {
        List<Map<String, Object>> result = new ArrayList<>();

        try {
            // 1. 检查频率限制
            User attempt = syyMapper.getLoginAttemptByTel(tel);
            if (attempt != null && attempt.getFailCount() >= 5) {
                long lastTime = attempt.getLastAttemptTime().getTime();
                long diffMinutes = (System.currentTimeMillis() - lastTime) / (1000 * 60);
                if (diffMinutes < 5) {
                    result.add(createErrorMap("FREQUENCY_LIMITED", "登录过于频繁，请5分钟后重试"));
                    return result;
                }
            }

            // 2. 查询用户
            List<Map<String, Object>> userList = syyMapper.findUserByid3(tel);
            if (userList.isEmpty()) {
                // 用户不存在，记录失败次数
                syyMapper.updateLoginFail(tel);
                result.add(createErrorMap("USER_NOT_FOUND", "该手机号未注册"));
                return result;
            }

            Map<String, Object> userMap = userList.get(0);
            String storedHashedPassword = (String) userMap.get("pwd");

            // 3. 验证密码（使用BCrypt）
            if (!EncryptionUtil.verifyPassword(pwd, storedHashedPassword)) {
                // 密码错误，记录失败次数
                syyMapper.updateLoginFail(tel);
                result.add(createErrorMap("PASSWORD_ERROR", "密码错误"));
                return result;
            }

            // 4. 登录成功：清除失败记录，设置Session和Cookie
            syyMapper.resetLoginFail(tel);
              //更新最后一次登录时间
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String  last_login_time  = now.format(formatter);
            System.out.println("这是最后一次登录时间"+last_login_time);
            syyMapper.updateLastLogin(last_login_time,tel);

            // 设置Session
            HttpSession session = request.getSession();
            Long uid = syyMapper.getUidByTel(tel);
            Integer perm = (Integer) userMap.get("perm");
            session.setAttribute("uid", uid);
            session.setAttribute("tel", tel);
            session.setAttribute("perm", perm);
            // 1. 存当前登录用户
            //session.setAttribute("loginUser", user);
            // 2. 查角色
            List<String> roleCodes = permissionService.getRoleCodesByUid(Math.toIntExact(uid));
            // 3. 查权限
            List<String> permissionCodes = permissionService.getPermissionCodesByUid(Math.toIntExact(uid));
            // 4. 存入 session
            session.setAttribute("roleCodes", roleCodes);
            session.setAttribute("permissionCodes", permissionCodes);
            System.out.println("用户角色role："+session.getAttribute("roleCodes"));
            System.out.println("用户权限permission："+session.getAttribute("permissionCodes"));
            // 使用 CookieUtils设置安全 Cookie (有效期7天)
            CookieUtils.addSecureCookie(request, response, "tel", tel, 60 * 60 * 24 * 7);

            // 移除密码字段，避免返回敏感信息
            userMap.remove("pwd");
            result.add(userMap);
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            result.add(createErrorMap("SYSTEM_ERROR", "登录失败，请稍后重试"));
            return result;
        }
    }

    // 从Session获取用户信息
//    public User getUserInfoFromSession(HttpServletRequest request) {
//        HttpSession session = request.getSession();
//        User user = new User();
//
//        if (session.getAttribute("uid") != null) {
//            user.setUid((Long) session.getAttribute("uid"));
//        }
//        if (session.getAttribute("perm") != null) {
//            user.setPerm((Integer) session.getAttribute("perm"));
//        }
//        if (session.getAttribute("tel") != null) {
//            user.setTel((String) session.getAttribute("tel"));
//        }
//        if (session.getAttribute("roleCodes") != null) {
//            List<String> roleCodes = (List<String>) session.getAttribute("roleCodes");
//            user.setroleCodes(roleCodes);
//        }
//
//        // 权限是 List<String>，不是 String！
//        if (session.getAttribute("permissionCodes") != null) {
//            List<String> permissionCodes = (List<String>) session.getAttribute("permissionCodes");
//            user.setpermissionCodes(permissionCodes);
//        }
//
//        return user;
//    }

    public User getUserInfoFromSession(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = new User();

        if (session.getAttribute("uid") != null) {
            user.setUid((Long) session.getAttribute("uid"));
        }
        if (session.getAttribute("perm") != null) {
            user.setPerm((Integer) session.getAttribute("perm"));
        }
        if (session.getAttribute("tel") != null) {
            user.setTel((String) session.getAttribute("tel"));
        }
        if (session.getAttribute("roleCodes") != null) {
            user.setRoleCodes((List<String>) session.getAttribute("roleCodes"));
        }
        if (session.getAttribute("permissionCodes") != null) {
            user.setPermissionCodes((List<String>) session.getAttribute("permissionCodes"));
        }

        return user;
    }
    // 创建错误响应Map
    private Map<String, Object> createErrorMap(String errorCode, String message) {
        Map<String, Object> errorMap = new HashMap<>();
        errorMap.put("errorCode", errorCode);
        errorMap.put("message", message);
        return errorMap;
    }
    //用户注册
    public int addUser(User user) {
        System.out.println("这是addUser的service");
        // 如果intr为空，则设置默认值
        if (user.getIntr() == null || user.getIntr().trim().isEmpty()) {
            user.setIntr("山友还没留下痕迹....");
        }
        return this.syyMapper.addUser(user);
    }



    // 添加根据UID查询的方法
    public List<User> findByUid(Long uid) {
        return syyMapper.findByUid(uid);
    }





    public User findUserByTelForReg(String tel) {
        List<User> userList = syyMapper.findUserBytel(tel);
        if (userList != null &&!userList.isEmpty()) {
            return userList.get(0);
        }
        return null;
    }


    //用户信息修改 保存没有？
    public int modifyUser(User user)
    {
        return   syyMapper.modifyUser(user);
    }
    public  List<User> findBytel(String tableName,String fieldName,String keyValue)
    {
        List<User> list=syyMapper.findBytel(tableName,fieldName,keyValue);
        for (User temp : list) {
            String avatar = temp.getAvatar();
            if (avatar != null && avatar.contains("|")) {
                // 只有当 pic 包含 "|" 时才进行分割
                String[] picParts = avatar.split("\\|", 2); // 使用正则表达式确保正确匹配 "|"，并限制分割次数为 2
                if (picParts.length > 1) {
                    // 取第二部分作为新的 pic 值
                    temp.setAvatar(picParts[1].trim()); // 去除可能存在的首尾空格
                }
            }
        }
        return list;
    }



    //活动发布
    public int addAct(Activity activity) {
        return syyMapper.addAct(activity);
    }
    //活动信息修改
    public int modifyAct(Activity activity)
    {
        return   syyMapper.modifyAct(activity);
    }

    public List<Activity> findByaid(String tableName, String fieldName, String keyValue) {
        List<Activity> list = syyMapper.findByaid(tableName, fieldName, keyValue);
        for (Activity temp : list) {
            String aimg = temp.getAimg();
            if (aimg != null && aimg.contains("|")) {
                String[] picParts = aimg.split("\\|", 2);
                if (picParts.length > 1) {
                    temp.setAimg(picParts[1].trim());
                }
            }
            System.out.println("查询到的活动aid值：" + temp.getAid());
        }
        return list;
    }
    //活动查询通过aid
    public List<Activity> findActByaid(String aid){
        return syyMapper.findActByaid(aid);
    }
    // 活动删除
    public int actDeleteByaid(String id) {
        return syyMapper.actDeleteByaid(id);
    }
    //活动查询前四个用于首页轮播
    public List<Activity> findActivity(){
        return syyMapper.findActivity();
    }

    public List<Activity> findActivity0(){return syyMapper.findActivity0();}
    //活动详情
    public List<Activity> findActivityDeatails(String aid){return syyMapper.findActivityDetails(aid);}
    //活动报名
    public int addSignup(Signup signup){
        return syyMapper.addSignup(signup);
    }

    //动态发布
    public int addDyn(Dynamic dynamic) {
        return syyMapper.addDyn(dynamic);
    }

    //动态全查询（社区展示）
    public List<Dynamic> findDynAll(int page, int pageSize, String category, String keyword) {
        page = Math.max(1, page);
        pageSize = Math.max(1, pageSize);
        int offset = (page - 1) * pageSize;

        if (category == null) category = "all";
        if (keyword == null) keyword = "";

        return syyMapper.findDynAll(category, keyword, offset, pageSize);
    }
    // SyyService.java

    public List<Dynamic> findDynAll1(Long uid, String category, String keyword, Integer page, Integer pageSize) {
        Integer offset = (page - 1) * pageSize;

        // 拼接动态 WHERE 条件
        StringBuilder where = new StringBuilder();
        List<String> conditions = new ArrayList<>();

        // 处理 category
        if (category != null && !"all".equals(category)) {
            conditions.add(String.format("d.type = '%s'", category));
        }
        // 处理 keyword
        if (keyword != null && !keyword.isEmpty()) {
            conditions.add(String.format("(d.dtitle LIKE '%%%s%%' OR d.content LIKE '%%%s%%')", keyword, keyword));
        }

        // 拼接成完整 WHERE 子句
        if (!conditions.isEmpty()) {
            where.append("WHERE ").append(String.join(" AND ", conditions));
        }

        return syyMapper.findDynAll1(uid, category, keyword, offset, pageSize, where.toString());
    }
    //查看我发布的动态
    public List<Dynamic> findDynAll2(Long uid, String category, String keyword, Integer page, Integer pageSize) {
        Integer offset = (page - 1) * pageSize;
        StringBuilder where = new StringBuilder();
        List<String> conditions = new ArrayList<>();

        // 添加发布者ID条件（关键修改）
        conditions.add(String.format("d.uid = %d", uid));

        // 处理category
        if (category != null && !"all".equals(category)) {
            conditions.add(String.format("d.type = '%s'", category));
        }
        // 处理keyword
        if (keyword != null && !keyword.isEmpty()) {
            conditions.add(String.format("(d.dtitle LIKE '%%%s%%' OR d.content LIKE '%%%s%%')", keyword, keyword));
        }

        if (!conditions.isEmpty()) {
            where.append("WHERE ").append(String.join(" AND ", conditions));
        }

        return syyMapper.findDynAll2(uid, category, keyword, offset, pageSize, where.toString());
    }

    //动态详情
    public List<Dynamic> findByDid(Long did) {
        return syyMapper.findByDid(did);
    }
    //动态评论发布
    public int addDcom(Dynamic_comment dcom) {
        return syyMapper.addDcom(dcom);
    }
    //动态评论查询显示
    public List<Dynamic_comment> findDcomBydid(String did){
        return syyMapper.findDcomBydid(did);
    }

//动态点赞+1
    public int addDlk(Dynamic_like dlk) {
        if (dlk.getLike_time() == null) {
            dlk.setLike_time((new Date()).toString());
        }
        return syyMapper.addDlk(dlk);
    }
    // 检查用户是否已点赞
    public boolean checkDlk(Long uid, Long did) {
        return syyMapper.checkDlk(uid, did);
    }
    // 动态点赞-1
    public int dlkDel(Long uid, Long did) {
        return syyMapper.dlkDel(uid, did);
    }


    //动态收藏+1
    public int addDcl(Dynamic_collect dcl) {
        if (dcl.getCollect_time() == null) {
            dcl.setCollect_time((new Date()).toString());
        }
        return syyMapper.addDcl(dcl);
    }
    // 检查用户是否已收藏
    public boolean checkDcl(Long uid, Long did) {
        return syyMapper.checkDcl(uid, did);
    }
    // 动态收藏-1
    public int dclDel(Long uid, Long did) {
        return syyMapper.dclDel(uid, did);
    }


    // 查询我的收藏动态记录列表
    /**
     * 查询我的收藏动态记录列表 - 支持分页、分类筛选和关键词搜索
     * @param uid 用户ID
     * @param page 页码
     * @param pageSize 每页数量
     * @param category 分类（all表示全部）
     * @param keyword 关键词
     * @return 收藏列表
     */
    public List<MyDynCollect> findMyDCl(Long uid, int page, int pageSize, String category, String keyword) {
        // 参数校验
        if (uid == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }

        // 确保页码和每页数量为正数
        page = Math.max(1, page);
        pageSize = Math.max(1, pageSize);

        // 处理默认值
        if (category == null) {
            category = "all";
        }

        if (keyword == null) {
            keyword = "";
        }

        // 计算偏移量
        int offset = (page - 1) * pageSize;

        return syyMapper.findMyDCl(uid, offset, pageSize, category, keyword);
    }
    // 查询我的收藏活动记录列表
    public List<MyActCollect> findMyACl(Long uid, int page, int pageSize, String category, String keyword) {
        // 参数校验
        if (uid == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }

        // 确保页码和每页数量为正数
        page = Math.max(1, page);
        pageSize = Math.max(1, pageSize);

        // 处理默认值
        if (category == null) {
            category = "all";
        }
        if (keyword == null) {
            keyword = "";
        }

        // 计算偏移量
        int offset = (page - 1) * pageSize;

        return syyMapper.findMyACl(uid, offset, pageSize, category, keyword);
    }


    // 获取点赞最多的前4条热门动态
    public List<Dynamic> findHotDynamics() {
        List<Dynamic> list = syyMapper.findHotDynamics();
        // 处理media字段：如果是视频，取第一张图作为封面；如果是图片列表，取第一张
        for (Dynamic dyn : list) {
            String media = dyn.getMedia();
            if (media != null && !media.isEmpty()) {
                String[] medias = media.split(",");
                if (medias.length > 0) {
                    String firstMedia = medias[0];
                    // 如果是视频，用默认封面；否则用第一张图
                    if (firstMedia.endsWith(".mp4") || firstMedia.endsWith(".mov") ||
                            firstMedia.endsWith(".avi") || firstMedia.endsWith(".wmv") || firstMedia.endsWith(".mkv")) {
                        dyn.setMedia("/static/image/img/cover1.png"); // 你的默认视频封面
                    } else {
                        dyn.setMedia(firstMedia);
                    }
                }
            } else {
                dyn.setMedia("/static/image/img/default_dyn.png"); // 无媒体时的默认图
            }
        }
        return list;
    }





}
