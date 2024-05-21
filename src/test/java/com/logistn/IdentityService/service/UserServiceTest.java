package com.logistn.IdentityService.service;

import com.logistn.IdentityService.dto.request.UserCreationRequest;
import com.logistn.IdentityService.dto.response.UserResponse;
import com.logistn.IdentityService.entity.Permission;
import com.logistn.IdentityService.entity.Role;
import com.logistn.IdentityService.entity.User;
import com.logistn.IdentityService.repository.RoleRepository;
import com.logistn.IdentityService.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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

        permissions = Permission.builder()
                .name("abcd")
                .description("abcd permission")
                .build();


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

        response = UserResponse.builder()
                .id("asdf")
                .username("john")
                .firstName("john")
                .lastName("doe")
                .dob(date)
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
                () -> assertEquals(roles.getName(), response.getRoles().stream().toList().getFirst().getName(), "Role don't match"),
                () -> assertTrue(passwordEncoder.matches(request.getPassword(), userCaptor.getValue().getPassword()), "Password don't match")
        );
    }

//    @Test
//    void createUser_withInvalidUsernameRequest_returnFail() throws Exception {
//        request.setUsername("111");
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.findAndRegisterModules();
//        String content = objectMapper.writeValueAsString(request);
//
//        mockMvc.perform(
//                        post("/users")
//                                .contentType(MediaType.APPLICATION_JSON_VALUE)
//                                .content(content))
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("code").value(9999))
//                .andExpect(jsonPath("message").value("Validate Exception"))
//                .andExpect(jsonPath("result.username").value("User not meet required"))
//        ;
//    }
}
