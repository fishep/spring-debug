package com.fishep.java.network.multi;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Set;

/**
 * @Author fly.fei
 * @Date 2024/10/25 15:36
 * @Desc
 **/
@Slf4j
public class EventLoop {

    private Selector selector;

    private SelectorHandler selectorHandler;

    public EventLoop(Selector selector, SelectorHandler selectorHandler) {
        this.selector = selector;
        this.selectorHandler = selectorHandler;
    }

    public void run() throws IOException {
        while (true) {
            int n = selector.select();
            log.info("select " + n + " event");

            if (n > 0){
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    try {
                        selectorHandler.handle(selectionKey);
                    } catch (IOException ioException) {
                        selectionKey.cancel();
                        selectionKey.channel().close();
                        log.warn(ioException.getMessage());
                    }
                    iterator.remove();
                }
            }
        }
    }
}
