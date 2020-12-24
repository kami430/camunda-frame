package com.camunda.demo;

import com.camunda.demo.base.repository.Pager;
import com.camunda.demo.base.response.BusinessException;
import com.camunda.demo.base.response.ResponseCode;
import com.camunda.demo.business.form.UserForm;
import com.camunda.demo.dataInterface.constant.IEntityStatus;
import com.camunda.demo.base.constant.IConst;
import com.camunda.demo.base.constant.IConstUtils;
import com.camunda.demo.base.constant.IConstInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Apptest {

    public static void main(String[] args) {
        List<String> a = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            a.add(i + "");
        }
        List<List<String>> page = pageList(a, 10);
        List<String> sheetNames = sheetName(a, 10);
        for (int i = 0; i < page.size(); i++) {
            System.out.print("[");
            for (int j = 0; j < page.get(i).size(); j++) {
                System.out.print(page.get(i).get(j) + ",");
            }
            System.out.println("]");
        }
        for (int i = 0; i < sheetNames.size(); i++) {
            System.out.println(sheetNames.get(i));
        }
    }

    public static <T> List<List<T>> pageList(List<T> data, int size) {
        Integer limit = data.size() % size == 0 ? data.size() / size : data.size() + 1;
        if (limit > 500) throw new BusinessException(ResponseCode.SERVER_ERROR, "sheet数量不能超过500");
        return Stream.iterate(0, n -> n + 1).limit(limit).parallel()
                .map(a -> data.stream().skip(a * size).limit(size).parallel().collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    public static <T> List<String> sheetName(List<T> data, int size) {
        Integer limit = data.size() % size == 0 ? data.size() / size : data.size() + 1;
        if (limit > 500) throw new BusinessException(ResponseCode.SERVER_ERROR, "sheet数量不能超过500");
        return Stream.iterate(0, n -> n + 1).limit(limit).parallel().map(a -> "sheet" + a).collect(Collectors.toList());
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

    public static String orderString(Pageable pageable) {
        if (pageable == null || pageable.getSort() == null || pageable.getSort().get().count() == 0L) return "";
        StringBuilder builder = new StringBuilder(" ORDER BY ");
        List<String> orders = new ArrayList<>();
        pageable.getSort().get().forEach(order -> {
            if (order.getDirection().equals(Sort.Direction.DESC)) {
                orders.add(order.getProperty() + " DESC");
            } else {
                orders.add(order.getProperty());
            }
        });
        builder.append(String.join(",", orders));
        return builder.toString();
    }
}
