package com.baseball.baseballcommunitybe.admin.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "post_status")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminPostStatus {

    @Id
    @Column(name = "post_id")
    private Long postId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "post_id")
    private AdminPost post;

    /** 기본 통계 */
    @Column(name = "comment_count", nullable = false)
    private Long commentCount;

    @Column(name = "like_count", nullable = false)
    private Long likeCount;

    @Column(name = "view_count", nullable = false)
    private Long viewCount;

    /** 관리자/운영용 상태 */
    @Column(name = "report_count", nullable = false)
    private Long reportCount;

    @Column(name = "contains_banned_word", nullable = false)
    private boolean containsBannedWord;

    @Column(name = "flagged", nullable = false)
    private boolean flagged;

    @Column(name = "last_flag_reason")
    private String lastFlagReason;
}
