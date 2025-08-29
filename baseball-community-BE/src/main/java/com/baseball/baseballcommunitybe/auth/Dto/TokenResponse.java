package com.baseball.baseballcommunitybe.auth.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenResponse {
    private String token;
    private String email;
    private String nickname;
}
