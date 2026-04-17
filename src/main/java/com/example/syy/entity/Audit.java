package com.example.syy.entity;

public class Audit {
    private String content_type;
    private String content_id;
    private String auditor_id;
    private String audit_time;
    private String audit_result;
    private String reject_reason;

    public Audit(String content_type, String content_id, String auditor_id, String audit_time, String audit_result, String reject_reason) {
        this.content_type = content_type;
        this.content_id = content_id;
        this.auditor_id = auditor_id;
        this.audit_time = audit_time;
        this.audit_result = audit_result;
        this.reject_reason = reject_reason;
    }

    public String getContent_type() {
        return content_type;
    }

    public void setContent_type(String content_type) {
        this.content_type = content_type;
    }

    public String getContent_id() {
        return content_id;
    }

    public void setContent_id(String content_id) {
        this.content_id = content_id;
    }

    public String getAuditor_id() {
        return auditor_id;
    }

    public void setAuditor_id(String auditor_id) {
        this.auditor_id = auditor_id;
    }

    public String getAudit_time() {
        return audit_time;
    }

    public void setAudit_time(String audit_time) {
        this.audit_time = audit_time;
    }

    public String getAudit_result() {
        return audit_result;
    }

    public void setAudit_result(String audit_result) {
        this.audit_result = audit_result;
    }

    public String getReject_reason() {
        return reject_reason;
    }

    public void setReject_reason(String reject_reason) {
        this.reject_reason = reject_reason;
    }
}
