package com.camunda.demo.dataInterface.constant;

import com.camunda.demo.dataInterface.constant.base.IBaseConvert;
import com.camunda.demo.dataInterface.constant.base.IBaseEnum;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum EntityStatus implements IBaseEnum {
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

    /**
     * 对应@RequestBody枚举的情况,静态方法,方法名任意,返回类型为当前类,关键为@JsonCreator注解
     * @param code
     * @return
     */
    @JsonCreator
    public static EntityStatus getStatus(Integer code){
        return IBaseEnum.ofCode(EntityStatus.class,code);
    }

    /**
     * 对应JPA映射数据库转换器,任意类名继承IBaseConvert<枚举类名>即可
     */
    public static class Convert extends IBaseConvert<EntityStatus> {
    }
}