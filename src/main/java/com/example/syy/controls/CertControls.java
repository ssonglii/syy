package com.example.syy.controls;

import com.example.syy.entity.UserCert;
import com.example.syy.service.CertService;
import com.example.syy.service.SecurityService;
import com.example.syy.service.SyyService;
import com.example.syy.utils.MyTools;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
@RestController//请求和响应都是json格式
@CrossOrigin//跨域注释
public class CertControls {

    @Autowired
    public CertControls(CertService certService, SecurityService securityService) {
        this.certService = certService;
        this.securityService = securityService;
    }

    @Value("${upload.path}")//从系统配置文件中直接取文件路径,以下的方法都共用它
    private String uploadPath;
    private MyTools myTools;

    private final CertService certService;
    private final SecurityService securityService;
    //用户达人认证信息提交** 带文件

    @PostMapping("/certaddAll")
    public int certaddAll(HttpServletRequest request, HttpSession session)
            throws IOException, ServletException, NoSuchFieldException, IllegalAccessException, ParseException {
        UserCert model0 = new UserCert();
        Collection<Part> parts = request.getParts();
        Enumeration<String> paramNames = request.getParameterNames();
        // 从 session 获取用户 ID，校验登录状态
        Long uid = (Long) session.getAttribute("uid");
        if (uid == null) {
            return -1;
        }
        model0.setUid(uid);
        // 设置创建时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        model0.setCreate_time(sdf.format(new Date()));
        // 定义允许的文件扩展名白名单
        Set<String> allowedExtensions = new HashSet<>(Arrays.asList(
                // 图片格式
                "jpg", "jpeg", "png", "gif", "bmp", "webp", "tiff", "svg",
                // 视频格式
                "mp4", "mov", "avi", "wmv", "mkv", "flv", "mpeg", "3gp",
                // 文档格式
                "pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "txt", "rtf",
                // 压缩格式
                "zip", "rar", "7z", "gz", "tar",
                // 其他常见格式
                "xml", "json", "csv"
        ));
        // 用于存储不同文件字段对应的文件路径集合
        Map<String, List<String>> fileFieldPathsMap = new HashMap<>();
        for (Part part : parts) {
            if (part.getContentType() == null) {
                // 处理非文件类型参数（普通表单字段）
                while (paramNames.hasMoreElements()) {
                    String paramName = paramNames.nextElement();
                    String[] paramValues = request.getParameterValues(paramName);
                    if (paramValues.length > 1) {
                        // 处理多选（如爱好等，多个值用 - 拼接）
                        StringBuilder multiValue = new StringBuilder();
                        for (String val : paramValues) {
                            multiValue.append(val).append("-");
                        }
                        if (multiValue.length() > 0) {
                            multiValue.setLength(multiValue.length() - 1);
                        }
                        setEntityField0(model0, paramName, multiValue.toString());
                    } else {
                        // 处理单个值参数
                        setEntityField0(model0, paramName, paramValues[0]);
                    }
                }
            } else {
                // 处理文件上传
                String filename = part.getSubmittedFileName();
                String fieldName = part.getName();

                if (filename != null && filename.length() >= 3) {
                    // 提取文件扩展名并转换为小写
                    String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();

                    // 校验文件扩展名是否在允许列表中
                    if (!allowedExtensions.contains(extension)) {
                        throw new IllegalArgumentException("不允许的文件类型: " + extension +
                                "，仅支持: " + String.join(", ", allowedExtensions));
                    }
                    // 安全处理：防止路径穿越攻击（移除路径信息，只保留文件名）
                    filename = FilenameUtils.getName(filename);
                    // 生成安全的文件名（使用UUID前缀防止覆盖）
                    filename = UUID.randomUUID().toString().substring(0, 5) + filename;
                    // 保存文件到服务器的指定目录下（例如：/static/image/upload/proofs）
                    part.write(uploadPath + "\\proofs\\" + filename);
                    // 构建文件访问路径
                    String newFilename = "/static/image/upload/proofs/" + filename;
                    // 将文件路径按字段名分类存储
                    fileFieldPathsMap.computeIfAbsent(fieldName, k -> new ArrayList<>()).add(newFilename);
                }
            }
        }

        // 遍历文件字段路径映射，将不同字段的文件路径分别设置到实体类中
        for (Map.Entry<String, List<String>> entry : fileFieldPathsMap.entrySet()) {
            String fieldName = entry.getKey();
            List<String> paths = entry.getValue();
            // 将多个路径用逗号拼接
            String pathsStr = String.join(",", paths);
            setEntityField0(model0, fieldName, pathsStr);
        }

        return certService.addUserCert(model0);
    }

    private void setEntityField0(UserCert model, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = model.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        if (field.getType() == Long.class) {
            field.set(model, Long.valueOf(value.toString()));
        } else if (field.getType() == Integer.class) {
            field.set(model, Integer.valueOf(value.toString()));
        } else {
            field.set(model, value);
        }
    }
    // 获取认证申请列表
    @GetMapping("/getClist")
    public Map<String, Object> getCertList(
            @RequestParam(required = false) Integer audit_status,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String cert_type,
            @RequestParam(required = false) String time_range,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {

        List<UserCert> list = certService.getCertList(audit_status, keyword, cert_type, time_range, pageNum, pageSize);
        int total = certService.countCertList(audit_status, keyword, cert_type, time_range);

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        return result;
    }

    // 获取认证详情
    @GetMapping("/detail/{cert_id}")
    public UserCert getCertDetail(@PathVariable String cert_id) {
        return certService.getCertDetail(cert_id);
    }

    // 审核认证申请
    @PostMapping("/certAudit")
    public int auditCert(@RequestBody Map<String, Object> params) {
        String cert_id = (String) params.get("cert_id");
        Integer audit_Status = (Integer) params.get("audit_status");
        String audit_remark = (String) params.get("audit_remark");

        return certService.auditCert(cert_id, audit_Status, audit_remark);
    }


}
