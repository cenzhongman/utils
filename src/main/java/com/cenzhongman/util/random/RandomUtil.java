package com.cenzhongman.util.random;

import java.util.Random;

/**
 * @author 岑忠满
 * @date 2018/12/22 14:01
 *
 * todo 还没写完
 */
public class RandomUtil {
    private static Random random = new Random();

    public String randomStr(int size) {
        return "";
    }

    public static int randomInt(int size) {
        return random.nextInt(size);
    }

    public static int randomInt(int from, int to) {
        return from + random.nextInt(to);
    }
}
