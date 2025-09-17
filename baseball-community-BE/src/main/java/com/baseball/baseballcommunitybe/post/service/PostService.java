package com.baseball.baseballcommunitybe.post.service;

import com.baseball.baseballcommunitybe.comment.dto.CommentSimpleDto;
import com.baseball.baseballcommunitybe.comment.repository.CommentRepository;
import com.baseball.baseballcommunitybe.like.repository.LikeRepository;
import com.baseball.baseballcommunitybe.post.dto.PostDetailResponseDto;
import com.baseball.baseballcommunitybe.post.dto.PostRequestDto;
import com.baseball.baseballcommunitybe.post.dto.PostResponseDto;
import com.baseball.baseballcommunitybe.post.entity.Post;
import com.baseball.baseballcommunitybe.post.entity.PostStatus;
import com.baseball.baseballcommunitybe.post.repository.PostRepository;
import com.baseball.baseballcommunitybe.post.repository.PostStatusRepository;
import com.baseball.baseballcommunitybe.user.entity.User;
import com.baseball.baseballcommunitybe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final PostStatusRepository postStatusRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;


    // 전체 게시글 최신순 조회
    public Page<PostResponseDto> getAllPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return postRepository.findAllWithStatus(pageable);
    }

    // 팀별 최신순 조회
    public Page<PostResponseDto> getPostsByTeam(Integer teamId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return postRepository.findByTeamIdWithStatus(teamId, pageable);
    }

    // 특정 유저의 글 조회
    public Page<PostResponseDto> getPostsByUser(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return postRepository.findByUserIdWithStatus(userId, pageable);
    }

    // 단일 게시글 조회 (댓글, 좋아요 여부 포함)
    @Transactional
    public PostDetailResponseDto getPostDetail(Long postId, Long currentUserId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음: " + postId));

        // 조회수 증가
        postStatusRepository.incrementViewCount(postId);

        // 댓글 리스트
        List<CommentSimpleDto> comments = commentRepository.findByPostId(postId)
                .stream()
                .map(CommentSimpleDto::from)
                .toList();

        // 상태 값 (댓글 수, 좋아요 수, 조회수)
        PostStatus status = postStatusRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("PostStatus 없음: " + postId));

        // 좋아요 여부
        boolean liked = (currentUserId != null) && likeRepository.existsByPost_IdAndUser_Id(postId, currentUserId);

        return PostDetailResponseDto.from(
                post,
                comments,
                status.getCommentCount(),
                status.getLikeCount(),
                status.getViewCount(),
                liked
        );
    }

    // 게시글 작성
    @Transactional
    public PostResponseDto createPost(PostRequestDto dto, Long currentUserId) {
        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Post post = Post.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .user(user)
                .teamId(dto.getTeamId().longValue())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        postRepository.save(post);

        //  PostStatus 생성 시 post만 세팅 → postId 자동 세팅
        PostStatus status = PostStatus.builder()
                .post(post)
                .commentCount(0L)
                .likeCount(0L)
                .viewCount(0L)
                .score(0L)
                .lastUpdated(LocalDateTime.now())
                .build();

        postStatusRepository.save(status);

        return PostResponseDto.from(post, 0L, 0L, 0L, false);
    }


    // 게시글 삭제
    @Transactional
    public void deletePost(Long postId, Long currentUserId) throws AccessDeniedException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음: " + postId));

        if (!post.getUser().getId().equals(currentUserId)) {
            throw new AccessDeniedException("본인 글만 삭제할 수 있습니다.");
        }

        postRepository.delete(post);
    }

    // 게시글 수정
    @Transactional
    public PostResponseDto updatePost(Long postId, PostRequestDto requestDto, Long currentUserId) throws AccessDeniedException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음: " + postId));

        if (!post.getUser().getId().equals(currentUserId)) {
            throw new AccessDeniedException("본인 글만 수정할 수 있습니다.");
        }

        post.setTitle(requestDto.getTitle());
        post.setContent(requestDto.getContent());
        post.setTeamId(requestDto.getTeamId());

        return new PostResponseDto(post);
    }

    // 검색
    public Page<PostResponseDto> searchPosts(String type, String keyword, int page, int size) {
        return postRepository.searchPosts(type, keyword, PageRequest.of(page, size));
    }



}
