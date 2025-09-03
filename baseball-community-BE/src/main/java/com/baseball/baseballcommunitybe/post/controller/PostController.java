package com.baseball.baseballcommunitybe.post.controller;

import com.baseball.baseballcommunitybe.post.dto.PostResponseDto;
import com.baseball.baseballcommunitybe.post.entity.Post;
import com.baseball.baseballcommunitybe.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 전체 게시글
    @GetMapping
    public Page<PostResponseDto> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return postService.getAllPosts(page, size)
                .map(PostResponseDto::from);
    }

    // 팀별 게시글
    @GetMapping("/team/{teamId}")
    public Page<PostResponseDto> getPostsByTeam(
            @PathVariable Integer teamId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return postService.getPostsByTeam(teamId, page, size)
                .map(PostResponseDto::from);
    }

    // 특정 유저 게시글 (이미 DTO로 반환 OK)
    @GetMapping("/user/{userId}")
    public Page<PostResponseDto> getPostsByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return postService.getPostsByUser(userId, page, size)
                .map(PostResponseDto::from);
    }

    // 단일 게시글
    @GetMapping("/{id}")
    public PostResponseDto getPost(@PathVariable Long id) {
        return PostResponseDto.from(postService.getPost(id));
    }
}
