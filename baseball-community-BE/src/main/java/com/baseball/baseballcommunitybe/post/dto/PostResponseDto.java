package com.baseball.baseballcommunitybe.post.dto;

import com.baseball.baseballcommunitybe.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

@Getter
@AllArgsConstructor
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private String nickname;
    private Long userId;
    private Long teamId;
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

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.userId = post.getUser().getId();
        this.teamId = post.getTeamId();
    }


    //  정적 팩토리 메서드 추가

    public static PostResponseDto from(Post post, Long commentCount, boolean likedByCurrentUser) {
        // XSS 방지: 위험한 스크립트 제거, 기본적인 스타일 태그 허용
        String safeContent = Jsoup.clean(post.getContent(), Safelist.relaxed());

        return new PostResponseDto(
                post.getId(),
                post.getTitle(),
                safeContent,   // 정제된 HTML 반환
                post.getUser() != null ? post.getUser().getNickname() : "알 수 없음",
                post.getUser() != null ? post.getUser().getId() : null, //  userId 추가
                post.getTeamId() != null ? post.getTeamId() : null,
                post.getCreatedAt() != null ? post.getCreatedAt().toString() : null,
                post.getUpdatedAt() != null ? post.getUpdatedAt().toString() : null,
                commentCount,
                likedByCurrentUser
        );
    }


}

