package com.fishep.java.network.multi;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

/**
 * @Author fly.fei
 * @Date 2024/10/21 12:14
 * @Desc
 **/
@Slf4j
public class SelectClient {

    public static void main(String[] args) throws IOException, InterruptedException {
//        syc();
        select();
    }

    public static void select() throws IOException, InterruptedException {
        Selector selector = Selector.open();

        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.configureBlocking(false);

        int randomPort = 10240 + new Random().nextInt(2);
        InetSocketAddress inetSocketAddress = new InetSocketAddress(randomPort);
        socketChannel.connect(inetSocketAddress);

        log.info("client connect port: " + randomPort);

        socketChannel.register(selector, SelectionKey.OP_CONNECT);

        SelectorHandler selectorHandler = SelectorHandlerFactoy.createSelectorHandler();
        while (true) {
            int n = selector.select();
            log.info("select " + n + " event");

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

            Thread.sleep(10000L);
        }
    }

    public static void syc() throws IOException, InterruptedException {
        SocketChannel socketChannel = SocketChannel.open();

        InetSocketAddress inetSocketAddress = new InetSocketAddress(10240);

        if (!socketChannel.connect(inetSocketAddress)) {
            while (!socketChannel.finishConnect()) {
                log.info("SelectClient wait for connect");
            }
        }

        ByteBuffer byteBuffer = ByteBuffer.allocate(64);
        while (true) {
            byteBuffer.clear();
            socketChannel.read(byteBuffer);
            String info = new String(byteBuffer.array(), StandardCharsets.UTF_8);
            log.info("read: " + info);

            byteBuffer.flip();
            socketChannel.write(byteBuffer);
            log.info("write: " + info);

            Thread.sleep(3000L);
        }
    }

}
