package com.camunda.demo.base.jxlsEngine.jxlsFunc;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 自定义的xml函数,可以改动
 */
public class JxlsFunc {
    // 函数域名
    public final static String NAMESPACE = "ju";
    // 日期格式化
    public String dateFmt(Date date, String fmt) {
        if (date == null) {
            return "";
        }
        try {
            SimpleDateFormat dateFmt = new SimpleDateFormat(fmt);
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
}