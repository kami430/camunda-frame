package com.flow.business.form;

import com.flow.base.repository.BaseForm;
import com.flow.dataInterface.constant.IEntityStatus;
import com.flow.base.constant.IConst;
import com.flow.dataInterface.entity.authorization.LoginUser;
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
