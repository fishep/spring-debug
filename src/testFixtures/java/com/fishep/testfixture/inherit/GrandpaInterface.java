package com.fishep.testfixture.inherit;

import com.fishep.testfixture.annotation.InheritedTypeAnnotation;
import com.fishep.testfixture.annotation.TypeAnnotation;

/**
 * @Author fly.fei
 * @Date 2024/2/24 12:09
 * @Desc
 **/
@TypeAnnotation
@InheritedTypeAnnotation
public interface GrandpaInterface {

    void grandpaMethod1();

    void grandpaMethod2(String p);

}
