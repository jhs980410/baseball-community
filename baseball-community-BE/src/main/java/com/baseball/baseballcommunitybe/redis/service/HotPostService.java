package com.baseball.baseballcommunitybe.redis.service;

import com.baseball.baseballcommunitybe.like.repository.LikeRepository;
import com.baseball.baseballcommunitybe.post.dto.PostResponseDto;
import com.baseball.baseballcommunitybe.post.entity.Post;
import com.baseball.baseballcommunitybe.post.entity.PostStatus;
import com.baseball.baseballcommunitybe.post.repository.PostRepository;
import com.baseball.baseballcommunitybe.post.repository.PostStatusRepository;
import com.baseball.baseballcommunitybe.redis.repository.HotPostRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class HotPostService {

    private final PostStatusRepository postStatusRepository;
    private final LikeRepository likeRepository;
    private final HotPostRedisRepository hotPostRedisRepository;

    private static final double HOT_THRESHOLD = 50.0;

    private double calculateScore(PostStatus status) {
        return status.getLikeCount() * 3.0
                + status.getCommentCount() * 2.0
                + status.getViewCount() * 0.5;
    }

    /**
     * 5분마다 DB → Redis 캐싱
     */
    @Scheduled(fixedDelay = 300000) // 5분마다
    @Transactional(readOnly = true)
    public void refreshHotPosts() {
        log.info("🔥 refreshHotPosts START");

        // 최근 7일치만 가져오기
        LocalDateTime since = LocalDateTime.now().minusDays(7);

        int page = 0;
        int size = 1000;
        Page<PostStatus> pageResult;

        do {
            pageResult = postStatusRepository.findRecent(since, PageRequest.of(page, size));

            for (PostStatus status : pageResult.getContent()) {
                double score = calculateScore(status);
                if (score >= HOT_THRESHOLD) {
                    Long teamId = status.getPost().getTeamId();
                    //  clear() 하지 않고, 기존 데이터에 점수 업데이트/추가
                    hotPostRedisRepository.updateScore(status.getPostId(), teamId, score);
                }
            }

            page++;
        } while (pageResult.hasNext());

        log.info("✅ refreshHotPosts END (총 {} 페이지 처리)", page);
    }


    /**
     * 인기글 조회 (전체 or 팀별) - Page 반환
     */
    public Page<PostResponseDto> getHotPosts(Long currentUserId, Long teamId, int page, int size) {
        List<Long> allIds = hotPostRedisRepository.getTopPosts(teamId, 200);
        if (allIds.isEmpty()) return Page.empty();

        int start = page * size;
        int end = Math.min(start + size, allIds.size());
        if (start >= allIds.size()) return Page.empty();

        List<Long> pagedIds = allIds.subList(start, end);

        // ✅ Post + PostStatus 한번에 가져오기
        List<PostStatus> statuses = postStatusRepository.findAllByPostIdIn(pagedIds);

        List<PostResponseDto> content = statuses.stream()
                .map(status -> {
                    Post post = status.getPost();
                    boolean liked = currentUserId != null &&
                            likeRepository.existsByPost_IdAndUser_Id(post.getId(), currentUserId);
                    return PostResponseDto.from(
                            post,
                            status.getCommentCount(),
                            status.getLikeCount(),
                            status.getViewCount(),
                            liked
                    );
                }).toList();

        return new PageImpl<>(content, PageRequest.of(page, size), allIds.size());
    }
}
