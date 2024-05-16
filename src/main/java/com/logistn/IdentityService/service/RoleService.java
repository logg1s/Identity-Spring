package com.logistn.IdentityService.service;

import com.logistn.IdentityService.dto.request.RoleRequest;
import com.logistn.IdentityService.dto.response.RoleResponse;
import com.logistn.IdentityService.entity.Permission;
import com.logistn.IdentityService.entity.Role;
import com.logistn.IdentityService.mapper.RoleMapper;
import com.logistn.IdentityService.repository.PermissionRepository;
import com.logistn.IdentityService.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final PermissionRepository permissionRepository;

    public RoleResponse create(RoleRequest request) {
        List<Permission> permissions = permissionRepository.findAllById(request.getPermissions());
        Role role = roleMapper.toRole(request);
        role.setPermissions(new HashSet<>(permissions));
        return roleMapper.toRoleResponse(roleRepository.save(role));
    }

    public List<RoleResponse> getAll() {
        return roleMapper.toListRoleResponses(roleRepository.findAll());

//        List<Role> roleList = roleRepository.findAll();
//        return roleList.stream().map(role -> {
//            Set<PermissionResponse> permissionResponses = role.getPermissions()
//                    .stream()
//                    .map(permission -> new PermissionResponse(
//                            permission.getName(), permission.getDescription()))
//                    .collect(Collectors.toSet());
//            return new RoleResponse(role.getName(), role.getDescription(), permissionResponses);
//        }).toList();
    }

    public void delete(String roleId) {
        roleRepository.deleteById(roleId);
    }
}
