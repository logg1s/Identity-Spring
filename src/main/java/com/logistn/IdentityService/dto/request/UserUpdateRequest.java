package com.logistn.IdentityService.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {
    @Size(min = 8, message = "PASSWORD_NOT_MET_REQUIRED")
    private String password;
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private Set<String> roles;
}
