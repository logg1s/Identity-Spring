package com.logistn.IdentityService.service;

import com.logistn.IdentityService.dto.request.UserCreationRequest;
import com.logistn.IdentityService.dto.request.UserUpdateRequest;
import com.logistn.IdentityService.dto.response.UserResponse;
import com.logistn.IdentityService.entity.User;
import com.logistn.IdentityService.exception.AppException;
import com.logistn.IdentityService.exception.ErrorMessage;
import com.logistn.IdentityService.mapper.UserMapper;
import com.logistn.IdentityService.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponse createUser(UserCreationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorMessage.USERNAME_EXISTED);
        }

        User user = userMapper.userCreationRequestToUser(request);
        return userMapper.userToUserResponse(userRepository.save(user));
    }

    public List<UserResponse> getUsers() {
        return userMapper.usersToUserResponses(userRepository.findAll());
    }

    public UserResponse getUser(String id) {
        return userMapper.userToUserResponse(userRepository.findById(id).orElseThrow(() -> new AppException(ErrorMessage.USER_NOT_FOUND)));
    }

    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorMessage.USER_NOT_FOUND));
        userMapper.updateUserFromUserUpdateRequest(user, request);
        return userMapper.userToUserResponse(userRepository.save(user));
    }

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }
}
