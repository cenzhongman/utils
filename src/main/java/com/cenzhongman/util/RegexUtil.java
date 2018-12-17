package com.cenzhongman.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式工具类
 *
 * @author 34566
 */
public class RegexUtil {

    /**
     * 从文本中抽取匹配中的内容
     *
     * @param str   需要匹配的字符串
     * @param regex 用于匹配的正则表达式
     * @return 匹配到的列表，未匹配到返回空数组
     */
    public static List<String> extract(String str, String regex) {
        List<String> matchStrings = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex  );
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            matchStrings.add(matcher.group());
        }
        return matchStrings;
    }

    /**
     * 获取匹配到的第一个
     *
     * @param str 需要匹配的字符串
     * @param regex 正则表达式
     * @return 匹配的第一结果，未匹配到返回null
     */
    public static String extractFirst(String str, String regex) {
        String matchString = null;
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        while (matcher.find()) {
            matchString = matcher.group();
        }
        return matchString;
    }

    /**
     * 获取匹配到的第i个
     *
     * @param str 需要匹配的字符串
     * @param regex 正则表达式
     * @param index 正则表达式的参数的index
     * @return 匹配的第一结果，未匹配到返回null
     */
    public static String extract(String str, String regex, int index) {
        String matchString = "";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);

        while (matcher.find()) {
            matchString = matcher.group(index);
        }
        return matchString;
    }

    /**
     * 规则检查，是否符合XX规则
     *
     * @param str 需要匹配的字符串
     * @param regex 正则表达式
     * @return 是否符合规则
     */
    public static boolean isMatch(String str, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);
        return matcher.find();
    }
}
