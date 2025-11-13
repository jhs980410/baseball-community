package com.baseball.baseballcommunitybe.admin.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "notices")
@NoArgsConstructor
public class AdminNotice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "is_pinned")
    private boolean isPinned = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    public void update(String title, String content, boolean isPinned) {
        this.title = title;
        this.content = content;
        this.isPinned = isPinned;
        this.updatedAt = LocalDateTime.now();
    }
}
