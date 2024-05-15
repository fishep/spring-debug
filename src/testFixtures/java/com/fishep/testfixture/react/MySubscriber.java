package com.fishep.testfixture.react;

import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/**
 * @Author fly.fei
 * @Date 2024/4/11 14:41
 * @Desc
 **/
@Slf4j
public class MySubscriber<T> implements Subscriber<T> {

    @Override
    public void onSubscribe(Subscription s) {
        log.trace("onSubscribe");
        s.request(2);
    }

    @Override
    public void onNext(T t) {
        log.trace("onNext");

        if (t instanceof String) {
            log.trace((String) t);
        }
    }

    @Override
    public void onError(Throwable t) {
        log.trace("onError");
    }

    @Override
    public void onComplete() {
        log.trace("onComplete");
    }

}
