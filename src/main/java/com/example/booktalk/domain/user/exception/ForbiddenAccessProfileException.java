package com.example.booktalk.domain.user.exception;

import com.example.booktalk.global.exception.ErrorCode;
import com.example.booktalk.global.exception.GlobalException;

public class ForbiddenAccessProfileException extends GlobalException {

    public ForbiddenAccessProfileException(ErrorCode errorCode) {
        super(errorCode);
    }
}
