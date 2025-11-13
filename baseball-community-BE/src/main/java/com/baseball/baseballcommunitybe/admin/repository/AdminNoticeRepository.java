package com.baseball.baseballcommunitybe.admin.repository;

import com.baseball.baseballcommunitybe.admin.entity.AdminNotice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminNoticeRepository extends JpaRepository<AdminNotice, Long> {

    List<AdminNotice> findAllByOrderByIsPinnedDescCreatedAtDesc();
}
