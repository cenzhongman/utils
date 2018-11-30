package com.cenzhongman.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author 岑忠满
 * @date 2018/11/30 10:33
 */
public class CollectionUtil {
    public static Set list2Set(List list){
        Set set = new HashSet(list);
        return set;
    }
}
