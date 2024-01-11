package com.example.booktalk.domain.category.exception;

import com.example.booktalk.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CategoryErrorCode implements ErrorCode {
    NOT_FOUND_CATEGORY(HttpStatus.NOT_FOUND, "해당하는 카테고리가 존재하지 않습니다!"),
    FORBIDDEM_DELETE_CATEGORY(HttpStatus.FORBIDDEN, "사용중인 카테고리이기때문에 삭제할 수 없습니다."),
    EXIST_CATEGORY(HttpStatus.BAD_REQUEST, "해당하는 카테고리가 이미 존재합니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
