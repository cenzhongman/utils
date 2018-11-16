package com.cenzhongman.util.http;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author 岑忠满
 * @date 2018/11/6 11:19
 */
public class UrlUtil {

    /**
     * URL编码
     *
     * @param s 需要编码的字符串
     * @return 编码结果
     */
    public static String encode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * URL解码
     *
     * @param s 需要解码的字符串
     * @return 解码结果
     */
    public static String decode(String s) {
        try {
            return URLDecoder.decode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
