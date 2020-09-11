package com.camunda.demo.dataInterface.dao;

import com.camunda.demo.base.repository.BaseJpaRepository;
import com.camunda.demo.dataInterface.entity.authorization.LoginCredential;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CredientialDao extends BaseJpaRepository<LoginCredential,Long> {
    LoginCredential getByLoginUserAccount(String account);

    boolean deleteByLoginUserAccount(String account);

    default List<LoginCredential> getCrediential(){
        return param().findList();
    }
}
