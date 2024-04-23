package com.fishep.java.debug.flow;

import java.util.List;
import java.util.concurrent.Flow;
import java.util.function.Function;

/**
 * @Author fly.fei
 * @Date 2024/4/11 12:00
 * @Desc
 **/
public class MyPublisher<T> implements Flow.Publisher<T> {

    private List<? extends T> list;

    @Override
    public void subscribe(Flow.Subscriber<? super T> subscriber) {

        Flow.Subscription subscription = new MySubscription<>(subscriber, list);

        subscriber.onSubscribe(subscription);
    }

    public <R> Flow.Publisher<R> map(Function<? super T, ? extends R> mapper) {
        return map(this, mapper);
    }

    public static <T> MyPublisher<T> fill(List<T> list) {

        MyPublisher<T> publisher = new MyPublisher<>();

        publisher.list = list;

        return publisher;
    }

    public static <T, R> Flow.Publisher<R> map(Flow.Publisher<T> publisher, Function<? super T, ? extends R> mapper) {

        Flow.Publisher<R> processor = new MyProcessor<>(publisher, mapper);

        return processor;
    }

}
