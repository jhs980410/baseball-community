package com.baseball.baseballcommunitybe.post.dto;

import com.baseball.baseballcommunitybe.comment.dto.CommentSimpleDto;
import com.baseball.baseballcommunitybe.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PostDetailResponseDto {
    private Long id;
    private String title;
    private String content;
    private String nickname;
    private String createdAt;
    private String updatedAt;
    private List<CommentSimpleDto> comments; // 댓글 리스트 포함

    public static PostDetailResponseDto from(Post post, List<CommentSimpleDto> comments) {
        return new PostDetailResponseDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getUser() != null ? post.getUser().getNickname() : "알 수 없음",
                post.getCreatedAt() != null ? post.getCreatedAt().toString() : null,
                post.getUpdatedAt() != null ? post.getUpdatedAt().toString() : null,
                comments
        );
    }
}
