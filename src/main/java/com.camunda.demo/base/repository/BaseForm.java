package com.camunda.demo.base.repository;

import com.camunda.demo.dataInterface.constant.EntityStatus;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 抽象表单
 *
 * @param <T>
 */
public interface BaseForm<T> {

    /**
     * 获取实例
     *
     * @return 返回实体类
     */
    default T buildEntity(){
        try {
            T t = (T) getGeneric().newInstance();
            copyProperties(this, t);
            return t;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    default void copyProperties(Object source, Object target) {

        BeanUtils.copyProperties(source, target);
    }

    //得到泛型类T
    default Class getGeneric() {
        Type type = getClass().getGenericInterfaces()[0];
        if (type instanceof ParameterizedType) {
            Type[] ptype = ((ParameterizedType) type).getActualTypeArguments();
            return (Class) ptype[0];
        } else {
            return Object.class;
        }
    }
}
