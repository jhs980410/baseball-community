// com.baseball.baseballcommunitybe.admin.entity.AdminDailyStats.java
package com.baseball.baseballcommunitybe.admin.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "daily_stats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminDailyStats {

    @Id
    @Column(name = "stat_date")
    private LocalDate statDate;

    @Column(name = "new_users", nullable = false)
    private int newUsers;

    @Column(name = "active_users", nullable = false)
    private int activeUsers;

    @Column(name = "withdrawn_users", nullable = false)
    private int withdrawnUsers;

    @Column(name = "new_posts", nullable = false)
    private int newPosts;

    @Column(name = "new_comments", nullable = false)
    private int newComments;

    @Column(name = "post_views", nullable = false)
    private long postViews;

    @Column(name = "reports", nullable = false)
    private int reports;
}
