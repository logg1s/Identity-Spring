package com.logistn.identity_service.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Set;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private LocalDate dob;

    @ManyToMany
    private Set<Role> roles;
}
