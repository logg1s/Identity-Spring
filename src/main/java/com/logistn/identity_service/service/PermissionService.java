package com.logistn.identity_service.service;

import com.logistn.identity_service.dto.request.PermissionRequest;
import com.logistn.identity_service.dto.response.PermissionResponse;
import com.logistn.identity_service.entity.Permission;
import com.logistn.identity_service.mapper.PermissionMapper;
import com.logistn.identity_service.repository.PermissionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

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
