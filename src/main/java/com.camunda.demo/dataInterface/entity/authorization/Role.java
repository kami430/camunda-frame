package com.camunda.demo.dataInterface.entity.authorization;

import lombok.Data;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "role", uniqueConstraints = @UniqueConstraint(columnNames = "code"))
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String name;
    @ManyToMany(mappedBy = "roles",fetch = FetchType.LAZY)
    private List<LoginUser> loginUsers;
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Permission> permissions;
}
