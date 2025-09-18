package com.baseball.baseballcommunitybe.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostEditHistoryDto {
    private Long id;
    private Long postId;
    private Long editorId;
    private String editorNickname;
    private String oldTitle;
    private String oldContent;
    private String newTitle;
    private String newContent;
    private LocalDateTime editedAt;
}