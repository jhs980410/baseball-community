package com.baseball.baseballcommunitybe.admin.service;

import com.baseball.baseballcommunitybe.admin.dto.notice.NoticeCreateRequestDto;
import com.baseball.baseballcommunitybe.admin.dto.notice.NoticeDto;
import com.baseball.baseballcommunitybe.admin.dto.notice.NoticeUpdateRequestDto;
import com.baseball.baseballcommunitybe.admin.entity.AdminNotice;
import com.baseball.baseballcommunitybe.admin.repository.AdminNoticeRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminNoticeService {

    private final AdminNoticeRepository noticeRepository;

    public List<NoticeDto> getNotices() {
        return noticeRepository.findAllByOrderByIsPinnedDescCreatedAtDesc()
                .stream()
                .map(NoticeDto::new)
                .toList();
    }

    public NoticeDto getNotice(Long id) {
        AdminNotice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("공지사항을 찾을 수 없습니다."));
        return new NoticeDto(notice);
    }

    @Transactional
    public NoticeDto createNotice(NoticeCreateRequestDto dto) {
        AdminNotice notice = new AdminNotice();
        notice.update(dto.getTitle(), dto.getContent(), dto.isPinned());
        noticeRepository.save(notice);
        return new NoticeDto(notice);
    }

    @Transactional
    public NoticeDto updateNotice(Long id, NoticeUpdateRequestDto dto) {
        AdminNotice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("공지사항을 찾을 수 없습니다."));
        notice.update(dto.getTitle(), dto.getContent(), dto.isPinned());
        return new NoticeDto(notice);
    }

    @Transactional
    public void deleteNotice(Long id) {
        noticeRepository.deleteById(id);
    }

    @Transactional
    public void togglePin(Long id) {
        AdminNotice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("공지사항을 찾을 수 없습니다."));
        notice.update(notice.getTitle(), notice.getContent(), !notice.isPinned());
    }
}
