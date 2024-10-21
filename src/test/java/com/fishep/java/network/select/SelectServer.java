package com.fishep.java.network.select;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @Author fly.fei
 * @Date 2024/10/19 10:22
 * @Desc
 **/
@Slf4j
public class SelectServer {

    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);

        InetSocketAddress inetSocketAddress = new InetSocketAddress(10240);
        serverSocketChannel.bind(inetSocketAddress);

        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        SelectorHandler selectorHandler = SelectorHandlerFactoy.createSelectorHandler();
        while (true) {
            int n = selector.select();
            log.info("select " + n + " event");

            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                selectorHandler.handle(selectionKey);

                iterator.remove();
            }
        }
    }

}
