package crawl.util;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by BFD_303 on 2017/2/6.
 */
public class EmptyUtil {
    //判断集合是否为空
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    //判断Map是否为空
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    //判断数组是否为空
    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    //判断List是否为空
    public static boolean isEmpty(List<Object> list) {
        return list == null || list.size() == 0;
    }

    //判断字符串是否为空，包括null和""，其中"  "无论有多少空格都返回true，表示为空
    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0;
    }
}
