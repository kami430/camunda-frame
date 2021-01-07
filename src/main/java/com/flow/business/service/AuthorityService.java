package com.flow.business.service;

import com.flow.dataInterface.entity.authorization.LoginUser;
import com.flow.dataInterface.entity.authorization.Permission;
import com.flow.dataInterface.entity.authorization.Role;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AuthorityService {
    @Transactional
    Role saveRole(Role role);

    @Transactional
    Boolean updateRole(Role role);

    @Transactional
    void deleteRole(Long roleId);

    @Transactional
    Permission savePermission(Permission permission);

    @Transactional
    Boolean updatePermission(Permission permission);

    @Transactional
    void deletePermission(Long permissionId);

    @Transactional
    LoginUser authUserRoles(Long userId, List<Long> roleIds);

    @Transactional
    LoginUser authUserPermissions(Long userId, List<Long> permissionIds);

    @Transactional
    Role authRolePermissions(Long roleId, List<Long> permissionIds);

    @Transactional
    Role authRoleUsers(Long roleId, List<Long> userIds);
}
