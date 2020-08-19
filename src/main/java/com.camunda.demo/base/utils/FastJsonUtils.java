package com.camunda.demo.base.utils;

import com.alibaba.fastjson.serializer.PropertyFilter;
import org.hibernate.collection.spi.PersistentCollection;
import org.hibernate.proxy.HibernateProxy;

public class FastJsonUtils {

    /**
     * fastjson 处理懒加载(忽略懒加载字段)
     */
    public static class JSONFilter implements PropertyFilter {
        @Override
        public boolean apply(Object object, String name, Object value) {
            if (value instanceof HibernateProxy) {//hibernate代理对象
                return false;
            } else if (value instanceof PersistentCollection) {//实体关联集合一对多等
                PersistentCollection collection = (PersistentCollection) value;
                if (!collection.wasInitialized()) {
                    return false;
                }
                Object val = collection.getValue();
                if (val == null) {
                    return false;
                }
            }
            return true;
        }
    }
}
