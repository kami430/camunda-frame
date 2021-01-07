package com.flow.dataInterface.dao;

import com.flow.base.repository.BaseJpaRepository;
import com.flow.dataInterface.entity.authorization.Permission;

import java.util.List;

public interface PermissionDao extends BaseJpaRepository<Permission, Long> {

    List<Permission> findByIdIn(List<Long> permissionIds);
}
