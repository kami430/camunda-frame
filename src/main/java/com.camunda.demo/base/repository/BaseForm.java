package com.camunda.demo.base.repository;

import org.springframework.beans.BeanUtils;

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
    public T buildEntity();

    default void copyProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target);
    }
}
