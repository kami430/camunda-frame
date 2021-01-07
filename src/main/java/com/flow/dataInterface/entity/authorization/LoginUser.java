package com.flow.dataInterface.entity.authorization;

import com.flow.dataInterface.constant.IEntityStatus;
import com.flow.base.constant.IConst;
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
    
    @IConst(IEntityStatus.class)
    private Integer status;
}
