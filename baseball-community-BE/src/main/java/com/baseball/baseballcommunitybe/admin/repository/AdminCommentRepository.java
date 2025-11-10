package com.baseball.baseballcommunitybe.admin.repository;

import com.baseball.baseballcommunitybe.admin.entity.AdminComment;
import com.baseball.baseballcommunitybe.admin.entity.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AdminCommentRepository extends JpaRepository<AdminComment, Long>  {

    // 댓글 수 조회
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.user.id = :userId")
    int countCommentsByUserId(@Param("userId") Long userId);


}
