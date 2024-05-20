package com.logistn.IdentityService.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@RequiredArgsConstructor
public enum ErrorMessage {
    USERNAME_EXISTED(1001, "Username has been exitsted", HttpStatus.BAD_REQUEST),
    USERNAME_NOT_MET_REQUIRED(1002, "User not meet required", HttpStatus.BAD_REQUEST),
    PASSWORD_NOT_MET_REQUIRED(1003, "Password not meet required, ${validatedValue}", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1004, "User not found", HttpStatus.NOT_FOUND),
    UNKNOWN_EXCEPTION(999, null, HttpStatus.INTERNAL_SERVER_ERROR),
    VALIDATOR_EXCEPTION(9999, "Validate Exception", HttpStatus.BAD_REQUEST),
    NO_RESOURCE_FOUND_EXCEPTION(-1111, "Can't access this resourse", HttpStatus.NOT_FOUND),
    UNAUTHENTICATED(-9999, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(-99, "You've not permissive to access this resource", HttpStatus.FORBIDDEN),
    INVALID_DOB(1005, "Invalid dob", HttpStatus.BAD_REQUEST);

    @Getter
    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
