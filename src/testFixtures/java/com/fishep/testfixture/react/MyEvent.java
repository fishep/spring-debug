package com.fishep.testfixture.react;

import java.util.Date;

/**
 * @Author fly.fei
 * @Date 2024/4/12 11:55
 * @Desc
 **/
public class MyEvent<T> {

    private Date date;

    private T t;

    public MyEvent(Date date, T t) {
        this.date = date;
        this.t = t;
    }

    @Override
    public String toString() {
        return "MyEvent{" +
                "date=" + date +
                ", t=" + t +
                '}';
    }

}
