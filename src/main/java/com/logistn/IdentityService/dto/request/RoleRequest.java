package com.logistn.IdentityService.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleRequest {
    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    Set<String> permissions;
}
