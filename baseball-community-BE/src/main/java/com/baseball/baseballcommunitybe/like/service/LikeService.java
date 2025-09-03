package com.baseball.baseballcommunitybe.like.service;

import com.baseball.baseballcommunitybe.like.dto.LikeResponseDto;
import com.baseball.baseballcommunitybe.like.entity.Like;
import com.baseball.baseballcommunitybe.like.entity.LikeId;
import com.baseball.baseballcommunitybe.like.repository.LikeRepository;
import com.baseball.baseballcommunitybe.post.repository.PostRepository;
import com.baseball.baseballcommunitybe.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import com.baseball.baseballcommunitybe.user.entity.User;
@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public List<LikeResponseDto> findByUser(Long userId) {
        return likeRepository.findByUser_Id(userId).stream()
                .map(l -> new LikeResponseDto(
                        l.getPost().getId(),
                        l.getPost().getTitle(),
                        l.getPost().getUser().getNickname(),
                        l.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }
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

    public void unlikePost(Long postId, Long userId) {
        likeRepository.deleteById(new LikeId(postId, userId));
    }

    public long countLikes(Long postId) {
        return likeRepository.countByPost_Id(postId);
    }
}
