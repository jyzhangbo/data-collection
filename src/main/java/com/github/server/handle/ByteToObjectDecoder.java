package com.github.server.handle;

import com.github.server.model.MessageHexBody;
import com.github.server.util.TransformUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.ReplayingDecoder;
import io.netty.util.CharsetUtil;
import org.nutz.lang.Strings;

import java.util.List;

/**
 * @Author:zhangbo
 * @Date:2020/6/3 12:04
 */
public class ByteToObjectDecoder extends ReplayingDecoder<ObjectDecoderState> {

    private int length;

    public ByteToObjectDecoder(){
        super(ObjectDecoderState.READ_HEADER);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        switch (state()) {
            case READ_HEADER:
                byte[] b = new byte[1];
                b[0] = in.readByte();
                String header = TransformUtils.byteToHexString(b);
                if(Strings.equalsIgnoreCase(header,"AA")) {
                    checkpoint(ObjectDecoderState.READ_DEVID);
                }
                return;
            case READ_DEVID:
                ByteBuf byteBuf = in.readBytes(12);
                String devId = byteBuf.toString(CharsetUtil.UTF_8);
                checkpoint(ObjectDecoderState.READ_VERSION);
                return;
            case READ_VERSION:
                ByteBuf V = in.readBytes(1);
                checkpoint(ObjectDecoderState.READ_DEVTYPE);
                return;
            case READ_DEVTYPE:
                ByteBuf d = in.readBytes(2);
                checkpoint(ObjectDecoderState.READ_NUM);
                return;
            case READ_NUM:
                ByteBuf c = in.readBytes(1);
                checkpoint(ObjectDecoderState.READ_COMMAND);
                return;
            case READ_COMMAND:
                ByteBuf ca = in.readBytes(1);
                checkpoint(ObjectDecoderState.READ_LENGTH);
                return;
            case READ_LENGTH:
                ByteBuf len = in.readBytes(2);
                length = 100;
                checkpoint(ObjectDecoderState.READ_CONTENT);
                return;
            case READ_CONTENT:
                ByteBuf content = in.readBytes(length);
                checkpoint(ObjectDecoderState.READ_CHECK);
                return;
            case READ_CHECK:
                ByteBuf check = in.readBytes(1);
                checkpoint(ObjectDecoderState.READ_END);
                return;
            case READ_END:
                ByteBuf end = in.readBytes(1);
                checkpoint(ObjectDecoderState.READ_HEADER);
                return;

            default:
                throw new Error("no ... ");

        }

    }
}
