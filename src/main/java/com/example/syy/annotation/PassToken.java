package com.example.syy.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 1. 用来跳过验证的PassToken*/
@Target({ElementType.METHOD, ElementType.TYPE})//可以修饰方法也可修饰类
@Retention(RetentionPolicy.RUNTIME)//运行时加载生效
public @interface PassToken {
    boolean required() default true;
}