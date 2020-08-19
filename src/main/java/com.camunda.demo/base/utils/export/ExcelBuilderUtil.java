//package com.camunda.demo.base.utils.export;
//
//import org.apache.commons.lang3.StringUtils;
//import org.apache.commons.lang3.time.DateFormatUtils;
//import org.apache.poi.ss.usermodel.*;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import javax.imageio.ImageIO;
//import javax.servlet.http.HttpServletRequest;
//import java.awt.image.BufferedImage;
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.net.URLEncoder;
//import java.util.Date;
//
///**
// * Created by Administrator on 2016/7/11.
// */
//public class ExcelBuilderUtil {
//
//    private static final Logger logger = LoggerFactory.getLogger(ExcelBuilderUtil.class);
//    public static final String FONT_NAME_SONGTI = "宋体";
//
//    /**
//     * 将整型数据转换为字符串
//     * @param value
//     * @param defaultValue
//     * @return
//     */
//    public static String int2string(Integer value, String defaultValue) {
//        return value == null ? defaultValue : String.valueOf(value);
//    }
//
//    /**
//     * 创建一个格子
//     * @param row
//     * @param style
//     * @param value
//     * @param colIndex
//     */
//    public static Cell createCell(Row row, CellStyle style, String value, int colIndex) {
//        Cell cell = row.createCell(colIndex);
//        if (value != null) {
//            cell.setCellValue(value);
//        }
//        cell.setCellStyle(style);
//        return cell;
//    }
//
//    /**
//     * 创建字体
//     * @param workbook
//     * @param fontHeight
//     * @return
//     */
//    public static Font createExcelFont(Workbook workbook, short fontHeight) {
//        return createExcelFont(workbook, FONT_NAME_SONGTI, fontHeight, null, false);
//    }
//
//    /**
//     * 创建字体
//     * @param workbook
//     * @param fontHeight
//     * @param isBold
//     * @return
//     */
//    public static Font createExcelFont(Workbook workbook, short fontHeight, boolean isBold) {
//        return createExcelFont(workbook, FONT_NAME_SONGTI, fontHeight, null, isBold);
//    }
//
//    /**
//     * 创建字体
//     * @param workbook
//     * @param fontName
//     * @param fontHeight
//     * @param colorIndex HSSFColor index
//     * @param isBold
//     * @return
//     */
//    public static Font createExcelFont(Workbook workbook, String fontName, short fontHeight, Short colorIndex, boolean isBold) {
//        Font font = workbook.createFont();
//        font.setFontName(fontName);
//        font.setFontHeightInPoints(fontHeight);
//        if (isBold) {
//            font.setBoldweight(Font.BOLDWEIGHT_BOLD);
//        }
//        if (colorIndex != null) {
//            font.setColor(colorIndex);
//        }
//        return font;
//    }
//
//    /**
//     * 创建一个四边都有边框的格子样式
//     * @param workbook
//     * @param font
//     * @param horizontalAlign
//     * @param verticalAlgin
//     * @param isWrapText
//     * @return
//     */
//    public static CellStyle createCellStyleAllBorder(Workbook workbook, Font font, Short horizontalAlign, Short verticalAlgin, boolean isWrapText) {
//        return createCellStyle(workbook, font, horizontalAlign, verticalAlgin,
//                CellStyle.BORDER_THIN, CellStyle.BORDER_THIN, CellStyle.BORDER_THIN, CellStyle.BORDER_THIN, isWrapText);
//    }
//
//    /**
//     * 创建一个没有边框的格子样式
//     * @param workbook
//     * @param font
//     * @param horizontalAlign
//     * @param verticalAlgin
//     * @param isWrapText
//     * @return
//     */
//    public static CellStyle createCellStyleNoBorder(Workbook workbook, Font font, Short horizontalAlign, Short verticalAlgin, boolean isWrapText) {
//        return createCellStyle(workbook, font, horizontalAlign, verticalAlgin, null, null, null, null, isWrapText);
//    }
//
//    /**
//     * 创建表格格式
//     * @param workbook
//     * @param font
//     * @param horizontalAlign
//     * @param verticalAlgin
//     * @param topBorder
//     * @param rightBorder
//     * @param bottomBorder
//     * @param leftBorder
//     * @param isWrapText
//     * @return
//     */
//    public static CellStyle createCellStyle(Workbook workbook, Font font, Short horizontalAlign, Short verticalAlgin,
//        Short topBorder, Short rightBorder, Short bottomBorder, Short leftBorder, boolean isWrapText) {
//        CellStyle style = workbook.createCellStyle();
//        if (horizontalAlign != null) {
//            style.setAlignment(horizontalAlign);
//        }
//        if (verticalAlgin != null) {
//            style.setVerticalAlignment(verticalAlgin);
//        }
//        if (topBorder != null) {
//            style.setBorderTop(topBorder);
//        }
//        if (rightBorder != null) {
//            style.setBorderRight(rightBorder);
//        }
//        if (bottomBorder != null) {
//            style.setBorderBottom(bottomBorder);
//        }
//        if (leftBorder != null) {
//            style.setBorderLeft(leftBorder);
//        }
//        style.setFont(font);
//        style.setWrapText(isWrapText);
//        return style;
//    }
//
//    /**
//     * 将图片添加到excel表格中
//     * @param workbook
//     * @param drawing
//     * @param fileName 带路径的图片文件名
//     * @param row1
//     * @param col1
//     * @param row2
//     * @param col2
//     */
//    public static void attachPicture(Workbook workbook, Drawing drawing, String fileName,
//                                     int row1, int col1, int row2, int col2) throws IOException {
//        int pictype = ExcelBuilderUtil.getPictureType(fileName);
//        byte[] logoBytes = ExcelBuilderUtil.getPictureByteArray(fileName);
//        // add a picture in this workbook
//        int logoIdx = workbook.addPicture(logoBytes, pictype);
//
//        CreationHelper creationHelper = workbook.getCreationHelper();
//        // anchor主要用于设置图片的位置
//        ClientAnchor anchor = creationHelper.createClientAnchor();
//        // row1, col1 图片的左上点所在格(位于该格子的左上点)
//        anchor.setRow1(row1);
//        anchor.setCol1(col1);
//        // row2, col2 图片的右下点所在格(位于该格子的左上点)
//        anchor.setRow2(row2);
//        anchor.setCol2(col2);
//        // dx1, dy1 图片在左上格子的位移坐标
//        anchor.setDx1(0);
//        anchor.setDy1(0);
//        // dx1, dy1 图片在右下格子的位移坐标
//        anchor.setDx2(0);
//        anchor.setDy2(0);
//        anchor.setAnchorType(ClientAnchor.AnchorType.DONT_MOVE_AND_RESIZE);
//        drawing.createPicture(anchor, logoIdx);
//    }
//
//    /**
//     * 根据文件名获取图片类型
//     * @param fileName
//     * @return
//     */
//    private static int getPictureType(String fileName) {
//        return fileName.endsWith(".png") ? Workbook.PICTURE_TYPE_PNG : Workbook.PICTURE_TYPE_JPEG;
//    }
//
//    /**
//     * 读取图片文件并转换为byte数组
//     * @param fileName 全路径的文件名
//     * @return
//     * @throws IOException
//     */
//    private static byte[] getPictureByteArray(String fileName) throws IOException {
//        if (StringUtils.isBlank(fileName)) {
//            return null;
//        }
//
//        ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
//        BufferedImage bufferImg = ImageIO.read(new File(fileName));
//        String formatName = getPictureType(fileName) == Workbook.PICTURE_TYPE_PNG ? "png" : "jpg";
//        ImageIO.write(bufferImg, formatName, byteArrayOut);
//
//        return byteArrayOut.toByteArray();
//    }
//
//    public static String buildFilename(HttpServletRequest request, String filename, String extension) {
//        if (StringUtils.isBlank(filename)) {
//            return DateFormatUtils.format(new Date(), "yyyyMMdd_HHmmss").concat(extension);
//        } else {
//            return encodeFileName(request, filename).concat(extension);
//        }
//    }
//
//    /**
//     * 根据不同浏览器将文件名中的汉字转为UTF8编码的串,以便下载时能正确显示另存的文件名.
//     *
//     * @param request
//     * @param sourcename 原文件名
//     * @return 重新编码后的文件名
//     */
//    public static String encodeFileName(HttpServletRequest request, final String sourcename) {
//        String agent = request.getHeader("User-Agent");
//        String result;
//        try {
//            boolean isFireFox = (agent != null && agent.toLowerCase().indexOf("firefox") != -1);
//            if (isFireFox) {
//                result = new String(sourcename.getBytes("UTF-8"), "ISO8859-1");
//            } else {
//                result = URLEncoder.encode(sourcename, "UTF-8");
//                if ((agent != null && agent.indexOf("MSIE") != -1)) {
//                    // see http://support.microsoft.com/default.aspx?kbid=816868
//                    if (result.length() > 150) {
//                        // 根据request的locale 得出可能的编码
//                        result = new String(result.getBytes("UTF-8"), "ISO8859-1");
//                    }
//                }
//            }
//        } catch (UnsupportedEncodingException e) {
//            logger.error(">> encode excel filename[" + sourcename + "] error", e);
//            result = sourcename;
//        }
//        return result;
//    }
//}
