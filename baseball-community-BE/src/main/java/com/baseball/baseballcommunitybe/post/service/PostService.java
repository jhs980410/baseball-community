package com.baseball.baseballcommunitybe.post.service;

import com.baseball.baseballcommunitybe.post.entity.Post;
import com.baseball.baseballcommunitybe.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    // 전체 게시글 최신순 조회
    public Page<Post> getAllPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return postRepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    // 팀별 최신글 최신순 조회
    public Page<Post> getPostsByTeam(Integer teamId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return postRepository.findByTeamIdOrderByCreatedAtDesc(teamId, pageable);
    }

    // 특정 유저의 글 최신순 조회
    public Page<Post> getPostsByUser(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return postRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    // 단일 게시글 조회
    public Post getPost(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글 없음: " + id));
    }
}
