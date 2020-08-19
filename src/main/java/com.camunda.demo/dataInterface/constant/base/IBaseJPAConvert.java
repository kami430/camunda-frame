package com.camunda.demo.dataInterface.constant.base;

import javax.persistence.AttributeConverter;
import java.lang.reflect.ParameterizedType;

public class IBaseJPAConvert<T extends IBaseJPAEnum> implements AttributeConverter<T, Integer> {

    @Override
    public Integer convertToDatabaseColumn(T attribute) {
        return attribute.getCode();
    }

    @Override
    public T convertToEntityAttribute(Integer dbData) {
        return IBaseJPAEnum.ofCode(getTypeClass(),dbData);
    }

    private Class<T> getTypeClass(){
        return (Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }
}
