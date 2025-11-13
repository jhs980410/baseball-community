package com.baseball.baseballcommunitybe.notice.service;

import com.baseball.baseballcommunitybe.notice.dto.NoticeListResponseDto;
import com.baseball.baseballcommunitybe.notice.dto.NoticeResponseDto;
import com.baseball.baseballcommunitybe.notice.entity.Notice;
import com.baseball.baseballcommunitybe.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {

    private final NoticeRepository noticeRepository;

    // 상단 고정 공지 불러오기
    public List<NoticeResponseDto> getPinnedNotices() {

        List<NoticeListResponseDto> dtoList = noticeRepository.findTopPinned();

        return dtoList.stream()
                .map(NoticeResponseDto::fromListDto)
                .toList();
    }
    //  공지 전체 리스트
    public Page<NoticeResponseDto> getNoticeList(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);

        return noticeRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(NoticeResponseDto::fromEntity);
    }

    public Page<NoticeListResponseDto> getNoticeList(Pageable pageable) {
        return noticeRepository.findAllNoticeList(pageable);
    }

    // 단일 조회
    public NoticeResponseDto getNotice(Long id) {
        Notice notice = noticeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("공지사항을 찾을 수 없습니다."));

        return NoticeResponseDto.fromEntity(notice);
    }
}
