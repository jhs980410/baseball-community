package com.baseball.baseballcommunitybe.post.service;

import com.baseball.baseballcommunitybe.comment.dto.CommentSimpleDto;
import com.baseball.baseballcommunitybe.comment.repository.CommentRepository;
import com.baseball.baseballcommunitybe.like.repository.LikeRepository;
import com.baseball.baseballcommunitybe.post.dto.PostDetailResponseDto;
import com.baseball.baseballcommunitybe.post.dto.PostRequestDto;
import com.baseball.baseballcommunitybe.post.dto.PostResponseDto;
import com.baseball.baseballcommunitybe.post.entity.Post;
import com.baseball.baseballcommunitybe.post.repository.PostRepository;
import com.baseball.baseballcommunitybe.user.entity.User;
import com.baseball.baseballcommunitybe.user.repository.UserRepository;
import com.baseball.baseballcommunitybe.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
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



    @Transactional
    public PostResponseDto insertPost(PostRequestDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // Quill 에디터 HTML 그대로 저장 (원본 유지)
        Post post = Post.builder()
                .title(dto.getTitle())
                .content(dto.getContent())  // 그대로 저장
                .user(user)
                .teamId(dto.getTeamId().byteValue())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        postRepository.save(post);
        return PostResponseDto.from(post, 0L, false);
    }
    @Transactional
    public void deletePost(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음: " + postId));

        postRepository.delete(post);
    }

    //  게시글 수정
    @Transactional
    public void updatePost(Long postId, String title, String content, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음: " + postId));

        // 작성자 본인인지 확인
        if (!post.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("본인 글만 수정할 수 있습니다.");
        }

        post.setTitle(title);
        post.setContent(content);
        // updatedAt은 @PreUpdate 에서 자동 업데이트되게 해두면 편리
    }

}
