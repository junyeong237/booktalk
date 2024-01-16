package com.example.booktalk.domain.user.exception;

import com.example.booktalk.global.exception.ErrorCode;
import com.example.booktalk.global.exception.GlobalException;

public class NotMatchPasswordException extends GlobalException {

    public NotMatchPasswordException(ErrorCode errorCode) {
        super(errorCode);
    }
}
