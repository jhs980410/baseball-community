package com.baseball.baseballcommunitybe.admin.dto.report;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminReportHandleRequestDto {
    private String action;     // 수행할 관리자 조치 코드
    private String adminNote;  // 선택적 메모 (사유, 비고 등)

}
