package com.baseball.baseballcommunitybe.admin.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Formula;
// AdminPost.java

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


    @Formula("(SELECT p.is_hidden FROM posts p WHERE p.id = id)")
    private Integer hiddenValue;  // DB 값 직접 읽기 (0 또는 1)

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", referencedColumnName = "post_id")
    private AdminPostStatus status;
}

