package com.example.booktalk.domain.userreport.exception;

import com.example.booktalk.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserReportErrorCode implements ErrorCode {


    NOT_PERMISSION_SELF_REPORT(HttpStatus.FORBIDDEN, "자기자신은 신고할 수 없습니다."),
    NOT_FOUND_REPORTED_USER(HttpStatus.NOT_FOUND, "신고된 해당 Id는 존재하지 않습니다.");


    private final HttpStatus httpStatus;
    private final String message;
}