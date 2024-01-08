package com.example.booktalk.domain.user.exception;

import com.example.booktalk.global.exception.ErrorCode;
import com.example.booktalk.global.exception.GlobalException;

public class BadLoginException extends GlobalException {

    public BadLoginException(ErrorCode errorCode) {
        super(errorCode);
    }
}
