package com.flow.base.utils;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class BaseUtils {

    // 交集
    public static <T> List<T> intersection(List<T> source, List<T> target) {
        return source.stream().filter(obj -> target.contains(obj)).collect(Collectors.toList());
    }

    // 新增差集
    public static <T> List<T> exceptionAdd(List<T> source, List<T> target) {
        return target.stream().filter(obj -> !source.contains(obj)).collect(Collectors.toList());
    }

    // 原有差集
    public static <T> List<T> exceptionDel(List<T> source, List<T> target) {
        return source.stream().filter(obj -> !target.contains(obj)).collect(Collectors.toList());
    }

    // 并集
    public static <T> List<T> union(List<T> source, List<T> target) {
        List<T> sourceList = source.parallelStream().collect(Collectors.toList());
        List<T> targetList = target.parallelStream().collect(Collectors.toList());
        sourceList.addAll(targetList);
        return sourceList;
    }

    // 去重并集
    public static <T> List<T> noRepeatUnion(List<T> source, List<T> target) {
        return union(source,target).stream().distinct().collect(Collectors.toList());
    }

    // instance of 方法版
    public static <T> void instance(Object target, Class<T> clazz, Consumer<T> consumer) {
        if (clazz.isAssignableFrom(target.getClass())) {
            consumer.accept((T) target);
        }
    }
}
