package com.example.syy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                // 关闭 CSRF（前端没处理的话，开发阶段可关，生产需酌情开启）
//                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(auth -> auth
//                        // ========== 1. 放行公开资源 & 页面 & 接口 ==========
//                        // 页面访问（control1 里的视图接口）
//                        .antMatchers(
//                                "/userReg2.html",
//                                "/Login.html",
//                                "/MyInfo.html",
//                                "/Index.html",
//                                "/pubAct.html",
//                                "/ModiAct.html",
//                                "/ActivityDetails.html",
//                                "/PubDynamic.html",
//                                "/pubDynamic1.html",
//                                "/Discover3.html",
//                                "/AllActivities.html",
//                                "/dynDetails.html",
//                                "/dynDetails1.html",
//                                "/Signup.html",
//                                "/MyCollects.html",
//                                "/myMessages.html",
//                                "/Audit.html",
//                                "/ActivityDetails0.html"
//                        ).permitAll()
//                        // 注册、加密接口
//                        .antMatchers("/useraddAll", "/userAdd", "/api/security/**").permitAll()
//                        // 登录接口
//                        .antMatchers("/finduserBytel").permitAll()
//                        // 首页活动/动态查询（公开接口）
//                        .antMatchers("/index", "index0", "/DynQueryAll", "/findActivity", "/findActivity0").permitAll()
//                        // 静态资源（CSS、JS、图片等）
//                        .antMatchers("/static/**").permitAll()
//                        // 活动评论、列表查询接口（公开）
//                        .antMatchers("/GetComments", "/GetReply", "/findAllActivities", "/findCounts", "/findAllActivities0", "/findAll").permitAll()
// // **新增：放行验证码相关路径**
//            .antMatchers("/generateVerifyCode", "/static/image/img/**").permitAll()
//                        // ========== 2. 其他请求需登录 ==========
//                        .anyRequest().authenticated()
//                )
//                // 配置登录页（如果登录逻辑是表单提交，需对应）
//                .formLogin(form -> form
//                        .loginPage("/Login.html") // 指向你的登录页
//                        .permitAll() // 登录页必须放行
//                );
//        return http.build();
//    }
@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
            // 关闭 CSRF（可选，开发阶段建议关）
            .csrf(csrf -> csrf.disable())
            // 放行所有请求
            .authorizeHttpRequests(auth -> auth
                    .anyRequest().permitAll() // 关键：所有请求都放行
            )
            // 关闭表单登录（可选，因为已经放行所有请求）
            .formLogin(form -> form.disable());
    return http.build();
}

}