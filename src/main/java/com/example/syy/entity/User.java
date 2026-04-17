package com.example.syy.entity;

import com.example.syy.service.PermissionService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class User {
    private Long uid;
    private String pwd;
    private String nickname;
    private String tel;
    private String encryptedTel;

    private String email;
    private String avatar;
    private String sex;
    private int perm;
    private String birth;
    private String fav;
    private String addr;
    private String intr;
    private String create_time;
    private String last_login_time;
    private int failCount; // 失败次数
    private Timestamp lastAttemptTime; // 最后一次尝试时间
    private String encryptedPwd; // 存储加密后的密码
      private String keyHash;

      private String  id_type;

      private List<String> roleCodes;

      private List<String> permissionCodes;



    public User() {
        pwd="123456";
        sex="未知";birth="2001-01-01";
        perm=1;fav="登山";intr="山友还没留下痕迹";
        // 获取当前当地时间并格式化为字符串，赋值给create_time
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.create_time = now.format(formatter);
        this.id_type="1";
        roleCodes=null;
        permissionCodes=null;
    }

    public User(Long uid, String pwd, String nickname, String tel, String encryptedTel, String email, String avatar, String sex, int perm, String birth, String fav, String addr, String intr, String create_time, String last_login_time, int failCount, Timestamp lastAttemptTime, String encryptedPwd, String keyHash) {
        this.uid = uid;
        this.pwd = pwd;
        this.nickname = nickname;
        this.tel = tel;
        this.encryptedTel = encryptedTel;
        this.email = email;
        this.avatar = avatar;
        this.sex = sex;
        this.perm = perm;
        this.birth = birth;
        this.fav = fav;
        this.addr = addr;
        this.intr = intr;
        this.create_time = create_time;
        this.last_login_time = last_login_time;
        this.failCount = failCount;
        this.lastAttemptTime = lastAttemptTime;
        this.encryptedPwd = encryptedPwd;
        this.keyHash = keyHash;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public int getPerm() {
        return perm;
    }

    public void setPerm(int perm) {
        this.perm = perm;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public String getFav() {
        return fav;
    }

    public void setFav(String fav) {
        this.fav = fav;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getIntr() {
        return intr;
    }

    public void setIntr(String intr) {
        // 如果传入值为空，则使用默认值
        this.intr = intr == null || intr.trim().isEmpty() ?
                "山友还没留下痕迹..." : intr.trim();
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getLast_login_time() {
        return last_login_time;
    }

    public void setLast_login_time(String last_login_time) {
        this.last_login_time = last_login_time;
    }

    public int getFailCount() {
        return failCount;
    }

    public void setFailCount(int failCount) {
        this.failCount = failCount;
    }

    public Date getLastAttemptTime() {
        return lastAttemptTime;
    }

    public String getEncryptedPwd() {
        return encryptedPwd;
    }

    public void setEncryptedPwd(String encryptedPwd) {
        this.encryptedPwd = encryptedPwd;
    }

    public String getEncryptedTel() {
        return encryptedTel;
    }

    public void setEncryptedTel(String encryptedTel) {
        this.encryptedTel = encryptedTel;
    }

    public String getKeyHash() {
        return keyHash;
    }

    public void setKeyHash(String keyHash) {
        this.keyHash = keyHash;
    }

    public void setLastAttemptTime(Timestamp lastAttemptTime) {
        this.lastAttemptTime = lastAttemptTime;
    }

    public String getId_type() {
        return id_type;
    }

    public void setId_type(String id_type) {
        this.id_type = id_type;
    }

    public List<String> getRoleCodes() {
        return roleCodes;
    }
    public void setRoleCodes(List<String> roleCodes) {
        this.roleCodes = roleCodes;
    }
    public List<String> getPermissionCodes() {
        return permissionCodes;
    }
    public void setPermissionCodes(List<String> permissionCodes) {
        this.permissionCodes = permissionCodes;
    }
}
