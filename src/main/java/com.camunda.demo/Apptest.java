package com.camunda.demo;

import com.camunda.demo.base.repository.Pager;
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

public class Apptest {

    public static void main(String[] args) {
        Pager pager = Pager.of(1, 10,null);
        System.out.println(orderString(pager.pageable()));
    }

    public static void fo(Object obj) throws IllegalAccessException {
        Class c = obj.getClass();
        Field[] fs = c.getDeclaredFields();
        for (Field field : fs) {
            IConst ca = field.getDeclaredAnnotation(IConst.class);
            if (ca != null) {
                field.setAccessible(true);
                Integer a = (Integer) field.get(obj);
                Class entity = ca.value();
                Field[] fields = entity.getDeclaredFields();
                for (Field fd : fields) {
                    fd.setAccessible(true);
                    IConstInfo info = fd.getDeclaredAnnotation(IConstInfo.class);
                    Integer code = info.code();
                    String remark = info.remark();
                    System.out.println(code);
                    System.out.println(remark);
                }
            }
            System.out.println("haha");
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
