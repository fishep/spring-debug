package com.fishep.java.debug.inherit;

/**
 * @Author fly.fei
 * @Date 2023/11/30 12:00
 * @Desc
 **/
public class Parent extends Grandpa implements ParentInterface {

    public String field1 = "Parent field1";

    public static String staticField1 = "Parent staticField1";

    public Parent() {
        System.out.println("Parent");
    }

    @Override
    public void parentMethod1() {

    }

    @Override
    public void parentMethod2() {

    }
}
