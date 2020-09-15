package com.camunda.demo.base.shiro.config;

import java.util.List;

public interface ShiroPermissionDao {
    List<ShiroPermission> getShiroPermission(String account);
}
