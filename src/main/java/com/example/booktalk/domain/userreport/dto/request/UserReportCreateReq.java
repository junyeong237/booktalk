package com.example.booktalk.domain.userreport.dto.request;

import lombok.Builder;
import lombok.Getter;

@Builder
public record UserReportCreateReq (
        Long reportedUserId,
        String reason
) {
}
