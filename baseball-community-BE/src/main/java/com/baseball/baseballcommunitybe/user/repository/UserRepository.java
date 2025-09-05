package com.baseball.baseballcommunitybe.user.repository;

import com.baseball.baseballcommunitybe.user.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);
    @Query("SELECT u FROM User u WHERE u.email = :email OR u.nickname = :nickname")
    List<User> findByEmailOrNickname(String email, String nickname);
}
