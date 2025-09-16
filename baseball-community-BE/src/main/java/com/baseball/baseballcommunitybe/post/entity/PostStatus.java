package com.baseball.baseballcommunitybe.post.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "post_status")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostStatus {

    @Id
    @Column(name = "post_id")
    private Long postId;  // posts.id와 1:1 매핑

    @Column(name = "comment_count", nullable = false)
    private Long commentCount;

    @Column(name = "like_count", nullable = false)
    private Long likeCount;

    @Column(name = "view_count", nullable = false)
    private Long viewCount;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;
    @Column(name = "score", nullable = false)
    private Long score;
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "post_id")
    private Post post;
    public PostStatus(Long postId, Long commentCount, Long likeCount, Long viewCount, Long score, LocalDateTime lastUpdated) {
        this.postId = postId;
        this.commentCount = commentCount;
        this.likeCount = likeCount;
        this.viewCount = viewCount;
        this.score = score;
        this.lastUpdated = lastUpdated;
    }


    // 카운트 업데이트용 헬퍼 메서드
    public void incrementCommentCount() { this.commentCount++; }
    public void decrementCommentCount() { this.commentCount--; }
    public void incrementLikeCount() { this.likeCount++; }
    public void decrementLikeCount() { this.likeCount--; }
    public void incrementViewCount() { this.viewCount++; }

    public void touchUpdatedTime() { this.lastUpdated = LocalDateTime.now(); }
}
