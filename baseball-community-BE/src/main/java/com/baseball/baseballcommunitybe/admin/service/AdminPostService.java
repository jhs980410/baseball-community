package com.baseball.baseballcommunitybe.admin.service;

import com.baseball.baseballcommunitybe.admin.dto.post.AdminPostDetailDto;
import com.baseball.baseballcommunitybe.admin.dto.post.AdminPostDto;
import com.baseball.baseballcommunitybe.admin.entity.AdminPost;
import com.baseball.baseballcommunitybe.admin.repository.AdminPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminPostService {

    private final AdminPostRepository adminPostRepository;

    /**  게시글 전체 조회 (페이징 + 상태 포함) */
    @Transactional(readOnly = true)
    public Page<AdminPostDto> getAllPosts(Pageable pageable) {
        Page<AdminPost> postsPage = adminPostRepository.findAllWithStatus(pageable);
        return postsPage.map(AdminPostDto::fromEntity);
    }

    /**  단건 상세조회 */
    @Transactional(readOnly = true)
    public AdminPostDetailDto getPostDetail(Long postId) {
        return adminPostRepository.findDetailWithHidden(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. ID: " + postId));
    }


    /**  게시글 숨김 처리 (Soft Delete) */
    @Transactional
    public void hidePost(Long postId) {
        int updated = adminPostRepository.hidePost(postId);
        if (updated == 0) {
            throw new IllegalArgumentException("게시글을 찾을 수 없습니다. ID: " + postId);
        }
    }

    /**  게시글 복구 처리 */
    @Transactional
    public void restorePost(Long postId) {
        int updated = adminPostRepository.restorePost(postId);
        if (updated == 0) {
            throw new IllegalArgumentException("게시글을 찾을 수 없습니다. ID: " + postId);
        }
    }

    /**  관리자 플래그 설정 */
    @Transactional
    public void flagPost(Long postId, String reason) {
        AdminPost post = adminPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. ID: " + postId));

        if (post.getStatus() == null) {
            throw new IllegalStateException("게시글 상태(post_status)를 찾을 수 없습니다.");
        }

        post.getStatus().setFlagged(true);
        post.getStatus().setLastFlagReason(reason != null ? reason : "관리자 지정 플래그");
    }

    /**  플래그 해제 및 복구 */
    @Transactional
    public void unflagPost(Long postId) {
        AdminPost post = adminPostRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다. ID: " + postId));

        if (post.getStatus() != null) {
            post.getStatus().setFlagged(false);
            post.getStatus().setLastFlagReason(null);
        }
    }

    /**  특정 유저의 게시글 수 조회 */
    @Transactional(readOnly = true)
    public long getPostCountByUser(Long userId) {
        return adminPostRepository.countByUserId(userId);
    }
}
