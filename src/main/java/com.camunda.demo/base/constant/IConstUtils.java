package com.camunda.demo.base.constant;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class IConstUtils {

    public static boolean validate(Object obj) {
        try {
            Field[] fieldsConsts = obj.getClass().getDeclaredFields();
            for (Field fieldsConst : fieldsConsts) {
                IConst iConstant = fieldsConst.getDeclaredAnnotation(IConst.class);
                if (iConstant != null) {
                    Integer fieldValue = getFieldCode(obj, fieldsConst);
                    if (fieldValue == null) return true;
                    Class constEntity = iConstant.value();
                    Field[] fields = constEntity.getDeclaredFields();
                    return Arrays.asList(fields).stream().map(field -> {
                        field.setAccessible(true);
                        IConstInfo info = field.getDeclaredAnnotation(IConstInfo.class);
                        if (info != null) return info.code();
                        else return null;
                    }).collect(Collectors.toList()).contains(fieldValue);
                }
            }
            return true;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static <T extends Annotation> boolean valudate(Class<T> tClass, Integer value) {
        if (value == null) return true;
        Field[] fields = tClass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            IConstInfo info = field.getDeclaredAnnotation(IConstInfo.class);
            if (info != null && info.code() == value) return true;
        }
        return false;
    }

    public static <T extends Annotation> Map<String, Object> getConstMap(Class<T> tClass) {
        Map<String, Object> constMap = new HashMap<>();
        Field[] fields = tClass.getDeclaredFields();
        Arrays.asList(fields).forEach(field -> {
            field.setAccessible(true);
            IConstInfo info = field.getDeclaredAnnotation(IConstInfo.class);
            if (info != null) constMap.put(String.valueOf(info.code()), info.remark());
        });
        return constMap;
    }

    public static String getFieldRemark(Object obj, String fieldName) {
        try {
            Field[] fieldsConsts = obj.getClass().getDeclaredFields();
            for (Field fieldsConst : fieldsConsts) {
                if (fieldName.equals(fieldsConst.getName())) {
                    IConst iConstant = fieldsConst.getDeclaredAnnotation(IConst.class);
                    if (iConstant != null) {
                        Integer fieldValue = getFieldCode(obj, fieldsConst);
                        if (fieldValue == null) return null;
                        Class constEntity = iConstant.value();
                        Field[] fields = constEntity.getDeclaredFields();
                        for (Field field : fields) {
                            field.setAccessible(true);
                            IConstInfo info = field.getDeclaredAnnotation(IConstInfo.class);
                            if (info != null && info.code() == fieldValue) return info.remark();
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static Integer getFieldCode(Object obj, Field fieldsConst) throws IllegalAccessException {
        fieldsConst.setAccessible(true);
        Object fieldValueObj = fieldsConst.get(obj);
        if (fieldValueObj == null) return null;
        return (Integer) fieldValueObj;
    }
}
