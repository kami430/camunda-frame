package com.flow.dataInterface.dao;

import com.flow.base.repository.BaseJpaRepository;
import com.flow.dataInterface.entity.authorization.LoginUser;

import java.util.List;

public interface UserDao extends BaseJpaRepository<LoginUser, Long> {

    LoginUser findByAccount(String account);

    List<LoginUser> findByIdIn(List<Long> userIds);
}
