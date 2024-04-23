package com.fishep.spring.debug.webflux.ws;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Slf4j
@Component
public class Handler implements WebSocketHandler {

    @Override
    public Mono<Void> handle(WebSocketSession session) {

        log.trace("handle session " + session.getId());

        Holder<FluxSink<WebSocketMessage>> holder = new Holder<>();

        Mono<Void> send = session.send(Flux.create(emitter -> {
            holder.accept(emitter);

            WebSocketMessage message = session.textMessage("welcome !");
            log.trace("emitter " + message.getPayloadAsText());
            emitter.next(message);
        }));

        Mono<Void> receive = session.receive().doOnNext((t) -> {
            log.trace("receive " + t);

            FluxSink<WebSocketMessage> emitter = holder.get();
            String message = "error, message is not text !";
            if (t.getType().equals(WebSocketMessage.Type.TEXT)) {
                message = t.getPayloadAsText();
            }
            log.trace("emitter " + message);
            emitter.next(session.textMessage(message));
        }).then();

        log.trace("flux set end ");

        return Mono.zip(send, receive).then();
    }

    @Override
    public List<String> getSubProtocols() {
        return WebSocketHandler.super.getSubProtocols();
    }

    public static class Holder<T> implements Supplier<T>, Consumer<T> {
        private T t;

        @Override
        public void accept(T t) {
            this.t = t;
        }

        @Override
        public T get() {
            return t;
        }
    }

}
