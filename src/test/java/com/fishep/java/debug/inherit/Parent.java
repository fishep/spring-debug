package com.fishep.java.debug.inherit;

import lombok.extern.slf4j.Slf4j;

/**
 * @Author fly.fei
 * @Date 2023/11/30 12:00
 * @Desc
 **/
@Slf4j
public class Parent extends Grandpa implements ParentInterface {

    public String field1 = "Parent field1";

    public static String staticField1 = "Parent staticField1";

    public Parent() {
        log.trace("Parent()");
    }

    @Override
    public void parentMethod1() {

    }

    @Override
    public void parentMethod2() {

    }
}
