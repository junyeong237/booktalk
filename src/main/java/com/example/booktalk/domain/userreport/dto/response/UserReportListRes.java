package com.example.booktalk.domain.userreport.dto.response;

import java.time.LocalDateTime;

public record UserReportListRes(String reason, LocalDateTime createdAt) {
}
