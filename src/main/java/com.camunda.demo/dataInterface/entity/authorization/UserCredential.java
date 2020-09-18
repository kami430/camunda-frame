package com.camunda.demo.dataInterface.entity.authorization;

import com.camunda.demo.base.shiro.config.ShiroUser;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 密码表
 */
@Entity
@Table(name = "credential")
@Data
public class UserCredential implements ShiroUser, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String password;

    private String salt;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private LoginUser loginUser;
}
