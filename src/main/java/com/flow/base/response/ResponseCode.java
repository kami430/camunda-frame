package com.flow.base.response;

public enum ResponseCode {

    OK("0000","OK"),
    UNAUTHORIZED("1001","会话过期"),
    UNLOGIN("1002","未授权"),
    PERMISSION_DENIED("1003","没有相关权限"),
    NOT_FOUND("1004","资源不存在"),
    SERVER_ERROR("1000","服务器异常");

    private final String code;

    private final String remark;

    ResponseCode(String code,String remark){
        this.code = code;
        this.remark = remark;
    }

    public String getCode(){
        return this.code;
    }

    public String getRemark(){
        return this.remark;
    }

    public static ResponseCode ofCode(String code){
        for(ResponseCode responseCode:values()){
            if(responseCode.getCode().equals(code))
                return responseCode;
        }
        return null;
    }
}
