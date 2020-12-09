package com.camunda.demo;

import com.camunda.demo.business.form.UserForm;
import com.camunda.demo.dataInterface.constant.EntityStatus;
import com.camunda.demo.dataInterface.entity.authorization.LoginUser;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Apptest {

    public static void main(String[] args) {
        UserForm form = new UserForm();
        form.setAccount("hello");
        form.setStatus(EntityStatus.ACTIVE);
        Map<String, Object> model = new HashMap<>();
        BeanUtils.copyProperties (form, model);
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
