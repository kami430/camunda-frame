package com.camunda.demo.base.jxlsEngine;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.camunda.demo.base.jxlsEngine.jxlsFunc.JxlsFunc;
import com.camunda.demo.base.response.BusinessException;
import com.camunda.demo.base.response.ResponseCode;
import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlEngine;
import org.jxls.common.Context;
import org.jxls.expression.JexlExpressionEvaluator;
import org.jxls.transform.Transformer;
import org.jxls.transform.poi.PoiTransformer;
import org.jxls.util.JxlsHelper;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;

/**
 * xls模版导出
 */
public class JxlsUtils {

    public static void exportExcel(String templatePath, HttpServletResponse response, Map<String, Object> model) {
        File template = getTemplate(templatePath);
        try (InputStream inputStream = new FileInputStream(template);
             OutputStream outputStream = response.getOutputStream()) {
            response.setContentType("application/x-download");
            response.addHeader("Content-Disposition", "attachment;filename=" + getDownloadFileName(template.getName()));
            response.setCharacterEncoding("UTF-8");
            exportExcel(inputStream, outputStream, model);
        } catch (Exception e) {
            throw new BusinessException(ResponseCode.SERVER_ERROR, e.getMessage(), e);
        }
    }

    public static void previewExcel(String templatePath, HttpServletResponse response, Map<String, Object> model) {
        File template = getTemplate(templatePath);
        try (InputStream inputStream = new FileInputStream(template);
             OutputStream outputStream = response.getOutputStream()) {
            response.setContentType(getContentType(templatePath));
            response.addHeader("Content-Disposition", "inline;filename=" + getDownloadFileName(template.getName()));
            response.setCharacterEncoding("UTF-8");
            exportExcel(inputStream, outputStream, model);
        } catch (Exception e) {
            throw new BusinessException(ResponseCode.SERVER_ERROR, e.getMessage(), e);
        }
    }

    public static void exportExcel(File xls, File out, Map<String, Object> model) throws IOException {
        exportExcel(new FileInputStream(xls), new FileOutputStream(out), model);
    }

    public static void exportExcel(String templatePath, OutputStream os, Map<String, Object> model) throws IOException {
        File template = getTemplate(templatePath);
        exportExcel(new FileInputStream(template), os, model);
    }

    public static void exportExcel(InputStream is, OutputStream os, Map<String, Object> model) throws IOException {
        Context context = PoiTransformer.createInitialContext();
        if (model != null) {
            for (String key : model.keySet()) {
                context.putVar(key, model.get(key));
            }
        }
        JxlsHelper jxlsHelper = JxlsHelper.getInstance();
        Transformer transformer = jxlsHelper.createTransformer(is, os);
        //获得配置
        JexlExpressionEvaluator evaluator = (JexlExpressionEvaluator) transformer.getTransformationConfig()
                .getExpressionEvaluator();
        //函数强制，自定义功能
        Map<String, Object> funcs = new HashMap<>();
        funcs.put(JxlsFunc.NAMESPACE, new JxlsFunc()); //添加自定义功能
        JexlBuilder jb = new JexlBuilder();
        jb.namespaces(funcs);
        //jb.silent(true); //设置静默模式，不报警告
        JexlEngine je = jb.create();
        evaluator.setJexlEngine(je);
        //必须要这个，否者表格函数统计会错乱
        jxlsHelper.setUseFastFormulaProcessor(false).processTemplate(context, transformer);
    }

    /**
     * 获取文件原名
     *
     * @param name
     * @return
     */
    private static String getDownloadFileName(String name) throws UnsupportedEncodingException {
        int index1 = name.lastIndexOf("_");
        int index2 = name.lastIndexOf(".");
        if (index1 != -1 && index2 != -1) {
            name = name.substring(0, index1) + name.substring(index2);
        } else if (index2 == -1) {
            name = name.substring(0, index1);
        }
        name = new String(name.getBytes("UTF-8"), "iso-8859-1");
        return name;
    }

    /**
     * 根据文件名/文件路径获取contentType
     *
     * @param path
     * @return
     * @throws IOException
     */
    private static String getContentType(String path) throws IOException {
        String contentType = Files.probeContentType(Paths.get(path));
        return StringUtils.isEmpty(contentType) ? "application/octet-stream" : contentType;
    }

    //获取jxls模版文件
    public static File getTemplate(String path) {
        File file = new File(path);
        if (!file.exists()) {
            throw new BusinessException(ResponseCode.NOT_FOUND, "模版不存在");
        }
        return file;
    }
}