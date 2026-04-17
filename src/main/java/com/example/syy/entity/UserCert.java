package com.example.syy.entity;

   public class UserCert {
   private  String cert_id;//用户认证申请ID 数据库自增
   private  Long uid;//用户ID 关联用户表ID
   private String avatar;
   private String sex;
   private String nickname;
   private  String real_name;//真实姓名
   private  String id_card;//身份证号
    private  String cert_type;//认证类型
   private  String phone;//手机号
   private  String email;//邮箱地址
   private  String expertise;//特长技能描述
   private  String outdoor_experience;//户外经验描述
   private  String skill_desc;//技能描述
   private  String id_card_front;//身份证正面照地址
   private  String id_card_back;//身份证反面照地址
   private  String experience_proofs;//经验证明材料地址
   private  String supplement_info;//补充信息描述
   private  int audit_status;//审核状态 0:待审核，1：通过，2：不通过
   private  String audit_remark;//审核备注信息
   private  String create_time;//创建时间
   private  String update_time;//状态更新时间

    public UserCert() {
        this.audit_status=0;
    }


       public UserCert(String cert_id, Long uid, String avatar, String sex, String nickname, String real_name, String id_card, String cert_type, String phone, String email, String expertise, String outdoor_experience, String skill_desc, String id_card_front, String id_card_back, String experience_proofs, String supplement_info, int audit_status, String audit_remark, String create_time, String update_time) {
           this.cert_id = cert_id;
           this.uid = uid;
           this.avatar = avatar;
           this.sex = sex;
           this.nickname = nickname;
           this.real_name = real_name;
           this.id_card = id_card;
           this.cert_type = cert_type;
           this.phone = phone;
           this.email = email;
           this.expertise = expertise;
           this.outdoor_experience = outdoor_experience;
           this.skill_desc = skill_desc;
           this.id_card_front = id_card_front;
           this.id_card_back = id_card_back;
           this.experience_proofs = experience_proofs;
           this.supplement_info = supplement_info;
           this.audit_status = audit_status;
           this.audit_remark = audit_remark;
           this.create_time = create_time;
           this.update_time = update_time;
       }

       public String getCert_id() {
        return cert_id;
    }

    public void setCert_id(String cert_id) {
        this.cert_id = cert_id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    public String getId_card() {
        return id_card;
    }

    public void setId_card(String id_card) {
        this.id_card = id_card;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    public String getOutdoor_experience() {
        return outdoor_experience;
    }

    public void setOutdoor_experience(String outdoor_experience) {
        this.outdoor_experience = outdoor_experience;
    }

    public String getSkill_desc() {
        return skill_desc;
    }

    public void setSkill_desc(String skill_desc) {
        this.skill_desc = skill_desc;
    }

    public String getId_card_front() {
        return id_card_front;
    }

    public void setId_card_front(String id_card_front) {
        this.id_card_front = id_card_front;
    }

    public String getId_card_back() {
        return id_card_back;
    }

    public void setId_card_back(String id_card_back) {
        this.id_card_back = id_card_back;
    }

    public String getExperience_proofs() {
        return experience_proofs;
    }

    public void setExperience_proofs(String experience_proofs) {
        this.experience_proofs = experience_proofs;
    }

    public String getSupplement_info() {
        return supplement_info;
    }

    public void setSupplement_info(String supplement_info) {
        this.supplement_info = supplement_info;
    }

    public int getAudit_status() {
        return audit_status;
    }

    public void setAudit_status(int audit_status) {
        this.audit_status = audit_status;
    }

    public String getAudit_remark() {
        return audit_remark;
    }

    public void setAudit_remark(String audit_remark) {
        this.audit_remark = audit_remark;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
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

       public String getNickname() {
           return nickname;
       }

       public void setNickname(String nickname) {
           this.nickname = nickname;
       }

       public String getCert_type() {
           return cert_type;
       }

       public void setCert_type(String cert_type) {
           this.cert_type = cert_type;
       }
   }
