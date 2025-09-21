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

    @Column(name = "like_count", nullable = false)
    private Long likeCount;
}
