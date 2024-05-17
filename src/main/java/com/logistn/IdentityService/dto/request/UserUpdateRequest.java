package com.logistn.IdentityService.dto.request;

import com.logistn.IdentityService.validator.DobConstraint;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    private String password;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;

    @DobConstraint(min = 18, message = "INVALID_DOB")
    @NotNull
    private LocalDate dob;

    private Set<String> roles;
}
