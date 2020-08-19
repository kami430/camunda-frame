package com.camunda.demo.business.service;

import com.camunda.demo.business.DTO.UserDto;
import com.camunda.demo.dataInterface.entity.authorization.LoginCredential;
import com.camunda.demo.dataInterface.entity.authorization.LoginUser;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface UserService {
    @Transactional
    LoginUser newUser(UserDto userDto);

    @Transactional
    void deleteUser(String account);

    @Transactional
    void freezeUser(String account);

    @Transactional
    void defreezeUser(String account);

    LoginUser getLoginUserByAccount(String account);

    List<LoginUser> findPageUser(Map<String, Object> params, int page, int pageSize);

    List<LoginCredential> loginCredential();
}
