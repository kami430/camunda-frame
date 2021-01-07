package com.flow.base.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class ResponseAdvice implements ResponseBodyAdvice<Object> {

    private final static Logger LOGGER = LoggerFactory.getLogger(ResponseAdvice.class);

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new Hibernate5Module().configure(Hibernate5Module.Feature.USE_TRANSIENT_ANNOTATION, false)
            .configure(Hibernate5Module.Feature.SERIALIZE_IDENTIFIER_FOR_LAZY_NOT_LOADED_OBJECTS, true));

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        Object responseEntity;
        if (body == null) {
            responseEntity = ResponseEntity.ok();
        } else if (body instanceof ResponseEntity) {
            responseEntity = body;
        } else if (body instanceof String) {
            responseEntity = jsonLog(ResponseEntity.ok(body));
        } else {
            responseEntity = ResponseEntity.ok(body);
        }
        LOGGER.info("接口调用：{} {}", request.getMethodValue(), request.getURI().getPath());
        LOGGER.info("接口返回数据：{}", (responseEntity instanceof String) ? responseEntity : jsonLog(responseEntity));
        return responseEntity;
    }


    @ExceptionHandler(value = {Exception.class})
    @ResponseBody
    public ResponseEntity exceptionHandler(Exception e) {
        LOGGER.error("接口返回错误：{}", e.getMessage(), e);
        if (e instanceof BusinessException) {
            return ((BusinessException) e).getResponseEntity();
        }
        return ResponseEntity.error(e.getMessage());
    }

    /**
     * jackson 处理懒加载(懒加载字段返回null)
     *
     * @return
     */
    @Bean
    public ObjectMapper objectMapper() {
        return this.objectMapper;
    }

    /**
     * 生成返回数据json日志
     *
     * @param object
     * @return
     */
    private String jsonLog(Object object) {
        try {
            return this.objectMapper.writeValueAsString(object);
        } catch (Exception e) {
            LOGGER.error(">>>>>数据转换json错误>>>>>{}", e.getMessage(), e);
            return "";
        }
    }
}
