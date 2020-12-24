package com.camunda.demo.base.jxlsEngine;

import com.camunda.demo.base.utils.JxlsUtils;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class JxlsEngine {
    public static void exportExcel(HttpServletResponse response, BaseJxlsModel model) {
        JxlsUtils.exportExcel(model.getTemplateName(), response, model.toMap());
    }

    public static void exportExcel(HttpServletResponse response, BaseJxlsModel model, Map<String, BaseJxlsFunc> func) {
        Map<String, Object> funcMap = new HashMap<>();
        func.forEach((key, fun) -> funcMap.put(key, fun));
        JxlsUtils.exportExcel(model.getTemplateName(), response, model.toMap(), funcMap);
    }

    public static void previewExcel(HttpServletResponse response, BaseJxlsModel model) {
        JxlsUtils.exportExcel(model.getTemplateName(), response, model.toMap());
    }

    public static void previewExcel(HttpServletResponse response, BaseJxlsModel model, Map<String, BaseJxlsFunc> func)  {
        Map<String, Object> funcMap = new HashMap<>();
        func.forEach((key, fun) -> funcMap.put(key, fun));
        JxlsUtils.exportExcel(model.getTemplateName(), response, model.toMap(), funcMap);
    }
}
