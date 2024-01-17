package com.example.booktalk.domain.user.dto.response;

import com.example.booktalk.domain.user.entity.UserRoleType;

public record UserReportRes(Long id, String name, Integer reportCount, UserRoleType roleType) {
}
