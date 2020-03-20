package com.zhu.rimxia.biz.jwt;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;


import java.security.Key;

public class Hex {

    private static  final char[] DIGITS={
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
    };

    /**
     * 采用16进制编码方式，对字节数组编码
     * @param bytes
     * @return
     */
    public static String encode(byte[] bytes){
        int length = bytes.length;
        char[] out = new char[length<<1];
        for(int i=0,j=0;i<length;i++){
            out[j++] = DIGITS[ bytes[i]&0xf0 >>> 4 ];
            out[j++] = DIGITS[ bytes[i]&0x0f ];
        }
       return  new String(out);
    }


    /**
     * 解码 16进制编码的字符串
     * @param src
     * @return
     */
    public static byte[] decode(String src){
        int length = src.length();
        /**
         * 长度不为偶数，非16进制编码
         */
        if((length&0x01) != 0){
            throw new IllegalArgumentException("该字符串长度非偶数，非16进制编码");
        }

        byte[] out = new byte[length>>1];
        for(int i=0; i<length; i+=2){
            char high = src.charAt(i);
            char low = src.charAt(i+1);
            out[i>>1] = (byte)((Character.digit(high,16) << 4) + Character.digit(low,16));
        }
        return out;
    }

    public static void main(String[] args) {

        Key keys = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        String en = encode(keys.getEncoded());
       // System.out.println(new String(keys.getEncoded()));
        System.out.println(encode(decode(en)));
        System.out.println(en);
    }
}
