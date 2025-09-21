// com.baseball.baseballcommunitybe.admin.entity.AdminReport.java
package com.baseball.baseballcommunitybe.admin.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reporter_id", nullable = false)
    private Long reporterId;

    @Column(nullable = false, length = 50)
    private String targetType; // POST, COMMENT

    @Column(nullable = false)
    private Long targetId;

    @Column(name = "reason", nullable = false, length = 100)
    private String reason;

    @Column(nullable = false)
    private LocalDateTime createdAt;
}
