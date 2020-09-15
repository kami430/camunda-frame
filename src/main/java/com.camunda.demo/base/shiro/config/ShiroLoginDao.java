package com.camunda.demo.base.shiro.config;

public interface ShiroLoginDao {
    ShiroLoginEntity getShiroLoginEntity(String account);
}
