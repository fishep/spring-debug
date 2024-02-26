package com.fishep.java.debug;

import com.fishep.java.debug.annotation.InheritedTypeAnnotation;
import com.fishep.java.debug.annotation.TypeAnnotation;
import com.fishep.java.debug.inherit.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
}