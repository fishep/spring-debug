package com.fishep.java.network;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * @Author fly.fei
 * @Date 2024/10/19 10:22
 * @Desc
 **/
@Slf4j
public class SelectServer {

    public interface SelectorHandler {
        void handle(SelectionKey selectionKey) throws IOException;
    }

    public static abstract class AbstractSelectorHandler implements SelectorHandler {
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

    public static class ConnectHandler extends AbstractSelectorHandler {
        @Override
        protected boolean shouldHandle(SelectionKey selectionKey) {
            return selectionKey.isConnectable();
        }

        @Override
        protected void doHandle(SelectionKey selectionKey) throws IOException {
            log.info("ConnectHandler do this job");
        }
    }

    public static class AcceptHandler extends AbstractSelectorHandler {
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

    public static class ReadHandler extends AbstractSelectorHandler {
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

                log.info("ReadHandler: peer close");
            }
        }
    }

    public static class WriteHandler extends AbstractSelectorHandler {
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

    public interface SelectorHandlerFactoy {
        static SelectorHandler createSelectorHandler() {
            ConnectHandler connectHandler = new ConnectHandler();
            AcceptHandler acceptHandler = new AcceptHandler();
            ReadHandler readHandler = new ReadHandler();
            WriteHandler writeHandler = new WriteHandler();

            connectHandler.setNext(acceptHandler).setNext(readHandler).setNext(writeHandler);

            return connectHandler;
        }
    }

    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(10240);
        serverSocketChannel.bind(inetSocketAddress);

        serverSocketChannel.configureBlocking(false);
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
