package com.baseball.baseballcommunitybe.admin;


import com.baseball.baseballcommunitybe.admin.entity.AdminDailyStats;
import com.baseball.baseballcommunitybe.admin.repository.AdminDailyStatsRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AdminDailyStatsRepositoryTest {

    @Autowired
    private AdminDailyStatsRepository repository;

    @Test
    @DisplayName("최근 7일 통계 조회 (날짜 내림차순)")
    void findTop7ByOrderByStatDateDesc() {
        // given
        for (int i = 0; i < 10; i++) {
            AdminDailyStats s = AdminDailyStats.builder()
                    .statDate(LocalDate.now().minusDays(i))
                    .newUsers(i)
                    .activeUsers(i)
                    .withdrawnUsers(0)
                    .newPosts(i + 1)
                    .newComments(i + 2)
                    .postViews(100)
                    .reports(i)
                    .build();
            repository.save(s);
        }

        // when
        List<AdminDailyStats> result = repository.findTop7ByOrderByStatDateDesc();

        // then
        assertThat(result).hasSize(7);
        assertThat(result.get(0).getStatDate()).isAfter(result.get(6).getStatDate());
        result.forEach(r -> System.out.println("📅 " + r.getStatDate() + " | posts=" + r.getNewPosts()));
    }
}
