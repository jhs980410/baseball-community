package com.baseball.baseballcommunitybe.admin.entity;


import com.baseball.baseballcommunitybe.report.entity.ReportReason;
import com.baseball.baseballcommunitybe.report.entity.ReportStatus;
import com.baseball.baseballcommunitybe.report.entity.ReportTargetType;
import com.baseball.baseballcommunitybe.user.entity.User;
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

    // 신고 대상 종류 (post / comment / user)
    @Enumerated(EnumType.STRING)
    @Column(name = "target_type", nullable = false)
    private ReportTargetType targetType;

    // 신고 대상 ID
    @Column(name = "target_id", nullable = false)
    private Long targetId;

    // 신고 사유 (spam / abuse / adult / personal_info)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportReason reason;

    // 신고 상태 (pending / reviewed / resolved)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReportStatus status = ReportStatus.PENDING;

    // 신고자 (user_id FK)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User reporter;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}
