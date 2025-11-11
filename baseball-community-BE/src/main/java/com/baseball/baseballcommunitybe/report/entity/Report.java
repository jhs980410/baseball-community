package com.baseball.baseballcommunitybe.report.entity;

import com.baseball.baseballcommunitybe.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(
        name = "reports",
        indexes = {
                @Index(name = "idx_target", columnList = "target_type, target_id")
        }
)
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id") // 명시적 매핑
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_type", nullable = false)
    private ReportTargetType targetType; // POST, COMMENT, USER

    @Column(name = "target_id", nullable = false)
    private Long targetId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User reporter;

    @Enumerated(EnumType.STRING)
    @Column(name = "reason", nullable = false)
    private ReportReason reason; // spam, abuse, adult, personal_info

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ReportStatus status = ReportStatus.PENDING;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Report(ReportTargetType targetType, Long targetId, User reporter, ReportReason reason) {
        this.targetType = targetType;
        this.targetId = targetId;
        this.reporter = reporter;
        this.reason = reason;
        this.status = ReportStatus.PENDING;
    }
}
