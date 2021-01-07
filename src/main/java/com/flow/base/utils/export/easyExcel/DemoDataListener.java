//package com.camunda.demo.base.utils.export.easyExcel;
//
//import com.alibaba.excel.context.AnalysisContext;
//import com.alibaba.excel.event.AnalysisEventListener;
//import com.alibaba.excel.metadata.CellData;
//import com.alibaba.excel.metadata.CellExtra;
//import com.alibaba.fastjson.JSON;
//import com.camunda.demo.dataInterface.entity.excel.CellModel;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//
///**
// * 模板的读取类
// *
// * @author Jiaju Zhuang
// */
//// 有个很重要的点 DemoDataListener 不能被spring管理，要每次读取excel都要new,然后里面用到spring可以构造方法传进去
//public class DemoDataListener extends AnalysisEventListener<Map<Integer, Object>> {
//    private static final Logger LOGGER = LoggerFactory.getLogger(DemoDataListener.class);
//    /**
//     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
//     */
//    private static final int BATCH_COUNT = 5;
//
//    private List<CellModel> cellModels = new LinkedList<>();
//
//    @Override
//    public void invokeHead(Map<Integer, CellData> headMap, AnalysisContext context) {
//        Integer rowIndex = context.readRowHolder().getRowIndex();
//        headMap.forEach((key,value)->{
//            CellModel cell = new CellModel();
//            cell.setFirstRowIndex(rowIndex);
//            cell.setFirstColumnIndex(key);
//            cell.setLastRowIndex(rowIndex);
//            cell.setLastColumnIndex(key);
//            cell.setValue(value.getStringValue());
//            cell.setIsMerge(false);
//            cell.setIsHeader(true);
//            cellModels.add(cell);
//        });
//    }
//
//    /**
//     * 这个每一条数据解析都会来调用
//     *
//     * @param data    one row value. Is is same as {@link AnalysisContext#readRowHolder()}
//     * @param context
//     */
//    @Override
//    public void invoke(Map<Integer, Object> data, AnalysisContext context) {
//        Integer rowIndex = context.readRowHolder().getRowIndex();
//        data.forEach((key, value) -> {
//            CellModel cell = new CellModel();
//            cell.setFirstRowIndex(rowIndex);
//            cell.setFirstColumnIndex(key);
//            cell.setLastRowIndex(rowIndex);
//            cell.setLastColumnIndex(key);
//            cell.setValue(value);
//            cell.setIsMerge(false);
//            cell.setIsHeader(false);
//            cellModels.add(cell);
//        });
//    }
//
//    @Override
//    public void extra(CellExtra extra, AnalysisContext context) {
//        switch (extra.getType()) {
//            case COMMENT:
//                LOGGER.info("额外信息是批注,在rowIndex:{},columnIndex;{},内容是:{}", extra.getRowIndex(), extra.getColumnIndex(),
//                        extra.getText());
//                break;
//            case HYPERLINK:
//                if ("Sheet1!A1".equals(extra.getText())) {
//                    LOGGER.info("额外信息是超链接,在rowIndex:{},columnIndex;{},内容是:{}", extra.getRowIndex(),
//                            extra.getColumnIndex(), extra.getText());
//                } else if ("Sheet2!A1".equals(extra.getText())) {
//                    LOGGER.info(
//                            "额外信息是超链接,而且覆盖了一个区间,在firstRowIndex:{},firstColumnIndex;{},lastRowIndex:{},lastColumnIndex:{},"
//                                    + "内容是:{}",
//                            extra.getFirstRowIndex(), extra.getFirstColumnIndex(), extra.getLastRowIndex(),
//                            extra.getLastColumnIndex(), extra.getText());
//                }
//                break;
//            case MERGE:
//                cellModels.forEach(cell -> {
//                    if (cell.getFirstRowIndex() == extra.getFirstRowIndex() &&
//                            cell.getFirstColumnIndex() == extra.getFirstColumnIndex()) {
//                        cell.setLastRowIndex(extra.getLastRowIndex());
//                        cell.setLastColumnIndex(extra.getLastColumnIndex());
//                        cell.setIsMerge(true);
//                    }
//                });
//                break;
//        }
//    }
//
//    /**
//     * 所有数据解析完成了 都会来调用
//     *
//     * @param context
//     */
//    @Override
//    public void doAfterAllAnalysed(AnalysisContext context) {
//        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
//        saveData();
//        LOGGER.info("所有数据解析完成！");
//    }
//
//    /**
//     * 加上存储数据库
//     */
//    private void saveData() {
//        LOGGER.info("{}", JSON.toJSONString(cellModels));
//    }
//}