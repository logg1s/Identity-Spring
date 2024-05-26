package com.logistn.identity_service.service;

import com.logistn.identity_service.dto.request.UserCreationRequest;
import com.logistn.identity_service.dto.request.UserUpdateRequest;
import com.logistn.identity_service.dto.response.UserResponse;
import com.logistn.identity_service.entity.Role;
import com.logistn.identity_service.entity.User;
import com.logistn.identity_service.exception.AppException;
import com.logistn.identity_service.exception.ErrorMessage;
import com.logistn.identity_service.mapper.UserMapper;
import com.logistn.identity_service.repository.RoleRepository;
import com.logistn.identity_service.repository.UserRepository;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public UserResponse createUser(UserCreationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorMessage.USERNAME_EXISTED);
        }
        User user = userMapper.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Role userRole =
                roleRepository.findById("USER").orElseThrow(() -> new AppException(ErrorMessage.ROLE_NOT_FOUND));
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers() {
        return userMapper.toListUserResponse(userRepository.findAll());
    }

    @PostAuthorize("hasRole('ADMIN') || returnObject.username == authentication.name")
    public UserResponse getUser(String id) {
        return userMapper.toUserResponse(
                userRepository.findById(id).orElseThrow(() -> new AppException(ErrorMessage.USER_NOT_FOUND)));
    }

    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getMyInfo() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorMessage.USER_NOT_FOUND));
        return userMapper.toUserResponse(user);
    }

    public UserResponse updateUser(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new AppException(ErrorMessage.USER_NOT_FOUND));
        userMapper.updateUser(user, request);

        List<Role> roles = roleRepository.findAllById(request.getRoles());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(new HashSet<>(roles));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }
}
