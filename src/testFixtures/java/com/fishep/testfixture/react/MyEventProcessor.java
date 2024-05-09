package com.fishep.testfixture.react;

/**
 * @Author fly.fei
 * @Date 2024/4/12 11:48
 * @Desc
 **/
public class MyEventProcessor<T> {

    private MyEventListener<T> myEventListener;

    public void register(MyEventListener<T> myEventListener) {
        this.myEventListener = myEventListener;
    }

    public void newEvent(MyEvent<T> myEvent) {
        this.myEventListener.onDataChunk(myEvent);
    }

    public void processComplete() {
        this.myEventListener.processComplete();
    }

}
