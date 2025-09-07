package com.baseball.baseballcommunitybe.like.dto;

import com.baseball.baseballcommunitybe.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class LikeResponseDto {
    private Long postId;
    private String title;
    private String author;
    private LocalDateTime date;
    private boolean likedByCurrentUser;
    private long likeCount;

    public static LikeResponseDto from(Post post, boolean likedByCurrentUser, long likeCount) {
        return new LikeResponseDto(
                post.getId(),
                post.getTitle(),
                post.getUser().getNickname(),
                post.getCreatedAt(),
                likedByCurrentUser,
                likeCount
        );
    }
}
