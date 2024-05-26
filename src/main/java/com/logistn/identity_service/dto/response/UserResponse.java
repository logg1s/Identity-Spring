package com.logistn.identity_service.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDate;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {
    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private Set<RoleResponse> roles;
}
