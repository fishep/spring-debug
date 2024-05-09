package com.fishep.java;

import com.fishep.testfixture.inherit.Children;
import com.fishep.testfixture.inherit.Parent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @Author fly.fei
 * @Date 2023/11/27 14:52
 * @Desc
 **/
public class ConstructionTests {

    @Test
    void testConstruction() {
        assertEquals("Children staticField1", Children.staticField1);
        assertEquals("Parent staticField1", Parent.staticField1);

        Children children = new Children();
        Parent parent = children;
        assertEquals("Children field1", children.field1);
        assertEquals("Parent field1", parent.field1);
        assertEquals("Children field1", children.getField1());
        assertEquals("Children field1", parent.getField1());

        assertEquals(1, children.field2);
        assertEquals(2, Children.staticField2);
    }

}
