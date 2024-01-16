package com.example.booktalk.domain.review.exception;

import com.example.booktalk.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ReviewErrorCode implements ErrorCode {

    NOT_FOUND_REVIEW(HttpStatus.NOT_FOUND, "해당하는 게시글이 존재하지 않습니다."),
    NOT_PERMISSION_REVIEW_AUTHORITY(HttpStatus.FORBIDDEN, "게시글 수정 및 삭제 권한이 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
