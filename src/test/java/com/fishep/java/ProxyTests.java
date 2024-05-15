package com.fishep.java;

import com.fishep.testfixture.inherit.ChildrenInterface;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * @Author fly.fei
 * @Date 2024/4/20 17:46
 * @Desc
 **/
@Slf4j
public class ProxyTests {

    @Test
    void proxyInterface() {

        InvocationHandler handler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                log.info("proxy " + proxy.getClass().getName());

                if (method.getName() == "childrenMethod2") {
                    log.info("invoke childrenMethod2");
                }
                if (method.getName() == "parentMethod2") {
                    log.info("invoke parentMethod2");
                }
                if (method.getName() == "grandpaMethod2") {
                    log.info("invoke grandpaMethod2");
                }
                log.info("args " + Arrays.toString(args));
                
//                自己调自己
//                method.invoke(proxy, args);

                return null;
            }
        };

        ChildrenInterface instance = (ChildrenInterface) Proxy.newProxyInstance(ChildrenInterface.class.getClassLoader(), new Class[]{ChildrenInterface.class}, handler);

        instance.childrenMethod2("pc");
        instance.parentMethod2("pp");
        instance.grandpaMethod2("pg");
    }

}
