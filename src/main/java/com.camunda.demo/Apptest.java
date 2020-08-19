package com.camunda.demo;

import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.function.Consumer;

public class Apptest {

    public static void main(String[] args) throws IOException {
//        Student a = new Student();
//        instance(a, People.class, tar -> System.out.println(tar));
        LocalDate now = LocalDate.now();
        now.minusMonths(1);
        System.out.println(now.getMonthValue());
        System.out.println(now);
    }

    public static <T> void instance(Object target, Class<T> clazz, Consumer<T> consumer) {
        if (clazz.isAssignableFrom(target.getClass())) {
            consumer.accept((T) target);
        }
    }

    public static class People{

    }

    public static class Student extends People{

    }
}
