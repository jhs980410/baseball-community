package com.baseball.baseballcommunitybe.comment.service;

import com.baseball.baseballcommunitybe.comment.dto.CommentRequestDto;
import com.baseball.baseballcommunitybe.comment.dto.CommentResponseDto;
import com.baseball.baseballcommunitybe.comment.dto.CommentSimpleDto;
import com.baseball.baseballcommunitybe.comment.entity.Comment;
import com.baseball.baseballcommunitybe.comment.repository.CommentRepository;
import com.baseball.baseballcommunitybe.post.repository.PostRepository;
import com.baseball.baseballcommunitybe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    /**
     * 마이페이지: 특정 유저의 댓글 목록 (페이징)
     */
    public Page<CommentResponseDto> findByUser(Long userId, Pageable pageable) {
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
    public CommentResponseDto create(CommentRequestDto dto) {
        var post = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        var user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Comment comment = new Comment(post, user, dto.getContent());
        commentRepository.save(comment);

        return CommentResponseDto.forPost(comment);
    }

    /**
     * 댓글 삭제
     */
    public void delete(Long id) {
        commentRepository.deleteById(id);
    }

    /**
     * 관리자/대시보드: 게시글 내 간단 댓글 리스트
     */
    public List<CommentSimpleDto> findSimpleByPost(Long postId) {
        return commentRepository.findByPostId(postId).stream()
                .map(CommentSimpleDto::from)
                .collect(Collectors.toList());
    }
}
