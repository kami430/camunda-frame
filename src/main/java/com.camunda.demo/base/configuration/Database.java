package com.camunda.demo.base.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * 双数据源，如不需要注释该类
 */
@Configuration
public class Database {

    @Autowired
    private DbProperties dbProperties;

    @Autowired
    private CamundaDbProperties camundaDbProperties;

    /**
     * 业务数据源
     *
     * @return
     */
    @Bean
    @Primary
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .driverClassName(dbProperties.getDriverClassName())
                .url(dbProperties.getUrl())
                .username(dbProperties.getUsername())
                .password(dbProperties.getPassword())
                .type(dbProperties.getType())
                .build();
    }

    /**
     * 流程数据源
     *
     * @return
     */
    @Bean(name = "camundaBpmDataSource")
    public DataSource camundaDataSource() {
        return DataSourceBuilder.create()
                .driverClassName(camundaDbProperties.getDriverClassName())
                .url(camundaDbProperties.getUrl())
                .username(camundaDbProperties.getUsername())
                .password(camundaDbProperties.getPassword())
                .type(camundaDbProperties.getType())
                .build();
    }

    @Component("primaryProp")
    @ConfigurationProperties(prefix = "spring.datasource")
    public class DbProperties {

        private String driverClassName;

        private String url;

        private String username;

        private String password;

        private Class<? extends DataSource> type;

        public String getDriverClassName() {
            return driverClassName;
        }

        public void setDriverClassName(String driverClassName) {
            this.driverClassName = driverClassName;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public Class<? extends DataSource> getType() {
            return type;
        }

        public void setType(Class<? extends DataSource> type) {
            this.type = type;
        }
    }

    @Component("prop")
    @ConfigurationProperties(prefix = "spring.datasource-proc")
    public class CamundaDbProperties {

        private String driverClassName;

        private String url;

        private String username;

        private String password;

        private Class<? extends DataSource> type;

        public String getDriverClassName() {
            return driverClassName;
        }

        public void setDriverClassName(String driverClassName) {
            this.driverClassName = driverClassName;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public Class<? extends DataSource> getType() {
            return type;
        }

        public void setType(Class<? extends DataSource> type) {
            this.type = type;
        }
    }
}
