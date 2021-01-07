package com.flow.base.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:ningyaobai@gzkit.com.cn">bernix</a>
 *         十二月 14, 2016
 * @version 1.0
 */
public class ViewUtils {

    public static Map<String, Object> successJsonMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("success", true);
        return map;
    }

    public static String successJson() {
        return "{\"success\":true}";
    }
}
