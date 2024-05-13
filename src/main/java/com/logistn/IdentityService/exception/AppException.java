package com.logistn.IdentityService.exception;

import lombok.Getter;

@Getter
public class AppException extends RuntimeException {
    private final ErrorMessage errorMessage;

    public AppException(ErrorMessage errorMessage) {
        super(errorMessage.getMessage());
        this.errorMessage = errorMessage;
    }
}
