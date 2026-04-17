package com.example.syy.service;

import com.example.syy.entity.UserCert;
import com.example.syy.mapper.CertMapper;
import com.example.syy.mapper.SyyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CertService {
    @Autowired
    private CertMapper certMapper;

    //用户达人认证信息提交
    public int addUserCert(UserCert userCert) {
        System.out.println("这是addUserCert的service");

        return this.certMapper.addUserCert(userCert);
    }
    // 获取认证申请列表（带分页）
    public List<UserCert> getCertList(Integer audit_status, String keyword,
                                      String cert_type, String time_range,
                                      int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        return certMapper.selectCertList(audit_status, keyword, cert_type, time_range, offset, pageSize);
    }

    // 获取认证申请总数
    public int countCertList(Integer audit_status, String keyword,
                             String cert_type, String time_range) {
        return certMapper.countCertList(audit_status, keyword, cert_type, time_range);
    }
    // 获取认证详情
    public UserCert getCertDetail(String cert_id) {
        return certMapper.selectCertDetail(cert_id);
    }



    // 审核认证申请
    public int auditCert(String cert_id, Integer audit_status, String audit_remark) {
        return certMapper.updateAuditStatus(cert_id, audit_status, audit_remark);
    }


}
