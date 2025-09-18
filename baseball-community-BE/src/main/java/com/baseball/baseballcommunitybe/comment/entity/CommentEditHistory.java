package com.baseball.baseballcommunitybe.comment.entity;

import com.baseball.baseballcommunitybe.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentEditHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "editor_id", nullable = false)
    private User editor;

    private String oldContent;
    private String newContent;

    private LocalDateTime editedAt;  //  수정 시간

    @PrePersist
    public void prePersist() {
        if (editedAt == null) {
            editedAt = LocalDateTime.now();
        }
    }
}
