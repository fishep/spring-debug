package com.fishep.testfixture.flow;

import java.util.concurrent.Flow;

/**
 * @Author fly.fei
 * @Date 2024/4/11 12:01
 * @Desc
 **/
public class MySubscriber<T> implements Flow.Subscriber<T> {

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        subscription.request(2);
    }

    @Override
    public void onNext(T item) {
        if (item instanceof String) {
        }
    }

    @Override
    public void onError(Throwable throwable) {
    }

    @Override
    public void onComplete() {
    }

}
