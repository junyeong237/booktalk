package com.example.booktalk.domain.admin.controller;

import com.example.booktalk.domain.admin.service.AdminUserReportService;
import com.example.booktalk.domain.userreport.dto.response.UserReportGetListRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/reports")
@RequiredArgsConstructor
public class AdminUserReportController {

    private final AdminUserReportService adminUserReportService;

    @GetMapping
    public ResponseEntity<List<UserReportGetListRes>> getUserReportList(
            @RequestParam(value = "sortBy", defaultValue = "createdAt") String sortBy,
            @RequestParam(value = "isAsc", defaultValue = "false") boolean isAsc
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(adminUserReportService.getUserReportList(sortBy, isAsc));
    }
}
