package com.camunda.demo.business.service.Impl;

import com.camunda.demo.base.http.BusinessException;
import com.camunda.demo.base.repository.Pager;
import com.camunda.demo.base.utils.EncryptUtils;
import com.camunda.demo.business.DTO.UserDto;
import com.camunda.demo.business.service.UserService;
import com.camunda.demo.dataInterface.constant.EntityStatus;
import com.camunda.demo.dataInterface.dao.CamundaDao;
import com.camunda.demo.dataInterface.dao.CredientialDao;
import com.camunda.demo.dataInterface.dao.UserDao;
import com.camunda.demo.dataInterface.entity.authorization.UserCredential;
import com.camunda.demo.dataInterface.entity.authorization.LoginUser;
import com.camunda.demo.dataInterface.entity.authorization.Permission;
import com.camunda.demo.dataInterface.entity.authorization.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private CredientialDao credientialDao;
    @Autowired
    private CamundaDao camundaDao;

    @Override
    @Transactional
    public LoginUser newUser(UserDto userDto) {
        UserCredential credential = credientialDao.getByLoginUserAccount(userDto.getAccount());
        if (credential != null && credential.getLoginUser().getStatus() == EntityStatus.FREEZE)
            throw new BusinessException("新增用户失败,该账户已存在!");
        String salt = EncryptUtils.randomUUID();
        LoginUser loginUser = new LoginUser();
        loginUser.setAccount(userDto.getAccount());
        loginUser.setName(userDto.getName());
        loginUser.setStatus(EntityStatus.ACTIVE);
        UserCredential loginCredential = new UserCredential();
        loginCredential.setSalt(salt);
        loginCredential.setPassword(EncryptUtils.simpleHash(userDto.getPassword(), salt, 2));
        loginCredential.setLoginUser(loginUser);
        if (credential != null && credential.getLoginUser().getStatus() == EntityStatus.INVALID) {
            loginCredential.setId(credential.getId());
            loginUser.setId(credential.getLoginUser().getId());
        }
        credientialDao.save(loginCredential);
        camundaDao.newUser(userDto.getAccount());
        return loginCredential.getLoginUser();
    }

    /**
     * 删除账户
     *
     * @param account
     */
    @Override
    @Transactional
    public void deleteUser(String account) {
        LoginUser user = userDao.findByAccount(account);
        if (user == null) throw new BusinessException("删除用户失败,该用户不存在!");
        user.setPermissions(Collections.EMPTY_LIST);
        user.setRoles(Collections.EMPTY_LIST);
        user.setStatus(EntityStatus.INVALID);
        if (userDao.save(user) == null) throw new BusinessException("删除用户失败,请联系管理员");
        // 删除流程用户与权限
        camundaDao.deleteUser(account);
    }

    /**
     * 冻结账户
     *
     * @param account
     */
    @Override
    @Transactional
    public void freezeUser(String account) {
        LoginUser user = userDao.findByAccount(account);
        if (user == null) throw new BusinessException("冻结用户失败,该用户不存在!");
        user.setStatus(EntityStatus.FREEZE);
        if (userDao.save(user) == null) throw new BusinessException("冻结用户失败,请联系管理员");
        // 删除流程用户与权限
        camundaDao.deleteUser(account);
    }

    /**
     * 解冻账户
     *
     * @param account
     * @return
     */
    @Override
    @Transactional
    public void defreezeUser(String account) {
        LoginUser user = userDao.findByAccount(account);
        if (user == null) throw new BusinessException("解冻结用户失败,该用户不存在!");
        user.setStatus(EntityStatus.ACTIVE);
        if (userDao.save(user) == null) throw new BusinessException("冻结用户失败,请联系管理员");
        // 恢复流程用户与权限
        List<String> roleCodes = user.getRoles().stream().map(Role::getCode).collect(Collectors.toList());
        List<String> permissionCodes = user.getPermissions().stream().map(Permission::getCode).collect(Collectors.toList());
        camundaDao.newUser(account);
        camundaDao.userGroup(account, roleCodes);
        camundaDao.userTenant(account, permissionCodes);
    }

    @Override
    public LoginUser getLoginUserByAccount(String account) {
        return userDao.findByAccount(account);
    }

    @Override
    public List<LoginUser> findPageUser(Map<String,Object> params, int page,int pageSize) {
        Pager pager = Pager.of(page,pageSize);
        return userDao.findPageByMoreField(params,pager);
    }

    @Override
    public List<UserCredential> loginCredential() {
        return credientialDao.getCrediential();
    }
}
