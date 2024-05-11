package com.logistn.IdentityService.exception;

public enum ErrorMessage {
    USERNAME_EXISTED(1001, "Username has been exitsted"),
    USERNAME_NOT_MET_REQUIRED(1002, "User not meet required"),
    PASSWORD_NOT_MET_REQUIRED(1003, "Password not meet required"),
    USER_NOT_FOUND(1004, "User not found"),
    UNKNOWN_EXCEPTION(999, "Unknown Exception"),
    VALIDATOR_EXCEPTION(9999, "Multiple Exception"),
    NO_RESOURCE_FOUND_EXCEPTION(-1111, "Can't access this resourse");
    private final int code;
    private final String message;

    ErrorMessage(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

}
