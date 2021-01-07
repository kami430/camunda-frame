package com.flow.dataInterface.entity.authorization;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "permission", uniqueConstraints = @UniqueConstraint(columnNames = "code"))
@Data
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;

    private String name;

    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
    private List<LoginUser> loginUsers;

    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
    private List<Role> roles;
}
