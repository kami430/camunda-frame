package com.camunda.demo.dataInterface.constant.base;

import java.util.Objects;

public interface IBaseJPAEnum {

    Integer getCode();

    String getRemark();

    static <T extends IBaseJPAEnum> T ofCode(Class<T> enumType, Integer code) {
        for (T object : enumType.getEnumConstants()) {
            if (Objects.equals(code, object.getCode())) {
                return object;
            }
        }
        throw new IllegalArgumentException("No enum code " + code + " of " + enumType.getCanonicalName());
    }
}
