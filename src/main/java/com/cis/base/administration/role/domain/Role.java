package com.cis.base.administration.role.domain;

import com.cis.base.administration.permission.domain.Permission;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.util.HashSet;
import java.util.Set;

/**
 * @author YSivlay
 */
@Getter
@Setter
@Entity
@Table(name = "roles", uniqueConstraints = {@UniqueConstraint(columnNames = {"name"}, name = "unq_name")})
public class Role extends AbstractPersistable<Long> {

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "role_permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private final Set<Permission> permissions = new HashSet<>();

    @Column(name = "name", unique = true, nullable = false, length = 100)
    private String name;

    @Column(name = "description", nullable = false, length = 500)
    private String description;

    @Column(name = "role_code", nullable = true)
    private String roleCode;

    @Column(name = "is_disabled", nullable = false)
    private Boolean disabled;

    @Column(name = "is_for_sign_up_process", nullable = false)
    private Boolean is_for_sign_up_process;

}
