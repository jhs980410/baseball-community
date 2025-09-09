package com.baseball.baseballcommunitybe.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostRequestDto {
    private String title;
    private String content;
    private Long userId; // 로그인한 유저 ID
    private Long teamId; // 선택한 팀 ID


}
