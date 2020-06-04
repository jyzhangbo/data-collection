package com.github.server.handle;

import com.github.server.model.MessageHexBody;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * @Author:zhangbo
 * @Date:2020/6/4 16:19
 */
public class HexToValueDecoder extends MessageToMessageDecoder<MessageHexBody> {

    @Override
    protected void decode(ChannelHandlerContext ctx, MessageHexBody msg, List<Object> out) throws Exception {

    }
}
