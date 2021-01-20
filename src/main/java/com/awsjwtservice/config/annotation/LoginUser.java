package com.awsjwtservice.config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER) // annotation 이 생성될 위치 -> parameter in this case -> meaning this will be like a middleware
@Retention(RetentionPolicy.RUNTIME)
public @interface LoginUser {  // 이 파일을 annotaion class로 지정 -> this @interface officially makes LoginUser a Annotation

}


/*
* create LoginUserArgumentResolver in the same directory
* LoginUserArgumentResolver is extended method of HandlerMethodArgumentResolver
* -> it's primary goal is to pass on Abstracted-Data in as parameter
* -> only if the condition is met
* */

