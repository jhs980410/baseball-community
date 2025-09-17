package com.baseball.baseballcommunitybe.comment.service;

import com.baseball.baseballcommunitybe.auth.jwt.JwtTokenProvider;
import com.baseball.baseballcommunitybe.comment.dto.CommentRequestDto;
import com.baseball.baseballcommunitybe.comment.dto.CommentResponseDto;
import com.baseball.baseballcommunitybe.comment.dto.CommentSimpleDto;
import com.baseball.baseballcommunitybe.comment.entity.Comment;
import com.baseball.baseballcommunitybe.comment.repository.CommentRepository;
import com.baseball.baseballcommunitybe.post.entity.Post;
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

import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostStatusRepository postStatusRepository;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 마이페이지: 특정 유저의 댓글 목록 (페이징, flat)
     */
    public Page<CommentResponseDto> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable) {
        return commentRepository.findByUserId(userId, pageable)
                .map(CommentResponseDto::forUser);
    }

    /**
     * 게시글 상세: 댓글 + 대댓글 트리 조회
     */
    @Transactional(readOnly = true)
    public List<CommentResponseDto> findByPost(Long postId) {
        List<Comment> comments = commentRepository.findByPostIdWithUser(postId);

        // 부모 댓글만 걸러서 트리 구조 반환
        return comments.stream()
                .filter(c -> c.getParent() == null)
                .map(CommentResponseDto::forPost) // children 포함 재귀 변환
                .collect(Collectors.toList());
    }

    /**
     * 댓글 작성
     */
    @Transactional
    public CommentResponseDto create(CommentRequestDto dto, HttpServletRequest request) {
        Long userId = extractUserIdFromRequest(request);

        Post post = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        if (post.getIsHidden()) {
            throw new IllegalStateException("숨김 처리된 게시글에는 댓글을 작성할 수 없습니다.");
        }
        if (user.getStatus() != User.Status.ACTIVE) {
            throw new IllegalStateException("해당 유저는 댓글 작성 권한이 없습니다.");
        }

        Comment parent = null;
        if (dto.getParentId() != null) {
            parent = commentRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("부모 댓글을 찾을 수 없습니다."));
        }

        Comment comment = new Comment(post, user, dto.getContent(), parent);
        commentRepository.save(comment);

        postStatusRepository.incrementCommentCount(dto.getPostId());

        return CommentResponseDto.forPost(comment);
    }

    /**
     * 댓글 수정
     */
    @Transactional
    public CommentResponseDto update(Long commentId, String newContent, Long currentUserId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        if (!comment.getUser().getId().equals(currentUserId)) {
            throw new SecurityException("댓글 수정 권한이 없습니다.");
        }

        comment.updateContent(newContent);

        return CommentResponseDto.forPost(comment);
    }

    /**
     * 댓글 삭제
     */
    @Transactional
    public void delete(Long commentId, Long currentUserId, boolean isAdmin) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        if (!comment.getUser().getId().equals(currentUserId) && !isAdmin) {
            throw new SecurityException("댓글 삭제 권한이 없습니다.");
        }

        Long postId = comment.getPost().getId();
        commentRepository.deleteById(commentId);

        postStatusRepository.decrementCommentCount(postId);
    }

    // ----------------- 내부 헬퍼 -----------------
    private Long extractUserIdFromRequest(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        if (token == null) {
            throw new IllegalArgumentException("인증 토큰이 없습니다.");
        }
        return jwtTokenProvider.getUserIdFromToken(token);
    }
}
