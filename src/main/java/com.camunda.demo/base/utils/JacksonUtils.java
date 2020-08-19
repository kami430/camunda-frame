package com.camunda.demo.base.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class JacksonUtils {

    private static JacksonUtils DEFAULT_INSTANCE = new JacksonUtils(null);
    private static JacksonUtils DATETIME_INSTANCE = new JacksonUtils("yyyy-MM-dd HH:mm:ss");
    private static JacksonUtils DATE_INSTANCE = new JacksonUtils("yyyy-MM-dd");
    private static JacksonUtils TIME_INSTANCE = new JacksonUtils("HH:mm:ss");

    private ObjectMapper mapper;

    private JacksonUtils(String dateformat) {
        init(dateformat);
    }

    private void init(String dateformat) {
        mapper = new ObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        if (StringUtils.isNotBlank(dateformat)) {
            mapper.setDateFormat(new SimpleDateFormat(dateformat));
        }
    }

    /**
     * Get the JacksonUtil instance with default ObjectMapper.
     *
     * @return a JacksonUtil instance
     */
    public static JacksonUtils defaultInstance() {
        return DEFAULT_INSTANCE;
    }

    /**
     * Get the JacksonUtil instance which will handle java.util.Date with the format string "yyyy-MM-dd HH:mm:ss"
     *
     * @return a JacksonUtil instance
     */
    public static JacksonUtils datetimeInstance() {
        return DATETIME_INSTANCE;
    }

    /**
     * Get the JacksonUtil instance which will handle java.util.Date with the format string "yyyy-MM-dd"
     *
     * @return a JacksonUtil instance
     */
    public static JacksonUtils dateInstance() {
        return DATE_INSTANCE;
    }

    /**
     * Get the JacksonUtil instance which will handle java.util.Date with the format string "HH:mm:ss"
     *
     * @return a JacksonUtil instance
     */
    public static JacksonUtils timeInstance() {
        return TIME_INSTANCE;
    }

    /**
     * Get a new JacksonUtil instance which will handle java.util.Date with specified datetime format
     *
     * @param dateformat the specified datetime format
     * @return a JacksonUtil instance
     */
    public static JacksonUtils customFormatInstance(String dateformat) {
        return new JacksonUtils(dateformat);
    }

    /**
     * 仅读取整个json中的某个属性值
     *
     * @param jsonString json string
     * @param fieldName  field name
     * @return the field value
     * @throws IOException
     */
    public String readField(String jsonString, String fieldName) throws IOException {
        JsonNode root = mapper.readTree(jsonString);
        JsonNode node = root.path(fieldName);
        return node.asText();
    }

    /**
     * 将json字符串转换为指定类型的java对象
     *
     * @param jsonString
     * @param clazz
     * @return
     * @throws IOException
     */
    public <T> T json2pojo(String jsonString, Class<T> clazz) {
        try {
            return mapper.readValue(jsonString, clazz);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将json字符串转换为HashMap(json里的子对象也将转换为Map)
     *
     * @param jsonString
     * @return
     * @throws IOException
     */
    public Map<String, Object> json2map(String jsonString) {
        try {
            return mapper.readValue(jsonString, new TypeReference<HashMap<String, Object>>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将java对象转换为json字符串
     *
     * @param pojo
     * @return
     * @throws JsonProcessingException
     */
    public String pojo2json(Object pojo) {
        try {
            return mapper.writeValueAsString(pojo);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将java对象转换为map对象
     *
     * @param pojo
     * @return
     * @throws IOException
     */
    public Map<String, Object> pojo2map(Object pojo) {
        return json2map(pojo2json(pojo));
    }

}
