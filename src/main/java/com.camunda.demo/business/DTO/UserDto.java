package com.camunda.demo.business.DTO;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserDto {
    String account;

    String name;

    String password;

    Integer page;

    Integer pageSize;
}
