package com.example.booktalk.domain.userreport.controller;


import com.example.booktalk.domain.userreport.dto.request.UserReportCreateReq;
import com.example.booktalk.domain.userreport.dto.response.UserReportCreateRes;
import com.example.booktalk.domain.userreport.service.UserReportService;
import com.example.booktalk.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/reports")
public class UserReportController {

    private final UserReportService userReportService;

    @PostMapping
    public ResponseEntity<UserReportCreateRes> createUserReport(
            @RequestBody UserReportCreateReq req,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userReportService.createUserReport(req, userDetails.getUser().getId()));
    }
}
