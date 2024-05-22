package com.logistn.IdentityService.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.logistn.IdentityService.dto.request.UserCreationRequest;
import com.logistn.IdentityService.dto.response.PermissionResponse;
import com.logistn.IdentityService.dto.response.RoleResponse;
import com.logistn.IdentityService.dto.response.UserResponse;
import com.logistn.IdentityService.entity.Permission;
import com.logistn.IdentityService.entity.Role;
import com.logistn.IdentityService.entity.User;
import com.logistn.IdentityService.exception.AppException;
import com.logistn.IdentityService.exception.ErrorMessage;
import com.logistn.IdentityService.mapper.RoleMapper;
import com.logistn.IdentityService.repository.RoleRepository;
import com.logistn.IdentityService.repository.UserRepository;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {
    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @Autowired
    private RoleMapper roleMapper;

    private UserCreationRequest request;

    private UserResponse response;

    private User user;

    private Role roles;

    private Permission permissions;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void initData() {
        LocalDate date = LocalDate.of(2000, 1, 1);
        request = UserCreationRequest.builder()
                .username("john")
                .firstName("john")
                .lastName("doe")
                .password("123123123")
                .dob(date)
                .build();

        permissions =
                Permission.builder().name("abcd").description("abcd permission").build();

        roles = Role.builder()
                .name("USER")
                .description("User role")
                .permissions(new HashSet<>(Set.of(permissions)))
                .build();

        user = User.builder()
                .id("asdf")
                .username("john")
                .firstName("john")
                .lastName("doe")
                .password(passwordEncoder.encode("12345678"))
                .dob(date)
                .roles(new HashSet<>(Set.of(roles)))
                .build();

        RoleResponse roleResponse = new RoleResponse(
                roles.getName(),
                roles.getDescription(),
                roles.getPermissions().stream()
                        .map(permission -> new PermissionResponse(permission.getName(), permission.getDescription()))
                        .collect(Collectors.toSet()));
        response = UserResponse.builder()
                .id("asdf")
                .username("john")
                .firstName("john")
                .lastName("doe")
                .dob(date)
                .roles(new HashSet<>(Set.of(roleResponse)))
                .build();
    }

    @Test
    void createUser_withValidRequest_returnSuccess() {
        when(userRepository.existsByUsername(any())).thenReturn(false);
        when(roleRepository.findById(any())).thenReturn(Optional.of(roles));
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        when(userRepository.save(userCaptor.capture())).thenAnswer(invocation -> invocation.getArgument(0));

        response = userService.createUser(request);

        assertAll(
                () -> assertEquals(request.getUsername(), response.getUsername(), "Username don't match"),
                () -> assertEquals(request.getFirstName(), response.getFirstName(), "First name don't match"),
                () -> assertEquals(request.getLastName(), response.getLastName(), "Last name don't match"),
                () -> assertEquals(request.getDob(), response.getDob(), "Date of birth don't match"),
                () -> assertEquals(
                        roles.getName(),
                        response.getRoles().stream().toList().getFirst().getName(),
                        "Role don't match"),
                () -> assertTrue(
                        passwordEncoder.matches(
                                request.getPassword(), userCaptor.getValue().getPassword()),
                        "Password don't match"));
    }

    @Test
    void createUser_validRequest_success() {
        when(userRepository.existsByUsername(any())).thenReturn(false);
        when(roleRepository.findById(any())).thenReturn(Optional.of(roles));
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        when(userRepository.save(userCaptor.capture())).thenAnswer(invocation -> invocation.getArgument(0));

        response = userService.createUser(request);

        assertAll(
                () -> assertEquals(request.getUsername(), response.getUsername(), "Username don't match"),
                () -> assertEquals(request.getFirstName(), response.getFirstName(), "First name don't match"),
                () -> assertEquals(request.getLastName(), response.getLastName(), "Last name don't match"),
                () -> assertEquals(request.getDob(), response.getDob(), "Date of birth don't match"),
                () -> assertEquals(
                        roles.getName(),
                        response.getRoles().stream().toList().getFirst().getName(),
                        "Role don't match"),
                () -> assertTrue(
                        passwordEncoder.matches(
                                request.getPassword(), userCaptor.getValue().getPassword()),
                        "Password don't match"));
    }

    @Test
    void createUser_existUserRequest_error() {
        when(userRepository.existsByUsername(any())).thenReturn(true);

        AppException exception = assertThrows(AppException.class, () -> userService.createUser(request));

        assertAll(
                () -> assertEquals(
                        ErrorMessage.USERNAME_EXISTED.getCode(),
                        exception.getErrorMessage().getCode()),
                () -> assertEquals(ErrorMessage.USERNAME_EXISTED.getMessage(), exception.getMessage()));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void getUsers_validRole_success() {
        when(userRepository.findAll()).thenReturn(new ArrayList<>(List.of(user)));
        var exec = userService.getUsers();
        assertThat(Objects.equals(exec.getFirst(), response)).isTrue();
    }

    @Test
    @WithMockUser(roles = {"asdfkjlasdf"})
    void getUsers_invalidRole_error() {
        assertThrows(AccessDeniedException.class, () -> userService.getUsers());
    }
}
