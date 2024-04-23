package com.fishep.java.debug.inherit;

import com.fishep.java.debug.annotation.InheritedTypeAnnotation;
import com.fishep.java.debug.annotation.TypeAnnotation;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author fly.fei
 * @Date 2023/11/30 12:15
 * @Desc
 **/
@Slf4j
@TypeAnnotation
@InheritedTypeAnnotation
public class Grandpa implements GrandpaInterface {

    public String field1 = "Grandpa field1";

    public static String staticField1 = "Grandpa staticField1";

    public Grandpa() {
        log.trace("Grandpa()");
    }

    @Override
    public void grandpaMethod1() {

    }

    @Override
    public void grandpaMethod2(String p) {

    }
}
