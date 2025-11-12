package com.baseball.baseballcommunitybe.admin.repository;

import com.baseball.baseballcommunitybe.admin.dto.user.AdminUserDto;
import com.baseball.baseballcommunitybe.admin.entity.AdminUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

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

    List<AdminUser> findByRoleIn(List<String> roles);

    boolean existsByEmail(String email);

    // SUPER_ADMIN 계정 수 세기
    long countByRole(AdminUser.Role role);


    Page<AdminUser> findByNicknameContainingIgnoreCase(String nickname, Pageable pageable);


    /**
     * 사용자 경고 (warning)
     */
    @Modifying
    @Query(value = """
        INSERT INTO user_suspensions (user_id, reason, restriction_type, admin_id)
        VALUES (:userId, :reason, :restrictionType, 1)
        """, nativeQuery = true)
    void warnUser(@Param("userId") Long userId,
                  @Param("restrictionType") String restrictionType,
                  @Param("reason") String reason);

    /**
     * 사용자 일시 정지 (기간 지정)
     */
    @Modifying
    @Query(value = """
        INSERT INTO user_suspensions (user_id, reason, restriction_type, suspended_at, released_at, admin_id)
        VALUES (:userId, :reason, 'ALL', NOW(), DATE_ADD(NOW(), INTERVAL :days DAY), 1)
        """, nativeQuery = true)
    void suspendUser(@Param("userId") Long userId,
                     @Param("days") int days,
                     @Param("reason") String reason);

    /**
     * 영구정지 (상태 SUSPENDED)
     */
    @Modifying
    @Query("UPDATE AdminUser u SET u.status = 'SUSPENDED' WHERE u.id = :userId")
    void banUser(@Param("userId") Long userId);

}