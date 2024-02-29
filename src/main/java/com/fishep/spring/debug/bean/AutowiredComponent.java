package com.fishep.spring.debug.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author fly.fei
 * @Date 2024/2/29 10:37
 * @Desc
 **/
@Component
public class AutowiredComponent {

    @Autowired
    private MyComponent myComponent;

    private String value;

    public AutowiredComponent() {
        //异常 静态变量或静态语句块 –> 实例变量或初始化语句块 –> 构造方法 -> @Autowired 的顺序
//        value = myComponent.method1();
    }

    public MyComponent getMyComponent() {
        return myComponent;
    }

    public String getValue() {
        return value;
    }

}
