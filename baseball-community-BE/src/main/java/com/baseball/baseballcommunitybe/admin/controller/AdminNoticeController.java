package com.baseball.baseballcommunitybe.admin.controller;

import com.baseball.baseballcommunitybe.admin.dto.notice.NoticeCreateRequestDto;
import com.baseball.baseballcommunitybe.admin.dto.notice.NoticeDto;
import com.baseball.baseballcommunitybe.admin.dto.notice.NoticeUpdateRequestDto;
import com.baseball.baseballcommunitybe.admin.service.AdminNoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/notices")
@RequiredArgsConstructor
public class AdminNoticeController {

    private final AdminNoticeService adminNoticeService;

    @GetMapping
    public List<NoticeDto> getNotices() {
        return adminNoticeService.getNotices();
    }

    @GetMapping("/{id}")
    public NoticeDto getNotice(@PathVariable Long id) {
        return adminNoticeService.getNotice(id);
    }

    @PostMapping
    public NoticeDto createNotice(@RequestBody NoticeCreateRequestDto dto) {
        return adminNoticeService.createNotice(dto);
    }

    @PutMapping("/{id}")
    public NoticeDto updateNotice(@PathVariable Long id, @RequestBody NoticeUpdateRequestDto dto) {
        return adminNoticeService.updateNotice(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotice(@PathVariable Long id) {
        adminNoticeService.deleteNotice(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/pin")
    public ResponseEntity<Void> togglePin(@PathVariable Long id) {
        adminNoticeService.togglePin(id);
        return ResponseEntity.ok().build();
    }
}
