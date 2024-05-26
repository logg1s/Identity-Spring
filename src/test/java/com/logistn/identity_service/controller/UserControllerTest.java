package com.logistn.identity_service.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.logistn.identity_service.dto.request.UserCreationRequest;
import com.logistn.identity_service.dto.response.UserResponse;
import com.logistn.identity_service.service.UserService;
import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private UserCreationRequest request;

    private UserResponse response;

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

        response = UserResponse.builder()
                .id("asdf")
                .username("john")
                .firstName("john")
                .lastName("doe")
                .dob(date)
                .build();
    }

    @Test
    void createUser_withValidRequest_returnSuccess() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        String content = objectMapper.writeValueAsString(request);
        when(userService.createUser(any())).thenReturn(response);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("code").value(1000))
                .andExpect(jsonPath("result.id").value("asdf"));
    }

    @Test
    void createUser_withInvalidUsernameRequest_returnFail() throws Exception {
        request.setUsername("111");
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        String content = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("code").value(9999))
                .andExpect(jsonPath("message").value("Validate Exception"))
                .andExpect(jsonPath("result.username").value("User not meet required"));
    }
}
