package com.logistn.IdentityService.controller;

import com.logistn.IdentityService.dto.request.PermissionRequest;
import com.logistn.IdentityService.dto.response.ApiResponse;
import com.logistn.IdentityService.service.PermissionService;
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
    public ApiResponse<Object> create(@RequestBody PermissionRequest request) {
        return ApiResponse.builder()
                .result(permissionService.create(request))
                .build();
    }

    @GetMapping
    public ApiResponse<Object> getAll() {
        return ApiResponse.builder()
                .result(permissionService.getAll())
                .build();
    }

    @DeleteMapping("{permissionId}")
    public ApiResponse<Object> delete(@PathVariable String permissionId) {
        permissionService.delete(permissionId);
        return ApiResponse.builder().message("Delete success").build();
    }
}
