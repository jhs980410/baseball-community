package com.baseball.baseballcommunitybe.like.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class CommentLikeId implements Serializable {
    private Long commentId;
    private Long userId;
}
