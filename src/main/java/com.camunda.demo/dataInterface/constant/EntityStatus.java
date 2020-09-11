package com.camunda.demo.dataInterface.constant;

import com.camunda.demo.dataInterface.constant.base.IBaseJPAConvert;
import com.camunda.demo.dataInterface.constant.base.IBaseJPAEnum;
import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EntityStatus implements IBaseJPAEnum {
    ACTIVE(1, "有效"),
    FREEZE(2, "冻结"),
    INVALID(0, "失效");

    private Integer code;

    private String remark;

    EntityStatus(Integer code, String remark) {
        this.code = code;
        this.remark = remark;
    }

    public String getRemark() {
        return this.remark;
    }

    public Integer getCode() {
        return this.code;
    }

    public static class Convert extends IBaseJPAConvert<EntityStatus> {
    }
}