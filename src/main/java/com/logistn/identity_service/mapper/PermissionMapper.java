package com.logistn.identity_service.mapper;

import com.logistn.identity_service.dto.request.PermissionRequest;
import com.logistn.identity_service.dto.response.PermissionResponse;
import com.logistn.identity_service.entity.Permission;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);

    List<PermissionResponse> toListPermissionResponses(List<Permission> listPermissions);
}
