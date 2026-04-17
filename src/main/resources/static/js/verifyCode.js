// 页面加载时生成初始验证码
$(document).ready(function() {
    generateVerifyCode();
});

function generateVerifyCode() {
    // 调用后端接口生成验证码（响应为图片流）
    $("#verifyImg").attr("src", "/generateVerifyCode?" + new Date().getTime()); // 防止缓存
}

function refreshVerifyCode() {
    generateVerifyCode(); // 刷新验证码图片
}