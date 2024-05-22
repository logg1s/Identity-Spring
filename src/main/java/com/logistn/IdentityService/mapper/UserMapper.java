package com.logistn.IdentityService.mapper;

import com.logistn.IdentityService.dto.request.UserCreationRequest;
import com.logistn.IdentityService.dto.request.UserUpdateRequest;
import com.logistn.IdentityService.dto.response.UserResponse;
import com.logistn.IdentityService.entity.User;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    @Mapping(target = "roles", ignore = true)
    User toUser(UserCreationRequest userCreationRequest);

    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest userUpdateRequest);

    List<UserResponse> toListUserResponse(List<User> users);
}
