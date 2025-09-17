package com.baseball.baseballcommunitybe.report.dto;

import com.baseball.baseballcommunitybe.report.entity.ReportReason;
import lombok.Getter;

@Getter
public class ReportRequestDto {
    private ReportReason reason; // "SPAM", "ABUSE", ...
}
