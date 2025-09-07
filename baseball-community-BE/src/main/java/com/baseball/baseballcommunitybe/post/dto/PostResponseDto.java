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
    private boolean likedByCurrentUser;



    // JPQL Constructor Expression에서 사용될 생성자
    public PostResponseDto(Long id, String title, String content,
                           String nickname, String createdAt, String updatedAt,
                           Long commentCount) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.nickname = nickname;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.commentCount = commentCount;
    }
}

