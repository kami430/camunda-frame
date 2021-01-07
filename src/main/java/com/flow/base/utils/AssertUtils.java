package com.flow.base.utils;

import com.flow.base.response.BusinessException;

/**
 * @author <a href="mailto:ningyaobai@gzkit.com.cn">bernix</a>
 *         星期一, 一月 09, 2017
 * @version 1.0
 */
public class AssertUtils {

    /**
     * 如果对象为null, 则抛出异常
     * @param object
     * @throws BusinessException
     */
    public static void notNull(Object object) throws BusinessException {
        notNull(object, "不能处理空对象");
    }

    /**
     * 如果对象为null, 则抛出异常
     * @param object 要判断的对象
     * @param errmsg 异常描述
     * @throws BusinessException
     */
    public static void notNull(Object object, String errmsg) throws BusinessException {
        if (object == null) {
            throw new BusinessException(errmsg);
        }
    }
}
