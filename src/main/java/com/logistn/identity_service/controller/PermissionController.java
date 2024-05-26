package com.logistn.identity_service.controller;

import com.logistn.identity_service.dto.request.PermissionRequest;
import com.logistn.identity_service.dto.response.ApiResponse;
import com.logistn.identity_service.service.PermissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/permissions")
public class PermissionController {
    private final PermissionService permissionService;

    @PostMapping
    public ApiResponse<Object> create(@RequestBody @Valid PermissionRequest request) {
        return ApiResponse.builder().result(permissionService.create(request)).build();
    }

    @GetMapping
    public ApiResponse<Object> getAll() {
        return ApiResponse.builder().result(permissionService.getAll()).build();
    }

    @DeleteMapping("{permissionId}")
    public ApiResponse<Object> delete(@PathVariable String permissionId) {
        permissionService.delete(permissionId);
        return ApiResponse.builder().message("Delete success").build();
    }
}
