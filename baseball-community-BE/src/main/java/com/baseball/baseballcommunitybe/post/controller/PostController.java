package com.baseball.baseballcommunitybe.post.controller;

import com.baseball.baseballcommunitybe.auth.jwt.JwtTokenProvider;
import com.baseball.baseballcommunitybe.common.aop.CheckSuspended;
import com.baseball.baseballcommunitybe.post.dto.PostDetailResponseDto;
import com.baseball.baseballcommunitybe.post.dto.PostRequestDto;
import com.baseball.baseballcommunitybe.post.dto.PostResponseDto;
import com.baseball.baseballcommunitybe.post.service.PostService;
import com.baseball.baseballcommunitybe.redis.service.HotPostService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final JwtTokenProvider jwtTokenProvider;
    private final HotPostService hotPostService;
    /**
     * 전체 게시글 조회 (검색 포함)
     */
    @GetMapping
    public Page<PostResponseDto> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String keyword
    ) {
        if (keyword != null && !keyword.isBlank()) {
            return postService.searchPosts(type, keyword, page, size);
        }
        return postService.getAllPosts(page, size);
    }

    /**
     * 팀별 게시글 조회
     */
    @GetMapping("/teams/{teamId}")
    public Page<PostResponseDto> getPostsByTeam(
            @PathVariable Integer teamId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return postService.getPostsByTeam(teamId, page, size);
    }

    /**
     * 특정 유저 게시글 조회
     */
    @GetMapping("/users/{userId}")
    public Page<PostResponseDto> getPostsByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return postService.getPostsByUser(userId, page, size);
    }
    /**
     * 내가 쓴 게시글 조회 (마이페이지 전용)
     */
    @GetMapping("/me")
    public Page<PostResponseDto> getMyPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request
    ) {
        Long currentUserId = extractUserId(request);
        if (currentUserId == null) {
            throw new RuntimeException("인증되지 않은 사용자입니다.");
        }
        return postService.getPostsByUser(currentUserId, page, size);
    }
    /**
     * 단일 게시글 조회
     */
    @GetMapping("/{postId}")
    public PostDetailResponseDto getPost(
            @PathVariable Long postId,
            HttpServletRequest request
    ) {
        Long currentUserId = extractUserId(request);
        return postService.getPostDetail(postId, currentUserId);
    }

    /**
     * 게시글 작성
     */
    @CheckSuspended
    @PostMapping
    public PostResponseDto createPost(
            @RequestBody PostRequestDto requestDto,
            HttpServletRequest request
    ) {
        Long currentUserId = extractUserId(request);
        return postService.createPost(requestDto, currentUserId);
    }

    /**
     * 게시글 수정
     */
    @CheckSuspended
    @PutMapping("/{postId}")
    public PostResponseDto updatePost(
            @PathVariable Long postId,
            @RequestBody PostRequestDto requestDto,
            HttpServletRequest request
    ) throws AccessDeniedException {
        Long currentUserId = extractUserId(request);
        return postService.updatePost(postId, requestDto, currentUserId);
    }

    /**
     * 게시글 삭제
     */
    @CheckSuspended
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long postId,
            HttpServletRequest request
    ) throws AccessDeniedException {
        Long currentUserId = extractUserId(request);
        postService.deletePost(postId, currentUserId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 전체 인기글 조회
     */
    @GetMapping("/hot")
    public ResponseEntity<Page<PostResponseDto>> getHotPosts(
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Long currentUserId = extractUserId(request);
        Page<PostResponseDto> hotPosts = hotPostService.getHotPosts(currentUserId, null, page, size);
        return ResponseEntity.ok(hotPosts);
    }

    /**
     * 팀별 인기글 조회
     */
    @GetMapping("/teams/{teamId}/hot")
    public ResponseEntity<Page<PostResponseDto>> getTeamHotPosts(
            @PathVariable Long teamId,
            HttpServletRequest request,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Long currentUserId = extractUserId(request);
        Page<PostResponseDto> hotPosts = hotPostService.getHotPosts(currentUserId, teamId, page, size);
        return ResponseEntity.ok(hotPosts);
    }

    // ------------------ 내부 헬퍼 ------------------
    private Long extractUserId(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        return (token != null) ? jwtTokenProvider.getUserIdFromToken(token) : null;
    }
}
