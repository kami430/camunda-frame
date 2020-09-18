package com.camunda.demo.dataInterface.dao;

import com.camunda.demo.base.repository.BaseJpaRepository;
import com.camunda.demo.dataInterface.entity.authorization.LoginUser;

import java.util.List;
import java.util.stream.Collectors;

public interface UserDao extends BaseJpaRepository<LoginUser, Long> {

    LoginUser findByAccount(String account);

    List<LoginUser> findByIdIn(List<Long> userIds);
}
