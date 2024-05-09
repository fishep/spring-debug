package com.fishep.java;

import com.fishep.testfixture.generic.Generic;
import com.fishep.testfixture.generic.GenericSingleton;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author fly.fei
 * @Date 2024/3/13 11:52
 * @Desc
 **/
public class GenericTests {

    @Test
    void reflection() {
        Generic<String, Boolean> generic1 = new Generic<>();
        Generic<String, String> generic2 = new Generic<>();
        Class<Generic> clazz = Generic.class;

        Class<? extends Generic> objClass1 = generic1.getClass();
        Class<? extends Generic> objClass2 = generic2.getClass();

        assertSame(clazz, objClass1);
        assertSame(objClass1, objClass2);

        TypeVariable<? extends Class<? extends Generic>>[] typeParameters = clazz.getTypeParameters();
        for (TypeVariable<? extends Class<? extends Generic>> typeParameter : typeParameters) {
            Type[] bounds = typeParameter.getBounds();
            for (Type tb : bounds) {
                Type t = tb;
            }
        }

        Type genericSuperclass = clazz.getGenericSuperclass();

        Type[] genericInterfaces = clazz.getGenericInterfaces();
    }

    @Test
    void singleton(){
        GenericSingleton<Object> instance = GenericSingleton.getInstance();
        instance.setT("hello");
        assertEquals("hello", instance.getT());
        assertInstanceOf(String.class, instance.getT());

        instance.setT(100L);
        assertEquals(100L, instance.getT());
        assertInstanceOf(Long.class, instance.getT());
    }

}
