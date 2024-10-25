package com.fishep.java.network.reactor;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @Author fly.fei
 * @Date 2024/10/21 14:23
 * @Desc
 **/
@Slf4j
public class AcceptHandler extends AbstractSelectorHandler {

    @Override
    protected boolean shouldHandle(SelectionKey selectionKey) {
        return selectionKey.isAcceptable();
    }

    @Override
    protected void doHandle(SelectionKey selectionKey) throws IOException {
        log.info("AcceptHandler do this job");

        Selector selector = selectionKey.selector();

        ServerSocketChannel channel = (ServerSocketChannel) selectionKey.channel();
        SocketChannel socketChannel = channel.accept();

        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
    }

}
