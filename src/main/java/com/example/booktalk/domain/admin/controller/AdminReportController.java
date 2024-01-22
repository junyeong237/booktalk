package com.example.booktalk.domain.admin.controller;

import com.example.booktalk.domain.product.dto.response.ProductListRes;
import com.example.booktalk.domain.user.dto.response.UserRes;
import com.example.booktalk.domain.userreport.dto.response.UserReportListRes;
import com.example.booktalk.domain.userreport.entity.UserReport;
import com.example.booktalk.domain.userreport.service.UserReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
@RequestMapping("/api/v2/admin/reports")
@RequiredArgsConstructor
public class AdminReportController {
private final UserReportService userReportService;
    @GetMapping("/{reportedUserId}")
    public List<UserReportListRes> getUserReports(@PathVariable Long reportedUserId) {

        return userReportService.getUserReports(reportedUserId);
    }
}
