package com.fishep.java;

import com.fishep.testfixture.annotation.InheritedTypeAnnotation;
import com.fishep.testfixture.annotation.TypeAnnotation;
import com.fishep.testfixture.inherit.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @Author fly.fei
 * @Date 2024/1/4 14:14
 * @Desc
 **/
public class InheritedAnnotationTests {

    @Test
    void classInherited() {
        assertTrue(Grandpa.class.isAnnotationPresent(TypeAnnotation.class));
        assertTrue(Grandpa.class.isAnnotationPresent(InheritedTypeAnnotation.class));

        assertFalse(Parent.class.isAnnotationPresent(TypeAnnotation.class));
        assertTrue(Parent.class.isAnnotationPresent(InheritedTypeAnnotation.class));

        assertFalse(Children.class.isAnnotationPresent(TypeAnnotation.class));
        assertTrue(Children.class.isAnnotationPresent(InheritedTypeAnnotation.class));
    }

    @Test
    void interfaceInherited() {
        assertTrue(GrandpaInterface.class.isAnnotationPresent(TypeAnnotation.class));
        assertTrue(GrandpaInterface.class.isAnnotationPresent(InheritedTypeAnnotation.class));

        assertFalse(ParentInterface.class.isAnnotationPresent(TypeAnnotation.class));
        assertFalse(ParentInterface.class.isAnnotationPresent(InheritedTypeAnnotation.class));

        assertFalse(ChildrenInterface.class.isAnnotationPresent(TypeAnnotation.class));
        assertFalse(ChildrenInterface.class.isAnnotationPresent(InheritedTypeAnnotation.class));
    }

}