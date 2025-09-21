package com.baseball.baseballcommunitybe.admin.repository;

import com.baseball.baseballcommunitybe.admin.entity.AdminUser;
import com.baseball.baseballcommunitybe.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminUserRepository extends JpaRepository<AdminUser, Long> {
    // JpaRepository 기본 제공: long count();
}
