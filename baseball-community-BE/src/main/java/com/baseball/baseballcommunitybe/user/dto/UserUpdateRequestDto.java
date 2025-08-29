package com.baseball.baseballcommunitybe.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UserUpdateRequestDto {
    private String nickname;
    private String password; // 변경할 경우
}
