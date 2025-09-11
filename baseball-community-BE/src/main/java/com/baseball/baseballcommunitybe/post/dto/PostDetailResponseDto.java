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
    private Long userId;
    private String title;
    private String content;
    private String nickname;
    private String createdAt;
    private String updatedAt;
    private long commentCount;
    private long likeCount;
    private long viewCount;              //  추가됨
    private boolean likedByCurrentUser;
    private Long teamId;
    private List<CommentSimpleDto> comments;

    public static PostDetailResponseDto from(Post post,
                                             List<CommentSimpleDto> comments,
                                             long commentCount,
                                             long likeCount,
                                             long viewCount,              //  추가
                                             boolean likedByCurrentUser) {
        return new PostDetailResponseDto(
                post.getId(),
                post.getUser() != null ? post.getUser().getId() : null,
                post.getTitle(),
                post.getContent(),
                post.getUser() != null ? post.getUser().getNickname() : "알 수 없음",
                post.getCreatedAt() != null ? post.getCreatedAt().toString() : null,
                post.getUpdatedAt() != null ? post.getUpdatedAt().toString() : null,
                commentCount,
                likeCount,
                viewCount,              // ✅ 매핑
                likedByCurrentUser,
                post.getTeamId(),
                comments
        );
    }
}
