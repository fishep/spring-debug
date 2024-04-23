package com.fishep.java.debug.flow;

import java.util.List;
import java.util.concurrent.Flow;

/**
 * @Author fly.fei
 * @Date 2024/4/11 12:01
 * @Desc
 **/
public class MySubscription<T> implements Flow.Subscription {

    private final Flow.Subscriber<? super T> mySubscriber;

    private final List<? extends T> data;

    public MySubscription(Flow.Subscriber<? super T> mySubscriber, List<? extends T> data) {
        this.mySubscriber = mySubscriber;
        this.data = data;
    }

    @Override
    public void request(long n) {

        if (n <= data.size()) {
            for (int i = 0; i < n; i++) {
                mySubscriber.onNext(data.get(i));
            }
        }

        mySubscriber.onComplete();
    }

    @Override
    public void cancel() {

    }

}
