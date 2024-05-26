package com.logistn.identity_service.mapper;

import com.logistn.identity_service.dto.request.UserCreationRequest;
import com.logistn.identity_service.dto.request.UserUpdateRequest;
import com.logistn.identity_service.dto.response.UserResponse;
import com.logistn.identity_service.entity.User;
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
