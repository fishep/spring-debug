package com.fishep.java.debug.flow;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Flow;

/**
 * @Author fly.fei
 * @Date 2024/4/11 12:01
 * @Desc
 **/
@Slf4j
public class MySubscriber<T> implements Flow.Subscriber<T> {

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        log.trace("onSubscribe");

        subscription.request(2);
    }

    @Override
    public void onNext(T item) {
        log.trace("onNext");

        if (item instanceof String) {
            log.info((String) item);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        log.trace("onError");
    }

    @Override
    public void onComplete() {
        log.trace("onComplete");
    }

}
