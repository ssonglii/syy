
// ====================
// 全局 CSRF 令牌（兼容 jQuery 1.8.3 版本）
// ====================
var CSRF_TOKEN = "";

// 页面加载完成后，立即获取 CSRF 令牌
$(function() {
    $.get("/api/security/get-csrf-token", function(token) {
        CSRF_TOKEN = token;
        console.log("✅ CSRF 令牌获取成功：", token);
    });
});

// 用 ajaxSend 全局绑定，所有 AJAX 请求自动携带 CSRF 令牌（兼容 jQuery 1.8.3）
$(document).ajaxSend(function(event, xhr, options) {
    // 只给 POST/DELETE/PUT 请求加 token（GET 请求不需要）
    if (options.type === "POST" || options.type === "DELETE" || options.type === "PUT") {
        if (CSRF_TOKEN) {
            xhr.setRequestHeader("X-CSRF-TOKEN", CSRF_TOKEN);
            console.log("✅ 已自动携带 CSRF 令牌：", CSRF_TOKEN);
        }
    }
});


/******session   token   cookie*******/


function getSession(key,Target)
{   $.ajax({
    type:"get",//提交数据方式post/get
    url:"bbs/getSession",
    data:{"key":key},//随便给一个无用参数
    dataType:"text", //从后台向前台传数据时的格式json
    success:function(re)
    {
        $("#"+Target).text(re);
    },
    error:function(re)
    { alert("no");
    }
})
}


/****************登录界面**************/
//密码可见 点击图标切换密码可见/不可见
function kejian() {
    var passwordInput = document.getElementById('pwd');
    if (passwordInput.type === 'password') {
        // 切换为文本输入框
        passwordInput.type = 'text';
    } else {
        // 切换回密码输入框
        passwordInput.type = 'password';
    }
}
//点击“注册”按钮
$(document).ready(function(){
    $("#reg").bind("click",function(){

        window.location.replace("/userReg2.html"); // 跳到注册界面
    });})
//点击“回到登录”按钮
$(document).ready(function(){
    $("#log").bind("click",function(){

        window.location.replace("/Login.html"); // 跳到注册界面
    });})
//登录
// $(document).ready(function(){
//     // 初始化验证码
//     generateVerifyCode();
//
//         // 登录按钮点击事件
//     $("#login2").bind("click", function() {
//         var tel = $("#tel").val().trim();
//         var pwd = $("#pwd").val().trim();
//         var verifyCode = $("#verifyCode").val().trim(); // 新增验证码获取
//         var telPattern = /^1[3-9]\d{9}$/;
//         var pwdPattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()_+~`\-=\\\[\]{}|;':",.<>/?])[A-Za-z\d!@#$%^&*()_+~`\-=\\\[\]{}|;':",.<>/?]{8,20}$/;
//
//         if (!telPattern.test(tel)) {
//                 alert("请输入有效的手机号码");
//                generateVerifyCode();
//                 $("#tel").focus();
//                 return false;
//             }
//
//             // 密码复杂度验证
//             if (!pwdPattern.test(pwd.toString().trim())) {
//                 alert("密码需满足：8-20位，包含大小写字母、数字和特殊符号");
//                 generateVerifyCode();
//                 $("#pwd").focus();
//                 return false;
//             }
//
//           // 新增：验证码非空校验
//           if (verifyCode === "") {
//               alert("请输入验证码");
//               $("#verifyCode").focus();
//               return false;
//           }
//
//
//         if (tel != "" && pwd != "") {
//             $.ajax({
//                 type: "get",
//                 url: "/finduserBytel",
//                 data: {
//                     "tel": tel,
//                     "pwd": pwd,
//                     "verifyCode": verifyCode // 新增：发送验证码到后端
//                 },
//                     async: false,
//                     dataType: "json",
//                     success: function(re) {
//                         console.log('后端返回数据：', re);
//                         // alert("1");
//                         // **优先判断是否为频率限制错误**
//                         if (re.length > 0 && re[0].hasOwnProperty("errorCode") && re[0].errorCode === "FREQUENCY_LIMITED") {
//                             alert(re[0].message);
//                             generateVerifyCode();
//                             return; // 终止后续执行
//
//                         }
//                         // **新增：验证码错误处理**
//                         if (re.length > 0 && re[0].hasOwnProperty("errorCode") && re[0].errorCode === "VERIFY_CODE_ERROR") {
//                             alert(re[0].message);
//                             generateVerifyCode(); // 刷新验证码
//                             return;
//                         }
//
//                         if (re.length == 0) {
//                             alert("该手机号未注册，请前往注册");
//                             window.location.replace("../userReg2.html");
//                         } else {
//                             re = re[0];
//                             if (re["pwd"].toString().trim() == pwd) {
//                                 reToken();
//
//                                 // 从Session获取UID和perm
//                                 $.ajax({
//                                     type: "get",
//                                     url: "/getUserInfoFromSession",
//                                     async: false,
//                                     success: function(data) {
//                                         sessionStorage.setItem("uid", data.uid);
//                                         sessionStorage.setItem("tel", tel);
//                                         sessionStorage.setItem("perm", data.perm);
//                                     }
//                                 });
//
//                                 alert("登录成功，进入主界面");
//                                 window.location.replace("Index.html");
//                             } else {
//                                 alert("密码错误！请重新输入");
//                                 $("#pwd").val("");
//                                 $("#pwd").focus();
//                             }
//                         }
//                     },
//                     error: function(xhr, status, error) {
//                         alert("登录失败，请检查网络连接或稍后再试");
//                         generateVerifyCode();
//                     }
//                 });
//             } else {
//                 alert("数据填写不完整，不能为空！");
//                  generateVerifyCode();
//             }
//         });
//     // 刷新验证码函数
//     function refreshVerifyCode() {
//         generateVerifyCode();
//     }
//
//     // 生成验证码函数
//     function generateVerifyCode() {
//         // 调用后端接口生成验证码（响应为图片流）
//         $("#verifyImg").attr("src", "/generateVerifyCode?" + new Date().getTime()); // 防止缓存
//     }
//
//
//     function reToken() {
//         $.ajax({
//             type:"get",//提交数据方式post/get
//             url:"bbs/reToken",//不加前面bbs/不拦截
//             data:null,
//             async: false,
//             dataType:"text",
//             success:function(re) { //后台使用list返回json数组，不是json
//                 sessionStorage.setItem("token",re);
//              //   alert("||"+sessionStorage.getItem("token")+"保存没有？");
//             },
//             error:function(re)
//             { alert("no success11111111111");}
//         })
//
//     }
//
// })
// $(document).ready(function() {
//     // 初始化验证码
//     generateVerifyCode();
//
//     // 登录按钮点击事件
//     $("#login2").bind("click", function() {
//         var tel = $("#tel").val().trim();
//         var pwd = $("#pwd").val().trim();
//         var verifyCode = $("#verifyCode").val().trim();
//         var telPattern = /^1[3-9]\d{9}$/;
//         var pwdPattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()_+~`\-=\\\[\]{}|;':",.<>/?])[A-Za-z\d!@#$%^&*()_+~`\-=\\\[\]{}|;':",.<>/?]{8,20}$/;
//
//         // 表单基础验证
//         if (!telPattern.test(tel)) {
//             alert("请输入有效的手机号码");
//             generateVerifyCode();
//             $("#tel").focus();
//             return false;
//         }
//
//         if (!pwdPattern.test(pwd.toString().trim())) {
//             alert("密码需满足：8-20位，包含大小写字母、数字和特殊符号");
//             generateVerifyCode();
//             $("#pwd").focus();
//             return false;
//         }
//
//         if (verifyCode === "") {
//             alert("请输入验证码");
//             $("#verifyCode").focus();
//             return false;
//         }
//
//         if (tel && pwd) {
//             // 1. 获取动态加密密钥
//             var encryptionKey = "";
//             $.ajax({
//                 url: "/api/security/get-encryption-key",
//                 async: false,
//                 success: function(key) {
//                     encryptionKey = key;
//                 },
//                 error: function() {
//                     alert("安全密钥获取失败，请重试");
//                     generateVerifyCode();
//                     return false;
//                 }
//             });
//
//             // 2. 加密密码（AES算法，与后端解密对应）
//             var encryptedPwd = encryptData(pwd, encryptionKey);
//
//             // 3. 发送登录请求（携带加密后的密码）
//             $.ajax({
//                 type: "get",
//                 url: "/finduserBytel",
//                 data: {
//                     "tel": tel,
//                     "pwd": encryptedPwd,  // 发送加密后的密码
//                     "verifyCode": verifyCode
//                 },
//                 // beforeSend: function() {
//                 //     console.log('实际发送的 tel：', tel); // 确认发送的内容
//                 // },
//                 async: false,
//                 dataType: "json",
//                 success: function(re) {
//                     console.log('后端返回数据：', re);
//
//                     // 频率限制错误处理
//                     if (re.length > 0 && re[0].hasOwnProperty("errorCode") && re[0].errorCode === "FREQUENCY_LIMITED") {
//                         alert(re[0].message);
//                         generateVerifyCode();
//                         return;
//                     }
//
//                     // 验证码错误处理
//                     if (re.length > 0 && re[0].hasOwnProperty("errorCode") && re[0].errorCode === "VERIFY_CODE_ERROR") {
//                         alert(re[0].message);
//                         generateVerifyCode();
//                         return;
//                     }
//
//                     if (re.length === 0) {
//                         alert("该手机号未注册，请前往注册");
//                         window.location.replace("../userReg2.html");
//                     } else {
//                         re = re[0];
//
//                         // 确保re包含uid和perm
//                         if (!re.uid || !re.perm) {
//                             alert("用户信息不完整，请重新登录");
//                             return;
//                         }
//                         // 登录成功后，直接从后端返回结果中获取用户信息
//                         sessionStorage.setItem("uid", re.uid);
//                         sessionStorage.setItem("tel", tel); // 使用前端输入的 tel
//                         sessionStorage.setItem("perm", re.perm);
//                         console.log("已登录，SessionStorage 已设置：", sessionStorage);
//                         reToken();
//
//                         // // 从Session获取用户信息
//                         // $.ajax({
//                         //     type: "get",
//                         //     url: "/getUserInfoFromSession",
//                         //     async: false,
//                         //     success: function(data) {
//                         //         console.log("从后端获取的用户信息：", data); // 确认返回格式
//                         //         if (data.uid && data.perm) {
//                         //             sessionStorage.setItem("uid", data.uid);
//                         //             sessionStorage.setItem("tel", tel);
//                         //             sessionStorage.setItem("perm", re.perm);
//                         //             console.log("已登录，Session 已设置：", sessionStorage);
//                         //         }
//                         //     },
//                         //     error: function() {
//                         //         alert("获取用户信息失败，请重新登录");
//                         //     }
//                         // });
//
//                         alert("登录成功，进入主界面");
//                         window.location.replace("Index.html");
//                     }
//                 },
//                 error: function(xhr, status, error) {
//                     alert("登录失败，请检查网络连接或稍后再试");
//                     generateVerifyCode();
//                     console.error("登录请求错误：", error);
//                 }
//             });
//         } else {
//             alert("数据填写不完整，不能为空！");
//             generateVerifyCode();
//         }
//     });
//
//     // 刷新验证码函数
//     function refreshVerifyCode() {
//         generateVerifyCode();
//     }
//
//     // 生成验证码函数
//     function generateVerifyCode() {
//         $("#verifyImg").attr("src", "/generateVerifyCode?" + new Date().getTime());
//     }
//
//     // 刷新Token函数
//     function reToken() {
//         $.ajax({
//             type: "get",
//             url: "bbs/reToken",
//             data: null,
//             async: false,
//             dataType: "text",
//             success: function(re) {
//                 sessionStorage.setItem("token", re);
//             },
//             error: function() {
//                 alert("Token获取失败，请重试");
//             }
//         });
//     }
//
//     // AES加密函数（依赖CryptoJS）
//     function encryptData(data, key) {
//         // 确保密钥长度为32字节（256位），不足时补全
//         key = key.padEnd(32, '0').substring(0, 32);
//
//         var encrypted = CryptoJS.AES.encrypt(
//             data,
//             CryptoJS.enc.Utf8.parse(key),
//             {
//                 mode: CryptoJS.mode.CBC,
//                 padding: CryptoJS.pad.Pkcs7,
//                 iv: CryptoJS.enc.Utf8.parse(key.substring(0, 16))  // 使用密钥前16位作为IV
//             }
//         );
//         return encrypted.toString();
//     }
// });



/****************用户注册******************/
function addAll(form, controlUrl) {
    // 获取密码并验证复杂度
    var tel = $("#tel").val().trim();
    var pwd = $("#pwd").val().trim();
    var pwdPattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[~@$.!%*?&])[A-Za-z\d~@$.!%*?&]{8,20}$/;
    var telPattern = /^1[3-9]\d{9}$/;
    if (!pwdPattern.test(pwd)) {
        alert("密码需满足：8-20位，包含大小写字母、数字和特殊符号");
        $("#pwd").focus();
        return false;
    }
    if (!telPattern.test(tel)) {
        alert("请输入有效的手机号码");
        $("#tel").focus();
        return false;
    }
    // 验证确认密码
    var confirmPwd = $("#confirmPwd").val().trim();
    if (pwd !== confirmPwd) {
        alert("两次输入的密码不一致");
        $("#confirmPwd").focus();
        return false;
    }
// ===== 获取服务器动态密钥 =====
    var encryptionKey = "";
    $.ajax({
        url: "/api/security/get-encryption-key",
        async: false, // 同步请求确保获取密钥
        success: function(key) {
            encryptionKey = key;
        },
        error: function() {
            alert("安全密钥获取失败，请重试");
            return false;
        }
    });

    // ===== 加密敏感数据 =====
    var encryptedPwd = encryptData(pwd, encryptionKey);//密码；手机号
    var encryptedTel = encryptData($("#tel").val().trim(), encryptionKey);

    var data0 = new FormData($("#" + form)[0]);
    data0.delete("confirmPwd");

// ===== 替换为加密值 =====
    data0.delete("pwd"); // 移除明文密码
    data0.delete("tel"); // 移除明文手机号
    data0.append("encryptedPwd", encryptedPwd); // 添加加密密码
    data0.append("encryptedTel", encryptedTel); // 添加加密手机号
    data0.append("keyHash", generateHash(encryptionKey)); // 添加密钥哈希（用于验证）

    $.ajax({
        type: "post",
        url: controlUrl,
        cache: false,
        processData: false,
        contentType: false,
        data: data0,
        dataType: "Json",
        async: false,
        success: function(re) {
            if (re.success) {
                alert("注册成功");
                window.location.href = "Login.html";
            } else {
                alert(re.message);
                if (re.message.indexOf("已注册") > -1) {
                    window.location.href = "Login.html";
                }
            }
        },
        error: function(re) {
            alert("注册失败，请稍后再试");
        }
    });
}
// ===== 加密函数 =====
// AES-CBC模式加密，密钥前16位作IV
function encryptData(data, key) {
    var encrypted = CryptoJS.AES.encrypt(
        data,
        CryptoJS.enc.Utf8.parse(key),
        {
            mode: CryptoJS.mode.CBC,
            padding: CryptoJS.pad.Pkcs7,
            iv: CryptoJS.enc.Utf8.parse(key.substring(0, 16)) // 使用密钥前16位作为IV
        }
    );
    return encrypted.toString();
}

// ===== 生成密钥哈希（简化版） =====
function generateHash(key) {
    // 使用更安全的 SHA-256
    return CryptoJS.SHA256(key).toString();
}
/****************我的资料******************/
//查询并显示我的资料信息到表单

// 修改查询用户信息的函数
// function querybyidReb2(tableName, fieldName) {
//     var uid = sessionStorage.getItem("uid");
//
//     if (!uid) {
//         alert("请先登录");
//         window.location.href = "Login.html";
//         return;
//     }
//
//     // 直接使用uid查询，不再使用tel
//     $.ajax({
//         url: "/findByUid",
//         type: "GET",
//         data: { "uid": uid },
//         dataType: "json",
//         async: false,
//         success: function(re) {
//             if (re && re.length > 0) {
//                 var item = re[0];
//                 // 手机号脱敏处理
//                 if (item.tel) {
//                     var maskedTel = maskPhoneNumber(item.tel);
//                     $("#tel1").val(maskedTel);
//                 } else {
//                     $("#tel1").val("");
//                 }
//
//                 // 其他字段赋值
//                 $("#nickname").val(item.nickname);
//                 $("#pwd").val(item["pwd"].toString());
//                 if(item["sex"].toString()=="男")
//                     $("#nan").prop("checked","checked");
//                 else if(item["sex"].toString()=="女")
//                     $("#nv").prop("checked","checked");
//                 else  $("#weizhi").prop("checked","checked");
//                 $("#email").val(item["email"].toString());
//                 $("#birth").val( item["birth"].toString());
//                 var picTemp=item["avatar"].toString();
//                 $("#avatar").attr("src",item["avatar"].toString());
//                 $("#fav").val(item["fav"].toString());
//                 $("#addr").val(item["addr"].toString());
//                 $("#intr").val(item["intr"].toString());
//             } else {
//                 alert("未找到用户信息");
//             }
//         },
//         error: function() {
//             alert("请求失败");
//         }
//     });
// }
// 手机号脱敏函数
function querybyidReb2(tableName, fieldName) {
    var uid = sessionStorage.getItem("uid");

    if (!uid) {
        alert("请先登录");
        window.location.href = "Login.html";
        return;
    }

    $.ajax({
        url: "/findByUid",
        type: "GET",
        data: { "uid": uid },
        dataType: "json",
        async: false,
        success: function(re) {
            if (re && re.length > 0) {
                var item = re[0];

                // 手机号脱敏
                if (item.tel) {
                    var maskedTel = maskPhoneNumber(item.tel);
                    $("#tel1").val(maskedTel);
                } else {
                    $("#tel1").val("");
                }

                // ====================== XSS 安全净化 ======================
                $("#nickname").val(DOMPurify.sanitize(item.nickname || ""));
                $("#pwd").val(DOMPurify.sanitize(item.pwd || ""));
                $("#email").val(DOMPurify.sanitize(item.email || ""));
                $("#birth").val(DOMPurify.sanitize(item.birth || ""));
                $("#fav").val(DOMPurify.sanitize(item.fav || ""));
                $("#addr").val(DOMPurify.sanitize(item.addr || ""));
                $("#intr").val(DOMPurify.sanitize(item.intr || ""));

                // 性别
                if(item.sex == "男") $("#nan").prop("checked", true);
                else if(item.sex == "女") $("#nv").prop("checked", true);
                else $("#weizhi").prop("checked", true);

                // 头像
                $("#avatar").attr("src", item.avatar || "");

            } else {
                alert("未找到用户信息");
            }
        },
        error: function() {
            alert("请求失败");
        }
    });
}
function maskPhoneNumber(phone) {
    // 验证手机号格式
    if (!phone || phone.length !== 11 || isNaN(phone)) {
        return phone || "";
    }
    // 将中间四位替换为星号
    return phone.replace(/(\d{3})\d{4}(\d{4})/, "$1****$2");
}

// 提交修改资料（XSS 安全版）
function modify1(form,controlUrl) {
    var re0 = null;
    var dataxs=new FormData($("#"+form)[0]);
    var entype0=$("#"+form).attr("enctype")
    $.ajax({
        type: "POST",
        url: controlUrl,
        cache: false,
        processData: false,
        contentType: false,
        data: dataxs,
        dataType: "Json",
        async: false,
        success: function(re) {
            re0 = re;
            alert("个人资料修改成功了");
        },
        error: function(re) {
            alert("个人资料修改不成功");
        }
    });
    return re0;
}
// 修改用户资料（入口，自动净化 XSS）
function  ModifyUser(form1,controlUrl ) {
    $("#nickname").val(DOMPurify.sanitize($("#nickname").val()));
    $("#email").val(DOMPurify.sanitize($("#email").val()));
    $("#birth").val(DOMPurify.sanitize($("#birth").val()));
    $("#fav").val(DOMPurify.sanitize($("#fav").val()));
    $("#addr").val(DOMPurify.sanitize($("#addr").val()));
    $("#intr").val(DOMPurify.sanitize($("#intr").val()));
    var re=modify1(form1,controlUrl);

    if(re==1)
        alert("修改成功");
}
//点击修改图片按钮，会增加一个input type=file
function  altFile2() {
    var f0=document.createElement("input");
    f0.setAttribute("id","avatar");f0.setAttribute("type","file");
    f0.setAttribute("name","avatar");
    document.getElementById("sp1").append(f0);
}
/****************活动发布界面******************/

//发布活动（安全防 XSS）
function addAct(form, controlUrl) {
    // ====================== XSS 安全净化 ======================
    let rawTitle = $('#atitle').val();
    let rawDesc = $('#adscpt').val();

    // 净化用户输入，防止恶意代码
    let safeTitle = DOMPurify.sanitize(rawTitle);
    let safeDesc = DOMPurify.sanitize(rawDesc);

    // 净化后的内容
    $('#atitle').val(safeTitle);
    $('#adscpt').val(safeDesc);

    var data0 = new FormData($("#" + form)[0]);
    var entype0 = $("#" + form).attr("enctype");

    $.ajax({
        type: "post",
        url: controlUrl,
        cache: false,
        processData: false,
        contentType: false,
        data: data0,
        dataType: "Json",
        async: false,
        success: function (re) {
            alert(re);
        },
        error: function (re) {
            alert("活动发布失败");
        }
    });
}
// function addAct(form,controlUrl)  {
//
//     // alert(form+" "+controlUrl);
//     var data0=new FormData($("#"+form)[0]);
//     var entype0=$("#"+form).attr("enctype")//判断表单源是什么方式编码提交
//     $.ajax({
//         type:"post",
//         url:controlUrl,  //对应后端control的xsaddAll
//         cache:false,
//         processData:false,//需设置为false。因为data值是FormData对象，不需要对数据做处理
//         contentType:false,//需设置为false。因为是FormData对象，且已经声明了属性enctype="multipart/form-data"
//         data:data0,
//         dataType:"Json",
//         async: false,
//         success:function(re)
//         {
//             alert("活动发布成功");
//         },
//         error:function(re)
//         { alert("活动发布失败"); }
//     })
//
// }
function getQueryString1(name) {  //以正则表达式去匹配URL传过来的参数，这个可模块化常用
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = decodeURI(window.location.search).substr(1).match(reg);
    if (r != null) return unescape(r[2]); return null;
}


function querybyidReb1(tableName, fieldName) {
    //后续改为通过session获取
// var aid=getSession()
    var aid="1";
   // alert("我在这1");
    $.ajax({
        url: "/findByaid",
        type: "GET",
        // 正确传递三个参数
        data: {
            "tableName": tableName,
            "fieldName": fieldName,
            "keyValue": aid
        },
        dataType: "json",
        async: false,
        success: function(re) {

            if (re && re.length > 0) {
                var item = re[0];
                console.log($("#aid").length > 0? "aid 元素存在" : "aid 元素不存在");
                console.log($("#uid").length > 0? "uid 元素存在" : "uid 元素不存在");
           //     alert("获取到的aid值："+ $("#aid").val(item["aid"]).toString()); // 添加这行日志
                $("#aid").val(item["aid"].toString());
                $("#uid").val(item["uid"].toString());
                $("#atitle").val(item["atitle"].toString());
                $("#adscpt").val(item["adscpt"].toString());
                $("#location").val(item["location"].toString());

                if(item["type"].toString()=="日常登山")
                    $("#richang").prop("checked","checked");
                else if(item["type"].toString()=="中长途旅游")
                    $("#zhongchangtu").prop("checked","checked");
                else if(item["type"].toString()=="其他")
                    $("#qita").prop("checked","checked");
                $("#start_time").val( item["start_time"].toString());
                $("#end_time").val( item["end_time"].toString());
                $("#max_people").val( item["max_people"].toString());
                $("#sign_up_start").val( item["sign_up_start"].toString());
                $("#sign_up_end").val( item["sign_up_end"].toString());
                $("#create_time").val( item["create_time"].toString());
               // alert("我在这2");

                var picTemp=item["aimg"].toString();
                $("#aimg").attr("src",item["aimg"].toString());
            }else {
                alert("未找到活动信息");
            }
        },
        error: function(jqXHR, textStatus, errorThrown) {
           // alert("我在这3");
            console.error("查询失败，状态码: ", jqXHR.status);
            console.error("状态文本: ", textStatus);
            console.error("错误信息: ", errorThrown);
            alert("查询失败");
        }
    });
}
//修改活动信息，修改图片和基本信息
function  modify2(form,controlUrl) {
    var re0 = null;
    var dataxs=new FormData($("#"+form)[0]);
    var entype0=$("#"+form).attr("enctype")//表单源是什么方式编码提交
    $.ajax({
        type: "POST",
        url: controlUrl,  //对应后端control的modifyXs
        cache: false,
        processData: false,//需设置为false。因为data值是FormData对象，不需要对数据做处理
        contentType: false,//需设置为false。因为是FormData对象，且已经声明了属性enctype="multipart/form-data"
        data: dataxs,
        dataType: "Json",
        async: false,
        success: function(re) {
            re0 = re;
          //  alert("通用修改数据和文件成功了");
        },
        error: function(re) {
            alert("通用修改数据不成功");
        }
    });
    return re0;
}
function  ModifyAct(form1,controlUrl ) {
    var re=modify2(form1,controlUrl);

    if (re == 1) // 假设后端返回了一个包含success属性的对象
        alert("修改成功");
    else
        alert("修改结果：" + JSON.stringify(re)); // 如果不是预期的结果，打印出来看
}
function  altFile() {
    var f0=document.createElement("input");
    f0.setAttribute("id","aimg");f0.setAttribute("type","file");
    f0.setAttribute("name","aimg");
    document.getElementById("sp1").append(f0);
}

//删除活动
function ActDel() {
  //  var aid = getQueryString("aid");
    //后续改为通过cookie获取
    var aid = '1';
    alert(aid);
    var data0 = {};
    data0.aid = aid;

    $.ajax({
        url: "actDeleteByaid",
        type: "post",
        dataType: "json",
        contentType: "application/json;charset=UTF-8",
        data: JSON.stringify(data0),
        success: function (re) {
            /*  var JsonString = JSON.stringify(re);*/
            alert("删除成功");
        },
        error: function (re2) {
            alert("删除失败");
        }
    });
}

//活动首页 快捷按钮***************************************8
//发布动态跳转1 √
function pubDynamicModal(){

    window.location.href = 'pubDynamic1.html';
}
//我的行程2
function myRouteModal(){
    window.location.href = 'MyActivities.html';
}
function openPublishModal(){
    window.location.href = 'pubAct.html';
}
function openAdminPanel(){
    window.location.href = 'Mana.html';
}
function openAuditPanel(){
    window.location.href = 'Audit.html';
}
function openModifyModal(){
    window.location.href = 'ModiAct.html';
}

function openMessageModal(){
    window.location.href = 'MySpace.html';
}
//我的收藏
function openCollectModal(){
    window.location.href = 'MyCollects.html';
}

//导航栏退出登录
//首页用户信息显示
$(document).ready(function() {
    // 处理退出登录点击事件
    $('.logout').on('click', function(e) {
        e.preventDefault();
        // 显示确认对话框
        if (confirm('确定要退出登录吗？')) {
            // 清除sessionStorage中的uid
            sessionStorage.removeItem('uid');
            sessionStorage.removeItem('tel');
            sessionStorage.removeItem('perm');
            // 刷新页面
            location.reload();
        }
    });

    // 从sessionStorage中获取uid
    var uid = sessionStorage.getItem('uid');

    console.log('sessionStorage', sessionStorage);
    console.log("从sessionStorage获取的uid：", uid);

    if (uid) {

        $.ajax({
            url: "/findByUid",
            type: "GET",
            data: { "uid": uid },
            dataType: "json",
            success: function(response) {
                if (response && response.length > 0) {
                    var user = response[0];
                    // 导航栏显示用户昵称
                    $('.user-info span').text(user.nickname);
                    // 导航栏显示用户头像
                    $('.user-info > img').attr('src', user.avatar).attr('alt', user.nickname);
                    // 活动发布显示用户昵称
                    $('.profile .name').text(user.nickname);
                    // 活动发布显示用户头像
                    $('.profile img').attr('src', user.avatar).attr('alt', user.nickname);
                    // 详情页评论框显示用户头像
                    $('.comment-input img.comment-avatar').attr('src', user.avatar).attr('alt', user.nickname);
                    // 个人资料卡数据填充（根据User实体类调整）
                    if (user) {
                        // 设置头像
                        $('.profile-card .avatar img').attr('src', user.avatar || '/static/image/img/default_avatar.png')
                            .attr('alt', user.nickname || '用户头像');

                        // 设置用户名
                        $('.profile-card .info .name-tag h2').text(user.nickname || '未设置昵称');

                        // 设置用户标签
                        var userTag = "普通用户";
                        if (user.perm === 3) userTag = "认证用户";
                        else if (user.perm === 2) userTag = "超级管理员";
                        $('.profile-card .info .name-tag span').text(userTag);

                        // 设置用户简介
                        $('.profile-card .info p').text(user.intr || '这家伙很懒，什么都没留下');

                        // 清空并重新填充meta信息
                        const metaContainer = $('.profile-card .info .meta');
                        metaContainer.empty();

                        // 设置加入时间
                        if (user.create_time) {
                            const joinDate = user.create_time.split(' ')[0];
                            const joinElement = $('<span class="meta-join"></span>')
                                .html(`<i><img src="/static/image/img/日历1.png"></i> 加入时间: ${joinDate}&nbsp&nbsp&nbsp`);
                            metaContainer.append(joinElement);
                        }

                        // 设置地址信息
                        if (user.addr) {
                            const addrElement = $('<span class="meta-addr"></span>')
                                .html(`<i><img src="/static/image/img/定位.png"></i>  ${user.addr}`);
                            metaContainer.append(addrElement);
                        }

                        // 设置评论区头像（如果有）
                        if (user.avatar) {
                            $('.comment-input img.comment-avatar').attr('src', user.avatar);
                        }
                    }
                }
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.error("查询用户信息失败", jqXHR, textStatus, errorThrown);
            }
        });
    } else {
        console.warn("未获取到uid，无法查询用户信息");
    }
});


//点击头像进入资料
    $(document).ready(function () {
        $('.user-info > img, .user-info span').click(function () {
            window.location.href = 'MyInfo.html';
        });
    });


    /****************动态发布界面******************/
//发布动态
function addDyn(form, controlUrl) {
    var formElement = document.getElementById(form);
    var formData = new FormData(formElement);
        console.log("FormData 中的文件数量", formData.getAll('media').length); // 确认表单是否包含多文件
    // 获取文件输入元素
    var fileInput = formElement.querySelector('input[type="file"]');
    console.log("前端选择的文件数量：", fileInput.files.length); // 确认是否多选
    var files = fileInput.files;
    // ===================== 【在你现有基础上修改：调整白名单+加数量+加大小】 =====================
    // 允许的文件类型：图片仅 jpg/png/webp，视频保持你原有
    var allowedImageExts = ['jpg', 'jpeg', 'png', 'webp'];
    var allowedVideoExts = ['mp4', 'mov'];
    var isFileValid = true;
    var invalidFiles = [];
    var imageCount = 0; // 新增：图片计数器
    var maxImageSize = 2 * 1024 * 1024; // 新增：2MB
    var maxVideoSize = 10 * 1024 * 1024; // 新增：10MB（可调整）

    // 检查每个文件类型
    for (var i = 0; i < files.length; i++) {
        var file = files[i];
        var fileName = file.name.toLowerCase();
        var fileExtension = fileName.split('.').pop();
        var isImage = allowedImageExts.includes(fileExtension);
        var isVideo = allowedVideoExts.includes(fileExtension);

        // 1. 检查文件扩展名
        if (!isImage && !isVideo) {
            isFileValid = false;
            invalidFiles.push(fileName + " (不支持的格式)");
            continue;
        }

        // 2. 新增：图片数量限制
        if (isImage) {
            imageCount++;
            if (imageCount > 6) {
                alert("图片最多上传6张！");
                return;
            }
        }

        // 3. 新增：文件大小限制
        if (isImage && file.size > maxImageSize) {
            isFileValid = false;
            invalidFiles.push(fileName + " (图片超过2MB)");
        }
        if (isVideo && file.size > maxVideoSize) {
            isFileValid = false;
            invalidFiles.push(fileName + " (视频超过10MB)");
        }
    }

    // 如果有非法文件，显示错误并终止上传
    if (!isFileValid) {
        alert('不允许上传以下文件：\n' + invalidFiles.join('\n') +
            '\n\n图片仅支持: jpg, png, webp (≤2MB，最多6张)\n视频仅支持: mp4, mov (≤10MB)');
        return; // 终止函数执行
    }

    // 发送表单数据到服务器
    $.ajax({
        type: "post",
        url: controlUrl,
        cache: false,
        processData: false,
        contentType: false,
        data: formData,
        dataType: "json",
        async: false,
        success: function (re) {
            if (re == -8) {
                alert("发布过于频繁！普通用户1小时最多发布20条，认证达人1小时最多30条，请稍后再试~");
                return;
            }
            if (re == -3) {
                alert("内容包含敏感词，请修改后再发布！");
                return;
            }else if (re === -1) {
                alert("请先登录后再发布动态！");
                window.location.href = "Login.html";
            } else {
                // 显示发布成功弹窗
                $("#successModal").css("display", "block");
                $("#modalOverlay").css("display", "block");

                // 绑定“查看动态”按钮点击事件
                $("#viewDynamicBtn").off("click").on("click", function () {
                    window.location.href = "Discover3.html";
                    // 隐藏弹窗和遮罩层
                    $("#successModal").css("display", "none");
                    $("#modalOverlay").css("display", "none");
                });

                // 绑定“继续发布”按钮点击事件
                $("#continuePublishBtn").off("click").on("click", function () {
                    // 清空表单内容
                    formElement.reset();
                    // 清空文件预览列表
                    $("#previewList").empty();
                    // 隐藏弹窗和遮罩层
                    $("#successModal").css("display", "none");
                    $("#modalOverlay").css("display", "none");
                });
            }
        },
        error: function (xhr, status, error) {
            console.error('发布失败：', error);
            alert('动态发布失败，请稍后重试！');
        }
    });
}

    /****************动态社区界面******************/

//发布动态评论
//     function addDcom(form, controlUrl) {
//
//         alert(form + " " + controlUrl);
//         var data0 = new FormData($("#" + form)[0]);
//         var entype0 = $("#" + form).attr("enctype")//判断表单源是什么方式编码提交
//         $.ajax({
//             type: "post",
//             url: controlUrl,
//             cache: false,
//             processData: false,
//             contentType: false,
//             data: data0,
//             dataType: "Json",
//             async: false,
//             success: function (re) {
//                 alert("通用基本数据和文件上传成功了");
//             },
//             error: function (re) {
//                 alert("通用基本数据和文件上传不成功");
//             }
//         })
//
//     }
// 发布评论（带防频繁提示）
// function addDcom(form, controlUrl) {
//
//     // alert(form + " " + controlUrl);
//     var data0 = new FormData($("#" + form)[0]);
//     var entype0 = $("#" + form).attr("enctype")
//
//     $.ajax({
//         type: "post",
//         url: controlUrl,
//         cache: false,
//         processData: false,
//         contentType: false,
//         data: data0,
//         dataType: "Json",
//         async: false,
//         success: function (re) {
//             // ===================== 我只加这里 =====================
//             if (re == -2) {
//                 alert("评论过快，请10分钟后再试~");
//                 return;
//             }
//             // =====================================================
//             alert("评论成功！");
//         },
//         error: function (re) {
//             alert("评论失败");
//         }
//     });
// }
// function addDcom(form, controlUrl) {
//
//     // alert(form + " " + controlUrl);
//     var data0 = new FormData($("#" + form)[0]);
//     var entype0 = $("#" + form).attr("enctype")
//
//     $.ajax({
//         type: "post",
//         url: controlUrl,
//         cache: false,
//         processData: false,
//         contentType: false,
//         data: data0,
//         dataType: "Json",
//         async: false,
//
//         // ====== 调试开始：看后端返回了什么 ======
//         success: function (re) {
//             console.log("【后端返回值】=>", re);  // 浏览器按 F12 看这里
//             alert("后端返回的值是：" + re);      // 弹窗告诉你返回了啥
//
//             if (re == -2) {
//                 alert("评论过快，请10分钟后再试~");
//                 return;
//             }
//
//             alert("评论成功！");
//         },
//
//         error: function (re) {
//             console.log("【请求出错】", re);
//             alert("评论失败，后端报错了");
//         }
//     });
// }
    /*****URL查询工具******/
    function getQueryString(name) {  //以正则表达式去匹配URL传过来的参数
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
        var r = decodeURI(window.location.search).substr(1).match(reg);
        if (r != null) return unescape(r[2]);
        return null;
    }


