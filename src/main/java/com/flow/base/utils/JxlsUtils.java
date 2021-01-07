package com.flow.base.utils;

import com.flow.base.response.BusinessException;
import com.flow.base.response.ResponseCode;
import org.apache.commons.jexl3.JexlBuilder;
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
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * xls模版导出
 */
public class JxlsUtils {

    /*模版目录*/
    private final static String BASE_EXCEL_TEMPLATE_PATH = "xlstmpl/";

    /*基础自定义方法*/
    private final static JxlsFunc JXLS_FUNC = new JxlsFunc();

    public static void exportExcel(String templatePath, HttpServletResponse response, Map<String, Object> model) {
        exportExcel(templatePath, response, model, null);
    }

    public static void exportExcel(String templatePath, HttpServletResponse response, Map<String, Object> model, Map<String, Object> funcMap) {
        File template = getTemplate(templatePath);
        try (InputStream inputStream = new FileInputStream(template);
             OutputStream outputStream = response.getOutputStream()) {
            response.setContentType("application/x-download");
            response.addHeader("Content-Disposition", "attachment;filename=" + getDownloadFileName(template.getName()));
            response.setCharacterEncoding("UTF-8");
            exportExcel(inputStream, outputStream, model, funcMap);
        } catch (Exception e) {
            throw new BusinessException(ResponseCode.SERVER_ERROR, e.getMessage(), e);
        }
    }

    public static void previewExcel(String templatePath, HttpServletResponse response, Map<String, Object> model) {
        previewExcel(templatePath, response, model, null);
    }

    public static void previewExcel(String templatePath, HttpServletResponse response, Map<String, Object> model, Map<String, Object> funcMap) {
        File template = getTemplate(templatePath);
        try (InputStream inputStream = new FileInputStream(template);
             OutputStream outputStream = response.getOutputStream()) {
            response.setContentType(getContentType(templatePath));
            response.addHeader("Content-Disposition", "inline;filename=" + getDownloadFileName(template.getName()));
            response.setCharacterEncoding("UTF-8");
            exportExcel(inputStream, outputStream, model, funcMap);
        } catch (Exception e) {
            throw new BusinessException(ResponseCode.SERVER_ERROR, e.getMessage(), e);
        }
    }

    public static void exportExcel(File in, File out, Map<String, Object> model) throws IOException {
        exportExcel(new FileInputStream(in), new FileOutputStream(out), model);
    }

    public static void exportExcel(File in, File out, Map<String, Object> model, Map<String, Object> funcMap) throws IOException {
        exportExcel(new FileInputStream(in), new FileOutputStream(out), model, funcMap);
    }

    public static void exportExcel(String templatePath, OutputStream os, Map<String, Object> model) throws IOException {
        File template = getTemplate(templatePath);
        exportExcel(new FileInputStream(template), os, model);
    }

    public static void exportExcel(String templatePath, OutputStream os, Map<String, Object> model, Map<String, Object> funcMap) throws IOException {
        File template = getTemplate(templatePath);
        exportExcel(new FileInputStream(template), os, model, funcMap);
    }

    public static void exportExcel(InputStream is, OutputStream os, Map<String, Object> model) throws IOException {
        exportExcel(is, os, model, null);
    }

    public static void exportExcel(InputStream is, OutputStream os, Map<String, Object> model, Map<String, Object> funcMap) throws IOException {
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
        if (funcMap != null && funcMap.size() != 0) {
            if (funcMap.containsKey(JxlsFunc.BASE_EXP_NAMESPACE) || funcMap.containsKey(JxlsFunc.BASE_NAMESPACE)) {
                throw new BusinessException(ResponseCode.SERVER_ERROR, "不能与基础域:ju/jx冲突");
            }
            funcs.putAll(funcMap);
        }
        funcs.put(JxlsFunc.BASE_EXP_NAMESPACE, JXLS_FUNC); //添加自定义功能
        JexlBuilder jb = new JexlBuilder();
        //jb.silent(true); //设置静默模式，不报警告
        evaluator.setJexlEngine(jb.namespaces(funcs).create());
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
     * 把数据分成多sheet,由于性能问题,限制500个sheet以内
     * (报表模版中:pager-数据,sheetNames-sheet名称列表,pager.name-单个sheet名称)
     * 表头例子1:
     * jx:area(lastCell="E6")
     * jx:each(items="pager",var="page",multisheet="page.name",lastCell="E6")
     * 表头例子2:
     * jx:area(lastCell="E6")
     * jx:each(items="pager",var="page",multisheet="sheetNames",lastCell="E6")
     * 列表数据例子:
     * jx:each(items="page.data",var="data",lastCell="E5")
     *
     * @param data 要分sheet的数据
     * @param size 每个sheet的数据量
     * @param <T>
     * @return
     */
    public static <T> Map<String, Object> multiSheet(List<T> data, int size) {
        Map<String, Object> pagerMap = new HashMap<>();
        Integer limit = data.size() % size == 0 ? data.size() / size : data.size() + 1;
        if (limit > 500) throw new BusinessException(ResponseCode.SERVER_ERROR, "sheet数量不能超过500");
        List<Pager> pagerList = new ArrayList<>();
        List<String> sheetNames = new ArrayList<>();
        Stream.iterate(0, n -> n + 1).limit(limit).forEach(i -> {
            List<T> list = data.stream().skip(i * size).limit(size).parallel().collect(Collectors.toList());
            String sheetName = i == 0 ? "sheet" : "sheet" + (i + 1);
            pagerList.add(new Pager(list, sheetName));
            sheetNames.add(sheetName);
        });
        pagerMap.put("pager", pagerList);
        pagerMap.put("sheetNames", sheetNames);
        return pagerMap;
    }

    /**
     * 多sheet分组
     *
     * @param <T>
     */
    public static class Pager<T> {
        private int size;
        private List<T> data;
        private String name;

        public Pager(List<T> data, String name) {
            this.size = data.size();
            this.data = data;
            this.name = name;
        }

        public List<T> getData() {
            return data;
        }

        public String getName() {
            return name;
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