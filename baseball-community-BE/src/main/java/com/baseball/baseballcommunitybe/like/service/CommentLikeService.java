package com.baseball.baseballcommunitybe.like.service;

import com.baseball.baseballcommunitybe.auth.jwt.JwtTokenProvider;
import com.baseball.baseballcommunitybe.comment.entity.Comment;
import com.baseball.baseballcommunitybe.comment.entity.CommentStatus;
import com.baseball.baseballcommunitybe.comment.repository.CommentRepository;

import com.baseball.baseballcommunitybe.like.dto.CommentLikeResponseDto;
import com.baseball.baseballcommunitybe.like.entity.CommentLike;
import com.baseball.baseballcommunitybe.like.entity.CommentLikeId;
import com.baseball.baseballcommunitybe.like.repository.CommentLikeRepository;
import com.baseball.baseballcommunitybe.user.entity.User;
import com.baseball.baseballcommunitybe.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.baseball.baseballcommunitybe.comment.repository.CommentStatusRepository;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CommentStatusRepository commentStatusRepository;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 댓글 좋아요 추가
     */
    @Transactional
    public CommentLikeResponseDto like(Long commentId, HttpServletRequest request) {
        Long userId = extractUserId(request);

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        boolean exists = commentLikeRepository.existsByIdCommentIdAndIdUserId(commentId, userId);
        if (!exists) {
            CommentLike like = CommentLike.builder()
                    .id(new CommentLikeId(commentId, userId))
                    .comment(comment)
                    .user(user)
                    .type(CommentLike.Type.UP) // 지금은 좋아요만 사용
                    .build();
            commentLikeRepository.save(like);

            // 상태 업데이트 (좋아요 수 +1)
            commentStatusRepository.incrementLikeCount(commentId);
        }

        return buildResponse(commentId, userId);
    }

    /**
     * 댓글 좋아요 취소
     */
    @Transactional
    public CommentLikeResponseDto unlike(Long commentId, HttpServletRequest request) {
        Long userId = extractUserId(request);

        if (commentLikeRepository.existsByIdCommentIdAndIdUserId(commentId, userId)) {
            commentLikeRepository.deleteByIdCommentIdAndIdUserId(commentId, userId);

            //  상태 업데이트 (좋아요 수 -1)
            commentStatusRepository.decrementLikeCount(commentId);
        }

        return buildResponse(commentId, userId);
    }

    /**
     * 댓글 좋아요 개수 조회 (status 기반)
     */
    public Long countLikes(Long commentId) {
        return commentStatusRepository.findById(commentId)
                .map(CommentStatus::getLikeCount)
                .orElse(0L);
    }

    // ---------------- 내부 헬퍼 ----------------
    private Long extractUserId(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        return jwtTokenProvider.getUserIdFromToken(token);
    }

    private CommentLikeResponseDto buildResponse(Long commentId, Long userId) {
        long likeCount = commentStatusRepository.findById(commentId)
                .map(CommentStatus::getLikeCount)
                .orElse(0L);

        boolean likedByCurrentUser = commentLikeRepository
                .findByIdCommentIdAndIdUserId(commentId, userId)
                .isPresent();

        // 📌 싫어요는 없는 구조 → 항상 0, false
        return new CommentLikeResponseDto(commentId, likeCount, 0L, likedByCurrentUser, false);
    }
}

