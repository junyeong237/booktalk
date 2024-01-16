package com.example.booktalk.domain.userreport.dto.request;

public record UserReportCreateReq (
        Long reportedUserId,
        String reason
) {
}
