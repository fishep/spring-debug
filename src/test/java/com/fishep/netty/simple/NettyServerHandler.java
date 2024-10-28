package com.fishep.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @Author fly.fei
 * @Date 2024/10/28 15:38
 * @Desc
 **/
@Slf4j
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    public NettyServerHandler() {
        super();

        log.info("NettyServerHandler()");
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        log.info("channelRegistered() begin");

        super.channelRegistered(ctx);

        log.info("channelRegistered() end");
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        log.info("channelUnregistered() begin");

        super.channelUnregistered(ctx);

        log.info("channelUnregistered() end");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("channelActive() begin");

        super.channelActive(ctx);

        String msg = "hello world, 你好 世界！\n";
        ctx.writeAndFlush(Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8));
        log.info("server send: " + msg);

        log.info("channelActive() end");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("channelInactive() begin");

        super.channelInactive(ctx);

        log.info("channelInactive() end");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("channelRead() begin");

        ByteBuf in = (ByteBuf) msg;
        ByteBuf out = Unpooled.copiedBuffer(in.toString(CharsetUtil.UTF_8), CharsetUtil.UTF_8);

        log.info("server recv: " + in.toString(CharsetUtil.UTF_8));
        log.info("server send: " + out.toString(CharsetUtil.UTF_8));
        ctx.writeAndFlush(out);

        super.channelRead(ctx, msg);

        log.info("channelRead() end");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        log.info("channelReadComplete() begin");

        super.channelReadComplete(ctx);

        log.info("channelReadComplete() end");
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        log.info("userEventTriggered() begin");

        super.userEventTriggered(ctx, evt);

        log.info("userEventTriggered() end");
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        log.info("channelWritabilityChanged() begin");

        super.channelWritabilityChanged(ctx);

        log.info("channelWritabilityChanged() end");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("exceptionCaught() begin");

        super.exceptionCaught(ctx, cause);

        log.info("exceptionCaught() end");
    }
}
