package com.baseball.baseballcommunitybe.like.service;

import com.baseball.baseballcommunitybe.like.dto.LikeResponseDto;
import com.baseball.baseballcommunitybe.like.entity.Like;
import com.baseball.baseballcommunitybe.like.repository.LikeRepository;
import com.baseball.baseballcommunitybe.post.entity.Post;
import com.baseball.baseballcommunitybe.post.repository.PostRepository;
import com.baseball.baseballcommunitybe.user.entity.User;
import com.baseball.baseballcommunitybe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    /**
     * 마이페이지 - 내가 좋아요한 글 (페이징)
     */
    public Page<LikeResponseDto> findByUser(Long userId, Pageable pageable) {
        return likeRepository.findByUser_Id(userId, pageable)
                .map(like -> LikeResponseDto.from(
                        like.getPost(),
                        true, // 내가 누른 글이므로 항상 true
                        likeRepository.countByPost_Id(like.getPost().getId())
                ));
    }

    /**
     * 좋아요 토글
     */
    @Transactional
    public LikeResponseDto toggleLike(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음: " + postId));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저 없음: " + userId));

        Optional<Like> existing = likeRepository.findByPost_IdAndUser_Id(postId, userId);

        if (existing.isPresent()) {
            // 좋아요 취소
            likeRepository.delete(existing.get());
        } else {
            // 좋아요 추가
            Like like = new Like(post, user);
            likeRepository.save(like);
        }

        long likeCount = likeRepository.countByPost_Id(postId);
        boolean liked = likeRepository.existsByPost_IdAndUser_Id(postId, userId);

        return LikeResponseDto.from(post, liked, likeCount);
    }

    /**
     * 게시글 좋아요 개수 카운트
     */
    public long countLikes(Long postId) {
        return likeRepository.countByPost_Id(postId);
    }
}
