package com.logistn.IdentityService.dto.request;

import com.logistn.IdentityService.validator.DobConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreationRequest {
    @Size(min = 3, message = "USERNAME_NOT_MET_REQUIRED")
    @NotNull
    private String username;
    @Size(min = 8, max = 32)
    @NotNull
    private String password;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;

    @DobConstraint(min = 18)
    @NotNull
    private LocalDate dob;
}
