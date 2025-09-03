package com.baseball.baseballcommunitybe.comment.service;

import com.baseball.baseballcommunitybe.comment.dto.CommentRequestDto;
import com.baseball.baseballcommunitybe.comment.dto.CommentResponseDto;
import com.baseball.baseballcommunitybe.comment.entity.Comment;
import com.baseball.baseballcommunitybe.comment.repository.CommentRepository;
import com.baseball.baseballcommunitybe.post.repository.PostRepository;
import com.baseball.baseballcommunitybe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public List<CommentResponseDto> findByUser(Long userId) {
        return commentRepository.findByUserId(userId).stream()
                .map(c -> new CommentResponseDto(
                        c.getId(),
                        c.getContent(),
                        c.getPost().getTitle(),
                        c.getUser().getNickname(),
                        c.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    public List<CommentResponseDto> findByPost(Long postId) {
        return commentRepository.findByPostId(postId).stream()
                .map(c -> new CommentResponseDto(
                        c.getId(),
                        c.getContent(),
                        c.getPost().getTitle(),
                        c.getUser().getNickname(),
                        c.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    public CommentResponseDto create(CommentRequestDto dto) {
        var post = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        var user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Comment comment = new Comment(post, user, dto.getContent());
        commentRepository.save(comment);

        return new CommentResponseDto(
                comment.getId(),
                comment.getContent(),
                post.getTitle(),
                user.getNickname(),
                comment.getCreatedAt()
        );
    }

    public void delete(Long id) {
        commentRepository.deleteById(id);
    }
}