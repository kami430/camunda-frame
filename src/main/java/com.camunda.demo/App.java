package com.camunda.demo;

import com.camunda.demo.base.repository.BaseJpaRepositoryImpl;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableJpaRepositories(repositoryBaseClass = BaseJpaRepositoryImpl.class)
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class,args);
    }
}
