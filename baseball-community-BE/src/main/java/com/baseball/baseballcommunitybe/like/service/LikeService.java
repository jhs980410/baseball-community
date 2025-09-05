package com.baseball.baseballcommunitybe.like.service;

import com.baseball.baseballcommunitybe.like.dto.LikeResponseDto;
import com.baseball.baseballcommunitybe.like.entity.Like;
import com.baseball.baseballcommunitybe.like.entity.LikeId;
import com.baseball.baseballcommunitybe.like.repository.LikeRepository;
import com.baseball.baseballcommunitybe.post.repository.PostRepository;
import com.baseball.baseballcommunitybe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
                .map(l -> new LikeResponseDto(
                        l.getPost().getId(),
                        l.getPost().getTitle(),
                        l.getPost().getUser().getNickname(),
                        l.getCreatedAt()
                ));
    }

    /**
     * 좋아요 추가
     */
    public void likePost(Long postId, Long userId) {
        var post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Like like = new Like(post, user);
        if (!likeRepository.existsById(like.getId())) {
            likeRepository.save(like);
        }
    }

    /**
     * 좋아요 취소
     */
    public void unlikePost(Long postId, Long userId) {
        likeRepository.deleteById(new LikeId(postId, userId));
    }

    /**
     * 게시글 좋아요 개수 카운트
     */
    public long countLikes(Long postId) {
        return likeRepository.countByPost_Id(postId);
    }
}
