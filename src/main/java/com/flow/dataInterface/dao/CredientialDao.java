package com.flow.dataInterface.dao;

import com.flow.base.repository.BaseJpaRepository;
import com.flow.dataInterface.entity.authorization.UserCredential;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface CredientialDao extends BaseJpaRepository<UserCredential, Long> {
    UserCredential getByLoginUserAccount(String account);

    boolean deleteByLoginUserAccount(String account);

    default List<UserCredential> getCrediential() {
        return param().findList();
    }

    default List<Map> abc(Long id, List<String> accounts) {
        String sql = "select b.id uid,a.password,a.salt,b.account,b.name from credential a inner join user b on a.user_id=b.id where 1=1 and %s and %s";
        return findBySql(multiParamFormat(sql, fmt -> fmt
                .add("a.id=?1", id)
                .add("b.account in(?2)", accounts)
        ), id, accounts);
    }

    UserCredential getById(Long id);
}
