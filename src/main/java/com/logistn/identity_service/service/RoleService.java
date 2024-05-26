package com.logistn.identity_service.service;

import com.logistn.identity_service.dto.request.RoleRequest;
import com.logistn.identity_service.dto.response.RoleResponse;
import com.logistn.identity_service.entity.Permission;
import com.logistn.identity_service.entity.Role;
import com.logistn.identity_service.mapper.RoleMapper;
import com.logistn.identity_service.repository.PermissionRepository;
import com.logistn.identity_service.repository.RoleRepository;
import java.util.HashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final PermissionRepository permissionRepository;
    private final AuthenticationService authenticationService;

    public RoleResponse create(RoleRequest request) {
        List<Permission> permissions = permissionRepository.findAllById(request.getPermissions());
        Role role = roleMapper.toRole(request);
        role.setPermissions(new HashSet<>(permissions));
        return roleMapper.toRoleResponse(roleRepository.save(role));
    }

    public List<RoleResponse> getAll() {
        return roleMapper.toListRoleResponses(roleRepository.findAll());
    }

    public void delete(String roleId) {
        roleRepository.deleteById(roleId);
    }
}
