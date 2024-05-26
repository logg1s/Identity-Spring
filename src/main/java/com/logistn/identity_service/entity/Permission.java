package com.logistn.identity_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permission {
    @Id
    private String name;

    private String description;
}
