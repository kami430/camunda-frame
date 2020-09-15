package com.camunda.demo.dataInterface.dao;

import com.camunda.demo.base.repository.BaseJpaRepository;
import com.camunda.demo.dataInterface.entity.authorization.Permission;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface PermissionDao extends BaseJpaRepository<Permission, Long> {

    List<Permission> findByIdIn(List<Long> permissionIds);
}
