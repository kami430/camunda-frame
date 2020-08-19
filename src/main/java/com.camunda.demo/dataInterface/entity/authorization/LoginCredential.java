package com.camunda.demo.dataInterface.entity.authorization;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 密码表
 */
@Entity
@Table(name = "credential")
@Data
public class LoginCredential implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String password;

    private String salt;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private LoginUser loginUser;
}
