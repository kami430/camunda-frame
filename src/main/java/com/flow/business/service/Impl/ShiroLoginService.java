package com.flow.business.service.Impl;

import com.flow.base.shiro.config.ShiroService;
import com.flow.base.shiro.config.ShiroUser;
import com.flow.dataInterface.dao.CredientialDao;
import com.flow.dataInterface.entity.authorization.Permission;
import com.flow.dataInterface.entity.authorization.Role;
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
