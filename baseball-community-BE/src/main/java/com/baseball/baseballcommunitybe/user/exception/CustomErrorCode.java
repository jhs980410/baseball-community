package com.baseball.baseballcommunitybe.user.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CustomErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
    USER_DELETED(HttpStatus.FORBIDDEN, "User already deleted"),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "Invalid request");

    private final HttpStatus status;
    private final String message;

    CustomErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
