package com.camunda.demo.dataInterface.dao;

import com.camunda.demo.base.repository.BaseJpaRepository;
import com.camunda.demo.dataInterface.entity.authorization.Role;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface RoleDao extends BaseJpaRepository<Role, Long> {
    List<Role> findByIdIn(List<Long> roleIds);
}
