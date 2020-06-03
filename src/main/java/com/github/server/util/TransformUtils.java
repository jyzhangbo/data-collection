package com.github.server.util;

/**
 * 转换工具.
 * @Author:zhangbo
 * @Date:2019/4/14 15:09
 */
public class TransformUtils {

    /**
     * 字节数组转16进制字符串.
     * @param b
     * @return
     */
    public static String byteToHexString(byte[] b){
        String result = "";
        for (int i=0; i < b.length; i++) {
            result += Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
        }
        return result;
    }

    /**
     * 16进制字符串转字节数组.
     * @param hexString
     * @return
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    /**
     * char转byte.
     * @param c
     * @return
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * int转字节数组.
     * @param res
     * @return
     */
    public static byte[] intTobyte4(int res) {
        byte[] targets = new byte[4];

        // 最低位
        targets[3] = (byte) (res & 0xff);
        // 次低位
        targets[2] = (byte) ((res >> 8) & 0xff);
        // 次高位
        targets[1] = (byte) ((res >> 16) & 0xff);
        // 最高位,无符号右移。
        targets[0] = (byte) (res >>> 24);
        return targets;
    }

    /**
     * short转字节数组.
     * @param n
     * @return
     */
    public static byte[] shortToByte(short n) {
        byte[] b = new byte[2];
        b[1] = (byte) (n & 0xff);
        b[0] = (byte) ((n >> 8) & 0xff);
        return b;
    }

}
