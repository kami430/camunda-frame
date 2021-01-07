package com.flow.base.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class YamlUtils {

    private static final Logger logger = LoggerFactory.getLogger(YamlUtils.class);

    private static Map<String, String> defaultProperties = null;

    private static final String MAIN_PROFILE = "application.yml";

    private static final String INCLUDE_PROFILES = "spring.profiles.include";

    private static final String DEFAULT_PROFILE = "DEFAULT_PROFILE";

    private static Map<String, String> getDefaultProperties() {
        if (defaultProperties == null) {
            logger.info(">>>>>>初始化默认配置");
            defaultProperties = getProperties(MAIN_PROFILE);
            if (!isEmpty(defaultProperties.get(DEFAULT_PROFILE))) {
                // 如果指定默认配置,则覆盖原有配置
                defaultProperties = getProperties(defaultProperties.get(DEFAULT_PROFILE));
            }
            if (!isEmpty(defaultProperties.get(INCLUDE_PROFILES))) {
                // 如果指定子配置,则初始化子配置
                Arrays.asList(defaultProperties.get(INCLUDE_PROFILES).split(","))
                        .forEach(profile -> {
                            logger.info(">>>>>>初始化子配置：{}", profile);
                            Map<String, String> includeProfile = getProperties("application-" + profile + ".yml");
                            if (includeProfile != null) defaultProperties.putAll(includeProfile);
                        });
            }
            logger.info(">>>>>>初始化配置结束");
        }
        return defaultProperties;
    }

    public static Map<String, String> loadCustomConfig(String propertiesFileName) {
        logger.info(">>>>>>初始化自定义配置");
        return getProperties(propertiesFileName);
    }

    private static Map<String, String> getProperties(String propertiesFileName) {
        Map<String, String> properties = null;
        Yaml yaml = new Yaml();
        try (InputStream in = Loader.class.getClassLoader().getResourceAsStream(propertiesFileName)) {
            Map<String, Object> tmp = yaml.loadAs(in, Map.class);
            properties = loadProperties("", tmp);
        } catch (Exception e) {
            logger.error(">>>>>>初始化配置失败>>>>>> {}", "文件读取失败：" + e.getMessage());
        }
        return properties;
    }

    public static String getValue(String key) {
        return getDefaultProperties().get(key);
    }

    private static class Loader {
    }

    private static Map<String, String> loadProperties(String parentkey, Map<String, Object> properties) {
        Map<String, String> loadMap = new HashMap<>();
        properties.forEach((key, val) -> {
            if (val instanceof Map) {
                loadMap.putAll(loadProperties(parentkey + (isEmpty(parentkey) ? "" : ".") + key, (Map<String, Object>) val));
            } else {
                loadMap.put(parentkey + (isEmpty(parentkey) ? "" : ".") + key, val.toString());
            }
        });
        return loadMap;
    }

    private static boolean isEmpty(String str) {
        return (str == null || "".equals(str));
    }
}
