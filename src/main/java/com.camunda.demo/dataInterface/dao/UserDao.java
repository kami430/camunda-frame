package com.camunda.demo.dataInterface.dao;

import com.camunda.demo.base.repository.BaseJpaRepository;
import com.camunda.demo.base.repository.Param;
import com.camunda.demo.dataInterface.entity.authorization.LoginUser;

import java.util.List;

public interface UserDao extends BaseJpaRepository<LoginUser, Long> {

    LoginUser findByAccount(String account);

    List<LoginUser> findByIdIn(List<Long> userIds);

    default List<LoginUser> haha(String name) {
        Param<LoginUser> param = param().addOrder("name", "desc");
        return param.findList();
    }

}
