package com.logistn.IdentityService.mapper;

import com.logistn.IdentityService.dto.request.PermissionRequest;
import com.logistn.IdentityService.dto.response.PermissionResponse;
import com.logistn.IdentityService.entity.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);

    List<PermissionResponse> toListPermissionResponses(List<Permission> listPermissions);
}