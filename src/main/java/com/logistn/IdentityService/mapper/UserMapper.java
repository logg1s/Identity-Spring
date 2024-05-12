package com.logistn.IdentityService.mapper;

import com.logistn.IdentityService.dto.request.UserCreationRequest;
import com.logistn.IdentityService.dto.request.UserUpdateRequest;
import com.logistn.IdentityService.dto.response.UserResponse;
import com.logistn.IdentityService.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    User userCreationRequestToUser(UserCreationRequest userCreationRequest);

    void updateUserFromUserUpdateRequest(@MappingTarget User user, UserUpdateRequest userUpdateRequest);

    UserResponse userToUserResponse(User user);

    List<UserResponse> usersToUserResponses(List<User> users);

}
