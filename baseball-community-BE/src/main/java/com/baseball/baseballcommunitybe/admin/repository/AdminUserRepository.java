package com.baseball.baseballcommunitybe.admin.repository;

import com.baseball.baseballcommunitybe.admin.dto.user.AdminUserDto;
import com.baseball.baseballcommunitybe.admin.entity.AdminUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AdminUserRepository extends JpaRepository<AdminUser, Long> {

    // DTO 반환용 커스텀 projection 쿼리
    @Query("""
        SELECT new com.baseball.baseballcommunitybe.admin.dto.user.AdminUserDto(
            u.id, u.email, u.nickname, u.role, u.status, u.createdAt
        )
        FROM AdminUser u
    """)
    Page<AdminUserDto> findAllAsDto(Pageable pageable);

    // 상태별 조회 (엔티티 기준)
    Page<AdminUser> findByStatus(AdminUser.Status status, Pageable pageable);



}