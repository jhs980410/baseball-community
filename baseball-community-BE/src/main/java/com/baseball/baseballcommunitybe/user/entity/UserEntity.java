package com.baseball.baseballcommunitybe.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password; // Bcrypt 해시 저장

    @Column(nullable = false, unique = true)
    private String nickname;

    @Enumerated(EnumType.STRING)
    private Role role;

    public enum Role {
        USER, ADMIN, MODERATOR;

        public static Role from(String value) {
            return Role.valueOf(value.toUpperCase());
        }
    }

    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        ACTIVE, SUSPENDED, DELETED
    }
}
