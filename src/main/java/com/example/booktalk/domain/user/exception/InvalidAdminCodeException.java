package com.example.booktalk.domain.user.exception;

import com.example.booktalk.global.exception.ErrorCode;
import com.example.booktalk.global.exception.GlobalException;

public class InvalidAdminCodeException extends GlobalException {

    public InvalidAdminCodeException(ErrorCode errorCode) {
        super(errorCode);
    }
}
