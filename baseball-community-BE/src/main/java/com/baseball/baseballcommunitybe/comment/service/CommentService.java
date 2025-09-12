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
     * 마이페이지: 특정 유저의 댓글 목록 (페이징)
     */
    public Page<CommentResponseDto> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable) {
        return commentRepository.findByUserId(userId, pageable)
                .map(CommentResponseDto::forUser);
    }
    /**
     * 게시글 상세: 해당 게시글의 댓글 목록
     */
    public List<CommentResponseDto> findByPost(Long postId) {
        return commentRepository.findByPostId(postId).stream()
                .map(CommentResponseDto::forPost)
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

        // 게시글 상태 체크
        if (post.getIsHidden()) {
            throw new IllegalStateException("숨김 처리된 게시글에는 댓글을 작성할 수 없습니다.");
        }

        // 유저 상태 체크
        if (user.getStatus() != User.Status.ACTIVE) {
            throw new IllegalStateException("해당 유저는 댓글 작성 권한이 없습니다.");
        }

        Comment comment = new Comment(post, user, dto.getContent());
        commentRepository.save(comment);

        // post_status 댓글 수 +1
        postStatusRepository.incrementCommentCount(dto.getPostId());

        return CommentResponseDto.forPost(comment);
    }

    /**
     * 댓글 삭제
     */
    /**
     * 댓글 수정
     */
    @Transactional
    public CommentResponseDto update(Long commentId, String newContent, Long currentUserId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        // 권한 체크
        if (!comment.getUser().getId().equals(currentUserId)) {
            throw new SecurityException("댓글 수정 권한이 없습니다.");
        }

        // 엔티티 수정 → JPA dirty checking 자동 반영
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

        // 권한 체크
        if (!comment.getUser().getId().equals(currentUserId) && !isAdmin) {
            throw new SecurityException("댓글 삭제 권한이 없습니다.");
        }

        Long postId = comment.getPost().getId();

        // 댓글 삭제
        commentRepository.deleteById(commentId);

        // post_status 댓글 수 -1
        postStatusRepository.decrementCommentCount(postId);
    }

    /**
     * 관리자/대시보드: 게시글 내 간단 댓글 리스트
     */
    public List<CommentSimpleDto> findSimpleByPost(Long postId) {
        return commentRepository.findByPostId(postId).stream()
                .map(CommentSimpleDto::from)
                .collect(Collectors.toList());
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
