package com.camunda.demo.dataInterface.entity.authorization;

import com.camunda.demo.dataInterface.constant.IEntityStatus;
import com.camunda.demo.base.constant.IConst;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * 用户表
 */
@Entity
@Table(name = "user", uniqueConstraints = @UniqueConstraint(columnNames = "account"))
@Data
public class LoginUser implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String account;

    private String name;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Role> roles;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Permission> permissions;

    //    @Convert(converter = EntityStatus.Convert.class)
    //    private EntityStatus status;
    @IConst(IEntityStatus.class)
    private Integer status;
}
