package com.logistn.IdentityService.controller;

import com.logistn.IdentityService.dto.request.UserCreationRequest;
import com.logistn.IdentityService.dto.request.UserUpdateRequest;
import com.logistn.IdentityService.dto.response.ApiResponse;
import com.logistn.IdentityService.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true)
public class UserController {
    private final UserService userService;

    @GetMapping
    ApiResponse<Object> getUsers() {
        ApiResponse<Object> userApiResponse = new ApiResponse<>();
        userApiResponse.setResult(userService.getUsers());
        return userApiResponse;
    }

    @GetMapping("/{userId}")
    ApiResponse<Object> getUser(@PathVariable String userId) {
        ApiResponse<Object> userApiResponse = new ApiResponse<>();
        userApiResponse.setResult(userService.getUser(userId));
        return userApiResponse;
    }

    @GetMapping("/me")
    ApiResponse<Object> getMyInfo() {
        ApiResponse<Object> userApiResponse = new ApiResponse<>();
        userApiResponse.setResult(userService.getMyInfo());
        return userApiResponse;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ApiResponse<Object> createUser(@RequestBody @Valid UserCreationRequest request) {
        ApiResponse<Object> userApiResponse = new ApiResponse<>();
        userApiResponse.setResult(userService.createUser(request));
        return userApiResponse;
    }

    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    ApiResponse<Object> updateUser(@PathVariable String userId, @RequestBody @Valid UserUpdateRequest request) {
        ApiResponse<Object> userApiResponse = new ApiResponse<>();
        userApiResponse.setResult(userService.updateUser(userId, request));
        return userApiResponse;
    }

    @DeleteMapping("/{userId}")
    ApiResponse<Object> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);

        ApiResponse<Object> userApiResponse = new ApiResponse<>();
        userApiResponse.setResult("User has been delete");
        return userApiResponse;
    }
}
