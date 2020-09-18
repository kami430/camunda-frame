package com.camunda.demo.business.service.Impl;

import com.camunda.demo.base.shiro.config.ShiroService;
import com.camunda.demo.base.shiro.config.ShiroUser;
import com.camunda.demo.dataInterface.dao.CredientialDao;
import com.camunda.demo.dataInterface.entity.authorization.Permission;
import com.camunda.demo.dataInterface.entity.authorization.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShiroLoginService implements ShiroService {

    @Autowired
    private CredientialDao dao;

    @Override
    public ShiroUser getShiroUser(String account) {
        return dao.getByLoginUserAccount(account);
    }

    @Override
    public List<String> getPermissions(String account) {
        return dao.getByLoginUserAccount(account).getLoginUser().getPermissions().stream().map(Permission::getCode).collect(Collectors.toList());
    }

    @Override
    public List<String> getRoles(String account) {
        return dao.getByLoginUserAccount(account).getLoginUser().getRoles().stream().map(Role::getCode).collect(Collectors.toList());
    }
}
