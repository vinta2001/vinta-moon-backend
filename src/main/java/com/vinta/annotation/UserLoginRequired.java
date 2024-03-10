package com.vinta.annotation;


import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UserLoginRequired {
    boolean required() default true;
}
