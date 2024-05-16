package com.logistn.IdentityService.service;

import com.logistn.IdentityService.dto.request.PermissionRequest;
import com.logistn.IdentityService.dto.response.PermissionResponse;
import com.logistn.IdentityService.entity.Permission;
import com.logistn.IdentityService.mapper.PermissionMapper;
import com.logistn.IdentityService.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class PermissionService {
    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    public PermissionResponse create(PermissionRequest request) {
        Permission permission = permissionMapper.toPermission(request);
        permissionRepository.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    public List<PermissionResponse> getAll() {
        List<Permission> list = permissionRepository.findAll();
        return permissionMapper.toListPermissionResponses(list);
    }

    public void delete(String permissionId) {
        permissionRepository.deleteById(permissionId);
    }
}
