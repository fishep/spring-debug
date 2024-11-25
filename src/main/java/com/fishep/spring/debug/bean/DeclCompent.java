package com.fishep.spring.debug.bean;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author fly.fei
 * @Date 2024/11/21 11:09
 * @Desc
 **/
@Slf4j
@Component
public class DeclCompent<T> {

    private T str;

    public T getStr() {
        return str;
    }

    public void setStr(T str) {
        this.str = str;
    }
}
