package com.fishep.testfixture.flow;

import java.util.concurrent.Flow;
import java.util.function.Function;

/**
 * @Author fly.fei
 * @Date 2024/4/11 16:29
 * @Desc
 **/
public class MyProcessor<T, R> implements Flow.Processor<T, R> {

    private final Flow.Publisher<T> publisher;

    private Flow.Subscriber<? super R> subscriber;

    Function<? super T, ? extends R> mapper;

    public MyProcessor(Flow.Publisher<T> publisher, Function<? super T, ? extends R> mapper) {
        this.publisher = publisher;
        this.mapper = mapper;
    }

    @Override
    public void subscribe(Flow.Subscriber<? super R> subscriber) {

        this.subscriber = subscriber;

        publisher.subscribe(this);
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscriber.onSubscribe(subscription);
    }

    @Override
    public void onNext(T item) {

        R r = mapper.apply(item);

        this.subscriber.onNext(r);
    }

    @Override
    public void onError(Throwable throwable) {
        this.subscriber.onError(throwable);
    }

    @Override
    public void onComplete() {
        this.subscriber.onComplete();
    }

}
