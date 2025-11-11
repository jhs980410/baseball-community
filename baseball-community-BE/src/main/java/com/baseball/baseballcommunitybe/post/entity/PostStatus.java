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
    private Long postId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
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

    @Column(name = "report_count", nullable = false)
    private Integer reportCount = 0;

    @PrePersist
    public void onCreate() {
        this.lastUpdated = LocalDateTime.now();
        if (this.commentCount == null) this.commentCount = 0L;
        if (this.likeCount == null) this.likeCount = 0L;
        if (this.viewCount == null) this.viewCount = 0L;
        if (this.score == null) this.score = 0L;
        if (this.reportCount == null) this.reportCount = 0;
    }

    @PreUpdate
    public void onUpdate() {
        this.lastUpdated = LocalDateTime.now();
    }

    public void incrementCommentCount() { this.commentCount++; touchUpdatedTime(); }
    public void decrementCommentCount() { this.commentCount--; touchUpdatedTime(); }
    public void incrementLikeCount() { this.likeCount++; touchUpdatedTime(); }
    public void decrementLikeCount() { this.likeCount--; touchUpdatedTime(); }
    public void incrementViewCount() { this.viewCount++; touchUpdatedTime(); }

    private void touchUpdatedTime() { this.lastUpdated = LocalDateTime.now(); }
}
