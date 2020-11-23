package com.camunda.demo.business.service;

import com.camunda.demo.business.form.UserForm;
import com.camunda.demo.dataInterface.entity.authorization.LoginUser;
import com.camunda.demo.dataInterface.entity.authorization.UserCredential;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface UserService {
    @Transactional
    LoginUser newUser(UserForm userForm);

    @Transactional
    void deleteUser(String account);

    @Transactional
    void freezeUser(String account);

    @Transactional
    void defreezeUser(String account);

    LoginUser getLoginUserByAccount(String account);

    List<LoginUser> findPageUser(Map<String, Object> params, int page, int pageSize);

    List<UserCredential> loginCredential();
}
