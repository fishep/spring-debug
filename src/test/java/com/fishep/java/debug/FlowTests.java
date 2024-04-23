package com.fishep.java.debug;

import com.fishep.java.debug.flow.MyPublisher;
import com.fishep.java.debug.flow.MySubscriber;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

/**
 * @Author fly.fei
 * @Date 2024/4/11 11:58
 * @Desc
 **/
public class FlowTests {

    @Test
    void flow() {
        List<String> data = new LinkedList<>();
        data.add("hello");
        data.add("world");
        data.add("good");

        MySubscriber<String> subscriber = new MySubscriber<>();

        MyPublisher.fill(data).subscribe(subscriber);
    }

    @Test
    void map() {
        List<Integer> data = new LinkedList<>();
        data.add(1);
        data.add(2);
        data.add(3);

        MySubscriber<String> subscriber = new MySubscriber<>();

        MyPublisher.fill(data).map(i -> "hello " + i).subscribe(subscriber);
    }
}
