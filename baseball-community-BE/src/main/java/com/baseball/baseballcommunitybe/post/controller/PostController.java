package com.baseball.baseballcommunitybe.post.controller;

import com.baseball.baseballcommunitybe.comment.service.CommentService;
import com.baseball.baseballcommunitybe.like.service.LikeService;
import com.baseball.baseballcommunitybe.post.dto.PostDetailResponseDto;
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
    private final CommentService commentService;
    private final LikeService likeService;
    @GetMapping
    public Page<PostResponseDto> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return postService.getAllPosts(page, size);
    }

    @GetMapping("/team/{teamId}")
    public Page<PostResponseDto> getPostsByTeam(
            @PathVariable Integer teamId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return postService.getPostsByTeam(teamId, page, size);
    }

    @GetMapping("/user/{userId}")
    public Page<PostResponseDto> getPostsByUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return postService.getPostsByUser(userId, page, size);
    }
    // 단일 게시글

    // 단일 게시글 조회
    @GetMapping("/{post_id}")
    public PostDetailResponseDto getPost(
            @PathVariable("post_id") Long postId,
            @RequestParam(required = false) Long userId   // 프론트에서 전달
    ) {
        return postService.getPostDetail(postId, userId);
    }


}
