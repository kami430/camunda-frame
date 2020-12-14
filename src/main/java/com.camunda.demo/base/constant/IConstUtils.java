package com.camunda.demo.base.constant;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class IConstUtils {

    /**
     * 验证对象数据的合法性
     *
     * @param obj 数据对象
     * @return
     */
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

    /**
     * 验证数据的合法性
     *
     * @param tClass 参考类
     * @param value  值
     * @param <T>    继承了Annotation的类型
     * @return
     */
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

    /**
     * 获取参照类的所有常量
     *
     * @param tClass 参考类
     * @param <T>    继承了Annotation的类型
     * @return
     */
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

    /**
     * 获取对象某个常量字段的说明(非常量字段和非法值返回null)
     *
     * @param obj       对象
     * @param fieldName 字段名
     * @return
     */
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
