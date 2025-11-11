package com.baseball.baseballcommunitybe.admin.controller;

import com.baseball.baseballcommunitybe.admin.dto.post.AdminPostDetailDto;
import com.baseball.baseballcommunitybe.admin.dto.post.AdminPostDto;
import com.baseball.baseballcommunitybe.admin.service.AdminPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/posts")
@RequiredArgsConstructor
public class AdminPostController {

    private final AdminPostService adminPostService;

    /** 🔹 전체 게시글 조회 (페이징 포함) */
    @GetMapping
    public ResponseEntity<Page<AdminPostDto>> getAllPosts(@PageableDefault(size = 20) Pageable pageable) {
        Page<AdminPostDto> posts = adminPostService.getAllPosts(pageable);
        return ResponseEntity.ok(posts);
    }

    /** 🔹 단건 상세조회 */
    @GetMapping("/{postId}")
    public ResponseEntity<AdminPostDetailDto> getPostDetail(@PathVariable Long postId) {
        return ResponseEntity.ok(adminPostService.getPostDetail(postId));
    }

    /** 🔹 게시글 숨김 (soft delete → DELETE 의미) */
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> hidePost(@PathVariable Long postId) {
        adminPostService.hidePost(postId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    /** 🔹 게시글 복구 (숨김 취소 → PATCH 의미) */
    @PatchMapping("/{postId}/restore")
    public ResponseEntity<Void> restorePost(@PathVariable Long postId) {
        adminPostService.restorePost(postId);
        return ResponseEntity.noContent().build();
    }

    /** 🔹 관리자 플래그 지정 */
    @PatchMapping("/{postId}/flag")
    public ResponseEntity<String> flagPost(@PathVariable Long postId, @RequestParam(required = false) String reason) {
        adminPostService.flagPost(postId, reason);
        return ResponseEntity.ok("게시글이 플래그 처리되었습니다.");
    }

    /** 🔹 유저별 게시글 수 조회 */
    @GetMapping("/count/{userId}")
    public ResponseEntity<Long> getPostCountByUser(@PathVariable Long userId) {
        long count = adminPostService.getPostCountByUser(userId);
        return ResponseEntity.ok(count);
    }
}
