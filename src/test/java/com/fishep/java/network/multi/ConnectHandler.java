package com.fishep.java.network.multi;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * @Author fly.fei
 * @Date 2024/10/21 14:23
 * @Desc
 **/
@Slf4j
public class ConnectHandler extends AbstractSelectorHandler {
    @Override
    protected boolean shouldHandle(SelectionKey selectionKey) {
        return selectionKey.isConnectable();
    }

    @Override
    protected void doHandle(SelectionKey selectionKey) throws IOException {
        log.info("ConnectHandler do this job");

        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        while (!socketChannel.finishConnect()) {
            log.info("SelectClient wait for connect");
        }

        selectionKey.interestOps(SelectionKey.OP_READ);
    }
}
