package com.fishep.java.network.master;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author fly.fei
 * @Date 2024/10/19 10:22
 * @Desc
 **/
@Slf4j
public class SelectServer {

    public static void main(String[] args) throws IOException {
        Selector mainSelector1 = Selector.open();
        Selector workSelector1 = Selector.open();
        Selector workSelector2 = Selector.open();
        LinkedList<Selector> selectors = new LinkedList<>();
        selectors.add(workSelector1);
        selectors.add(workSelector2);

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(false);

        InetSocketAddress inetSocketAddress = new InetSocketAddress(10240);
        serverSocketChannel.bind(inetSocketAddress);

        serverSocketChannel.register(mainSelector1, SelectionKey.OP_ACCEPT);

        ExecutorService es = Executors.newFixedThreadPool(3);
        es.submit(() -> {
            log.info("main1 service start!");
            SelectorHandler selectorHandler = new MainAcceptHandler(selectors);
            EventLoop eventLoop = new EventLoop(mainSelector1, selectorHandler);
            try {
                eventLoop.run();
            } catch (IOException e) {
                e.printStackTrace();
            }
            log.info("main1 service end!");
        });
        es.submit(() -> {
            log.info("work1 service start!");
            SelectorHandler selectorHandler = SelectorHandlerFactoy.createSelectorHandler();
            EventLoop eventLoop = new EventLoop(workSelector1, selectorHandler);
            try {
                eventLoop.run();
            } catch (IOException e) {
                e.printStackTrace();
            }
            log.info("work1 service end!");
        });
        es.submit(() -> {
            log.info("work2 service start!");
            SelectorHandler selectorHandler = SelectorHandlerFactoy.createSelectorHandler();
            EventLoop eventLoop = new EventLoop(workSelector2, selectorHandler);
            try {
                eventLoop.run();
            } catch (IOException e) {
                e.printStackTrace();
            }
            log.info("work2 service end!");
        });
    }

}
