package com.baseball.baseballcommunitybe.like.entity;

import com.baseball.baseballcommunitybe.post.entity.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.baseball.baseballcommunitybe.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "likes")
@Getter
@NoArgsConstructor
public class Like {

    @EmbeddedId
    private LikeId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("postId")
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public Like(Post post, User user) {   // JPA User 엔티티 import
        this.post = post;
        this.user = user;
        this.id = new LikeId(post.getId(), user.getId());
    }
}
