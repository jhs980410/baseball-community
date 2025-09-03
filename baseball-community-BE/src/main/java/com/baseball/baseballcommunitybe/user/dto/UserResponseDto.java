package com.baseball.baseballcommunitybe.user.dto;

import com.baseball.baseballcommunitybe.user.entity.User;

import lombok.Getter;

@Getter
public class UserResponseDto {
    private Long id;
    private String email;
    private String nickname;
    private String role;

    public UserResponseDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.role = user.getRole().name();
    }
}
