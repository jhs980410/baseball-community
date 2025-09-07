package com.baseball.baseballcommunitybe.post.service;

import com.baseball.baseballcommunitybe.comment.dto.CommentSimpleDto;
import com.baseball.baseballcommunitybe.comment.repository.CommentRepository;
import com.baseball.baseballcommunitybe.like.repository.LikeRepository;
import com.baseball.baseballcommunitybe.post.dto.PostDetailResponseDto;
import com.baseball.baseballcommunitybe.post.dto.PostResponseDto;
import com.baseball.baseballcommunitybe.post.entity.Post;
import com.baseball.baseballcommunitybe.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    // 전체 게시글 최신순 조회 (댓글 수 포함)
    public Page<PostResponseDto> getAllPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return postRepository.findAllWithCommentCount(pageable);
    }

    // 팀별 최신글 최신순 조회 (댓글 수 포함)
    public Page<PostResponseDto> getPostsByTeam(Integer teamId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return postRepository.findByTeamIdWithCommentCount(teamId, pageable);
    }

    // 특정 유저의 글 최신순 조회 (댓글 수 포함)
    public Page<PostResponseDto> getPostsByUser(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return postRepository.findByUserIdWithCommentCount(userId, pageable);
    }


    // 단일 게시글 조회 (댓글 수 + 좋아요 여부 + 상세 DTO)
    public PostDetailResponseDto getPostDetail(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음: " + postId));

        // 댓글 리스트 (간단 DTO로 변환)
        List<CommentSimpleDto> comments = commentRepository.findByPostId(postId)
                .stream()
                .map(CommentSimpleDto::from)
                .toList();

        // 댓글 수
        long commentCount = comments.size();

        // 좋아요 여부
        boolean liked = (userId != null) && likeRepository.existsByPost_IdAndUser_Id(postId, userId);

        // 좋아요 수
        long likeCount = likeRepository.countByPost_Id(postId);

        return PostDetailResponseDto.from(post, comments, commentCount, likeCount, liked);
    }
}
