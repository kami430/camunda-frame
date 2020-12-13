package com.camunda.demo.business.form;

import com.camunda.demo.base.repository.BaseForm;
import com.camunda.demo.dataInterface.constant.IEntityStatus;
import com.camunda.demo.base.constant.IConst;
import com.camunda.demo.dataInterface.entity.authorization.LoginUser;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserForm implements BaseForm<LoginUser> {
    String account;

    String name;

    String password;

    @IConst(IEntityStatus.class)
    Integer status;

    @Override
    public String toString() {
        return "UserForm{" +
                "account='" + account + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", status=" + status +
                '}';
    }
}
