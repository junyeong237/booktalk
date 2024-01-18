package com.example.booktalk.domain.productLike.exception;

import com.example.booktalk.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ProductLikeErrorCode implements ErrorCode {

    NOT_PERMISSION_MINE(HttpStatus.FORBIDDEN, "본인의 상품에는 좋아요를 누를 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
