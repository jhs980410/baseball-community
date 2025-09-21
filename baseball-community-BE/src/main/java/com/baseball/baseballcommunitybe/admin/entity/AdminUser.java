package com.baseball.baseballcommunitybe.admin.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "users")
public class AdminUser {
    @Id
    private Long id;

    private String email;

    private String nickname;

    @Column(name = "status")
    private String status; // ACTIVE, BANNED 등
}
