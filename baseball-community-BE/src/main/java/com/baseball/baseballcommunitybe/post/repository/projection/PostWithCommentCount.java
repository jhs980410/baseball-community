package com.baseball.baseballcommunitybe.post.repository.projection;

import com.baseball.baseballcommunitybe.post.entity.Post;

public interface PostWithCommentCount {
    Post getPost();
    Long getCommentCount();
}
