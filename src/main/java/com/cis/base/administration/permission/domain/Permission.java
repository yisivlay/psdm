package com.cis.base.administration.permission.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * @author YSivlay
 */
@Getter
@Setter
@Entity
@Table(name = "permissions")
public class Permission extends AbstractPersistable<Long> {

    @Column(name = "grouping", nullable = false, length = 45)
    private String grouping;

    @Column(name = "code", nullable = false, length = 100)
    private String code;

    @Column(name = "entity_name", nullable = true, length = 100)
    private String entityName;

    @Column(name = "action_name", nullable = true, length = 100)
    private String actionName;

    @Column(name = "can_maker_checker", nullable = false)
    private boolean canMakerChecker;

    @Column(name = "is_visible", nullable = false)
    private boolean isVisible;

}
