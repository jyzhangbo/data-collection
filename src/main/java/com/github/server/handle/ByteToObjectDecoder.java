package com.github.server.handle;

import com.github.server.model.MessageHexBody;
import com.github.server.model.ObjectDecoderState;
import com.github.server.util.TransformUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;
import org.nutz.lang.Strings;

import java.util.List;

/**
 * @Author:zhangbo
 * @Date:2020/6/3 12:04
 */
public class ByteToObjectDecoder extends ReplayingDecoder<ObjectDecoderState> {

    private int length;

    private MessageHexBody body = new MessageHexBody();

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
                byte[] devId = new byte[12];
                byteBuf.readBytes(devId);
                body.devId = devId;
                checkpoint(ObjectDecoderState.READ_VERSION);
                return;
            case READ_VERSION:
                ByteBuf version = in.readBytes(1);
                checkpoint(ObjectDecoderState.READ_DEVTYPE);
                return;
            case READ_DEVTYPE:
                ByteBuf devType = in.readBytes(2);
                checkpoint(ObjectDecoderState.READ_NUM);
                return;
            case READ_NUM:
                ByteBuf num = in.readBytes(1);
                checkpoint(ObjectDecoderState.READ_COMMAND);
                return;
            case READ_COMMAND:
                ByteBuf command = in.readBytes(1);
                byte[] commandByte = new byte[1];
                command.readBytes(commandByte);
                body.command = commandByte;
                checkpoint(ObjectDecoderState.READ_LENGTH);
                return;
            case READ_LENGTH:
                short i = in.readShort();
                length = Short.toUnsignedInt(i);
                checkpoint(ObjectDecoderState.READ_CONTENT);
                return;
            case READ_CONTENT:
                ByteBuf content = in.readBytes(length);
                byte[] contentByte = new byte[length];
                content.readBytes(contentByte);
                body.content = contentByte;
                checkpoint(ObjectDecoderState.READ_CHECK);
                return;
            case READ_CHECK:
                ByteBuf check = in.readBytes(1);
                checkpoint(ObjectDecoderState.READ_END);
                return;
            case READ_END:
                ByteBuf end = in.readBytes(1);
                checkpoint(ObjectDecoderState.READ_HEADER);
                out.add(body);
                return;
            default:
                throw new Error("no ... ");

        }

    }

    public static void main(String[] args) {
        short i = 1;
        int length = Short.toUnsignedInt(i);
        System.out.println(length);
    }
}
