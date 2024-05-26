package com.logistn.identity_service.mapper;

import com.logistn.identity_service.dto.request.RoleRequest;
import com.logistn.identity_service.dto.response.RoleResponse;
import com.logistn.identity_service.entity.Role;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);

    List<RoleResponse> toListRoleResponses(List<Role> roles);
}
