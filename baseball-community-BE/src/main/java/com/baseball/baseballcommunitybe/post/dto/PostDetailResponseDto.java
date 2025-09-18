package com.baseball.baseballcommunitybe.post.dto;

import com.baseball.baseballcommunitybe.comment.dto.CommentSimpleDto;
import com.baseball.baseballcommunitybe.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class PostDetailResponseDto {
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private String nickname;
    private String createdAt;
    private boolean edited;          // 수정됨 여부
    private LocalDateTime editedAt;  // 마지막 수정 시각 (선택)
    private String updatedAt;
    private long commentCount;
    private long likeCount;
    private long viewCount;
    private boolean likedByCurrentUser;
    private Long teamId;
    private List<CommentSimpleDto> comments;
    public static PostDetailResponseDto from(
            Post post,
            List<CommentSimpleDto> comments,
            long commentCount,
            long likeCount,
            long viewCount,
            boolean likedByCurrentUser,
            boolean edited // 👈 추가
    ) {
        return new PostDetailResponseDto(
                post.getId(),
                post.getUser() != null ? post.getUser().getId() : null,
                post.getTitle(),
                post.getContent(),
                post.getUser() != null ? post.getUser().getNickname() : "알 수 없음",
                post.getCreatedAt() != null ? post.getCreatedAt().toString() : null,
                edited, // 여기 값 세팅
                post.getUpdatedAt(),
                post.getUpdatedAt() != null ? post.getUpdatedAt().toString() : null,
                commentCount,
                likeCount,
                viewCount,
                likedByCurrentUser,
                post.getTeamId(),
                comments
        );
    }
    }

