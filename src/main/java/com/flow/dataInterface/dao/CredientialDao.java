package com.flow.dataInterface.dao;

import com.flow.base.repository.BaseJpaRepository;
import com.flow.dataInterface.entity.authorization.UserCredential;

import java.util.List;

public interface CredientialDao extends BaseJpaRepository<UserCredential, Long> {
    UserCredential getByLoginUserAccount(String account);

    boolean deleteByLoginUserAccount(String account);

    default List<UserCredential> getCrediential() {
        return param().findList();
    }
}
