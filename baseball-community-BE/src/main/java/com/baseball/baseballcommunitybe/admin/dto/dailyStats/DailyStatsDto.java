package com.baseball.baseballcommunitybe.admin.dto.dailyStats;


import com.baseball.baseballcommunitybe.admin.entity.AdminDailyStats;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DailyStatsDto {
    private LocalDate statDate;
    private int newUsers;
    private int newPosts;
    private int newComments;

    public static DailyStatsDto fromEntity(AdminDailyStats entity) {
        return DailyStatsDto.builder()
                .statDate(entity.getStatDate())
                .newUsers(entity.getNewUsers())
                .newPosts(entity.getNewPosts())
                .newComments(entity.getNewComments())
                .build();
    }
}