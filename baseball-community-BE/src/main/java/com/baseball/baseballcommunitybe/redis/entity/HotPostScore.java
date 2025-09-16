package com.baseball.baseballcommunitybe.redis.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HotPostScore implements Serializable {
    private Long postId;     // 게시글 ID
    private Double score;    // 가중치 계산된 점수
    private Long likeCount;
    private Long commentCount;
    private Long viewCount;
}
