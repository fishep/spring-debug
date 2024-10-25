package com.fishep.java.network.reactor;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.channels.SelectionKey;

/**
 * @Author fly.fei
 * @Date 2024/10/21 14:21
 * @Desc
 **/
@Slf4j
public abstract class AbstractSelectorHandler implements SelectorHandler {
    protected AbstractSelectorHandler next;

    protected abstract boolean shouldHandle(SelectionKey selectionKey);

    protected abstract void doHandle(SelectionKey selectionKey) throws IOException;

    public AbstractSelectorHandler setNext(AbstractSelectorHandler handler) {
        next = handler;
        return next;
    }

    @Override
    public void handle(SelectionKey selectionKey) throws IOException {
        if (shouldHandle(selectionKey)) {
            doHandle(selectionKey);
        } else if (next != null) {
            next.handle(selectionKey);
        } else {
            log.info("no body do this job");
        }
    }
}
