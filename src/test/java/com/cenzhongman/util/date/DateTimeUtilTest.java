package com.cenzhongman.util.date;

import com.cenzhongman.util.date.DateTimeUtil;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.util.Locale;

/**
 * @author 岑忠满
 * @date 2018/10/22 16:12
 */
class DateTimeUtilTest {

    @Test
    void toDate() throws ParseException {
        System.out.println(DateTimeUtil.toDate("a2018年10月22日T16点13分18a"));
        System.out.println(DateTimeUtil.toDate("a2018年10月22日 16点13分a"));
        System.out.println(DateTimeUtil.toDate("a1540197865a"));
        System.out.println(DateTimeUtil.toDate("a1540197865000a"));
        System.out.println(DateTimeUtil.toDate("a2018-10-22a"));
        System.out.println(DateTimeUtil.toDate("a2018-10-22 16:13a"));
        System.out.println(DateTimeUtil.toDate("a2018-10-22 16:13:10a"));
        System.out.println(DateTimeUtil.toDate("a2018-10-22 16:13:10a"));
        System.out.println(DateTimeUtil.toDate("a05/09/2018 16:29a"));
        System.out.println(DateTimeUtil.toDate("a20 October 2018 19：12:12a"));
        System.out.println(DateTimeUtil.toDate("a20 Apr 2018 19:12:12a"));
        System.out.println(DateTimeUtil.toDate("a2018年09.13日 19时28：23s"));
        System.out.println(DateTimeUtil.toDate("a October 25, 2018a"));
        System.out.println(DateTimeUtil.toDate("a Apr 25, 2018a"));

        // 只支持这三种没有年份的
        System.out.println(DateTimeUtil.toDate("20 Apr,haha"));
        System.out.println(DateTimeUtil.toDate("October 25,"));
        System.out.println(DateTimeUtil.toDate("10月22日,"));

    }

    @Test
    void format() throws ParseException {
        System.out.println(DateTimeUtil.format(DateTimeUtil.toDate("2018年10月22日T16点13分")));
    }
}