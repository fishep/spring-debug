/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.fishep.netty.echo;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * Handler implementation for the echo server.
 */
@Slf4j
@Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        String data = ((ByteBuf) msg).toString(CharsetUtil.UTF_8);

        log.info("EchoServerHandler channelRead()");
        log.info("ChannelHandlerContext: " + ctx.hashCode());
        log.info("Channel: " + ctx.channel().hashCode());
        log.info("ChannelPipeline: " + ctx.pipeline().hashCode());

        ctx.channel().eventLoop().submit(() -> {
            try {
                Thread.sleep(10000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("EchoServerHandler channelRead() task");
            log.info("ChannelHandlerContext: " + ctx.hashCode());
            log.info("Channel: " + ctx.channel().hashCode());
            log.info("ChannelPipeline: " + ctx.pipeline().hashCode());
            log.info("object: " + hashCode() + ", server recv: " + data);
            ctx.write(msg);
            log.info("object: " + hashCode() + ", server send: " + data);
            ctx.flush();
            log.info("object: " + hashCode() + ", server flush");
        });
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
