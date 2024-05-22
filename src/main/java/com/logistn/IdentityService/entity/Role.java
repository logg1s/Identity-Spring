package com.logistn.IdentityService.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import java.util.Set;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {
    @Id
    private String name;

    private String description;

    @ManyToMany
    Set<Permission> permissions;
}
