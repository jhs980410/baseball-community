package com.baseball.baseballcommunitybe.admin.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "posts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "team_id")
    private Long teamId;

    //  PostStatus 매핑 추가
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", referencedColumnName = "post_id")
    private AdminPostStatus status;

    @Column(name = "like_count")
    private Integer likeCount;
}
