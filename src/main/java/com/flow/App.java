package com.flow;

import com.flow.base.repository.BaseJpaRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableJpaRepositories(repositoryBaseClass = BaseJpaRepositoryImpl.class)
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}
