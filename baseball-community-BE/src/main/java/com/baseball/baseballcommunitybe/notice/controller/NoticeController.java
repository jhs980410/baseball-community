package com.baseball.baseballcommunitybe.notice.controller;

import com.baseball.baseballcommunitybe.notice.dto.NoticeListResponseDto;
import com.baseball.baseballcommunitybe.notice.dto.NoticeResponseDto;
import com.baseball.baseballcommunitybe.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notices")
@RequiredArgsConstructor
public class NoticeController {

    private final NoticeService noticeService;
    // 단일 조회
    @GetMapping("/{id}")
    public NoticeResponseDto getNotice(@PathVariable Long id) {
        return noticeService.getNotice(id);
    }

    //  공지사항 전체 목록 (페이징)
//    @GetMapping
//    public Page<NoticeResponseDto> getNoticeList(
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size
//    ) {
//        return noticeService.getNoticeList(page, size);
//    }
    @GetMapping
    public Page<NoticeListResponseDto> getNotices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return noticeService.getNoticeList(pageable);
    }
    /**
     * 상단 고정 공지 불러오기
     */
    @GetMapping("/top")
    public List<NoticeResponseDto> getPinnedNotices() {
        return noticeService.getPinnedNotices();
    }
}
