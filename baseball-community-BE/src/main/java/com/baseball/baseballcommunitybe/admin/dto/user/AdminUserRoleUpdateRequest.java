package com.baseball.baseballcommunitybe.admin.dto.user;

import com.baseball.baseballcommunitybe.admin.entity.AdminUser;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminUserRoleUpdateRequest {
    private AdminUser.Role role; //  Enum으로 받기
}