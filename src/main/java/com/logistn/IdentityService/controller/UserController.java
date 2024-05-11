package com.logistn.IdentityService.controller;

import com.logistn.IdentityService.dto.request.UserCreationRequest;
import com.logistn.IdentityService.dto.request.UserUpdateRequest;
import com.logistn.IdentityService.dto.response.ApiResponse;
import com.logistn.IdentityService.entity.User;
import com.logistn.IdentityService.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    ResponseEntity<Object> getUsers() {
        ApiResponse<List<User>> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.getUsers());
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @GetMapping("/{userId}")
    ResponseEntity<Object> getUser(@PathVariable String userId) {
        ApiResponse<User> apiResponse = new ApiResponse<>();
        apiResponse.setResult(userService.getUser(userId));
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @PostMapping
    ResponseEntity<Object> createUser(@RequestBody @Valid UserCreationRequest request) {
        ApiResponse<User> userApiResponse = new ApiResponse<>();
        userApiResponse.setResult(userService.createUser(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(userApiResponse);
    }

    @PutMapping("/{userId}")
    ResponseEntity<Object> updateUser(@PathVariable String userId, @RequestBody @Valid UserUpdateRequest request) {
        ApiResponse<User> userApiResponse = new ApiResponse<>();
        userApiResponse.setResult(userService.updateUser(userId, request));
        return ResponseEntity.status(HttpStatus.CREATED).body(userApiResponse);
    }

    @DeleteMapping("/{userId}")
    ResponseEntity<Object> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        ApiResponse<String> userApiResponse = new ApiResponse<>();
        userApiResponse.setResult("User has been delete");
        return ResponseEntity.status(HttpStatus.CREATED).body(userApiResponse);
    }
}
