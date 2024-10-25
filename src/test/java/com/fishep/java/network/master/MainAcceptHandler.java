package com.fishep.java.network.master;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.Random;

/**
 * @Author fly.fei
 * @Date 2024/10/21 14:23
 * @Desc
 **/
@Slf4j
public class MainAcceptHandler extends AbstractSelectorHandler {

    private List<Selector> workSelectors;

    public MainAcceptHandler(List<Selector> workSelectors) {
        this.workSelectors = workSelectors;
    }

    @Override
    protected boolean shouldHandle(SelectionKey selectionKey) {
        return selectionKey.isAcceptable();
    }

    @Override
    protected void doHandle(SelectionKey selectionKey) throws IOException {
        log.info("MainAcceptHandler do this job");

        ServerSocketChannel channel = (ServerSocketChannel) selectionKey.channel();
        SocketChannel socketChannel = channel.accept();

        int i = new Random().nextInt(workSelectors.size());
        log.info("i = " + i);

        Selector workSelector = workSelectors.get(i);

        socketChannel.configureBlocking(false);
        socketChannel.register(workSelector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
        workSelector.wakeup();
    }

}
