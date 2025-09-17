package com.baseball.baseballcommunitybe.like.service;

import com.baseball.baseballcommunitybe.auth.jwt.JwtTokenProvider;
import com.baseball.baseballcommunitybe.comment.entity.Comment;
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


    @Service
    @RequiredArgsConstructor
    public class CommentLikeService {

        private final CommentLikeRepository commentLikeRepository;
        private final CommentRepository commentRepository;
        private final UserRepository userRepository;
        private final JwtTokenProvider jwtTokenProvider;

        @Transactional
        public CommentLikeResponseDto like(Long commentId, HttpServletRequest request) {
            Long userId = extractUserId(request);

            Comment comment = commentRepository.findById(commentId)
                    .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

            // 이미 좋아요 했는지 확인
            boolean exists = commentLikeRepository.existsByIdCommentIdAndIdUserId(commentId, userId);
            if (!exists) {
                CommentLike like = CommentLike.builder()
                        .id(new CommentLikeId(commentId, userId))
                        .comment(comment)
                        .user(user)
                        .type(CommentLike.Type.UP)
                        .build();
                commentLikeRepository.save(like);
            }

            return buildResponse(commentId, userId);
        }

        @Transactional
        public CommentLikeResponseDto unlike(Long commentId, HttpServletRequest request) {
            Long userId = extractUserId(request);
            commentLikeRepository.deleteByIdCommentIdAndIdUserId(commentId, userId);
            return buildResponse(commentId, userId);
        }

        public Long countLikes(Long commentId) {
            return commentLikeRepository.countByIdCommentIdAndType(commentId, CommentLike.Type.UP);
        }

        private Long extractUserId(HttpServletRequest request) {
            String token = jwtTokenProvider.resolveToken(request);
            return jwtTokenProvider.getUserIdFromToken(token);
        }
        private CommentLikeResponseDto buildResponse(Long commentId, Long userId) {
            Long likeCount = commentLikeRepository.countByIdCommentIdAndType(commentId, CommentLike.Type.UP);
            Long dislikeCount = commentLikeRepository.countByIdCommentIdAndType(commentId, CommentLike.Type.DOWN);

            // 현재 유저가 누른 상태 확인
            CommentLike.Type currentUserType = commentLikeRepository
                    .findByIdCommentIdAndIdUserId(commentId, userId)
                    .map(CommentLike::getType)
                    .orElse(null);

            boolean likedByCurrentUser = currentUserType == CommentLike.Type.UP;
            boolean dislikedByCurrentUser = currentUserType == CommentLike.Type.DOWN;

            return new CommentLikeResponseDto(commentId, likeCount, dislikeCount, likedByCurrentUser, dislikedByCurrentUser);
        }

    }



