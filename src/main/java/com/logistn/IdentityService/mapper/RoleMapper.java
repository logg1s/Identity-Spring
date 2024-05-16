package com.logistn.IdentityService.mapper;

import com.logistn.IdentityService.dto.request.RoleRequest;
import com.logistn.IdentityService.dto.response.RoleResponse;
import com.logistn.IdentityService.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);
    RoleResponse toRoleResponse(Role role);
    List<RoleResponse> toListRoleResponses(List<Role> roles);
}
