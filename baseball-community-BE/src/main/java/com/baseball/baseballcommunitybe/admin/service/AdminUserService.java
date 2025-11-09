package com.baseball.baseballcommunitybe.admin.service;


import com.baseball.baseballcommunitybe.admin.entity.AdminUser;
import com.baseball.baseballcommunitybe.admin.repository.AdminUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final AdminUserRepository adminUserRepository;

    public Page<AdminUser> getAllUsers(Pageable pageable) {
        return adminUserRepository.findAll(pageable);
    }
}
