package com.fishep.testfixture.inherit;

/**
 * @Author fly.fei
 * @Date 2023/11/30 12:02
 * @Desc
 **/
public class Children extends Parent implements ChildrenInterface {

    public String field1 = "Children field1";

    public static String staticField1 = "Children staticField1";

    public int field2 = staticField2 + 1;

    public static int staticField2;

    public Children() {
        this("Children(String field1)");

        staticField2 = field2 + 1;
    }

    public Children(String field1) {

    }

    @Override
    public void childrenMethod1() {

    }

    @Override
    public void childrenMethod2(String p) {

    }

    @Override
    public String getField1() {
        return field1;
    }
}
