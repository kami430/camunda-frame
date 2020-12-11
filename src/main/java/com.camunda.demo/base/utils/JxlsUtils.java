package com.camunda.demo.base.utils;

import com.camunda.demo.base.response.BusinessException;
import com.camunda.demo.base.response.ResponseCode;
import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlEngine;
import org.jxls.common.Context;
import org.jxls.expression.JexlExpressionEvaluator;
import org.jxls.transform.Transformer;
import org.jxls.transform.poi.PoiTransformer;
import org.jxls.util.JxlsHelper;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * xls模版导出
 */
public class JxlsUtils {

    /*模版目录*/
    private final static String BASE_EXCEL_TEMPLATE_PATH = "xlstmpl/";

    private static Map<String, Object> funcs = new HashMap<>();

    /*基础自定义方法*/
    private final static JxlsFunc JXLS_FUNC = new JxlsFunc();

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

    public static void exportExcel(File in, File out, Map<String, Object> model) throws IOException {
        exportExcel(new FileInputStream(in), new FileOutputStream(out), model);
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
        funcs.put(JxlsFunc.BASE_EXP_NAMESPACE, JXLS_FUNC); //添加自定义功能
        JexlBuilder jb = new JexlBuilder();
        jb.namespaces(funcs);
        //jb.silent(true); //设置静默模式，不报警告
        JexlEngine je = jb.create();
        evaluator.setJexlEngine(je);
        //必须要这个，否者表格函数统计会错乱
        jxlsHelper.setUseFastFormulaProcessor(false).processTemplate(context, transformer);
    }

    /**
     * 添加自定义方法(与setFunctions只能生效一个)
     */
    public static JxlsUtils setFunc(String namespace, Object bean) {
        funcs.clear();
        if (JxlsFunc.BASE_NAMESPACE.equals(namespace)
                || JxlsFunc.BASE_EXP_NAMESPACE.equals(namespace))
            throw new BusinessException(ResponseCode.SERVER_ERROR,"");
        funcs.put(namespace, bean);
        return null;
    }

    /**
     * 添加自定义方法(与setFunction只能生效一个)
     */
    public static JxlsUtils setFunc(Map<String, Object> funcstions) {
        funcs.clear();
        Set<String> keys = funcstions.keySet();
        for (String key : keys) {
            if (JxlsFunc.BASE_NAMESPACE.equals(key)
                    || JxlsFunc.BASE_EXP_NAMESPACE.equals(key)) return null;
        }
        funcstions.forEach((key, bean) -> {
            funcs.put(key, bean);
        });
        return null;
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
    private static File getTemplate(String file) {
        try {
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            String locationPattern = "classpath:" + BASE_EXCEL_TEMPLATE_PATH + (file != null ? file : "./");
            File tmpl = resolver.getResource(locationPattern).getFile();
            return tmpl;
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException(ResponseCode.NOT_FOUND, "模版不存在");
        }
    }


    /**
     * 基本自定义函数
     */
    public static class JxlsFunc {
        /*jxl基础函数域名*/
        private final static String BASE_NAMESPACE = "jx";
        /*自定义函数域名*/
        private final static String BASE_EXP_NAMESPACE = "ju";

        // 日期格式化
        public String dateFmt(Date date, String pattern) {
            if (date == null) return "";
            try {
                SimpleDateFormat dateFmt = new SimpleDateFormat(pattern);
                return dateFmt.format(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        // if判断
        public Object ifElse(boolean b, Object o1, Object o2) {
            return b ? o1 : o2;
        }

        // 金钱格式化
        public String moneyFmt(Double money, String pattern) {
            try {
                return new DecimalFormat(pattern).format(money);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        // 手机号隐藏
        public String phoneHide(String phone) {
            try {
                if (phone.length() != 11) throw new Exception("非手机号码");
                return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        // 身份证号码隐藏
        public String idCardHide(String idCard) {
            try {
                if (idCard.length() != 18 && idCard.length() != 15) throw new Exception("非身份证号码");
                if (idCard.length() == 18) return idCard.replaceAll("(\\d{4})\\d{10}(\\d{4})", "$1****$2");
                else return idCard.replaceAll("(\\d{4})\\d{8}(\\d{3})", "$1****$2");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return idCard;
        }
    }
}