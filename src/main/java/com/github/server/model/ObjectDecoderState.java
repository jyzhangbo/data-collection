package com.github.server.model;

/**
 * @Author:zhangbo
 * @Date:2020/6/3 14:22
 */
public enum  ObjectDecoderState {

    READ_HEADER,

    READ_DEVID,

    READ_VERSION,

    READ_DEVTYPE,

    READ_NUM,

    READ_COMMAND,

    READ_LENGTH,

    READ_CONTENT,

    READ_CHECK,

    READ_END;

}
