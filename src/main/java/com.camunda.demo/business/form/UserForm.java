package com.camunda.demo.business.form;

import com.camunda.demo.base.repository.BaseForm;
import com.camunda.demo.dataInterface.entity.authorization.LoginUser;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserForm implements BaseForm<LoginUser> {
    String account;

    String name;

    String password;

    Integer page;

    Integer pageSize;

    @Override
    public LoginUser buildEntity() {
        LoginUser user = new LoginUser();
        copyProperties(this, user);
        return user;
    }
}
