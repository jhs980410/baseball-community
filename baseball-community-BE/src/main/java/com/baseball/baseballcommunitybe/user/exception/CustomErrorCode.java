package com.baseball.baseballcommunitybe.user.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CustomErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    USER_ALREADY_DELETED(HttpStatus.BAD_REQUEST, "이미 탈퇴한 회원입니다."),

    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다.")
    ;

    private final HttpStatus status;
    private final String message;

    CustomErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
