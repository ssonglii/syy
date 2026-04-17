package com.example.syy.controls;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.syy.entity.*;
import com.example.syy.mapper.SyyMapper;
import com.example.syy.service.SecurityService;
import com.example.syy.service.SyyService;
import com.example.syy.service.VerifyCodeService;
import com.example.syy.utils.ACAutomatonFilter;
import com.example.syy.utils.CsrfUtil;
import com.example.syy.utils.EncryptionUtil;
import com.example.syy.utils.MyTools;
import org.apache.commons.io.FilenameUtils;
import org.owasp.encoder.Encode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@RestController//请求和响应都是json格式
@CrossOrigin//跨域注释
public class controlSyy {

    @Autowired
    private MyTools myTools;
    @Autowired
    private SyyMapper syyMapper;
    @Autowired
    private VerifyCodeService verifyCodeService; // 注入验证码服务
    @Autowired
    private ACAutomatonFilter acAutomatonFilter;

    // 获取 CSRF Token（前端必须先调用这个）
    @GetMapping("/api/security/get-csrf-token")
    public String getCsrfToken(HttpServletRequest request) {
        return CsrfUtil.generateToken(request.getSession());
    }


    // 白名单：允许常见字符，仅限制高风险SQL操作符
    String safeChars = "^[a-zA-Z0-9\\u4e00-\\u9fa5\\s,.?!:;'\"()\\[\\]\\-+*/]+$";
    // 黑名单：特殊场景下禁止的高风险组合
    String dangerousPatterns = "(?i).*(;|--|#|\\/\\*|\\*\\/|%00|UNION|SELECT|DROP|DELETE).*";
    private int countSpecialChars(String content) {
        return content.replaceAll("[a-zA-Z0-9\\u4e00-\\u9fa5\\s]", "").length();
    }

    // 构造函数注入
    public controlSyy(SyyService syyService, SecurityService securityService) {
        this.syyService = syyService;
        this.securityService = securityService;
    }

    @Value("${upload.path}")//从系统配置文件中直接取文件路径,以下的方法都共用它
    private String uploadPath;


    private final SyyService syyService;
    private final SecurityService securityService;

//注册
    @PostMapping("/useraddAll")
    public Map<String, Object> useraddAll(HttpServletRequest request)
            throws Exception {
        Map<String, Object> result = new HashMap<>();

        // ===== 获取加密密钥 =====
        String keyHash = request.getParameter("keyHash");
        String encryptionKey = securityService.getAndValidateKey(keyHash, request);
        if (encryptionKey == null) {
            result.put("success", false);
            result.put("message", "安全验证失败，请刷新页面重试");
            return result;
        }
        // ===== 解密敏感数据 =====
        String encryptedPwd = request.getParameter("encryptedPwd");
        String encryptedTel = request.getParameter("encryptedTel");
        String decryptedPwd = EncryptionUtil.decryptAES(encryptedPwd, encryptionKey);
        String decryptedTel = EncryptionUtil.decryptAES(encryptedTel, encryptionKey);
        // 检查手机号是否已注册
        User existingUser = syyService.findUserByTelForReg(decryptedTel);
        if (existingUser != null) {
            result.put("success", false);
            result.put("message", "该手机已注册，请前往登录");
            return result;
        }
        User model0=new User();//准备实体，存放模型数据
        model0.setTel(decryptedTel);

        // ===== 密码哈希存储 =====
        String hashedPwd = EncryptionUtil.hashPassword(decryptedPwd);
        model0.setPwd(hashedPwd);

        Collection<Part> parts=request.getParts();
        boolean file0=false;
        Enumeration<String> values0=request.getParameterNames();//把所有的元素先在part循环之前取出来，不能放在for(Part part:parts)循环内部去了，否则多次重复
        String allFileName="";//如果有多个文件，文件名累加
        String fileNameInMode="";//保存实体模型中的文件列照片列的成员变量名，如photo列的名字
        for(Part part:parts) //去遍历每个表单控件部分
        {
            if (part.getContentType() == null)//非文件数据
            {
                while (values0.hasMoreElements())//把非文件数据遍历取出来
                {
                    // 以下是你原有的代码逻辑
                    String str0 = values0.nextElement();//数据项的name取出来
                    if (request.getParameterValues(str0).length > 1)  //前端多个数据项的name相同，如爱好，多文件同一个name
                    {
                        // 以下是你原有的代码逻辑
                        String muliName = ""; //累加同name的复选框数据
                        for (int i = 0; i < request.getParameterValues(str0).length; i++) {
                            muliName = muliName + request.getParameterValues(str0)[i] + "-";
                        }
                        muliName = muliName.substring(0, muliName.length() - 1);//去掉多子项最后“-”
                        //反射到实体中去
                        Field f0 = null;//用于反射取得实体的对应成员
                        f0 = model0.getClass().getDeclaredField(str0);
                        f0.setAccessible(true);//可反射操作实体私有成员
                        f0.set(model0, muliName);//把多个同name的参数写到实体成员中
                    } else //处理非同name的前端数据到实体
                    {

                        Field f0 = null;
                        f0 = model0.getClass().getDeclaredField(str0);
                        f0.setAccessible(true);
                        //要判断取出的数据类型，字符串没问题，日期和数值可能会出错，映射到实体要报错
                        if (f0.getType() == Integer.class)//准备映射整数
                            f0.set(model0, Integer.parseInt(request.getParameter(str0)));
                        else if (f0.getType() == Date.class) {//准备映射日期型，根据需要设置
                            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                            f0.set(model0, df.parse(request.getParameter(str0)));
                        } else //不需要处理类型其它数据
                            f0.set(model0, request.getParameter(str0));
                    }
                }
            } else //下面处理文件了，要考虑多个文件哟，每个文件也是一个part，所以要考虑文件名字累加到数据库
            {
                // 如果上传文件太大，要在application.properties系统属性中设置
                String filename = part.getSubmittedFileName();
                // 先要判断上传来的文件是否为空
                fileNameInMode = part.getName();//从part中取出该文件name（如name="photo")
                System.out.print("\n" + fileNameInMode + "\n");
                if (!(filename.equals(null) || filename.length() < 3)) {
                    System.out.print("\n这是前端过来的文件名字：" + filename + "\n");
                    filename = UUID.randomUUID().toString().substring(0,5) + filename;
                    // 保存文件到服务器，使用UUID+文件名
                    part.write(uploadPath + filename);
                    // 拼接数据库中要存储的文件名（带上路径前缀）
                    String newFilename = "/static/image/upload/" + filename;
                    allFileName +=newFilename+"｜"  ;//注意 是一个全角的竖线，半角下这些标点尽量不用，否则可能被识别为转义或控制字符
                    allFileName=allFileName.substring(0, allFileName.length()-1);
                    System.out.print("\n这是前端过来的所有文件名字：" + allFileName + "\n");
                }
            }
        }
        // 当所有part循环完了，再单独把若干个文件名累加后的结果映射到实体中去
        Field f0 = null;
        f0 = model0.getClass().getDeclaredField(fileNameInMode);
        f0.setAccessible(true);
        f0.set(model0, allFileName);

        int resultCode = syyService.addUser(model0);
        if (resultCode > 0) {
            result.put("success", true);
            result.put("message", "注册成功");
        } else {
            result.put("success", false);
            result.put("message", "注册失败，请稍后再试");
        }
// 注册成功后销毁密钥
        securityService.destroyEncryptionKey(request);
        return result;
    }
    // ===== 获取加密密钥接口 =====
    @GetMapping("/api/security/get-encryption-key")

    public String getEncryptionKey(HttpServletRequest request) {
        return securityService.generateEncryptionKey(request);
    }



    // 登录接口
    @GetMapping("/finduserBytel")
    public List<?> findUserByid3(
            @RequestParam("tel") String tel,
            @RequestParam("pwd") String pwd,
            @RequestParam(value = "verifyCode", required = false) String verifyCode,
            HttpServletRequest request,
            HttpServletResponse response) {

        // 1. 验证验证码
        if (verifyCode != null && !verifyCode.isEmpty()) {
            if (!verifyCodeService.validateCode(request, verifyCode)) {
                Map<String, String> errorMap = new HashMap<>();
                errorMap.put("errorCode", "VERIFY_CODE_ERROR");
                errorMap.put("message", "验证码错误，请重新输入");
                List<Map<String, String>> errorList = new ArrayList<>();
                errorList.add(errorMap);
                return errorList;
            }
        }
        // 2. 调用登录逻辑
        return syyService.findUserByid3(tel, pwd,request, response);
    }
    // 从Session获取用户信息接口
    @GetMapping("/getUserInfoFromSession")
    public User getUserInfoFromSession(HttpServletRequest request) {
        return syyService.getUserInfoFromSession(request);
    }



    @GetMapping("/findByUid")
    public List<User> findByUid(@RequestParam("uid") Long uid) {
        return syyService.findByUid(uid);
    }
    //用户注册
    @PostMapping({"/userAdd"})
    public Map<String, Object> addUser(@RequestBody User user) {
        System.out.println("control：" + user.getNickname());

        Map<String, Object> result = new HashMap<>();

        // 密码复杂度验证
        String pwdPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[~@$.!%*?&])[A-Za-z\\d~@$.!%*?&]{8,20}$";
        if (!user.getPwd().matches(pwdPattern)) {
            result.put("success", false);
            result.put("message", "密码格式不符合要求，请使用8-20位，包含大小写字母、数字和特殊符号的密码");
            return result;
        }

        // 检查手机号是否已注册
        User existingUser = syyMapper.findUserByTel(user.getTel());
        if (existingUser != null) {
            result.put("success", false);
            result.put("message", "该手机号已注册");
            return result;
        }
        String create_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        // 设置创建时间
        user.setCreate_time(create_time);

        int count = this.syyService.addUser(user);
        if (count > 0) {
            result.put("success", true);
            result.put("message", "注册成功");
        } else {
            result.put("success", false);
            result.put("message", "注册失败");
        }

        return result;
    }
    //用户信息查询 编辑
    @GetMapping("/findBytel")
    public List<User> findBytel(@RequestParam("tableName") String tableName,@RequestParam("fieldName") String fieldName,@RequestParam("keyValue") String keyValue) {
        return syyService.findBytel(tableName,fieldName,keyValue);
    }
    //用户信息修改
    @PostMapping("/modifyUser")
    public int modifyUser(HttpServletRequest request) throws NoSuchFieldException,ParseException,IllegalAccessException,ServletException,IOException {
        User user=new User();
        myTools.requestDataAandFilesToModel(request, user);
        return syyService.modifyUser(user);

    }

    //活动发布 带文件
    @PostMapping("/actaddAll")//请求方式url,前端是post请求，数据是json格式
    public int actaddAll(HttpServletRequest request)
            throws IOException, ServletException, NoSuchFieldException, IllegalAccessException, ParseException {
        Activity model0=new Activity();//准备实体，存放模型数据
        Collection<Part> parts=request.getParts();
        boolean file0=false;
        Enumeration<String> values0=request.getParameterNames();//把所有的元素先在part循环之前取出来，不能放在for(Part part:parts)循环内部去了，否则多次重复
        String allFileName="";//如果有多个文件，文件名累加
        String fileNameInMode="";//保存实体模型中的文件列照片列的成员变量名，如photo列的名字
        for(Part part:parts) //去遍历每个表单控件部分
        {
            if (part.getContentType() == null)//非文件数据
            {
                while (values0.hasMoreElements())//把非文件数据遍历取出来
                {
                    String str0 = (String) values0.nextElement();//数据项的name取出来
                    if (request.getParameterValues(str0).length > 1)  //前端多个数据项的name相同，如爱好，多文件同一个name
                    {
                        String muliName = ""; //累加同name的复选框数据
                        for (int i = 0; i < request.getParameterValues(str0).length; i++) {
                            muliName = muliName + request.getParameterValues(str0)[i] + "-";

                        }
                        muliName = muliName.substring(0, muliName.length() - 1);//去掉多子项最后“-”
                        //反射到实体中去
                        Field f0 = null;//用于反射取得实体的对应成员
                        f0 = model0.getClass().getDeclaredField(str0);
                        f0.setAccessible(true);//可反射操作实体私有成员
                        f0.set(model0, muliName);//把多个同name的参数写到实体成员中
                    } else //处理非同name的前端数据到实体
                    {
                        Field f0 = null;
                        f0 = model0.getClass().getDeclaredField(str0);
                        f0.setAccessible(true);
                        //要判断取出的数据类型，字符串没问题，日期和数值可能会出错，映射到实体要报错
                        if (f0.getType() == Integer.class)//准备映射整数
                            f0.set(model0, Integer.parseInt(request.getParameter(str0)));
                        else if (f0.getType() == Date.class) {//准备映射日期型，根据需要设置
                            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                            f0.set(model0, df.parse(request.getParameter(str0)));
                        } else //不需要处理类型其它数据
                            f0.set(model0, request.getParameter(str0));

                    }
                } } else //下面处理文件了，要考虑多个文件哟，每个文件也是一个part，所以要考虑文件名字累加到数据库
            {//如果上传文件太大，要在application.properties系统属性中设置
                String filename = part.getSubmittedFileName();
                //先要判断上传来的文件是否为空
                fileNameInMode = part.getName();//从part中取出该文件name（如name="photo")
                System.out.print("\n" + fileNameInMode + "\n");
                if (!(filename.equals(null) || filename.length() < 3)) {
                    System.out.print("\n这是前端过来的文件名字：" + filename + "\n");
                    filename = UUID.randomUUID().toString().substring(0,5) + filename;

                    part.write(uploadPath + filename);

                    /* allFileName +=filename+"｜"  ;//注意 是一个全角的竖线，半角下这些标点尽量不用，否则可能被识别为转义或控制字符*/
                    // 拼接数据库中要存储的文件名（带上路径前缀）
                    String newFilename = "/static/image/upload/" + filename;
                    allFileName +=newFilename+"｜"  ;//注意 是一个全角的竖线，半角下这些标点尽量不用，否则可能被识别为转义或控制字符
                    allFileName=allFileName.substring(0, allFileName.length()-1);
                    System.out.print("\n这是前端过来的所有文件名字：" + allFileName + "\n");
                }
            }
        }
        //当所有part循环完了，再单独把若干个文件名累加后的结果映射到实体中去
        Field f0 = null;
        f0 = model0.getClass().getDeclaredField(fileNameInMode);
        f0.setAccessible(true);
        f0.set(model0, allFileName);

        return  syyService.addAct(model0);
    }
    //活动修改
    @PostMapping("/modifyAct")
    public int modifyAct(HttpServletRequest request) throws NoSuchFieldException,ParseException,IllegalAccessException,ServletException,IOException {
        Activity activity=new Activity();
        myTools.requestDataAandFilesToModel(request, activity);//映射到实体xxs 空空空
        return syyService.modifyAct(activity); //如果前端没有传照片，原照片列不修改
    }
    //活动信息查询 编辑
    @GetMapping("/findByaid")
    public List<Activity> findByaid(
            @RequestParam("tableName") String tableName,
            @RequestParam("fieldName") String fieldName,
            @RequestParam("keyValue") String keyValue
    ) {
        System.out.println("接收到的查询参数: " + keyValue);
        return syyService.findByaid(tableName, fieldName, keyValue);
    }

    //活动查询通过aid
    @PostMapping("/ActQuery")
    public List<Activity> findActByaid(@RequestBody String str) {
        JSONObject json = JSON.parseObject(str);
        String id = json.getString("aid");
        if (id == "") id = null;
        System.out.print(id);
        return syyService.findActByaid(id);
    }
    //活动删除
    @PostMapping ("/actDeleteByaid")

    public int  actDeleteByaid(@RequestBody String str) {
        JSONObject json = JSON.parseObject(str);
        String id = json.getString("aid");
        if (id == "") id = null;
        //  System.out.print(id);
        return syyService.actDeleteByaid(id);
    }

    //活动查询前四个用于首页轮播
    @PostMapping("/index")
    public String findActivity(){
        // System.out.println("111");
        List<Activity> activityList= syyService.findActivity();
        //  System.out.println(JSON.toJSONString(activityList));
        return JSON.toJSONString(activityList);
    }
    @PostMapping("index0")
    public String findActivity0(){
        List<Activity> activityList=syyService.findActivity0();
        return JSON.toJSONString(activityList);
    }
    //活动详情
//    @PostMapping("/ActivityDetails")
//    public String findActivityDetails(@RequestBody String str) {
//        JSONObject json= JSON.parseObject(str);
//        String aid=json.getString("keyword");
//        List<Activity> activityDetails = syyService.findActivityDeatails(String.valueOf(aid));
//        return JSON.toJSONString(activityDetails);
//    }

        // 活动详情
        @PostMapping("/ActivityDetails")
        public String findActivityDetails(@RequestBody String str) {
            JSONObject json = JSON.parseObject(str);
            String aid = json.getString("keyword");
            List<Activity> activityDetails = syyService.findActivityDeatails(String.valueOf(aid));
            StringBuilder encodedOutput = new StringBuilder();
            encodedOutput.append("["); // 开始 JSON 数组

            for (int i = 0; i < activityDetails.size(); i++) {
                Activity activity = activityDetails.get(i);
                // 对需要输出到 HTML 的字段进行编码
                String encodedAid = Encode.forHtml(activity.getAid());
                String encodedNickname = Encode.forHtml(activity.getNickname());
                String encodedAtitle = Encode.forHtml(activity.getAtitle());
                String encodedType = Encode.forHtml(activity.getType());
                String encodedStatus = Encode.forHtml(activity.getStatus());
                String encodedStartTime = Encode.forHtml(activity.getStart_time());
                String encodedEndTime = Encode.forHtml(activity.getEnd_time());
                String encodedMaxPeople = Encode.forHtml(activity.getMax_people());
                String encodedSignUpStart = Encode.forHtml(activity.getSign_up_start());
                String encodedSignUpEnd = Encode.forHtml(activity.getSign_up_end());
                String encodedUid = Encode.forHtml(activity.getUid());
                String encodedLocation = Encode.forHtml(activity.getLocation());
                String encodedAdscpt = Encode.forHtml(activity.getAdscpt());
                String encodedCreateTime = Encode.forHtml(activity.getCreate_time());
                String encodedAimg = Encode.forHtml(activity.getAimg());
                String encodedCurrentPeople = Encode.forHtml(activity.getCurrent_people());
                String encodedLikes = Encode.forHtml(activity.getLikes());
                String encodedCollections = Encode.forHtml(activity.getCollections());

                encodedOutput.append("{");
                encodedOutput.append("\"aid\":\"").append(encodedAid).append("\",");
                encodedOutput.append("\"nickname\":\"").append(encodedNickname).append("\",");
                encodedOutput.append("\"atitle\":\"").append(encodedAtitle).append("\",");
                encodedOutput.append("\"type\":\"").append(encodedType).append("\",");
                encodedOutput.append("\"status\":\"").append(encodedStatus).append("\",");
                encodedOutput.append("\"start_time\":\"").append(encodedStartTime).append("\",");
                encodedOutput.append("\"end_time\":\"").append(encodedEndTime).append("\",");
                encodedOutput.append("\"max_people\":\"").append(encodedMaxPeople).append("\",");
                encodedOutput.append("\"sign_up_start\":\"").append(encodedSignUpStart).append("\",");
                encodedOutput.append("\"sign_up_end\":\"").append(encodedSignUpEnd).append("\",");
                encodedOutput.append("\"uid\":\"").append(encodedUid).append("\",");
                encodedOutput.append("\"location\":\"").append(encodedLocation).append("\",");
                encodedOutput.append("\"adscpt\":\"").append(encodedAdscpt).append("\",");
                encodedOutput.append("\"create_time\":\"").append(encodedCreateTime).append("\",");
                encodedOutput.append("\"aimg\":\"").append(encodedAimg).append("\",");
                encodedOutput.append("\"current_people\":\"").append(encodedCurrentPeople).append("\",");
                encodedOutput.append("\"likes\":\"").append(encodedLikes).append("\",");
                encodedOutput.append("\"collections\":\"").append(encodedCollections).append("\"");
                encodedOutput.append("}");

                if (i < activityDetails.size() - 1) {
                    encodedOutput.append(",");
                }
            }

            encodedOutput.append("]"); // 结束 JSON 数组
            return encodedOutput.toString();
        }

    //报名活动
    @PostMapping("/Signup")
    public int addSignup(@RequestBody Signup signup){
        signup.setSign_time(String.valueOf(LocalDate.now()));
        return syyService.addSignup(signup);
    }



    // 动态发布 带文件
    // ===================== 【新增4：图片文件头魔数校验，防止恶意文件伪装】 =====================
    private boolean isValidImageFile(Part part, String extension) {
        try (InputStream is = part.getInputStream()) {
            byte[] header = new byte[8]; // 读取前8个字节
            int bytesRead = is.read(header);
            if (bytesRead < 8) return false;

            // 魔数对照表
            switch (extension.toLowerCase()) {
                case "jpg":
                case "jpeg":
                    // JPEG: FF D8 FF
                    return (header[0] & 0xFF) == 0xFF &&
                            (header[1] & 0xFF) == 0xD8 &&
                            (header[2] & 0xFF) == 0xFF;
                case "png":
                    // PNG: 89 50 4E 47 0D 0A 1A 0A
                    return (header[0] & 0xFF) == 0x89 &&
                            (header[1] & 0xFF) == 0x50 &&
                            (header[2] & 0xFF) == 0x4E &&
                            (header[3] & 0xFF) == 0x47 &&
                            (header[4] & 0xFF) == 0x0D &&
                            (header[5] & 0xFF) == 0x0A &&
                            (header[6] & 0xFF) == 0x1A &&
                            (header[7] & 0xFF) == 0x0A;
                case "webp":
                    // WebP: RIFF .... WEBP
                    return (header[0] & 0xFF) == 0x52 &&
                            (header[1] & 0xFF) == 0x49 &&
                            (header[2] & 0xFF) == 0x46 &&
                            (header[3] & 0xFF) == 0x46 &&
                            (header[8] & 0xFF) == 0x57 && // 注意：WebP需要多读4个字节
                            (header[9] & 0xFF) == 0x45 &&
                            (header[10] & 0xFF) == 0x42 &&
                            (header[11] & 0xFF) == 0x50;
                default:
                    return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    // ===================== 【新增5：视频文件头魔数校验，防止恶意文件伪装】 =====================
    private boolean isValidVideoFile(Part part, String extension) {
        try (InputStream is = part.getInputStream()) {
            byte[] header = new byte[16];
            int bytesRead = is.read(header);
            if (bytesRead < 8) return false;

            switch (extension.toLowerCase()) {
                case "mp4":
                    // ftyp
                    return (header[4] & 0xFF) == 0x66
                            && (header[5] & 0xFF) == 0x74
                            && (header[6] & 0xFF) == 0x79
                            && (header[7] & 0xFF) == 0x70;
                case "mov":
                    return (header[0] & 0xFF) == 0x66
                            && (header[1] & 0xFF) == 0x72
                            && (header[2] & 0xFF) == 0x65
                            && (header[3] & 0xFF) == 0x65;
                default:
                    return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
@PostMapping("/dynaddAll")
public int dynaddAll(HttpServletRequest request, HttpSession session)
        throws IOException, ServletException, NoSuchFieldException, IllegalAccessException, ParseException {
    Dynamic model0 = new Dynamic();// 准备实体，存放模型数据
    Collection<Part> parts = request.getParts();
    Enumeration<String> values0 = request.getParameterNames();// 先取出所有参数名，避免循环干扰
    String allFileName = "";// 累加多个文件名称
    String fileFieldName = "";// 记录实体中文件字段名（如 media 对应前端的 name）

    // 从 session 获取用户 ID，校验登录状态
    Long uid = (Long) session.getAttribute("uid");
    if (uid == null) {
        return -1; // 返回特定值，前端用于判断未登录
    }
    model0.setUid(uid);
    // 设置创建时间，格式化为字符串（和你的表格式完全一致）
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    model0.setCreate_time(sdf.format(new Date()));

    // 允许的文件扩展名白名单
    Set<String> allowedExtensions = new HashSet<>(Arrays.asList(
            "jpg", "jpeg", "png", "gif", "bmp", // 图片格式
            "mp4", "mov", "avi", "wmv", "mkv"  // 视频格式
    ));

    // ===================== 【把非文件参数取出来，用于敏感词检查】 =====================
    String tempDtitle = "";
    String tempContent = "";
    // 先遍历一遍非文件参数，把标题和内容暂存
    for (Part part : parts) {
        if (part.getContentType() == null) {
            Enumeration<String> tempValues = request.getParameterNames();
            while (tempValues.hasMoreElements()) {
                String paramName = tempValues.nextElement();
                if ("dtitle".equals(paramName)) {
                    tempDtitle = request.getParameter(paramName);
                }
                if ("content".equals(paramName)) {
                            tempContent = request.getParameter(paramName);
                }
            }
            break; // 只取一次非文件参数
        }
    }

    // ===================== 【敏感词拦截】 =====================
    if (acAutomatonFilter.containsSensitive(tempDtitle) || acAutomatonFilter.containsSensitive(tempContent)) {
        return -3; // -3 = 包含敏感词
    }

    // ===================== 【发布频次限制 匹配perm权限】 =====================
    try {
        // 1. 计算1小时前的时间（和你动态表create_time格式100%匹配）
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, -1);
        String oneHourAgo = sdf.format(cal.getTime());

        // 2. 查询用户1小时内已发布的动态数
        int publishedCount = syyMapper.countUserDynIn1Hour(uid, oneHourAgo);

        // 3. 查询用户perm权限，分配对应限额
        int userPerm = syyMapper.getUserPermStatus(uid);
        int maxLimit;
        switch (userPerm) {
            case 3: // 认证达人
                maxLimit = 30;
                break;
            case 2: // 管理员
                maxLimit = 50;
                break;
            case 1: // 普通用户
            default: // 异常情况默认按普通用户处理
                maxLimit = 20;
                break;
        }

        // 4. 超限拦截
        if (publishedCount >= maxLimit) {
            System.out.println("用户" + uid + "发布超限，权限等级" + userPerm + "，1小时内已发" + publishedCount + "条，限额" + maxLimit);
            return -8; // -8 = 发布频次超限
        }
    } catch (Exception e) {
        e.printStackTrace();

    }

    // =================================================================
    int imageCount = 0;
    // 文件处理、表单参数处理逻
    for (Part part : parts) {
        if (part.getContentType() == null) {
            // 处理非文件类型参数（普通表单字段）
            while (values0.hasMoreElements()) {
                String paramName = values0.nextElement();
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
                    setEntityField(model0, paramName, multiValue.toString());
                } else {
                    // 处理单个值参数
                    setEntityField(model0, paramName, paramValues[0]);
                }
            }
        } else {
            // 处理文件上传
            String filename = part.getSubmittedFileName();
//            System.out.println("第一步：filename："+filename);
            String fieldName = part.getName(); // 获取字段名（如 "media"）
//            System.out.println("第一步字段名：fieldName："+fieldName);

            // 只在第一个文件时记录字段名，避免覆盖
            if (fileFieldName.isEmpty()) {
                fileFieldName = fieldName;
            }

            if (filename != null && filename.length() >= 3) {
                // 1. 提取文件扩展名
                String extension = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();

                // ===================== 【新增1：严格限制图片格式为 jpg/png/webp】 =====================
                // 区分图片和视频，图片只允许 jpg/png/webp，视频保持你原有逻辑
                boolean isImage = allowedExtensions.contains(extension) &&
                        Arrays.asList("jpg", "jpeg", "png", "webp").contains(extension);
                boolean isVideo = allowedExtensions.contains(extension) &&
                        Arrays.asList("mp4", "mov", "avi", "wmv", "mkv").contains(extension);

                if (!isImage && !isVideo) {
                    throw new IllegalArgumentException("不允许的文件类型: " + extension +
                            "，图片仅支持: jpg, png, webp；视频仅支持: mp4, mov, avi, wmv, mkv");
                }
// ===================== 【图片分辨率校验 ≥300*300】 =====================
                if (isImage) {
                    try (InputStream is = part.getInputStream()) {
                        BufferedImage bufferedImage = ImageIO.read(is);
                        if (bufferedImage == null) {
                            throw new IllegalArgumentException("不是有效的图片：" + filename);
                        }
                        int width = bufferedImage.getWidth();
                        int height = bufferedImage.getHeight();
                        if (width < 300 || height < 300) {
                            throw new IllegalArgumentException("图片尺寸过小，至少 300×300px");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw new IllegalArgumentException("图片解析失败：" + filename);
                    }
                }
                // ===================== 【新增2：图片真实类型校验+图片数量限制，最多6张】 =====================
                if (isImage) {
                    imageCount++;
                    if (!isValidImageFile(part, extension)) {
                        throw new IllegalArgumentException("图片文件已损坏或不是真实图片：" + filename);
                    }
                    if (imageCount > 6) {
                        throw new IllegalArgumentException("图片最多上传6张！");
                    }

                }
               // ===================== 【视频真实类型校验】 =====================
                if (isVideo) {
                    if (!isValidVideoFile(part, extension)) {
                        throw new IllegalArgumentException("视频文件不合法：" + filename);
                    }
                }
                // ===================== 【新增3：单张文件大小限制，图片≤2MB，视频≤10MB（可调整）】 =====================
                long fileSize = part.getSize();
                long maxImageSize = 2 * 1024 * 1024; // 2MB
                long maxVideoSize = 10 * 1024 * 1024; // 10MB（可根据你需求调整）

                if (isImage && fileSize > maxImageSize) {
                    throw new IllegalArgumentException("图片大小不能超过2MB！当前文件: " + filename + " (" + (fileSize / 1024 / 1024) + "MB)");
                }
                if (isVideo && fileSize > maxVideoSize) {
                    throw new IllegalArgumentException("视频大小不能超过50MB！当前文件: " + filename + " (" + (fileSize / 1024 / 1024) + "MB)");
                }

                // 安全处理：防止路径穿越攻击（移除路径信息，只保留文件名）
                filename = FilenameUtils.getName(filename);

                // 生成安全的文件名（使用UUID前缀防止覆盖）
                filename = UUID.randomUUID().toString().substring(0, 5) + filename;

                // 保存文件到服务器
                part.write(uploadPath + filename);

                // 记录文件路径（使用逗号分隔多个文件）
                String newFilename = "/static/image/upload/" + filename;
                allFileName += newFilename + ",";
                System.out.println("串联："+allFileName);
            }
        }
    }

    // 处理文件名称，去除最后多余的分隔符，并设置到实体
    if (allFileName.endsWith(",")) {
        allFileName = allFileName.substring(0, allFileName.length() - 1);
        System.out.println("最终："+allFileName);
    }
    // 设置文件路径到实体（使用正确的字段名）
    if (!fileFieldName.isEmpty()) {
        setEntityField(model0, fileFieldName, allFileName);
    }

    return syyService.addDyn(model0);
}


    // 抽取的反射设置实体字段方法，避免重复代码
    private void setEntityField(Dynamic model, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
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

    //动态全查询（社区展示）
    @GetMapping("/DynQueryAll")
    public List<Dynamic> DynQueryAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "all") String category,
            @RequestParam(defaultValue = "") String keyword
    ) {
        try {
            return syyService.findDynAll(page, pageSize, category, keyword);
        } catch (Exception e) {
            System.err.println("查询公开动态出错: " + e.getMessage());
            throw e;
        }
    }
    // controlSyy.java
    @GetMapping("/DynQueryAll1")
    public List<Dynamic> findDynAll1(
            HttpSession session,
            @RequestParam(defaultValue = "all") String category,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        Long uid = (Long) session.getAttribute("uid");
        return syyService.findDynAll1(uid, category, keyword, page, pageSize);
    }

    // 查询我发布的动态
    @GetMapping("/DynQueryAll2")
    public List<Dynamic> findDynAll2(
            HttpSession session,
            @RequestParam(defaultValue = "all") String category,
            @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        Long uid = (Long) session.getAttribute("uid");
        // 增加登录校验
        if (uid == null) {
            throw new RuntimeException("用户未登录");
        }
        return syyService.findDynAll2(uid, category, keyword, page, pageSize);
    }

    //动态详情
//    @GetMapping("/findByDid")
//    public List<Dynamic> findByDid(@RequestParam("did") Long did) {
//        return syyService.findByDid(did);
//    }

    // 动态详情
    @GetMapping("/findByDid")
    public List<Dynamic> findByDid(@RequestParam("did") Long did) {
        List<Dynamic> dynamics = syyService.findByDid(did);
        List<Dynamic> encodedDynamics = new ArrayList<>();

        for (Dynamic dynamic : dynamics) {
            Dynamic encodedDynamic = new Dynamic();
            encodedDynamic.setDid(dynamic.getDid());
            encodedDynamic.setUid(dynamic.getUid());
            encodedDynamic.setDtitle(Encode.forHtml(dynamic.getDtitle()));
            encodedDynamic.setContent(Encode.forHtml(dynamic.getContent()));
            encodedDynamic.setMedia(Encode.forHtml(dynamic.getMedia()));
            encodedDynamic.setType(Encode.forHtml(dynamic.getType()));
            encodedDynamic.setCreate_time(Encode.forHtml(dynamic.getCreate_time()));
            encodedDynamic.setLike_count(dynamic.getLike_count());
            encodedDynamic.setComment_count(dynamic.getComment_count());
            encodedDynamic.setCollect_count(dynamic.getCollect_count());
            encodedDynamic.setNickname(Encode.forHtml(dynamic.getNickname()));
            encodedDynamic.setAvatar(Encode.forHtml(dynamic.getAvatar()));
            encodedDynamic.setIsLiked(dynamic.getIsLiked());
            encodedDynamic.setIsCollected(dynamic.getIsCollected());
            encodedDynamic.setStatus(Encode.forHtml(dynamic.getStatus()));

            encodedDynamics.add(encodedDynamic);
        }

        return encodedDynamics;
    }
    //动态评论发布
    // 动态评论发布（带 10分钟8条 频率限制 重复内容拦截）
// 动态评论发布（最终完整版）
    @PostMapping({"/dcomAdd"})
    public int addDcom(@RequestBody Dynamic_comment dcom, HttpSession session) {
        // 登录校验
        Long uid = (Long) session.getAttribute("uid");
        if (uid == null) {
            return -1; // 未登录
        }
        dcom.setUid(uid);
        String content = dcom.getContent();

        // 评论频率限制（10分钟8条）
        Long did = dcom.getDid();
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, -10);
        String tenMinutesAgo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cal.getTime());
        int count = syyMapper.countUserCommentInLast10Min(uid, did, tenMinutesAgo);
        if (count >= 8) {
            return -2; // 评论过于频繁
        }

        // 10分钟相同评论拦截
        Integer repeatCount = syyMapper.countSameCommentIn10Min(uid, did, content, tenMinutesAgo);
        if (repeatCount >= 1) {
            return -4; // 重复评论
        }

        // ====================== 垃圾评论过滤 ======================
        // 1. 高危注入拦截
        if (content != null && content.matches(dangerousPatterns)) {
            return 0; // 违规内容
        }

        // 2. 特殊字符过多拦截
        if (!content.matches(safeChars)) {
            if (countSpecialChars(content) > content.length() * 0.3) {
                return 2; // 特殊字符超标
            }
        }

        // 3. AC自动机敏感词匹配（真正本地词库+高效匹配）
        if (content != null && acAutomatonFilter.containsSensitive(content)) {
            return 3; // 包含敏感词
        }
        // =================================================================

        // 发布评论
        dcom.setCreate_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        return syyService.addDcom(dcom);
    }

    // 动态评论查询展示
    @PostMapping("/DcomQuery")
    public List<Dynamic_comment> findDcomBydid(@RequestBody String str) {
        JSONObject json = JSONObject.parseObject(str);
        String id = json.getString("did");
        if (id == null || id.isEmpty()) {
            id = null;
        }
//        System.out.println("查询动态评论，did: " + id);
        return syyService.findDcomBydid(id);

    }
    //动态点赞+1
//    @PostMapping({"/dlkAdd"})
//    public int addDlk(@RequestBody Dynamic_like dlk) {
//        System.out.println("点赞的用户：" + dlk.getUid());
//        System.out.println("动态ID：" + dlk.getDid());
//        return this.syyService.addDlk(dlk);
//    }
// 动态点赞+1 【加频率限制】

//    @PostMapping("/dlkAdd")
//    public int addDlk(@RequestBody Dynamic_like dlk, HttpSession session) {
//        try {
//            // 1. 从 session 取登录用户（最安全）
//            Long uid = (Long) session.getAttribute("uid");
//            if (uid == null) {
//                return -1; // 未登录
//            }
//
//            // 2. 强制覆盖为真实UID，防止前端乱传
//            dlk.setUid(uid);
//
//            // 3. 频次校验（1小时最多50次）
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            Calendar cal = Calendar.getInstance();
//            cal.add(Calendar.HOUR, -1);
//            String oneHourAgo = sdf.format(cal.getTime());
//
//            int count = syyMapper.countUserLikeIn1Hour(uid, oneHourAgo);
//            System.out.printf("点赞取消次数: " + count);
//
//            if (count >= 50) {
//                return -5; // 超限
//            }
//
//            // 4. 执行点赞
//            return syyService.addDlk(dlk);
//
//        } catch (Exception e) {
//            e.printStackTrace(); // 打印崩溃日志
//            return 0;
//        }
//    }
// 内存缓存：key = "uid_did"，value = 操作次数
    private final Map<String, Integer> operationCount = new ConcurrentHashMap<>();
    // 记录开始时间
    private final Map<String, Long> operationTime = new ConcurrentHashMap<>();

    @PostMapping("/dlkAdd")
    public int addDlk(@RequestBody Dynamic_like dlk, HttpSession session) {
        Long uid = (Long) session.getAttribute("uid");
        if (uid == null) {
            return -1;
        }
        dlk.setUid(uid);
        Long did = dlk.getDid();

        // ===================== 【极简频繁操作判断】 =====================
        String key = uid + "_" + did;
        long now = System.currentTimeMillis();
        long tenMinutes = 10 * 60 * 1000; // 10分钟

        // 第一次操作 → 初始化
        if (!operationCount.containsKey(key)) {
            operationCount.put(key, 1);
            operationTime.put(key, now);
        } else {
            // 10分钟内 → 计数+1
            if (now - operationTime.get(key) < tenMinutes) {
                int count = operationCount.get(key) + 1;
                operationCount.put(key, count);
                System.out.printf("点赞取消次数: " + count);
                //  超过20次 → 直接拦截
                if (count >= 20) {
                    System.out.println("频繁操作拦截：" + key);
                    return -6; // 返回频繁操作码
                }
            } else {
                // 超过10分钟 → 重置
                operationCount.put(key, 1);
                operationTime.put(key, now);
            }
        }
        // ==================================================================

        return syyService.addDlk(dlk);
    }
    // 检查用户是否已点赞
    @GetMapping("/checkDlk")
    public boolean checkDlk(@RequestParam Long uid, @RequestParam Long did) {
        return syyService.checkDlk(uid, did);
    }

    // 动态点赞-1
    @PostMapping("/dlkDel")
    public int dlkDel(@RequestBody Dynamic_like dlk) {

//        System.out.println("接收的 uid: " + dlk.getUid());
//        System.out.println("接收的 did: " + dlk.getDid());

        return syyService.dlkDel(dlk.getUid(), dlk.getDid());
    }
    // 批量查询用户对多个动态是否已点赞
    @GetMapping("/checkDlkBatch")
    public Map<Long, Boolean> checkDlkBatch(
            @RequestParam Long uid,
            @RequestParam String dids // 逗号分隔的 did 字符串
    ) {
        List<Long> didList = Arrays.stream(dids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        Map<Long, Boolean> result = new HashMap<>();
        didList.forEach(did -> result.put(did, syyService.checkDlk(uid, did)));
        return result;
    }
    //动态收藏+1
//    @PostMapping({"/dclAdd"})
//    public int addDcl(@RequestBody Dynamic_collect dcl) {
//        System.out.println("收藏的用户：" + dcl.getUid());
//        System.out.println("动态ID：" + dcl.getDid());
//        return this.syyService.addDcl(dcl);
//    }
    @PostMapping({"/dclAdd"})
    public int addDcl(@RequestBody Dynamic_collect dcl, HttpSession session) {
        Long uid = (Long) session.getAttribute("uid");
        if (uid == null) {
            return -1;
        }
        dcl.setUid(uid);
        Long did = dcl.getDid();

        // ===================== 【频繁操作判断】 =====================
        String key = uid + "_" + did;
        long now = System.currentTimeMillis();
        long tenMinutes = 10 * 60 * 1000; // 10分钟

        // 第一次操作 → 初始化
        if (!operationCount.containsKey(key)) {
            operationCount.put(key, 1);
            operationTime.put(key, now);
        } else {
            // 10分钟内 → 计数+1
            if (now - operationTime.get(key) < tenMinutes) {
                int count = operationCount.get(key) + 1;
                operationCount.put(key, count);
                System.out.printf("收藏取消次数: " + count);
                //  超过20次 → 直接拦截
                if (count >= 20) {
                    System.out.println("频繁操作拦截：" + key);
                    return -7; // 返回频繁操作码
                }
            } else {
                // 超过10分钟 → 重置
                operationCount.put(key, 1);
                operationTime.put(key, now);
            }
        }
        return this.syyService.addDcl(dcl);

    }
    // 检查用户是否已收藏
    @GetMapping("/checkDcl")
    public boolean checkDcl(@RequestParam Long uid, @RequestParam Long did) {
        return syyService.checkDcl(uid, did);
    }

    // 动态收藏-1
    @PostMapping("/dclDel")
    public int dclDel(@RequestBody Dynamic_collect dcl) {
        System.out.println("接收的 uid: " + dcl.getUid());
        System.out.println("接收的 did: " + dcl.getDid());

        return syyService.dclDel(dcl.getUid(), dcl.getDid());
    }
    // 批量查询用户对多个动态是否已收藏
    @GetMapping("/checkDclBatch")
    public Map<Long, Boolean> checkDclBatch(
            @RequestParam Long uid,
            @RequestParam String dids // 逗号分隔的 did 字符串
    ) {
        List<Long> didList = Arrays.stream(dids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        Map<Long, Boolean> result = new HashMap<>();
        didList.forEach(did -> result.put(did, syyService.checkDcl(uid, did)));
        return result;
    }





    // 查询我的收藏动态记录列表
//    @GetMapping("/findMyDCl")
//    public List<MyDynCollect> findMyDCl(@RequestParam("uid") Long uid) {
//
//        return syyService.findMyDCl(uid);
//    }
    /**
     * 查询我的收藏动态记录列表 - 支持分页、分类筛选和关键词搜索
     * @param uid 用户ID
     * @param page 页码，默认1
     * @param pageSize 每页数量，默认6
     * @param category 分类，默认all（全部）
     * @param keyword 关键词，默认空
     * @return 收藏列表
     */
    @GetMapping("/findMyDCl")
    public List<MyDynCollect> findMyDCl(@RequestParam("uid") Long uid,
                                        @RequestParam(defaultValue = "1") int page,
                                        @RequestParam(defaultValue = "6") int pageSize,
                                        @RequestParam(defaultValue = "all") String category,
                                        @RequestParam(defaultValue = "") String keyword) {
        try {
            return syyService.findMyDCl(uid, page, pageSize, category, keyword);
        } catch (Exception e) {
            // 记录异常信息
            System.err.println("查询收藏列表出错: " + e.getMessage());
            e.printStackTrace();

            // 重新抛出异常，让Spring的全局异常处理器处理
            throw e;
        }
    }

//    // 查询我的收藏活动记录列表
    /**
     * 查询我的收藏活动记录列表 - 支持分页、分类筛选和关键词搜索
     * @param uid 用户ID
     * @param page 页码，默认1
     * @param pageSize 每页数量，默认6
     * @param category 分类，默认all（全部）
     * @param keyword 关键词，默认空
     * @return 收藏列表
     */
    @GetMapping("/findMyACl")
    public List<MyActCollect> findMyACl(
            @RequestParam("uid") Long uid,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "6") int pageSize,
            @RequestParam(defaultValue = "all") String category,
            @RequestParam(defaultValue = "") String keyword
    ) {
        try {
            return syyService.findMyACl(uid, page, pageSize, category, keyword);
        } catch (Exception e) {
            // 记录异常信息
            System.err.println("查询收藏活动列表出错: " + e.getMessage());
            e.printStackTrace();
            // 重新抛出异常，让Spring的全局异常处理器处理
            throw e;
        }
    }

    // 获取热门动态（点赞Top4）
    @PostMapping("/getHotDynamics")
    public List<Dynamic> getHotDynamics() {
        return syyService.findHotDynamics();
    }




}
