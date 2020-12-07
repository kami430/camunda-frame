package com.camunda.demo.dataInterface.constant.base;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

import javax.persistence.AttributeConverter;
import java.lang.reflect.ParameterizedType;

public class IBaseConvert<T extends IBaseEnum> implements AttributeConverter<T, Integer>, ConverterFactory<String, IBaseEnum> {

    @Override
    public Integer convertToDatabaseColumn(T attribute) {
        return attribute.getCode();
    }

    @Override
    public T convertToEntityAttribute(Integer dbData) {
        return IBaseEnum.ofCode(getTypeClass(),dbData);
    }

    @Override
    public <T extends IBaseEnum> Converter<String, T> getConverter(Class<T> targetType) {
        return new StringToEnum(targetType);
    }


    private Class<T> getTypeClass(){
        return (Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @SuppressWarnings("all")
    private static class StringToEnum<T extends IBaseEnum> implements Converter<String, T> {
        private Class<T> targetType;

        public StringToEnum(Class<T> targetType) {
            this.targetType = targetType;
        }

        @Override
        public T convert(String source) {
            if (source == null || "".equals(source.trim())) return null;
            return (T) IBaseEnum.ofCode(this.targetType, Integer.valueOf(source));
        }
    }
}
