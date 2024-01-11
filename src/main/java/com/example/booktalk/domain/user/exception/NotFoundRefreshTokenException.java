package com.example.booktalk.domain.user.exception;

import com.example.booktalk.global.exception.ErrorCode;
import com.example.booktalk.global.exception.GlobalException;

public class NotFoundRefreshTokenException extends GlobalException {

    public NotFoundRefreshTokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
