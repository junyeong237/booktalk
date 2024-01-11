package com.example.booktalk.domain.product.exception;

import com.example.booktalk.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ProductErrorCode implements ErrorCode {

    NOT_FOUND_PRODUCT(HttpStatus.NOT_FOUND, "해당하는 상품이 존재하지 않습니다!"),
    ALREADY_COMPLETED_TRANSACTION(HttpStatus.BAD_REQUEST, "이미 거래가 다 완료된 상품입니다."),
    NOT_PERMISSION_AUTHORITHY(HttpStatus.FORBIDDEN, "작성자가 아닙니다."),
    DELETED_PRODUCT(HttpStatus.NOT_FOUND, "이미 삭제된 상품입니다.");

    private final HttpStatus httpStatus;
    private final String message;
}


