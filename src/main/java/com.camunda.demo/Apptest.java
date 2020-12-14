package com.camunda.demo;

import com.camunda.demo.business.form.UserForm;
import com.camunda.demo.dataInterface.constant.IEntityStatus;
import com.camunda.demo.base.constant.IConst;
import com.camunda.demo.base.constant.IConstUtils;
import com.camunda.demo.base.constant.IConstInfo;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.function.Consumer;

public class Apptest {

    public static void main(String[] args) {
        UserForm form = new UserForm();
        form.setStatus(IEntityStatus.ACTIVE);
        System.out.println(IConstUtils.validate(form));
        Map<String, Object> constMap = IConstUtils.getConstMap(IEntityStatus.class);
        System.out.println(constMap);
        String remark = IConstUtils.getFieldRemark(form, "status");
        System.out.println(remark);
        System.out.println(IConstUtils.valudate(IEntityStatus.class, null));
        System.out.println(IConstUtils.valudate(IEntityStatus.class, 5));
        System.out.println(IConstUtils.valudate(IEntityStatus.class, IEntityStatus.ACTIVE));
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
}
