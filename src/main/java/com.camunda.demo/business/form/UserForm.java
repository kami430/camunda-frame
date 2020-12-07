package com.camunda.demo.business.form;

import com.camunda.demo.base.repository.BaseForm;
import com.camunda.demo.dataInterface.constant.EntityStatus;
import com.camunda.demo.dataInterface.entity.authorization.LoginUser;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Convert;

@Setter
@Getter
public class UserForm implements BaseForm<LoginUser> {
    String account;

    String name;

    String password;

    Integer page;

    Integer pageSize;

    @Convert(converter = EntityStatus.Convert.class)
    EntityStatus status;

    @Override
    public String toString() {
        return "UserForm{" +
                "account='" + account + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", page=" + page +
                ", pageSize=" + pageSize +
                ", status=" + status +
                '}';
    }
}
