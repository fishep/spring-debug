package com.fishep.java.network.select;

import java.io.IOException;
import java.nio.channels.SelectionKey;

/**
 * @Author fly.fei
 * @Date 2024/10/21 14:21
 * @Desc
 **/
public interface SelectorHandler {
    void handle(SelectionKey selectionKey) throws IOException;
}
