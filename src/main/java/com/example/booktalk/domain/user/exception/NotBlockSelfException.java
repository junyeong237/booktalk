package com.example.booktalk.domain.user.exception;

import com.example.booktalk.global.exception.ErrorCode;
import com.example.booktalk.global.exception.GlobalException;

public class NotBlockSelfException extends GlobalException {

    public NotBlockSelfException(ErrorCode errorCode) {
        super(errorCode);
    }
}
