package com.camunda.demo.base.http;

/**
 * @author <a href="mailto:ningyaobai@gzkit.com.cn">bernix</a>
 *         十一月 09, 2016
 * @version 1.0
 */
public class BusinessException extends RuntimeException {

    private ResponseEntity responseEntity;

    public BusinessException() {
        this.responseEntity = ResponseEntity.error();
    }

    public BusinessException(String message) {
        super(message);
        this.responseEntity = ResponseEntity.error(message);
    }

    public BusinessException(ResponseCode code,String message){
        super(message);
        this.responseEntity = ResponseEntity.error(code,message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
        this.responseEntity = ResponseEntity.error(message);
    }

    public BusinessException(ResponseCode code,String message,Throwable cause){
        super(message, cause);
        this.responseEntity = ResponseEntity.error(code,message);
    }

    public BusinessException(Throwable cause) {
        super(cause);
        this.responseEntity = ResponseEntity.error();
    }

    public BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.responseEntity = ResponseEntity.error(message);
    }

    public BusinessException(ResponseCode code,String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.responseEntity = ResponseEntity.error(code,message);
    }

    public ResponseEntity getResponseEntity() {
        return responseEntity;
    }
}
