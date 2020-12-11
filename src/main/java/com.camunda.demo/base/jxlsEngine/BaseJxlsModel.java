package com.camunda.demo.base.jxlsEngine;

import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public interface BaseJxlsModel {
    String getTemplateName();

    default Map<String, Object> toMap() {
        Map<String, Object> model = new HashMap<>();
        try {
            BeanUtils.populate(this, model);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return model;
    }
}
