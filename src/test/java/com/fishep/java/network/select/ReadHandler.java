package com.fishep.java.network.select;

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
public class ReadHandler extends AbstractSelectorHandler {

    @Override
    protected boolean shouldHandle(SelectionKey selectionKey) {
        return selectionKey.isReadable();
    }

    @Override
    protected void doHandle(SelectionKey selectionKey) throws IOException {
        log.info("ReadHandler do this job");

        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(64);
        int n = socketChannel.read(byteBuffer);
        if (n > 0) {
            String info = new String(byteBuffer.array(), StandardCharsets.UTF_8);

            selectionKey.interestOps(SelectionKey.OP_WRITE);
            selectionKey.attach(info);

            log.info("ReadHandler: " + info);
        } else {
            selectionKey.cancel();
            socketChannel.close();

            log.info("ReadHandler: peer close");
        }
    }

}
