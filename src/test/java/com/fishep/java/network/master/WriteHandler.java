package com.fishep.java.network.master;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * @Author fly.fei
 * @Date 2024/10/21 14:24
 * @Desc
 **/
@Slf4j
public class WriteHandler extends AbstractSelectorHandler {

    @Override
    protected boolean shouldHandle(SelectionKey selectionKey) {
        return selectionKey.isWritable();
    }

    @Override
    protected void doHandle(SelectionKey selectionKey) throws IOException {
        log.info("WriteHandler do this job");

        String attachment = (String) selectionKey.attachment();
        if (attachment == null) {
            attachment = "hello world, 你好 世界！\n";
        }

        byte[] bytes = attachment.getBytes(StandardCharsets.UTF_8);

        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        socketChannel.write(ByteBuffer.wrap(bytes));

        selectionKey.interestOps(SelectionKey.OP_READ);

        log.info("WriteHandler: " + attachment);
    }

}
