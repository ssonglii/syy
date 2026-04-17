package com.example.syy.controls;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.syy.entity.*;
import com.example.syy.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController//请求和响应都是json格式
@CrossOrigin//跨域注释
public class ActivityControls {
    @Autowired
    private ActivityService activityService;
    // 白名单：允许常见字符，仅限制高风险SQL操作符
    String safeChars = "^[a-zA-Z0-9\\u4e00-\\u9fa5\\s,.?!:;'\"()\\[\\]\\-+*/]+$";
    // 黑名单：特殊场景下禁止的高风险组合（需根据业务调整）
    String dangerousPatterns = "(?i).*(;|--|#|\\/\\*|\\*\\/|%00|UNION|SELECT|DROP|DELETE).*";
    private int countSpecialChars(String content) {
        return content.replaceAll("[a-zA-Z0-9\\u4e00-\\u9fa5\\s]", "").length();
    }


    @PostMapping("/GetComments")
    public String activityComments(@RequestBody String str){
        JSONObject json= JSON.parseObject(str);
        String aid=json.getString("aid");
        System.out.println(aid);
        List<ActivityComments> activityComments=activityService.activityComments(aid);
        return JSON.toJSONString(activityComments);
    }
    @RequestMapping("/GetReply")
    public String commentsReply(@RequestBody String str){
        JSONObject json=JSON.parseObject(str);
        String fbid=json.getString("keyword");
        List<CommentsReply> commentsReply=activityService.commentsReply(fbid);
        return JSON.toJSONString(commentsReply);
    }
    @RequestMapping("/addActivityComments")
    public int addActivityComments(@RequestBody ActivityComments activityComments){
        int keyword = 0;
        //校验敏感字段（如content）
        String content = activityComments.getContent();
        if (content != null && content.matches(dangerousPatterns)) {
            keyword=0;
            return keyword;
            //throw new IllegalArgumentException("评论内容包含非法字符，禁止提交");
        }
        // 3. 检查是否超出允许的字符范围（白名单）
        if (!content.matches(safeChars)) {
            // 允许一定比例的特殊字符，但需进一步验证
            if (countSpecialChars(content) > content.length() * 0.3) {
                keyword=2;
                return keyword;
                //throw new IllegalArgumentException("特殊字符比例过高");
            }
        }
        SensitiveWordFilter filter = new SensitiveWordFilter();
        if (content != null && filter.containsSensitiveWord(content)) {
            // 包含敏感词，禁止提交
            keyword=3;
            return keyword;
        }
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = now.format(formatter); // 例如：2023-10-15 14:30:25
        activityComments.setCreate_time(formattedDateTime);
        keyword=activityService.addActivityComments(activityComments);
        return keyword;
    }

    @RequestMapping("/addCommentsReply")
    public int addCommentsReply(@RequestBody CommentsReply commentsReply){
        int keyword = 0;
        //校验敏感字段（如content）
        String content = commentsReply.getReply_content();
        if (content != null && content.matches(dangerousPatterns)) {
            keyword=0;
            return keyword;
            //throw new IllegalArgumentException("评论内容包含非法字符，禁止提交");
        }
        // 3. 检查是否超出允许的字符范围（白名单）
        if (!content.matches(safeChars)) {
            // 允许一定比例的特殊字符，但需进一步验证
            if (countSpecialChars(content) > content.length() * 0.3) {
                keyword=2;
                return keyword;
                //throw new IllegalArgumentException("特殊字符比例过高");
            }
        }
        SensitiveWordFilter filter = new SensitiveWordFilter();
        if (content != null && filter.containsSensitiveWord(content)) {
            // 包含敏感词，禁止提交
            keyword=3;
            return keyword;
        }
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = now.format(formatter); // 例如：2023-10-15 14:30:25
        commentsReply.setReply_time(formattedDateTime);
        keyword=activityService.addCommentsReply(commentsReply);
        return keyword;
    }

    @RequestMapping("/addActivityLikes")
    public int addActivityLikes(@RequestBody ActivityLikes activityLikes){
        System.out.println(activityLikes.getAid());
        activityLikes.setCreate_time(String.valueOf(LocalDate.now()));
        return activityService.addActivityLikes(activityLikes);
    }

    @RequestMapping("/addActivityCollects")
    public int addActivityCollects(@RequestBody ActivityCollects activityCollects){
        activityCollects.setCreate_time(String.valueOf(LocalDate.now()));
        return activityService.addActivityCollects(activityCollects);
    }

    @RequestMapping("/findActivityLikes")
    public String findActivityLikes(@RequestBody String str){
        JSONObject json= JSON.parseObject(str);
        String aid=json.getString("aid");
        System.out.println(aid);
        String uid=json.getString("uid");
        List<ActivityLikes> activityLikes=activityService.findActivityLikes(aid,uid);
        System.out.println(JSON.toJSONString(activityLikes));
        return JSON.toJSONString(activityLikes);
    }

    @RequestMapping("/findActivityCollects")
    public String findActivityCollects(@RequestBody String str){
        JSONObject json= JSON.parseObject(str);
        String aid=json.getString("aid");
        //System.out.println(aid);
        String uid=json.getString("uid");
        List<ActivityCollects> activityCollects=activityService.findActivityCollects(aid,uid);
        return JSON.toJSONString(activityCollects);
    }

    @RequestMapping("/delActivityLikes")
    public int delActivityLikes(@RequestBody String str){
        JSONObject json= JSON.parseObject(str);
        String aid=json.getString("aid");
        //System.out.println(aid);
        String uid=json.getString("uid");
        return activityService.delActivityLikes(aid,uid);
    }

    @RequestMapping("/delActivityCollects")
    public int delActivityCollects(@RequestBody String str){
        JSONObject json= JSON.parseObject(str);
        String aid=json.getString("aid");
        //System.out.println(aid);
        String uid=json.getString("uid");
        return activityService.delActivityCollects(aid,uid);
    }

    @RequestMapping("/findCommentsLikes")
    public String findCommentsLikes(@RequestBody String str){
        JSONObject json= JSON.parseObject(str);
        String fbid=json.getString("fbid");
        System.out.println(fbid);
        String uid=json.getString("uid");
        List<CommentsLikes> commentsLikes=activityService.findCommentsLikes(fbid,uid);
        return JSON.toJSONString(commentsLikes);
    }

    @RequestMapping("/addCommentsLikes")
    public int addCommentsLikes(@RequestBody CommentsLikes commentsLikes){
        commentsLikes.setCreate_time(String.valueOf(LocalDate.now()));
        return activityService.addCommentsLikes(commentsLikes);
    }

    @RequestMapping("/delCommentsLikes")
    public int delCommentsLikes(@RequestBody String str){
        JSONObject json= JSON.parseObject(str);
        String fbid=json.getString("fbid");
        //System.out.println(aid);
        String uid=json.getString("uid");
        return activityService.delCommentsLikes(fbid,uid);
    }

    @RequestMapping("/findAllActivities")
    public List<Activity> findAllActivities(){
        return activityService.findAllActivities();
    }
    @RequestMapping("/findCounts")
    public List<Activity> findCounts(@RequestBody String str){
        JSONObject json= JSON.parseObject(str);
        String aid=json.getString("aid");
        List<Activity> activityList=activityService.findCounts(aid);
        return activityList;
    }

    @RequestMapping("/findAllActivities0")
    public List<Activity> findAllActivities0(@RequestBody String str){
        JSONObject json= JSON.parseObject(str);
        String keyword=json.getString("keyword");
        System.out.println(keyword);
        List<Activity> activityList=activityService.findAllActivities0(keyword);
        return activityList;
    }

    @PostMapping("/findAll")
    public List<Activity> findAll(@RequestBody String str) {
        JSONObject json = JSON.parseObject(str);
        String category = json.getString("category");
        String status = json.getString("status");
        List<Activity> activityList = null;
        int keyword=0;
        if (Objects.equals(category, "activity") && Objects.equals(status, "unreviewed")) {
            keyword = 0;
            activityList = activityService.findActivityUnreviewed(keyword);
        }else if(Objects.equals(category, "activity") && Objects.equals(status, "reviewed")){
            keyword = 1;
            activityList = activityService.findActivityUnreviewed(keyword);
        }else if(Objects.equals(category, "activity") && Objects.equals(status, "rejected")){
            keyword = 2;
            activityList = activityService.findActivityUnreviewed(keyword);
        }else if(Objects.equals(category, "activity") && Objects.equals(status, "all")){
            activityList = activityService.findAllActivities();
        }
        return activityList;
    }

    @PostMapping("/findAll0")
    public List<Dynamic> findDynamic(@RequestBody String str){
        JSONObject json = JSON.parseObject(str);
        String category = json.getString("category");
        String status = json.getString("status");
        System.out.println(category);
        List<Dynamic> dynamicList=null;
        int keyword;
        if(Objects.equals(category,"dynamic")&& Objects.equals(status,"unreviewed")){
            keyword=0;
            dynamicList=activityService.findDynamic(keyword);
        }else if(Objects.equals(category,"dynamic")&& Objects.equals(status,"reviewed")){
            keyword=1;
            dynamicList=activityService.findDynamic(keyword);
        }else if(Objects.equals(category,"dynamic")&& Objects.equals(status,"rejected")){
            keyword=2;
            dynamicList=activityService.findDynamic(keyword);
        }else if(Objects.equals(category,"dynamic")&& Objects.equals(status,"all")){
            dynamicList=activityService.findAllDynamic();
        }
        System.out.println(JSON.toJSONString(dynamicList));
        return dynamicList;
    }

    @PostMapping("/findAll1")
    public List<Signup> findSignup(@RequestBody String str){
        JSONObject json = JSON.parseObject(str);
        String category = json.getString("category");
        String status = json.getString("status");
        System.out.println(category);
        List<Signup> signups=null;
        int keyword;
        if(Objects.equals(category,"signup")&& Objects.equals(status,"unreviewed")){
            keyword=0;
            signups=activityService.findSignup(String.valueOf(keyword));
        }else if(Objects.equals(category,"signup")&& Objects.equals(status,"reviewed")){
            keyword=1;
            signups=activityService.findSignup(String.valueOf(keyword));
        }else if(Objects.equals(category,"signup")&& Objects.equals(status,"rejected")){
            keyword=2;
            signups=activityService.findSignup(String.valueOf(keyword));
        }else if(Objects.equals(category,"signup")&& Objects.equals(status,"all")){
            signups=activityService.findSignup0();
        }
        System.out.println(JSON.toJSONString(signups));
        return signups;
    }

    @RequestMapping("/findDynamicDetails")
    public String findDynamicDetails(@RequestBody String str){
        JSONObject json= JSON.parseObject(str);
        String did=json.getString("keyword");
        List<Dynamic> dynamicList = activityService.findDynamicDetails(did);
        return JSON.toJSONString(dynamicList);
    }

    @RequestMapping("/updateActivity")
    public int updateAudit(@RequestBody String str){
        JSONObject json = JSON.parseObject(str);
        String aid = json.getString("aid");
        String status=json.getString("status");
        return activityService.updateActivity(status,aid);
    }


    @PostMapping("/updateDynamic")
    public int updateAudit0(@RequestBody String str) {
        JSONObject json = JSON.parseObject(str);
        String did = json.getString("did");
        String status = json.getString("status");
        return activityService.updateDynamic(status, did);
    }
    @RequestMapping("/updateSignup")
    public int updateAudit2(@RequestBody String str){
        JSONObject json = JSON.parseObject(str);
        String sid = json.getString("sid");
        String status=json.getString("status");
        return activityService.updateSignup(status,sid);
    }

    @RequestMapping("/addAudit")
    public int addAudit(@RequestBody Audit audit){
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = now.format(formatter); // 例如：2023-10-15 14:30:25
        audit.setAudit_time(formattedDateTime);
        return activityService.addAudit(audit);
    }

    @RequestMapping("/findAllUsers")
    public String findAllUsers(){
        List<User> users=activityService.findAllUsers();
        return JSON.toJSONString(users);
    }

    @RequestMapping("/updateUser")
    public int updateUser(@RequestBody String str){
        JSONObject json = JSON.parseObject(str);
        String uid = json.getString("uid");
        String id_type=json.getString("id_type");
        System.out.println(uid);
        return activityService.updateUser(id_type,uid);
    }

    @RequestMapping("/submitUnbanRequest")
    public int updateUser0(@RequestBody String str){
        JSONObject json = JSON.parseObject(str);
        String tel = json.getString("tel");
        String id_type=json.getString("id_type");
        System.out.println(tel);
        return activityService.updateUser0(id_type,tel);
    }

    @RequestMapping("/findMyJoinedActivities")
    public List<Activity> findMyJoinedActivities(@RequestBody Map<String, String> requestBody) {
        String uid = requestBody.get("uid"); // 从请求体中获取 uid
        return activityService.findMyJoinedActivities(uid);
    }


    @RequestMapping("/findMyActivities0")
    public List<Activity> findMyActivities0(@RequestBody Map<String, String> requestBody) {
        String keyword = requestBody.get("keyword"); // 从请求体中获取关键词
        String uid = requestBody.get("uid"); // 从请求体中获取 uid
        return activityService.findMyActivities0(keyword, uid); // 传递 uid 给 Service
    }
}
