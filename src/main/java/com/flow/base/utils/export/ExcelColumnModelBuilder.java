//package com.camunda.demo.base.utils.export;
//
//import org.apache.commons.lang3.StringUtils;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 用于创建excel列设置实例列表的帮助类
// *
// * @author <a href="mailto:ningyaobai@gzkit.com.cn">bernix</a>
// *         八月 22, 2016
// * @version 1.0
// */
//public class ExcelColumnModelBuilder {
//    private List<ExcelColumnModel> columns;
//
//    private ExcelColumnModelBuilder() {
//        columns = new ArrayList<ExcelColumnModel>();
//    }
//
//    /**
//     * 创建一个ExcelColumnModelBuilder实例对象
//     * @return
//     */
//    public static ExcelColumnModelBuilder create() {
//        return new ExcelColumnModelBuilder();
//    }
//
//    /**
//     * 构建一个excel列设置实例列表
//     * @return
//     */
//    public List<ExcelColumnModel> build() {
//        return columns;
//    }
//
//    /**
//     * 添加一个excel列设置实例对象, 标题使用field, 宽度使用excel的默认列宽度.
//     * field必须指定, 若不提供field, 则忽略此添加操作;
//     *
//     * @param field
//     * @return
//     */
//    public ExcelColumnModelBuilder add(String field) {
//        return add(field, null);
//    }
//
//    /**
//     * 添加一个excel列设置实例对象, 此列的宽度使用excel的默认列宽度.
//     * field必须指定, 若不提供field, 则忽略此添加操作;
//     * 若title没有指定，则此列没有标题;
//     *
//     * @param field
//     * @param title
//     * @return
//     */
//    public ExcelColumnModelBuilder add(String field, String title) {
//        return add(field, title, null);
//    }
//
//    /**
//     * 添加一个excel列设置实例对象.
//     * field必须指定, 若不提供field, 则忽略此添加操作;
//     * 若title没有指定，则此列没有标题;
//     * 若width没有指定, 或者width小于等于0, 则使用excel的默认列宽度
//     *
//     * @param field
//     * @param title
//     * @param width
//     * @return
//     */
//    public ExcelColumnModelBuilder add(String field, String title, Integer width) {
//        if (StringUtils.isNotBlank(field)) {
//            columns.add(buildColumnModel(field, title, width));
//        }
//        return this;
//    }
//
//    /**
//     * 创建一个excel列设置实例对象.
//     * field必须指定, 若不提供field, 则返回<code>null</code>;
//     * 若title没有指定，则此列没有标题;
//     * 若width没有指定, 或者width小于等于0, 则使用excel的默认列宽度
//     *
//     * @param field
//     * @param title
//     * @param width
//     * @return
//     */
//    private ExcelColumnModel buildColumnModel(String field, String title, Integer width) {
//
//        ExcelColumnModel model = new ExcelColumnModel();
//        model.setField(field);
//        model.setTitle(title);
//        if (width != null && width > 0) {
//            model.setWidth(width);
//        }
//        return model;
//    }
//}
