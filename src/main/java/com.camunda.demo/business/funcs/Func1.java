package com.camunda.demo.business.funcs;

public class Func1 {
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

    public Integer add(Integer a, Integer b) {
        return a + b;
    }
}
