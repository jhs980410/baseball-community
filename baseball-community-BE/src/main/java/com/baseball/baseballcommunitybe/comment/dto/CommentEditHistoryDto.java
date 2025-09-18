package com.baseball.baseballcommunitybe.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommentEditHistoryDto {
    private Long id;
    private Long commentId;        // 어떤 댓글의 수정내역인지
    private Long editorId;         // 수정한 유저 ID
    private String editorNickname; // 수정한 유저 닉네임
    private String oldContent;     // 이전 내용
    private String newContent;     // 수정된 내용
    private LocalDateTime editedAt;// 수정 시간
}