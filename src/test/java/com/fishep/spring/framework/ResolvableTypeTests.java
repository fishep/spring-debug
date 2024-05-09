package com.fishep.spring.framework;

import com.fishep.testfixture.generic.Generic;
import com.fishep.testfixture.generic.GenericParent;
import org.junit.jupiter.api.Test;
import org.springframework.core.ResolvableType;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @Author fly.fei
 * @Date 2024/4/20 15:34
 * @Desc
 **/
public class ResolvableTypeTests {

    @Test
    void isAssignableFrom() {
        assertTrue(Object.class.isAssignableFrom(String.class));
        assertFalse(String.class.isAssignableFrom(Object.class));
        assertFalse(String.class.isAssignableFrom(Boolean.class));
        assertFalse(Boolean.class.isAssignableFrom(Object.class));

        assertTrue(ResolvableType.forType(Object.class).isAssignableFrom(String.class));
        assertFalse(ResolvableType.forType(String.class).isAssignableFrom(Object.class));
        assertFalse(ResolvableType.forType(String.class).isAssignableFrom(Boolean.class));
        assertFalse(ResolvableType.forType(Boolean.class).isAssignableFrom(String.class));

        assertTrue(ResolvableType.forType(GenericParent.class).isAssignableFrom(Generic.class));
        assertFalse(ResolvableType.forType(Generic.class).isAssignableFrom(GenericParent.class));
    }

    @Test
    void generic() {
        ResolvableType generic1 = ResolvableType.forType(Generic.class).getGeneric(0);
        ResolvableType generic2 = ResolvableType.forType(Generic.class).getGeneric(1);

        assertTrue(generic1.isAssignableFrom(String.class));
        assertTrue(generic1.isAssignableFrom(Boolean.class));
        assertTrue(generic1.isAssignableFrom(Generic.class));

        assertTrue(generic2.isAssignableFrom(Boolean.class));
        assertTrue(generic2.isAssignableFrom(String.class));
        assertTrue(generic2.isAssignableFrom(Generic.class));
    }

}
