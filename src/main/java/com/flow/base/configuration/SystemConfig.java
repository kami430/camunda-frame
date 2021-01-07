package com.flow.base.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("system")
public class SystemConfig {

    private String fileDesk;

    @Bean
    public Integer initConfig() {
        Static.setProperties(this);
        return 0;
    }

    public static SystemConfig getConfig() {
        return Static.getConfig();
    }

    public static class Static {
        private static SystemConfig config;

        public static void setProperties(SystemConfig systemConfig) {
            config = systemConfig;
        }

        public static SystemConfig getConfig() {
            return config;
        }
    }
}
