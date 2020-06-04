package com.github.server.handle;

import com.github.server.model.MessageHexBody;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;

public class CollectServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
       /* ByteBuf buffer = ctx.alloc().buffer(4);
        buffer.writeInt((int)(System.currentTimeMillis()/1000L));
        ChannelFuture channelFuture = ctx.writeAndFlush(buffer);
        channelFuture.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) {
                assert future == future;
                ctx.close();
            }
        });*/

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        MessageHexBody body = (MessageHexBody) msg;

        System.out.println(Json.toJson(body,JsonFormat.tidy()));

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        ctx.close();
    }
}
