package com.example.booktalk.domain.admin.controller;

import com.example.booktalk.domain.userreport.dto.response.UserReportListRes;
import com.example.booktalk.domain.userreport.service.UserReportService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/admin/reports")
@RequiredArgsConstructor
public class AdminReportController {

    private final UserReportService userReportService;

    @GetMapping("/{reportedUserId}")
    public ResponseEntity<List<UserReportListRes>> getUserReports(
        @PathVariable Long reportedUserId) {
        List<UserReportListRes> res = userReportService.getUserReports(reportedUserId);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
