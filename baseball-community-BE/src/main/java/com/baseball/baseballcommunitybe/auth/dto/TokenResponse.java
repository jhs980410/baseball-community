package com.baseball.baseballcommunitybe.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenResponse {
    private String token;
    private Long id;
    private String email;
    private String nickname;
}
