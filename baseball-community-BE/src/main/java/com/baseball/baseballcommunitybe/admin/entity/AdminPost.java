package com.baseball.baseballcommunitybe.admin.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Formula;

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

    //  Formula는 Integer로 받고, 실제 Boolean 변환은 getter에서 수행
    @Formula("(SELECT p.is_hidden FROM posts p WHERE p.id = id)")
    private Integer hiddenValue;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", referencedColumnName = "post_id")
    private AdminPostStatus status;

    //  실제 접근용 getter (DTO 매핑 시에도 사용 가능)
    public Boolean getIsHidden() {
        return hiddenValue != null && hiddenValue == 1;
    }


}
