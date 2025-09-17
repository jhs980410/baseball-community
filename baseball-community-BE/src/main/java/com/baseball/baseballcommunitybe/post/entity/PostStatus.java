package com.baseball.baseballcommunitybe.post.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "post_status")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class PostStatus {

    @Id
    @Column(name = "post_id")
    private Long postId;  // posts.id와 1:1 매핑

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId   //  post의 PK가 post_status.post_id로 매핑됨
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(name = "comment_count", nullable = false)
    private Long commentCount = 0L;

    @Column(name = "like_count", nullable = false)
    private Long likeCount = 0L;

    @Column(name = "view_count", nullable = false)
    private Long viewCount = 0L;

    @Column(name = "score", nullable = false)
    private Long score = 0L;

    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated = LocalDateTime.now();

    // 카운트 업데이트용 헬퍼 메서드
    public void incrementCommentCount() { this.commentCount++; touchUpdatedTime(); }
    public void decrementCommentCount() { this.commentCount--; touchUpdatedTime(); }
    public void incrementLikeCount() { this.likeCount++; touchUpdatedTime(); }
    public void decrementLikeCount() { this.likeCount--; touchUpdatedTime(); }
    public void incrementViewCount() { this.viewCount++; touchUpdatedTime(); }

    private void touchUpdatedTime() { this.lastUpdated = LocalDateTime.now(); }
}
