package com.camunda.demo.base.shiro.config;

import java.util.List;

public interface ShiroRoleDao {
    List<ShiroRole> getShiroRole(String account);
}
