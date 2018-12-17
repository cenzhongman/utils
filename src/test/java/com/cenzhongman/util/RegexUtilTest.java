package com.cenzhongman.util;

import org.junit.jupiter.api.Test;

/**
 * @author 岑忠满
 * @date 2018/10/24 10:02
 */
class RegexUtilTest {

    @Test
    void extract() {
        System.out.println(RegexUtil.extract("新华社消息，小米的CEO雷军，这些年", "[^，|。]+[CEO|董事长][^，|^。]+"));
        System.out.println(RegexUtil.extract("新华社消息，小米的CEO雷军，这些年", "a"));
    }

    @Test
    void extractFirst() {
//        System.out.println(RegexUtil.extract("新华社消息，小米的CEO雷军，这些年", "[^，|。]+[CEO|董事长][^，|^。]+"));
//        System.out.println(RegexUtil.extract("新华社消息，小米的CEO雷军，这些年", "a"));
        System.out.println(RegexUtil.extract("新华社消息，小米的CEO雷军，这些年", "(小米的)(CEO)",2));
    }

    @Test
    void isMatch() {
        System.out.println(RegexUtil.isMatch("新华社消息，小米的CEO雷军，这些年", "[^，|。]+[CEO|董事长][^，|^。]+"));
        System.out.println(RegexUtil.isMatch("新华社消息，小米的CEO雷军，这些年", "a"));
    }
}