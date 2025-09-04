package com.baseball.baseballcommunitybe.post.dto;

import com.baseball.baseballcommunitybe.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private String nickname;
    private String createdAt;
    private String updatedAt;
    private Long commentCount;



    public static PostResponseDto from(Post post,  Long commentCount) {
        return new PostResponseDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getUser() != null ? post.getUser().getNickname() : "알 수 없음",
                post.getCreatedAt() != null ? post.getCreatedAt().toString() : null,
                post.getUpdatedAt() != null ? post.getUpdatedAt().toString() : null,
                commentCount
        );
    }
}

