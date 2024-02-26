package com.fishep.java.debug.annotation;

import java.lang.annotation.*;

/**
 * @Author fly.fei
 * @Date 2024/2/24 11:53
 * @Desc
 **/

@Inherited
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface InheritedMethodAnnotation {
}
