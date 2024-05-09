package com.fishep.testfixture.annotation;

import java.lang.annotation.*;

/**
 * @Author fly.fei
 * @Date 2024/2/24 11:53
 * @Desc
 **/

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface TypeAnnotation {
}
