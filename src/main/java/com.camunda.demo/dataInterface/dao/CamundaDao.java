package com.camunda.demo.dataInterface.dao;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.Tenant;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CamundaDao {

    @Autowired
    private IdentityService idService;

    /**
     * 新增用户
     *
     * @param userId
     */
    public void newUser(String userId) {
        if (idService.createUserQuery().userId(userId).singleResult() != null) {
            idService.saveUser(idService.newUser(userId));
        }
    }

    /**
     * 新增组/角色
     *
     * @param groupId
     */
    public void newGroup(String groupId) {
        if (idService.createGroupQuery().groupId(groupId).singleResult() != null) {
            idService.saveGroup(idService.newGroup(groupId));
        }
    }

    /**
     * 新增租户/权限
     *
     * @param tenantId
     */
    public void newTenant(String tenantId) {
        if (idService.createTenantQuery().tenantId(tenantId).singleResult() != null) {
            idService.saveTenant(idService.newTenant(tenantId));
        }
    }

    /**
     * 删除用户
     *
     * @param userId
     */
    public void deleteUser(String userId) {
        if (idService.createUserQuery().userId(userId).singleResult() != null) {
            // 删除组/角色关联
            List<Group> relateGroup = idService.createGroupQuery().groupMember(userId).list();
            for (Group group : relateGroup) idService.deleteMembership(userId, group.getId());
            // 删除租户/权限关联
            List<Tenant> relateTenant = idService.createTenantQuery().userMember(userId).list();
            for (Tenant tenant : relateTenant) idService.deleteTenantUserMembership(tenant.getId(), userId);
            // 删除用户
            idService.deleteUser(userId);
        }
    }

    /**
     * 删除组/角色
     *
     * @param groupId
     */
    public void deleteGroup(String groupId) {
        if (idService.createGroupQuery().groupId(groupId).singleResult() != null) {
            // 删除用户关联
            List<User> relateUser = idService.createUserQuery().memberOfGroup(groupId).list();
            for (User user : relateUser) idService.deleteMembership(user.getId(), groupId);
            // 删除租户/权限关联
            List<Tenant> relateTenant = idService.createTenantQuery().groupMember(groupId).list();
            for (Tenant tenant : relateTenant) idService.deleteTenantGroupMembership(tenant.getId(), groupId);
            // 删除组/角色
            idService.deleteGroup(groupId);
        }
    }

    /**
     * 删除租户/权限
     *
     * @param tenantId
     */
    public void deleteTenant(String tenantId) {
        if (idService.createTenantQuery().tenantId(tenantId).singleResult() != null) {
            // 删除用户关联
            List<User> relateUser = idService.createUserQuery().memberOfTenant(tenantId).list();
            for (User user : relateUser) idService.deleteMembership(user.getId(), tenantId);
            // 删除组/角色关联
            List<Group> relateGroup = idService.createGroupQuery().groupMember(tenantId).list();
            for (Group group : relateGroup) idService.deleteTenantGroupMembership(group.getId(), tenantId);
            // 删除租户/权限
            idService.deleteGroup(tenantId);
        }
    }

    /**
     * 用户添加(组/角色)
     *
     * @param userId
     * @param groupIds
     */
    public void userGroup(String userId, List<String> groupIds) {
        if (idService.createUserQuery().userId(userId).singleResult() != null) {
            List<String> groupSource = idService.createGroupQuery().groupMember(userId).list().stream().map(Group::getId).collect(Collectors.toList());
            // 新增
            List<String> memAdd = groupIds.stream().filter(id -> !groupSource.contains(id)).collect(Collectors.toList());
            for (String id : memAdd) idService.createMembership(userId, id);
            // 删除
            List<String> memDel = groupSource.stream().filter(id -> !groupIds.contains(id)).collect(Collectors.toList());
            for (String id : memDel) idService.deleteMembership(userId, id);
        }
    }

    /**
     * (组/角色)添加用户
     *
     * @param groupId
     * @param userIds
     */
    public void groupUser(String groupId, List<String> userIds) {
        if (idService.createGroupQuery().groupId(groupId).singleResult() != null) {
            List<String> userSource = idService.createUserQuery().memberOfGroup(groupId).list().stream().map(User::getId).collect(Collectors.toList());
            // 新增
            List<String> memAdd = userIds.stream().filter(id -> !userSource.contains(id)).collect(Collectors.toList());
            for (String id : memAdd) idService.createMembership(id, groupId);
            // 删除
            List<String> memDel = userSource.stream().filter(id -> !userIds.contains(id)).collect(Collectors.toList());
            for (String id : memDel) idService.deleteMembership(id, groupId);
        }
    }

    /**
     * 用户添加(租户/权限)
     *
     * @param userId
     * @param tenantIds
     */
    public void userTenant(String userId, List<String> tenantIds) {
        if (idService.createUserQuery().userId(userId).singleResult() != null) {
            List<String> tenantSource = idService.createTenantQuery().userMember(userId).list().stream().map(Tenant::getId).collect(Collectors.toList());
            // 新增
            List<String> memAdd = tenantIds.stream().filter(id -> !tenantSource.contains(id)).collect(Collectors.toList());
            for (String id : memAdd) idService.createTenantUserMembership(id, userId);
            // 删除
            List<String> memDel = tenantSource.stream().filter(id -> !tenantIds.contains(id)).collect(Collectors.toList());
            for (String id : memDel) idService.deleteTenantUserMembership(id, userId);
        }
    }

    /**
     * (租户/权限添加用户)
     *
     * @param tenantId
     * @param userIds
     */
    public void tenantUser(String tenantId, List<String> userIds) {
        if (idService.createTenantQuery().tenantId(tenantId).singleResult() != null) {
            List<String> userSource = idService.createUserQuery().memberOfTenant(tenantId).list().stream().map(User::getId).collect(Collectors.toList());
            // 新增
            List<String> memAdd = userIds.stream().filter(id -> !userSource.contains(id)).collect(Collectors.toList());
            for (String id : memAdd) idService.createTenantUserMembership(tenantId, id);
            // 删除
            List<String> memDel = userSource.stream().filter(id -> !userIds.contains(id)).collect(Collectors.toList());
            for (String id : memDel) idService.deleteTenantUserMembership(tenantId, id);
        }
    }

    /**
     * (组/角色)添加(租户/权限)
     *
     * @param groupId
     * @param tenantIds
     */
    public void groupTenant(String groupId, List<String> tenantIds) {
        if (idService.createGroupQuery().groupId(groupId).singleResult() != null) {
            List<String> tenantSource = idService.createTenantQuery().groupMember(groupId).list().stream().map(Tenant::getId).collect(Collectors.toList());
            // 新增
            List<String> memAdd = tenantIds.stream().filter(id -> !tenantSource.contains(id)).collect(Collectors.toList());
            for (String id : memAdd) idService.createTenantGroupMembership(id, groupId);
            // 删除
            List<String> memDel = tenantSource.stream().filter(id -> !tenantIds.contains(id)).collect(Collectors.toList());
            for (String id : memDel) idService.deleteTenantGroupMembership(id, groupId);
        }
    }

    /**
     * (租户/权限)添加(组/角色)
     *
     * @param tenantId
     * @param groupIds
     */
    public void tenantGroup(String tenantId, List<String> groupIds) {
        if (idService.createTenantQuery().tenantId(tenantId).singleResult() != null) {
            List<String> groupSource = idService.createGroupQuery().memberOfTenant(tenantId).list().stream().map(Group::getId).collect(Collectors.toList());
            // 新增
            List<String> memAdd = groupIds.stream().filter(id -> !groupSource.contains(id)).collect(Collectors.toList());
            for (String id : memAdd) idService.createTenantGroupMembership(tenantId, id);
            // 删除
            List<String> memDel = groupSource.stream().filter(id -> !groupIds.contains(id)).collect(Collectors.toList());
            for (String id : memDel) idService.deleteTenantGroupMembership(tenantId, id);
        }
    }
}
