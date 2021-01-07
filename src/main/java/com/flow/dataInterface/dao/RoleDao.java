package com.flow.dataInterface.dao;

import com.flow.base.repository.BaseJpaRepository;
import com.flow.dataInterface.entity.authorization.Role;

import java.util.List;

public interface RoleDao extends BaseJpaRepository<Role, Long> {
    List<Role> findByIdIn(List<Long> roleIds);
}
