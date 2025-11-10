package com.baseball.baseballcommunitybe.admin.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminUserCreateRequest {
    private String email;
    private String nickname;
    private String password;
}
