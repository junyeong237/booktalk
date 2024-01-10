package com.example.booktalk.domain.user.exception;

import com.example.booktalk.global.exception.ErrorCode;
import com.example.booktalk.global.exception.GlobalException;

public class NotFoundUserException extends GlobalException {

    public NotFoundUserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
