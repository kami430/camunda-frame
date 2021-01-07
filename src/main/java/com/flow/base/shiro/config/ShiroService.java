package com.flow.base.shiro.config;

import java.util.List;

public interface ShiroService {
    ShiroUser getShiroUser(String account);

    List<String> getPermissions(String account);

    List<String> getRoles(String account);
}
