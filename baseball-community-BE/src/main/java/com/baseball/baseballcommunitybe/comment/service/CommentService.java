package com.baseball.baseballcommunitybe.comment.service;

import com.baseball.baseballcommunitybe.auth.jwt.JwtTokenProvider;
import com.baseball.baseballcommunitybe.comment.dto.CommentRequestDto;
import com.baseball.baseballcommunitybe.comment.dto.CommentResponseDto;
import com.baseball.baseballcommunitybe.comment.entity.Comment;
import com.baseball.baseballcommunitybe.comment.entity.CommentEditHistory;
import com.baseball.baseballcommunitybe.comment.entity.CommentStatus;
import com.baseball.baseballcommunitybe.comment.repository.CommentEditHistoryRepository;
import com.baseball.baseballcommunitybe.comment.repository.CommentRepository;
import com.baseball.baseballcommunitybe.comment.repository.CommentStatusRepository;
import com.baseball.baseballcommunitybe.post.entity.Post;
import com.baseball.baseballcommunitybe.post.repository.PostRepository;
import com.baseball.baseballcommunitybe.post.repository.PostStatusRepository;
import com.baseball.baseballcommunitybe.redis.repository.DailyStatsRedisRepository;
import com.baseball.baseballcommunitybe.user.entity.User;
import com.baseball.baseballcommunitybe.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostStatusRepository postStatusRepository;
    private final CommentStatusRepository commentStatusRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final CommentEditHistoryRepository commentEditHistoryRepository;
    private final DailyStatsRedisRepository dailyStatsRedisRepository;
    /**
     * 마이페이지: 특정 유저의 댓글 목록 (페이징)
     */
    public Page<CommentResponseDto> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable) {
        return commentRepository.findByUserId(userId, pageable)
                .map(this::mapToDtoWithEditInfoUser);
    }

    /**
     * 게시글 상세: 댓글 + 대댓글 트리 조회
     */
    @Transactional(readOnly = true)
    public List<CommentResponseDto> findByPost(Long postId) {
        List<Comment> comments = commentRepository.findByPostIdWithUser(postId);

        return comments.stream()
                .filter(c -> c.getParent() == null)
                .map(this::mapToDtoWithEditInfoPost)
                .collect(Collectors.toList());
    }

    // 📌 마이페이지 변환
    private CommentResponseDto mapToDtoWithEditInfoUser(Comment comment) {
        boolean edited = commentEditHistoryRepository.existsByComment_Id(comment.getId());
        LocalDateTime editedAt = edited
                ? commentEditHistoryRepository.findTopByComment_IdOrderByEditedAtDesc(comment.getId())
                .map(CommentEditHistory::getEditedAt)
                .orElse(null)
                : null;

        return CommentResponseDto.forUser(comment, edited, editedAt);
    }

    // 📌 게시글 상세 변환 (재귀)
    private CommentResponseDto mapToDtoWithEditInfoPost(Comment comment) {
        boolean edited = commentEditHistoryRepository.existsByComment_Id(comment.getId());
        LocalDateTime editedAt = edited
                ? commentEditHistoryRepository.findTopByComment_IdOrderByEditedAtDesc(comment.getId())
                .map(CommentEditHistory::getEditedAt)
                .orElse(null)
                : null;

        List<CommentResponseDto> children = comment.getChildren().stream()
                .map(this::mapToDtoWithEditInfoPost)
                .collect(Collectors.toList());
        System.out.println("댓글 수정내역임" + edited);
        return CommentResponseDto.forPost(comment, edited, editedAt, children);
    }

    /**
     * 댓글 작성
     */
    @Transactional
    public CommentResponseDto create(CommentRequestDto dto, HttpServletRequest request) {
        Long userId = extractUserIdFromRequest(request);

        Post post = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음"));

        if (post.getIsHidden()) {
            throw new IllegalStateException("숨김 처리된 게시글에는 댓글 작성 불가");
        }
        if (user.getStatus() != User.Status.ACTIVE) {
            throw new IllegalStateException("해당 유저는 댓글 작성 권한 없음");
        }

        Comment parent = null;
        if (dto.getParentId() != null) {
            parent = commentRepository.findById(dto.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("부모 댓글 없음"));
        }

        Comment comment = new Comment(post, user, dto.getContent(), parent);
        commentRepository.save(comment);

        //  PostStatus 댓글 수 증가
        postStatusRepository.incrementCommentCount(dto.getPostId());

        //  CommentStatus 생성
        CommentStatus status = CommentStatus.builder()
                .comment(comment)
                .likeCount(0L)
                .dislikeCount(0L)
                .lastUpdated(LocalDateTime.now())
                .build();
        String today = LocalDate.now().toString();
        dailyStatsRedisRepository.increment(today, "new_comments");
        commentStatusRepository.save(status);

        return mapToDtoWithEditInfoPost(comment);
    }


    /**
     * 댓글 수정
     */
    @Transactional
    public CommentResponseDto update(Long commentId, String newContent, Long currentUserId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글 없음"));

        if (!comment.getUser().getId().equals(currentUserId)) {
            throw new SecurityException("댓글 수정 권한 없음");
        }

        String oldContent = comment.getContent();
        comment.updateContent(newContent);

        CommentEditHistory history = CommentEditHistory.builder()
                .comment(comment)
                .editor(comment.getUser())
                .oldContent(oldContent)
                .newContent(newContent)
                .build();

        commentEditHistoryRepository.save(history);

        return mapToDtoWithEditInfoPost(comment);
    }

    /**
     * 댓글 삭제
     */
    // 댓글 삭제
    @Transactional
    public void delete(Long commentId, Long currentUserId, boolean isAdmin) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글 없음"));

        if (!comment.getUser().getId().equals(currentUserId) && !isAdmin) {
            throw new SecurityException("댓글 삭제 권한 없음");
        }

        Long postId = comment.getPost().getId();

        //  CommentStatus 삭제
        commentStatusRepository.deleteById(commentId);

        commentRepository.deleteById(commentId);

        //  PostStatus 댓글 수 감소
        postStatusRepository.decrementCommentCount(postId);
    }
    // ----------------- 내부 헬퍼 -----------------
    private Long extractUserIdFromRequest(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        if (token == null) {
            throw new IllegalArgumentException("인증 토큰 없음");
        }
        return jwtTokenProvider.getUserIdFromToken(token);
    }
}
