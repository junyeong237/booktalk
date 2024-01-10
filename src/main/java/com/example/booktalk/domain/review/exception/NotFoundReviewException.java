package com.example.booktalk.domain.review.exception;

import com.example.booktalk.global.exception.ErrorCode;
import com.example.booktalk.global.exception.GlobalException;
import lombok.Getter;

@Getter
public class NotFoundReviewException extends GlobalException {

    public NotFoundReviewException(ErrorCode errorCode) {
        super(errorCode);
    }
}
