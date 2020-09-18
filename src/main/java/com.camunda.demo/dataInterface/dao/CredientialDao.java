package com.camunda.demo.dataInterface.dao;

import com.camunda.demo.base.repository.BaseJpaRepository;
import com.camunda.demo.dataInterface.entity.authorization.UserCredential;

import java.util.List;

public interface CredientialDao extends BaseJpaRepository<UserCredential, Long> {
    UserCredential getByLoginUserAccount(String account);

    boolean deleteByLoginUserAccount(String account);

    default List<UserCredential> getCrediential() {
        return param().findList();
    }
}
