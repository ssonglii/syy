package com.example.syy.config;

import com.example.syy.interceptor.CsrfInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new CsrfInterceptor())
                .addPathPatterns("/**") // 拦截所有接口
                .excludePathPatterns("/api/security/get-csrf-token"); // 获取token接口放行
    }
}