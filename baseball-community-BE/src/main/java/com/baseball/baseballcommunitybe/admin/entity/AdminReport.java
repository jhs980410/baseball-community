// com.baseball.baseballcommunitybe.admin.entity.AdminReport.java
package com.baseball.baseballcommunitybe.admin.entity;

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

    // reporterId 제거
    private String targetType;
    private Long targetId;
    private String reason;
    private LocalDateTime createdAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User reporter;
}


