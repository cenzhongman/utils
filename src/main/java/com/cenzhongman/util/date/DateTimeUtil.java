package com.cenzhongman.util.date;

import com.cenzhongman.util.RegexUtil;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author 岑忠满
 * @date 2018/10/22 15:36
 */
public class DateTimeUtil {
    private final static String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private final static String LINK = "[T_]";
    private static final String DATE_REGEX_S = "[\\D]*[\\d]{10}[\\D]*";
    private static final String DATE_REGEX_MS = "[^\\d]*[\\d]{13}[^\\d]*";

    private static final Map<Integer, Character> DEFAULT_CHAR_MAP = new HashMap<>();

    static {
        DEFAULT_CHAR_MAP.put(1, 'y');
        DEFAULT_CHAR_MAP.put(2, 'M');
        DEFAULT_CHAR_MAP.put(3, 'd');
        DEFAULT_CHAR_MAP.put(4, 'H');
        DEFAULT_CHAR_MAP.put(5, 'm');
        DEFAULT_CHAR_MAP.put(6, 's');
    }

    /**
     * 使用yyyy-MM-dd HH:mm:ss格式化时间
     *
     * @param date   时间
     * @param format 时间格式
     * @return yyyy-MM-dd HH:mm:ss标准时间
     */
    public static String format(Date date, String format) {
        SimpleDateFormat simpleDateFormat = format.isEmpty() ? new SimpleDateFormat(DEFAULT_FORMAT) : new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    /**
     * 使用yyyy-MM-dd HH:mm:ss格式化时间
     *
     * @param date 时间
     * @return yyyy-MM-dd HH:mm:ss标准时间
     */
    public static String format(Date date) {
        return format(date, DEFAULT_FORMAT);
    }

    /**
     * 将String转成Date
     *
     * @param dateStr 时间字符串
     * @param format  格式
     * @return Date
     * @throws ParseException 解析异常
     */
    public static Date toDate(String dateStr, String format) throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat(format);
        return sf.parse(dateStr);
    }

    /**
     * 将String转成Date
     *
     * @param dateStr 时间字符串
     * @param format  格式
     * @param locale  枚举类型
     * @return Date
     * @throws ParseException 解析异常
     */
    public static Date toDate(String dateStr, String format, Locale locale) throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat(format, locale);
        return sf.parse(dateStr);
    }

    /**
     * 注意：不支持同时有多个时间的格式
     *
     * @param dateStr 含有时间的String
     * @return 标准时间格式
     * @throws ParseException 时间格式不支持
     */
    public static Date toDate(String dateStr) throws ParseException {
        return (Date) toDateWithSource(dateStr).get("date");
    }


    /**
     * 将String转成Date，同时携带代码中的源String
     *
     * @param dateStr 时间字符串，支持10位、13位时间戳、yyyy/MM/dd hh/mm/ss这种顺序的格式(yyyy/MM/dd这种都是支持的)
     * @return 转化后的日期
     * @throws ParseException 解析异常
     */
    public static Map<String, Object> toDateWithSource(String dateStr) throws ParseException {
        if ("".equals(dateStr)){
            throw new ParseException("参数为空",0);
        }

        Map<String, Object> rstMap = new HashMap<>();
        Date rstDate = null;
        String rstSource = null;

        // 去掉除了月份和数字以外的末尾信息
        if (!RegexUtil.isMatch(dateStr, "(Jan|January|Feb|February|Mar|March|Apr|April|May|Jun|June|Jul|July|Aug|August|Sept|September|Oct|October|Nov|November|Dec|December)")
                || RegexUtil.extract(dateStr, "\\d").size() > 4) {
            dateStr = dateStr.replaceAll("([^年|^月|^日|^时|^分|^秒|^0-9])*$", "");
        }

        // 支持13位时间戳
        if (RegexUtil.isMatch(dateStr, DATE_REGEX_MS)) {
            rstSource = RegexUtil.extractFirst(dateStr, "[\\d]{13}");
            rstDate = new Date(Long.parseLong(rstSource));
            rstMap.put("date", rstDate);
            rstMap.put("source", rstSource);
            return rstMap;
        }

        // 支持10位时间戳
        if (RegexUtil.isMatch(dateStr, DATE_REGEX_S)) {
            rstSource = RegexUtil.extractFirst(dateStr, "\\d{10}");
            rstDate = new Date(Long.parseLong(rstSource) * 1000);
            rstMap.put("date", rstDate);
            rstMap.put("source", rstSource);
            return rstMap;
        }

        // 支持dd/MMM/yyyy hh:mm:ss
        // 20 Apr 2015
        String re1 = "(\\d{1,2})[^a-zA-Z0-9]+([a-zA-Z]{3,})[^a-zA-Z0-9]+(\\d{2,4})\\D*(\\d*):*(\\d*):*(\\d*)";
        if (RegexUtil.isMatch(dateStr, re1)) {
            Map<Integer, Character> map = new HashMap<>();
            map.put(1, 'd');
            map.put(2, 'M');
            map.put(3, 'y');
            map.put(4, 'h');
            map.put(5, 'm');
            map.put(6, 's');
            rstSource = RegexUtil.extractFirst(dateStr, re1);
            rstDate = toDate(rstSource, re1, map, Locale.ENGLISH);
            rstMap.put("date", rstDate);
            rstMap.put("source", rstSource);
            return rstMap;
        }

        // 支持MMM/dd/yyyy hh:mm:ss
        // October 25, 2018
        String re3 = "([a-zA-Z]{3,})[^a-zA-Z0-9]+(\\d{1,2})[^a-zA-Z0-9]+(\\d{2,4})\\D*(\\d*):*(\\d*):*(\\d*)";
        if (RegexUtil.isMatch(dateStr, re3)) {
            Map<Integer, Character> map = new HashMap<>();
            map.put(1, 'M');
            map.put(2, 'd');
            map.put(3, 'y');
            map.put(4, 'h');
            map.put(5, 'm');
            map.put(6, 's');
            rstSource = RegexUtil.extractFirst(dateStr, re3);
            rstDate = toDate(rstSource, re3, map, Locale.US);
            rstMap.put("date", rstDate);
            rstMap.put("source", rstSource);
            return rstMap;
        }

        // 支持dd/MM/yyyy hh:mm:ss
        // 20 06 2018
        String re2 = "(\\d{1,2})[^a-zA-Z0-9]+(\\d{1,2})[^a-zA-Z0-9]+(\\d{4})\\D*(\\d*):*(\\d*):*(\\d*)";
        if (RegexUtil.isMatch(dateStr, re2)) {
            Map<Integer, Character> map = new HashMap<>();
            map.put(1, 'd');
            map.put(2, 'M');
            map.put(3, 'y');
            map.put(4, 'h');
            map.put(5, 'm');
            map.put(6, 's');
            rstSource = RegexUtil.extractFirst(dateStr, re2);
            rstDate = toDate(rstSource, re2, map);
            rstMap.put("date", rstDate);
            rstMap.put("source", rstSource);
            return rstMap;
        }

        // yyyy/MM/dd/ hh:mm:ss这种顺序的格式
        // 2018年10月25日
        String re = "(\\d{4})\\D+(\\d{1,2})\\D+(\\d{1,2})\\D*(\\d*)\\D*(\\d*)\\D*(\\d*)";
        if (RegexUtil.isMatch(dateStr, re)) {
            rstSource = RegexUtil.extractFirst(dateStr, re);
            rstDate = toDate(rstSource, re, DEFAULT_CHAR_MAP);
            rstMap.put("date", rstDate);
            rstMap.put("source", rstSource);
            return rstMap;
        }

        // Jan 1
        String re5 = "(Jan|January|Feb|February|Mar|March|Apr|April|May|Jun|June|Jul|July|Aug|August|Sept|September|Oct|October|Nov|November|Dec|December)\\D+(\\d{1,2})";
        if (RegexUtil.isMatch(dateStr, re5)) {
            String dateStr1 = RegexUtil.extractFirst(dateStr, re5);
            rstSource = dateStr1;
            dateStr1 = dateStr1 + " " + 2018;
            rstDate = toDate(dateStr1);
            rstMap.put("date", rstDate);
            rstMap.put("source", rstSource);
            return rstMap;
        }

        // 12月12日
        String re6 = "\\d{1,2}月\\d{1,2}日";
        if (RegexUtil.isMatch(dateStr, re6)) {
            String dateStr1 = RegexUtil.extractFirst(dateStr, re6);
            rstSource = dateStr1;
            dateStr1 = "2018年" + dateStr1;
            rstDate = toDate(dateStr1);
            rstMap.put("date", rstDate);
            rstMap.put("source", rstSource);
            return rstMap;
        }

        String re7 = "\\d{4}-\\d{2}";
        if (RegexUtil.isMatch(dateStr,re7)){
            String dateStr1 = RegexUtil.extractFirst(dateStr, re7);
            rstSource = dateStr1;
            dateStr1 = dateStr1 + "-01";
            rstDate = toDate(dateStr1);
            rstMap.put("date", rstDate);
            rstMap.put("source", rstSource);
            return rstMap;
        }

        String re8 = "[1,2]\\d{3}(?!\\d)";
        if (RegexUtil.isMatch(dateStr,re8)){
            String dateStr1 = RegexUtil.extractFirst(dateStr, re8);
            rstSource = dateStr1;
            dateStr1 = dateStr1 + "-01-01";
            rstDate = toDate(dateStr1);
            rstMap.put("date", rstDate);
            rstMap.put("source", rstSource);
            return rstMap;
        }

        throw new ParseException("Can not find Date String!" + dateStr, 0);
    }


    private static Date toDate(String dateStr, String regex, Map<Integer, Character> characterMap, Locale locale) throws ParseException {
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(dateStr);
        StringBuilder sb = new StringBuilder(dateStr);
        if (m.find(0)) {
            int count = m.groupCount();
            for (int i = 1; i <= count; i++) {
                for (Map.Entry<Integer, Character> entry : characterMap.entrySet()) {
                    if (entry.getKey() == i) {
                        sb.replace(m.start(i), m.end(i), replaceEachChar(m.group(i), entry.getValue()));
                    }
                }
            }
        }
        String format = sb.toString().replaceAll(LINK, " ");
        return toDate(dateStr.replaceAll(LINK, " "), format.replaceAll(LINK, " "), locale);
    }


    private static Date toDate(String dateStr, String regex, Map<Integer, Character> characterMap) throws ParseException {
        return toDate(dateStr, regex, characterMap, Locale.CHINA);
    }



    private static String replaceEachChar(String s, Character c) {
        StringBuilder sb = new StringBuilder();
        for (Character c1 : s.toCharArray()) {
            if (c1 != ' ') {
                sb.append(String.valueOf(c));
            }
        }
        return sb.toString();
    }
}