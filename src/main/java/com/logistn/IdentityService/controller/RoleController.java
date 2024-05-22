package com.logistn.IdentityService.controller;

import com.logistn.IdentityService.dto.request.RoleRequest;
import com.logistn.IdentityService.dto.response.ApiResponse;
import com.logistn.IdentityService.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/roles")
public class RoleController {
    private final RoleService roleService;

    @PostMapping
    public ApiResponse<Object> create(@RequestBody @Valid RoleRequest request) {
        return ApiResponse.builder().result(roleService.create(request)).build();
    }

    @GetMapping
    public ApiResponse<Object> getAll() {
        return ApiResponse.builder().result(roleService.getAll()).build();
    }

    @DeleteMapping("{roleId}")
    public ApiResponse<Object> delete(@PathVariable String roleId) {
        roleService.delete(roleId);
        return ApiResponse.builder().message("Delete role successfully").build();
    }
}
