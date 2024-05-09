package com.fishep.testfixture.react;

/**
 * @Author fly.fei
 * @Date 2024/4/12 11:54
 * @Desc
 **/
public interface MyEventListener<T> {

    void onDataChunk(MyEvent<T> event);

    void processComplete();

}
