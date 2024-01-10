package com.example.booktalk.domain.review.exception;

import com.example.booktalk.global.exception.ErrorCode;
import com.example.booktalk.global.exception.GlobalException;
import lombok.Getter;

@Getter
public class NotPermissionReviewAuthorityException extends GlobalException {

    public NotPermissionReviewAuthorityException(ErrorCode errorCode) {
        super(errorCode);
    }
}
