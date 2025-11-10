package com.baseball.baseballcommunitybe.admin.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserDetailDto {
    private Long id;
    private String email;
    private String nickname;
    private String role;
    private String status;
    private int reportCount;
    private int postCount;
    private int commentCount;
    private LocalDateTime createdAt;
}
