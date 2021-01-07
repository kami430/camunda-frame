package com.flow.base.utils;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Spring加载的时候加载的ApplicationContextAware，方便在pojo类里面获得context或者bean。
 * Created by Administrator on 2016/11/07.
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {

    private static ApplicationContext appContext; // Spring应用上下文环境

    public static ApplicationContext getApplicationContext() {
        return appContext;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.appContext = applicationContext;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String name) throws BeansException {
        return (T) appContext.getBean(name);
    }

    public static <T> T getBean(Class<T> requiredType) throws BeansException {
        return appContext.getBean(requiredType);
    }

    public static <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return appContext.getBean(name, requiredType);
    }

    public static boolean containsBean(String name) {
        return appContext.containsBean(name);
    }

    public static boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
        return appContext.isSingleton(name);
    }

    public static Class<?> getType(String name) throws NoSuchBeanDefinitionException {
        return appContext.getType(name);
    }

    public static String[] getAliases(String name) throws NoSuchBeanDefinitionException {
        return appContext.getAliases(name);
    }
}
