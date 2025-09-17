package com.baseball.baseballcommunitybe.report.controller;

import com.baseball.baseballcommunitybe.auth.jwt.JwtTokenProvider;
import com.baseball.baseballcommunitybe.report.dto.ReportRequestDto;
import com.baseball.baseballcommunitybe.report.entity.ReportTargetType;
import com.baseball.baseballcommunitybe.report.service.ReportService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;
    private final JwtTokenProvider jwtTokenProvider;
    @PostMapping("/posts/{postId}")
    public ResponseEntity<Void> reportPost(
            @PathVariable Long postId,
            @RequestBody ReportRequestDto dto,
            HttpServletRequest request
    ) {
        String token = jwtTokenProvider.resolveToken(request);
        Long userId = jwtTokenProvider.getUserIdFromToken(token); // JWT filter에서 셋팅
        reportService.report(postId, ReportTargetType.POST, dto, userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/comments/{commentId}")
    public ResponseEntity<Void> reportComment(
            @PathVariable Long commentId,
            @RequestBody ReportRequestDto dto,
            HttpServletRequest request
    ) {
        String token = jwtTokenProvider.resolveToken(request);
        Long userId = jwtTokenProvider.getUserIdFromToken(token);

        System.out.println("토큰:" + token);
        System.out.println("user id "+userId);
        reportService.report(commentId, ReportTargetType.COMMENT, dto, userId);
        return ResponseEntity.ok().build();
    }
}
