package com.example.booktalk.domain.userreport.dto.response;

public record UserReportGetListRes (
        Long id,
        Long reportedUserId,
        String reason
) {
}
