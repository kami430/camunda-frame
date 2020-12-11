package com.camunda.demo;

import com.camunda.demo.business.form.UserForm;
import com.camunda.demo.dataInterface.constant.EntityStatus;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.cglib.beans.BeanMap;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Apptest {

    public static void main(String[] args) {
        UserForm form = new UserForm();
        form.setAccount("hello");
        form.setStatus(EntityStatus.ACTIVE);
        Map<String, Object> model = new HashMap<>();
        BeanMap beanMap = BeanMap.create(form);
        for (Object key : beanMap.keySet()) {
            model.put(key.toString(), beanMap.get(key));
        }
        for (String key : model.keySet()) {
            System.out.println(model.get(key));
        }
    }

    public static <T> void instance(Object target, Class<T> clazz, Consumer<T> consumer) {
        if (clazz.isAssignableFrom(target.getClass())) {
            consumer.accept((T) target);
        }
    }

    public static class People {

    }

    public static class Student extends People {

    }
}
