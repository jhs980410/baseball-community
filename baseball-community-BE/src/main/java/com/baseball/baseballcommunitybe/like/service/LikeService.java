package com.baseball.baseballcommunitybe.like.service;

import com.baseball.baseballcommunitybe.auth.jwt.JwtTokenProvider;
import com.baseball.baseballcommunitybe.like.dto.LikeResponseDto;
import com.baseball.baseballcommunitybe.like.entity.Like;
import com.baseball.baseballcommunitybe.like.repository.LikeRepository;
import com.baseball.baseballcommunitybe.post.entity.Post;
import com.baseball.baseballcommunitybe.post.entity.PostStatus;
import com.baseball.baseballcommunitybe.post.repository.PostRepository;
import com.baseball.baseballcommunitybe.post.repository.PostStatusRepository;
import com.baseball.baseballcommunitybe.user.entity.User;
import com.baseball.baseballcommunitybe.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostStatusRepository postStatusRepository;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 마이페이지 - 내가 좋아요한 글 (페이징)
     */
    public Page<LikeResponseDto> findMyLikes(HttpServletRequest request, Pageable pageable) {
        Long userId = extractUserIdFromRequest(request);

        return likeRepository.findByUser_Id(userId, pageable)
                .map(like -> LikeResponseDto.from(
                        like.getPost(),
                        true,
                        postStatusRepository.findById(like.getPost().getId())
                                .map(PostStatus::getLikeCount)
                                .orElse(0L)
                ));
    }

    /**
     * 게시글 좋아요 토글
     */
    @Transactional
    public LikeResponseDto toggleLike(Long postId, HttpServletRequest request) {
        Long userId = extractUserIdFromRequest(request);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음: " + postId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음: " + userId));

        Optional<Like> existing = likeRepository.findByPost_IdAndUser_Id(postId, userId);

        boolean liked;
        if (existing.isPresent()) {
            likeRepository.delete(existing.get());
            postStatusRepository.decrementLikeCount(postId);
            liked = false;
        } else {
            Like like = new Like(post, user);
            likeRepository.save(like);
            postStatusRepository.incrementLikeCount(postId);
            liked = true;
        }

        long likeCount = postStatusRepository.findById(postId)
                .map(PostStatus::getLikeCount)
                .orElse(0L);

        return LikeResponseDto.from(post, liked, likeCount);
    }

    public long countLikes(Long postId) {
        return postStatusRepository.findById(postId)
                .map(PostStatus::getLikeCount)
                .orElse(0L);
    }

    // 내부 헬퍼
    private Long extractUserIdFromRequest(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        if (token == null) {
            throw new IllegalArgumentException("인증 토큰이 없습니다.");
        }
        return jwtTokenProvider.getUserIdFromToken(token);
    }
}
