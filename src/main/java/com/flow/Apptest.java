package com.flow;

import com.flow.base.constant.IConstUtils;
import com.flow.base.response.BusinessException;
import com.flow.dataInterface.constant.ILeaveReason;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public class Apptest {

    public static void main(String[] args){
        Map<String,Object> map = IConstUtils.getConstMap(ILeaveReason.class);
        for (String key:map.keySet()){
            System.out.println(key+":"+map.get(key));
        }
    }

    private static List<String> getab() {
        throw new NullPointerException();
    }

    public static <T> T execute(Supplier<T> supplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> List<T> executeList(Supplier<List<T>> supplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }
}
