package com.baseball.baseballcommunitybe.like.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentLikeResponseDto {
    private Long commentId;
    private Long likeCount;
    private Long dislikeCount;
    private boolean likedByCurrentUser;
    private boolean dislikedByCurrentUser;
}
