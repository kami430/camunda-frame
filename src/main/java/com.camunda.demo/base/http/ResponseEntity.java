package com.camunda.demo.base.http;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ResponseEntity extends HashMap<String, Object> {

    private static final String CODE = "code";

    private static final String ERROR_MSG = "err_msg";

    private static final String DATA = "data";

    private Map<String,Object> data = new HashMap<>();

    public ResponseEntity() {
        put(CODE, ResponseCode.OK.getCode());
        put(ERROR_MSG,"");
        put(DATA,data);
    }

    public static ResponseEntity ok() {
        return new ResponseEntity();
    }

    public static ResponseEntity ok(Consumer<Map<String,Object>> mapConsumer) {
        ResponseEntity responseEntity = ok();
        mapConsumer.accept(responseEntity.data);
        return responseEntity;
    }

    public static ResponseEntity ok(Object data) {
        ResponseEntity responseEntity = ok();
        responseEntity.put(DATA, data!=null?data: Collections.emptyMap());
        return responseEntity;
    }

    public static ResponseEntity error() {
        ResponseEntity responseEntity = ok();
        responseEntity.put(CODE, ResponseCode.SERVER_ERROR.getCode());
        return responseEntity;
    }

    public static ResponseEntity error(ResponseCode code, String errmsg) {
        ResponseEntity responseEntity = ok();
        responseEntity.put(CODE, code.getCode());
        responseEntity.put(ERROR_MSG, errmsg);
        return responseEntity;
    }

    public static ResponseEntity error(String errmsg) {
        ResponseEntity responseEntity = ok();
        responseEntity.put(CODE, ResponseCode.SERVER_ERROR.getCode());
        responseEntity.put(ERROR_MSG, errmsg);
        return responseEntity;
    }
}
