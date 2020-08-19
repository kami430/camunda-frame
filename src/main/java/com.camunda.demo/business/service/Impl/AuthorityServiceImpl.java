package com.camunda.demo.business.service.Impl;

import com.camunda.demo.base.http.BusinessException;
import com.camunda.demo.business.service.AuthorityService;
import com.camunda.demo.dataInterface.dao.CamundaDao;
import com.camunda.demo.dataInterface.dao.PermissionDao;
import com.camunda.demo.dataInterface.dao.RoleDao;
import com.camunda.demo.dataInterface.dao.UserDao;
import com.camunda.demo.dataInterface.entity.authorization.LoginUser;
import com.camunda.demo.dataInterface.entity.authorization.Permission;
import com.camunda.demo.dataInterface.entity.authorization.Role;
import org.camunda.bpm.engine.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorityServiceImpl implements AuthorityService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;
    @Autowired
    private CamundaDao camundaDao;
    @Autowired
    private IdentityService identityService;

    /**
     * 保存角色
     *
     * @param role
     * @return
     */
    @Override
    @Transactional
    public Role saveRole(Role role) {
        Role result;
        if ((result = roleDao.save(role)) == null)
            throw new BusinessException(">>>>>>新增角色失败>>>>>>>>");
        camundaDao.newGroup(role.getCode());
        return result;
    }

    /**
     * 更新角色
     *
     * @param role
     * @return
     */
    @Override
    @Transactional
    public Boolean updateRole(Role role) {
        Role source = roleDao.findByKey(role.getId());
        if (source == null) throw new BusinessException(">>>>>>角色不存在>>>>>>");
        if (!source.getCode().equals(role.getCode())) throw new BusinessException(">>>>>>同步id不能修改>>>>>>");
        return roleDao.update(role, role.getId());
    }

    /**
     * 删除role并删除关联关系
     *
     * @param roleId
     */
    @Override
    @Transactional
    public void deleteRole(Long roleId) {
        Role role = roleDao.findById(roleId).orElse(null);
        if (role == null) throw new BusinessException(">>>>>>角色不存在>>>>>>");

        List<LoginUser> loginUsers = role.getLoginUsers();
        for (LoginUser user : loginUsers) {
            user.getRoles().remove(role);
            if (!userDao.update(user, user.getId())) throw new BusinessException(">>>>>>删除用户-角色关联失败>>>>>>");
        }
        role.setPermissions(null);
        if (!roleDao.update(role, roleId)) throw new BusinessException(">>>>>>删除权限-角色关联失败>>>>>>");
        if (!roleDao.deleteByKey(roleId)) throw new BusinessException(">>>>>>删除角色失败>>>>>>");
        camundaDao.deleteGroup(role.getCode());
    }

    /**
     * 保存权限
     *
     * @param permission
     * @return
     */
    @Override
    @Transactional
    public Permission savePermission(Permission permission) {
        Permission result;
        if ((result = permissionDao.save(permission)) == null)
            throw new BusinessException(">>>>>>新增权限失败>>>>>>>>");
        camundaDao.newTenant(result.getCode());
        return result;
    }

    /**
     * 更新权限
     *
     * @param permission
     * @return
     */
    @Override
    @Transactional
    public Boolean updatePermission(Permission permission) {
        Permission source = permissionDao.findByKey(permission.getId());
        if (source == null) throw new BusinessException(">>>>>>权限不存在>>>>>>");
        if (!source.getCode().equals(permission.getCode())) throw new BusinessException(">>>>>>同步id不能修改>>>>>>");
        return permissionDao.update(permission, permission.getId());
    }

    /**
     * 删除权限
     *
     * @param permissionId
     */
    @Override
    @Transactional
    public void deletePermission(Long permissionId) {
        Permission permission = permissionDao.findById(permissionId).orElse(null);
        if (permission == null) throw new BusinessException(">>>>>>权限不存在>>>>>>");
        List<LoginUser> loginUsers = permission.getLoginUsers();
        for (LoginUser user : loginUsers) {
            user.getRoles().remove(permission);
            if (!userDao.update(user, user.getId())) throw new BusinessException(">>>>>>删除权限-用户关联失败>>>>>>");
        }
        List<Role> roles = permission.getRoles();
        for (Role role : roles) {
            role.getPermissions().remove(permission);
            if (!roleDao.update(role, role.getId())) throw new BusinessException(">>>>>>删除权限-角色关联失败>>>>>>");
        }
        if (!permissionDao.deleteByKey(permissionId)) throw new BusinessException(">>>>>>删除权限失败>>>>>>");
        camundaDao.deleteTenant(permission.getCode());
    }

    /**
     * 授予用户角色
     *
     * @param userId
     * @param roleIds
     * @return
     */
    @Override
    @Transactional
    public LoginUser authUserRoles(Long userId, List<Long> roleIds) {
        LoginUser user = userDao.findByKey(userId);
        if (user == null) throw new BusinessException("用户不存在");
        List<Role> roles = roleDao.findByIdIn(roleIds);
        user.setRoles(roles);
        if (userDao.update(user, userId)) {
            List<String> roleCodes = roles.stream().map(Role::getCode).collect(Collectors.toList());
            camundaDao.userGroup(user.getAccount(), roleCodes);
            return user;
        }
        return null;
    }

    /**
     * 授予用户权限
     *
     * @param userId
     * @param permissionIds
     * @return
     */
    @Override
    @Transactional
    public LoginUser authUserPermissions(Long userId, List<Long> permissionIds) {
        LoginUser user = userDao.findByKey(userId);
        if (user == null) throw new BusinessException("用户不存在");
        List<Permission> permissions = permissionDao.findByIdIn(permissionIds);
        user.setPermissions(permissions);
        if (userDao.update(user, userId)) {
            List<String> permissionCodes = permissions.stream().map(Permission::getCode).collect(Collectors.toList());
            camundaDao.userTenant(user.getAccount(), permissionCodes);
            return user;
        }
        return null;
    }

    /**
     * 授角色权限
     *
     * @param userId
     * @param permissionIds
     * @return
     */
    @Override
    @Transactional
    public Role authRolePermissions(Long roleId, List<Long> permissionIds) {
        Role role = roleDao.findByKey(roleId);
        if (role == null) throw new BusinessException("角色不存在");
        List<Permission> permissions = permissionDao.findByIdIn(permissionIds);
        role.setPermissions(permissions);
        if (roleDao.update(role, roleId)) {
            List<String> permissionCodes = permissions.stream().map(Permission::getCode).collect(Collectors.toList());
            camundaDao.groupTenant(role.getCode(), permissionCodes);
            return role;
        }
        return null;
    }

    /**
     * 授角色用户
     *
     * @param userId
     * @param permissionIds
     * @return
     */
    @Override
    @Transactional
    public Role authRoleUsers(Long roleId, List<Long> userIds) {
        Role role = roleDao.findByKey(roleId);
        if (role == null) throw new BusinessException("角色不存在");
        List<LoginUser> users = userDao.findByIdIn(userIds);
        for (LoginUser user : users) {
            if (!user.getRoles().contains(role)) {
                user.getRoles().add(role);
                if (userDao.update(user, user.getId())) throw new BusinessException("用户授权失败");
            }
        }
        List<String> accounts = users.stream().map(LoginUser::getAccount).collect(Collectors.toList());
        camundaDao.groupUser(role.getCode(), accounts);
        return role;
    }
}
