package com.fishep.java.network.select;

/**
 * @Author fly.fei
 * @Date 2024/10/21 14:24
 * @Desc
 **/
public class SelectorHandlerFactoy {

    static SelectorHandler createSelectorHandler() {
        ConnectHandler connectHandler = new ConnectHandler();
        AcceptHandler acceptHandler = new AcceptHandler();
        ReadHandler readHandler = new ReadHandler();
        WriteHandler writeHandler = new WriteHandler();

        connectHandler.setNext(acceptHandler).setNext(readHandler).setNext(writeHandler);

        return connectHandler;
    }

}
