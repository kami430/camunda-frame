package com.camunda.demo.dataInterface.dao;

import com.camunda.demo.base.repository.BaseJpaRepository;
import com.camunda.demo.base.shiro.config.ShiroLoginDao;
import com.camunda.demo.base.shiro.config.ShiroLoginEntity;
import com.camunda.demo.dataInterface.entity.authorization.LoginCredential;

import java.util.List;

public interface CredientialDao extends BaseJpaRepository<LoginCredential, Long>, ShiroLoginDao {
    LoginCredential getByLoginUserAccount(String account);

    boolean deleteByLoginUserAccount(String account);

    default List<LoginCredential> getCrediential() {
        return param().findList();
    }

    default ShiroLoginEntity getShiroLoginEntity(String account) {
        return getByLoginUserAccount(account);
    }
}
