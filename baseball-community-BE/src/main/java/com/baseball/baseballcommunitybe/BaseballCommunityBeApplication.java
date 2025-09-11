package com.baseball.baseballcommunitybe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@SpringBootApplication(scanBasePackages = "com.baseball.baseballcommunitybe")
@EnableJpaRepositories(
        basePackages = "com.baseball.baseballcommunitybe",
        excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*redis.*")
)
@EnableRedisRepositories(
        basePackages = "com.baseball.baseballcommunitybe.redis"
)
public class BaseballCommunityBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(BaseballCommunityBeApplication.class, args);
        System.out.println("Baseball Community Backend Application Start");
    }

}
