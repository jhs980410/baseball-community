package com.baseball.baseballcommunitybe.comment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "comment_status")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CommentStatus {

    @Id
    @Column(name = "comment_id")
    private Long commentId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @Column(name = "like_count", nullable = false)
    private Long likeCount = 0L;

    @Column(name = "dislike_count", nullable = false)
    private Long dislikeCount = 0L;

    @Column(name = "last_updated", nullable = false)
    private LocalDateTime lastUpdated = LocalDateTime.now();
}
