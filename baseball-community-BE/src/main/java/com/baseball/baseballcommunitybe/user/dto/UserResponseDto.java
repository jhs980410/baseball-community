package com.baseball.baseballcommunitybe.user.dto;

import com.baseball.baseballcommunitybe.user.entity.UserEntity;

import lombok.Getter;

@Getter
public class UserResponseDto {
    private Long id;
    private String email;
    private String nickname;
    private String role;

    public UserResponseDto(UserEntity user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.role = user.getRole().name();
    }
}
