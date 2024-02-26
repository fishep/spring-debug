package com.fishep.java.debug.inherit;

/**
 * @Author fly.fei
 * @Date 2023/11/30 12:02
 * @Desc
 **/
public class Children extends Parent implements ChildrenInterface {

    public String field1 = "Children field1";

    public static String staticField1 = "Children staticField1";

    public Children() {
        this("Children(String field1)");

        System.out.println("Children");
    }

    public Children(String field1) {
        System.out.println("Children(String field1)");

        this.field1 = field1;
    }

    @Override
    public void childrenMethod1() {

    }

    @Override
    public void childrenMethod2() {

    }

}
